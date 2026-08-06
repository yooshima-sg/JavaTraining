package com.s_giken.training.webapp.model.form;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 加入者削除フォーム
 *
 * 加入者情報の削除リクエストで受け取る加入者IDを保持する。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteMemberForm {
    @NotNull
    private Long memberId;
}
