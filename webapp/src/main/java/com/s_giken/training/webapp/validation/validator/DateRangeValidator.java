package com.s_giken.training.webapp.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalDate;

import com.s_giken.training.webapp.validation.annotation.DateRangeValid;

/**
 * 開始日と終了日の前後関係を検証するバリデータ
 *
 * {@link DateRangeValid}で指定された2つの日付フィールドを比較し、
 * 終了日が開始日以降であることを検証する。
 */
public class DateRangeValidator implements ConstraintValidator<DateRangeValid, Object> {

    private String startDateFieldName;
    private String endDateFieldName;

    /**
     * アノテーションで指定された日付フィールド名を初期化する。
     *
     * @param constraintAnnotation 検証対象に付与された{@link DateRangeValid}アノテーション
     */
    @Override
    public void initialize(DateRangeValid constraintAnnotation) {
        this.startDateFieldName = constraintAnnotation.startDateField();
        this.endDateFieldName = constraintAnnotation.endDateField();
    }

    /**
     * 終了日が開始日以降であることを検証する。
     *
     * 検証対象、または開始日・終了日のいずれかがnullの場合は検証をスキップし、正常とみなす。
     *
     * @param value   検証対象のオブジェクト
     * @param context バリデーションコンテキスト
     * @return 終了日が開始日以降であればtrue、そうでなければfalse
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        LocalDate startDate = (LocalDate) getFieldValue(value, startDateFieldName);
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

        return true;
    }

    /**
     * リフレクションを用いて、指定した名前のフィールド値を取得する。
     *
     * @param object    値を取得する対象のオブジェクト
     * @param fieldName 取得するフィールド名
     * @return フィールドの値
     * @throws IllegalStateException 指定したフィールドがクラス内に存在しない、またはアクセスできない場合
     */
    private Object getFieldValue(Object object, String fieldName) {
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(object);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(fieldName + "フィールドにアクセスできません。", e);
            }
        }
        throw new IllegalStateException(fieldName + "がクラス内に見つかりません。");
    }
}
