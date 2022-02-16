package nbbang.com.nbbang.global.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private int statusCode;
    private String message;
    private List<FieldErrorInfo> errors;

    public ErrorResponse(final int statusCode, final String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.errors = null;
    }

    public static ErrorResponse res(final int statusCode, final String message) {
        return res(statusCode, message, null);
    }

    public static ErrorResponse res(final int statusCode, final String message, final List<FieldErrorInfo> errors) {
        return ErrorResponse.builder()
                .errors(errors)
                .statusCode(statusCode)
                .message(message)
                .build();
    }
}