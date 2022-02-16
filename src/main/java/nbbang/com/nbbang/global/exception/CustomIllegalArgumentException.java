package nbbang.com.nbbang.global.exception;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class CustomIllegalArgumentException extends IllegalArgumentException {
    private BindingResult bindingResult;
    public CustomIllegalArgumentException(String message, BindingResult bindingResult) {
        super(message);
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
