package nbbang.com.nbbang.global.validator;

import nbbang.com.nbbang.global.validator.PartyId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PartyIdValidator implements ConstraintValidator<PartyId, String> {
    @Override
    public void initialize(PartyId constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            Long.parseLong(value);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
