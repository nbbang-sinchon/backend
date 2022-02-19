package nbbang.com.nbbang.global.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;

@Constraint(validatedBy = PartyIdValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD,PARAMETER })
public @interface PartyId{
    public String message() default "order id not found ";
    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default{};
}