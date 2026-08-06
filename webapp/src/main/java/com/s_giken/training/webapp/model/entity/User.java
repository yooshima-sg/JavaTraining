package com.s_giken.training.webapp.model.entity;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザ情報エンティティ
 *
 * T_USERテーブルの1レコードに対応し、認証に使用する。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Nullable
    @Size(min = 1, max = 255)
    private String userName;

    @NotBlank
    @Size(min = 1, max = 255)
    private String password;

    @NotNull
    private Boolean isEnabled;
}
