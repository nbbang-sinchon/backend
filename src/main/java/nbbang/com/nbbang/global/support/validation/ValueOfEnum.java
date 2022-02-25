package nbbang.com.nbbang.global.support.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validations for Enum Types
 * 5. Validating That a String Matches a Value of an Enum
 * Instead of validating an enum to match a String, we could also do the opposite. For this, we can create an annotation that checks if the String is valid for a specific enum
 *
 * This annotation can be added to a String field and we can pass any enum class.
 *
 * @ValueOfEnum(enumClass = CustomerType.class)
 * private String customerTypeString;
 *
 * Reference: https://www.baeldung.com/javax-validations-enums
 */



@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ValueOfEnumValidator.class)
public @interface ValueOfEnum {
    Class<? extends Enum<?>> enumClass();
    String message() default "Place 값은 NONE / SINCHON / CHANGCHEON / YEONHUI 중 하나여야 합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}