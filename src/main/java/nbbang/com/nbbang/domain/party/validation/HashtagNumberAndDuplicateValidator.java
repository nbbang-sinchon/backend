package nbbang.com.nbbang.domain.party.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

import static nbbang.com.nbbang.domain.party.controller.PartyResponseMessage.*;

public class HashtagNumberAndDuplicateValidator implements ConstraintValidator<HashtagNumberAndDuplicate, List<String>> {

    @Override
    public void initialize(HashtagNumberAndDuplicate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<String> contents, ConstraintValidatorContext context) {
        boolean isValid = true;
        if (contents != null) {
            if (contents.size() > 10) {
                addConstraintViolation(context, HASHTAG_NUMBER_ERROR);
                isValid = false;
            }
            if (contents.size() != contents.stream().distinct().count()) {
                addConstraintViolation(context, HASHTAG_DUPLICATE_ERROR);
                isValid = false;
            }
            if (contents.stream().anyMatch(content -> content.trim().isEmpty())) {
                addConstraintViolation(context, HASHTAG_BLANK_ERROR);
                isValid = false;
            }
        }
        return isValid;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String msg) {
        //기본 메시지 비활성화
        context.disableDefaultConstraintViolation();
        //새로운 메시지 추가
        context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    }
}
