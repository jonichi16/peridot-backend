package com.jonichi.peridot.common.util;

import java.time.LocalDate;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;

public class DateUtilTest {

    @Test
    public void getCurrentPeriod_shouldReturnTheCurrentPeriod() throws Exception {
        // given
        LocalDate expected = LocalDate.of(
                LocalDate.now().getYear(),
                LocalDate.now().getMonth(),
                1
        );

        // when
        LocalDate actual = DateUtil.getCurrentPeriod();

        // then
        assertThat(actual).isEqualTo(expected);
    }

}
