package com.s_giken.training.webapp.repository;

import org.springframework.stereotype.Repository;

import com.s_giken.training.webapp.model.entity.User;

/**
 * ユーザ情報のリポジトリインターフェース
 */
@Repository
public interface IUserRepository {
    /**
     * ユーザ名に一致するユーザ情報を取得する。
     *
     * @param userName 検索するユーザ名
     * @return 一致したUserオブジェクト。存在しない場合はnull
     */
    public User findByUsername(String userName);
}
