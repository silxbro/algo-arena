package algo_arena.common.exception.dto;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private int status;
    private String detail;
    private String message;
    private String path;
    private Timestamp timestamp;

}