package com.jonichi.peridot.common.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class ErrorResponseTest {

    @Test
    public void builder_shouldCreateTheCorrectObject() throws Exception {
        // given
        int code = 400;
        String message = "Error";
        String error = "Sample error";
        String errorCode = "ERR_001";

        // when
        ApiResponse<String> response = ErrorResponse.<String>builder()
                .code(code)
                .message(message)
                .errorCode(errorCode)
                .error(error)
                .build();

        // then
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getCode()).isEqualTo(code);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getError()).isEqualTo(error);
        assertThat(response.getErrorCode()).isEqualTo(errorCode);
        assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    public void builder_shouldHaveNullValueForData() throws Exception {
        // given
        int code = 400;
        String message = "Error";
        String error = "Sample error";
        String errorCode = "ERR_001";

        // when
        ApiResponse<String> response = ErrorResponse.<String>builder()
                .code(code)
                .message(message)
                .errorCode(errorCode)
                .error(error)
                .build();

        // then
        assertThat(response.getData()).isNull();
    }

    @Test
    public void builder_shouldNotHaveDataField() throws Exception {
        // given
        int code = 400;
        String message = "Error";
        String error = "Sample error";
        String errorCode = "ERR_001";

        // when
        ApiResponse<String> response = ErrorResponse.<String>builder()
                .code(code)
                .message(message)
                .errorCode(errorCode)
                .error(error)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(response);

        // then
        assertThat(json).contains("error");
        assertThat(json).contains("errorCode");
        assertThat(json).doesNotContain("data");
    }

}
