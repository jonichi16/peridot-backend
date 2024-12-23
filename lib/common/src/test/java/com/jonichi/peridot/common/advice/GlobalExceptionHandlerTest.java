package com.jonichi.peridot.common.advice;

import com.jonichi.peridot.common.constant.ErrorCode;
import com.jonichi.peridot.common.dto.ApiResponse;
import com.jonichi.peridot.common.exception.PeridotDuplicateException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();


    @Test
    public void handleNotValidException_shouldReturnBadRequestError() throws Exception {
        // given
        FieldError fieldError1 = new FieldError(
                "TestObject",
                "field1",
                "must not be null"
        );
        FieldError fieldError2 = new FieldError(
                "TestObject",
                "field2",
                "must not be null"
        );
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        // when
        ResponseEntity<ApiResponse<Map<String, List<String>>>> response =
                globalExceptionHandler.handleNotValidException(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ApiResponse<Map<String, List<String>>> body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(Objects.requireNonNull(body).getMessage()).isEqualTo("Validation error");
        assertThat(body.getCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(body.getErrorCode()).isEqualTo(ErrorCode.NESTED_ERROR);

        Map<String, List<String>> errors = body.getError();
        assertThat(errors).isNotNull();
        assertThat(errors.get("field1").getFirst()).contains("must not be null");
        assertThat(errors.get("field2").getFirst()).contains("must not be null");
    }

    @Test
    public void handlePeridotDuplicateException_shouldReturnBadRequestError() throws Exception {
        // given
        PeridotDuplicateException exception = new PeridotDuplicateException("Object already exists");

        // when
        ResponseEntity<ApiResponse<Void>> response = globalExceptionHandler.handlePeridotDuplicateException(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(Objects.requireNonNull(response.getBody()).getCode()).isEqualTo(400);
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("Object already exists");
        assertThat(response.getBody().getErrorCode()).isEqualTo(ErrorCode.DUPLICATE);
        assertThat(response.getBody().getTimestamp()).isNotNull();
    }


    @Test
    public void handleDataIntegrityViolationException_shouldReturnBadRequestError() throws Exception {
        // given
        DataIntegrityViolationException exception = new DataIntegrityViolationException(
                "Detail: Key (email)=(test@mail.com) already exists."
        );

        // when
        ResponseEntity<ApiResponse<Void>> response = globalExceptionHandler.handleDataIntegrityViolationException(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(Objects.requireNonNull(response.getBody()).getCode()).isEqualTo(400);
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("email already exists");
        assertThat(response.getBody().getErrorCode()).isEqualTo(ErrorCode.DUPLICATE);
        assertThat(response.getBody().getTimestamp()).isNotNull();
    }


    @Test
    public void handleNoResourceFoundException_shouldReturn404NotFoundError() throws Exception {
        // given
        NoResourceFoundException exception = new NoResourceFoundException(HttpMethod.POST, "/sample/path");

        // when
        ResponseEntity<ApiResponse<Void>> response = globalExceptionHandler.handleNoResourceFoundException(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(Objects.requireNonNull(response.getBody()).getCode()).isEqualTo(404);
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo(exception.getMessage());
        assertThat(response.getBody().getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND);
        assertThat(response.getBody().getTimestamp()).isNotNull();
    }

    @Test
    public void handleBadCredentialsException_shouldReturnBadRequestError() throws Exception {
        // given
        BadCredentialsException exception = new BadCredentialsException("Invalid username or password");

        // when
        ResponseEntity<ApiResponse<Void>> response = globalExceptionHandler.handleBadCredentials(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(Objects.requireNonNull(response.getBody()).getCode()).isEqualTo(401);
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid username or password");
        assertThat(response.getBody().getErrorCode()).isEqualTo(ErrorCode.UNAUTHORIZED);
        assertThat(response.getBody().getTimestamp()).isNotNull();
    }


    @Test
    public void handleAll_shouldReturn500InternalServerError() throws Exception {
        // given
        Exception exception = new RuntimeException("Something went wrong");

        // when
        ResponseEntity<ApiResponse<Void>> response = globalExceptionHandler.handleAll(exception);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(Objects.requireNonNull(response.getBody()).getCode()).isEqualTo(500);
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("Internal Server Error");
        assertThat(response.getBody().getErrorCode()).isEqualTo(ErrorCode.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getTimestamp()).isNotNull();
    }
}
