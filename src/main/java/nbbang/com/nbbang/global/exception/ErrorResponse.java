package nbbang.com.nbbang.global.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.global.response.DefaultResponse;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse<T> {

    private int statusCode;
    private String message;
    private T errors;

    public ErrorResponse(final int statusCode, final String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.errors = null;
    }

    public static<T> ErrorResponse<T> res(final int statusCode, final String message) {
        return res(statusCode, message, null);
    }

    public static<T> ErrorResponse<T> res(final int statusCode, final String message, final T errors) {
        return ErrorResponse.<T>builder()
                .errors(errors)
                .statusCode(statusCode)
                .message(message)
                .build();
    }
}