package xyz.joseyamut.updatequerybuilder.repository.util;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UpdateQueryHelperTest {

    static class TestEntity {
        @Id
        @Column(name = "id")
        private Integer id;

        @Column(name = "name")
        private String name;

        @Column(name = "description")
        private String description;

        @Column(name = "updated_at")
        private Timestamp updatedAt;

        // Not annotated, should be ignored
        private String ignoredField;

        public TestEntity(Integer id, String name, String description, Timestamp updatedAt, String ignoredField) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.updatedAt = updatedAt;
            this.ignoredField = ignoredField;
        }
    }

    private UpdateQueryHelper helper;

    @BeforeEach
    void setUp() {
        helper = new UpdateQueryHelper();
        helper.setSchema("public");
        helper.setTable("test_table");
        helper.setOperator("AND");
        helper.setTargetZoneId("UTC");
        helper.setTableKeyColumns(List.of("id"));
        helper.setMandatoryColumns(List.of("updated_at"));
        helper.setIgnoreColumns(List.of("ignored_column"));
    }

    @Test
    void buildStatement_generatesCorrectQuery() {
        Timestamp now = Timestamp.from(Instant.parse("2024-01-01T12:00:00Z"));
        TestEntity entity = new TestEntity(1, "O'Reilly", "How to train your dragon", now, "shouldBeIgnored");
        helper.setUpdate(entity);

        String query = helper.buildStatement();

        assertTrue(query.startsWith("UPDATE public.test_table SET"));
        assertTrue(query.contains("name='O''Reilly'"));
        assertTrue(query.contains("description='How to train your dragon'"));
        assertTrue(query.contains("updated_at="));
        assertTrue(query.contains("WHERE id=1"));
    }

    @Test
    void buildStatement_ignoresNullAndNonColumnFields() {
        TestEntity entity = new TestEntity(2, null, "The quick brown fox jumps over the lazy dog!", Timestamp.from(Instant.now()), "ignored");
        helper.setUpdate(entity);

        String query = helper.buildStatement();

        assertFalse(query.contains("name="));
        assertFalse(query.contains("ignoredField"));
        assertTrue(query.contains("id=2"));
        assertTrue(query.contains("description='The quick brown fox jumps over the lazy dog!'"));
    }

    @Test
    void buildStatement_throwsIfNoNonMandatoryFields() {
        TestEntity entity = new TestEntity(3, null, null, Timestamp.from(Instant.now()), null);
        helper.setUpdate(entity);

        Exception ex = assertThrows(IllegalArgumentException.class, helper::buildStatement);
        assertEquals("Zero non-mandatory fields found! Nothing to update.", ex.getMessage());
    }
}