package algo_arena.common.exception.handler;

import static algo_arena.common.exception.enums.ErrorType.*;

import algo_arena.common.exception.BaseException;
import algo_arena.common.exception.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(BaseException e, HttpServletRequest request) {
        log.error("[BaseException] url: {} | errorType: {} | errorMessage: {} | cause Exception: ",
            request.getRequestURL(), e.getErrorType(), e.getMessage(), e.getCause());

        return ResponseEntity
            .status(e.getErrorType().getHttpStatus())
            .body(new ErrorResponse(e.getErrorType()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMethodException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("[HttpRequestMethodNotSupportedException] " +
                "url: {} | errorType: {} | errorMessage: {} | cause Exception: ",
            request.getRequestURL(), INVALID_HTTP_METHOD, INVALID_HTTP_METHOD.getMessage(), e);

        return ResponseEntity
            .status(INVALID_HTTP_METHOD.getHttpStatus())
            .body(new ErrorResponse(INVALID_HTTP_METHOD));
    }

    // Validation Exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String validationMessage = Objects.requireNonNull(e.getFieldError()).getDefaultMessage();
        log.error("[MethodArgumentNotValidException] "
                + "url: {} | errorType: {} | errorMessage: {} | cause Exception: ",
            request.getRequestURL(), INVALID_VALUE, validationMessage, e);

        BaseException baseException = new BaseException(INVALID_VALUE, validationMessage);
        return ResponseEntity
            .status(INVALID_VALUE.getHttpStatus())
            .body(new ErrorResponse(INVALID_VALUE));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e, HttpServletRequest request) {
        log.error("[Common Exception] url: {} | errorMessage: {}",
            request.getRequestURL(), e.getMessage());
        return ResponseEntity
            .status(SERVER_ERROR.getHttpStatus())
            .body(new ErrorResponse(SERVER_ERROR));
    }

}