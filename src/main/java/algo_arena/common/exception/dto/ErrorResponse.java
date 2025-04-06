package algo_arena.common.exception.dto;

import algo_arena.common.exception.enums.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private int code;
    private String detail;
    private String message;

    public ErrorResponse(ErrorType errorType) {
        this.code = errorType.getCode();
        this.detail = errorType.getDetail();
        this.message = errorType.getMessage();
    }
}