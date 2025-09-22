package xyz.joseyamut.updatequerybuilder.util;

import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeFormatHelper {

    public static final String DEFAULT_TARGET_ZONE_ID = "UTC";
    public static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    public static DateTimeFormatter dateTimeFormatter(String pattern) {
        return DateTimeFormatter.ofPattern(pattern, Locale.US);
    }

    public static String convertWithZonedDateTime(Timestamp sourceTimeStamp,
                                                  String sourceZone, String targetZone,
                                                  String formatPattern) {
        Instant localInstant = sourceTimeStamp.toInstant();
        ZoneId sourceZoneId = ZoneId.of(sourceZone);
        ZoneId targetZoneId = ZoneId.of(targetZone);
        ZonedDateTime localZonedDateTime = localInstant.atZone(sourceZoneId);
        ZonedDateTime targetZonedDateTime = localZonedDateTime.withZoneSameInstant(targetZoneId);
        if (!StringUtils.hasText(formatPattern)) {
            formatPattern = DateTimeFormatHelper.FORMAT_PATTERN;
        }
        return targetZonedDateTime.format(dateTimeFormatter(formatPattern));
    }
}
