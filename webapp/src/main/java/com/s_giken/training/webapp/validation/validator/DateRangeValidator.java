package com.s_giken.training.webapp.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;

import com.s_giken.training.webapp.validation.annotation.DateRangeValid;

public class DateRangeValidator implements ConstraintValidator<DateRangeValid, Object> {

    private String startDateFleldName;
    private String endDateFieldName;

    @Override
    public void initialize(DateRangeValid constraintAnnotaion) {
        this.startDateFleldName = constraintAnnotaion.startDateField();
        this.endDateFieldName = constraintAnnotaion.endDateField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            LocalDate startDate = (LocalDate) getFieldValue(value, startDateFleldName);
            LocalDate endDate = (LocalDate) getFieldValue(value, endDateFieldName);

            if (startDate == null || endDate == null) {
                return true;
            }

            if (startDate.isAfter(endDate)) {
                context.disableDefaultConstraintViolation();
                context
                        .buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addPropertyNode(endDateFieldName)
                        .addConstraintViolation();
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException("");
        }

        return true;
    }

    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(object);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchFileException(fieldName + "がクラス内に見つかりません。");
    }
}
