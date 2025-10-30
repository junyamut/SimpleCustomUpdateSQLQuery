package xyz.joseyamut.updatequerybuilder.configuration;

import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PersistenceContext
@EnableJpaRepositories(basePackages = {"xyz.joseyamut.updatequerybuilder.repository"}, repositoryImplementationPostfix = "Definition")
public class JpaRepositoryConfiguration {
}
