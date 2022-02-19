package nbbang.com.nbbang.global.error.exception;

import org.springframework.validation.BindingResult;

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
