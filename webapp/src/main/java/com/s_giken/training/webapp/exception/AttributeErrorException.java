package com.s_giken.training.webapp.exception;

/**
 * 属性(プロパティ)の値が不正な場合の例外クラス
 */
public class AttributeErrorException extends RuntimeException {
    /**
     * コンストラクタ
     *
     * @param message エラーメッセージ
     */
    public AttributeErrorException(String message) {
        super(message);
    }
}
