package nbbang.com.nbbang.global.error.exception;


// MVC 2 275p
public class UserException extends RuntimeException {
    public UserException() {
        super();
    }
    public UserException(String message) {
        super(message);
    }
    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
    public UserException(Throwable cause) {
        super(cause);
    }
    protected UserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}