package com.s_giken.training.webapp.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import com.s_giken.training.webapp.validation.validator.DateRangeValidator;

@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DateRangeValid {
    String message() default "終了日は開始日以降の日付を入力してください。";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String startDateField() default "startDate";

    String endDateField() default "endDate";
}
