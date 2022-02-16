package nbbang.com.nbbang.global.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

// https://devlog-wjdrbs96.tistory.com/197

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DefaultResponse<T> {

    private int statusCode;
    private String message;
    private T data;

    public DefaultResponse(final int statusCode, final String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = null;
    }

    public static<T> DefaultResponse<T> res(final int statusCode, final String message) {
        return res(statusCode, message, null);
    }

    public static<T> DefaultResponse<T> res(final int statusCode, final String message, final T t) {
        return DefaultResponse.<T>builder()
                .data(t)
                .statusCode(statusCode)
                .message(message)
                .build();
    }
}