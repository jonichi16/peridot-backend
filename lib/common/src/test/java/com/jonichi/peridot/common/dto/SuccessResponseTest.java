package com.jonichi.peridot.common.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class SuccessResponseTest {

    @Test
    public void builder_shouldCreateTheCorrectObject() throws Exception {
        // given
        int code = 200;
        String message = "Success";
        String data = "Sample data";

        // when
        ApiResponse<String> response = SuccessResponse.<String>builder()
                .code(code)
                .message(message)
                .data(data)
                .build();

        // then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getCode()).isEqualTo(code);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getData()).isEqualTo(data);
        assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    public void builder_shouldHaveNullErrorAndErrorCode() throws Exception {
        // given
        int code = 200;
        String message = "Success";
        String data = "Sample data";

        // when
        ApiResponse<String> response = SuccessResponse.<String>builder()
                .code(code)
                .message(message)
                .data(data)
                .build();

        // then
        assertThat(response.getError()).isNull();
        assertThat(response.getErrorCode()).isNull();
    }

    @Test
    public void builder_shouldNotHaveErrorAndErrorCodeFields() throws Exception {
        // given
        int code = 200;
        String message = "Success";
        String data = "Sample data";

        // when
        ApiResponse<String> response = SuccessResponse.<String>builder()
                .code(code)
                .message(message)
                .data(data)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(response);

        // then]
        assertThat(json).contains("data");
        assertThat(json).doesNotContain("error");
        assertThat(json).doesNotContain("errorCode");
    }
    
}
