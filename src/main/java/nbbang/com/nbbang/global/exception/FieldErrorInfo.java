package nbbang.com.nbbang.global.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FieldErrorInfo{
    private String field;
    private String errorMessage;
}