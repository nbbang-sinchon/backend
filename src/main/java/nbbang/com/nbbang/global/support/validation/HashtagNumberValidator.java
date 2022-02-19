package nbbang.com.nbbang.global.support.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class HashtagNumberValidator implements ConstraintValidator<HashtagNumber, List<String>> {

    @Override
    public void initialize(HashtagNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<String> values, ConstraintValidatorContext context) {
        if(!values.isEmpty() & values.size()>10){
            return false;
        }
        return true;
    }
}
