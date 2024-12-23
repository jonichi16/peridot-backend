package com.jonichi.peridot.common.util;

import java.time.LocalDate;

/**
 * Utility class for date-related operations.
 */
public interface DateUtil {

    /**
     * Retrieves the current period as the first day of the current month.
     *
     * <p>The returned {@link LocalDate} object represents the first day of the
     * current month in the system's default time zone.</p>
     *
     * @return a {@link LocalDate} object representing the first day of the current month
     */
    static LocalDate getCurrentPeriod() {
        LocalDate now = LocalDate.now();

        return LocalDate.of(
                now.getYear(),
                now.getMonth(),
                1
        );
    }
}
