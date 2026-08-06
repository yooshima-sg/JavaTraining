package com.s_giken.training.webapp.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.s_giken.training.webapp.model.entity.User;

/**
 * データベースからのT_USERデータをUserオブジェクトにマッピングする
 *
 * @Autowired で注入できるように、DIコンテナのコンポーネントとする。
 */
@Component
public class UserRowMapper implements RowMapper<User> {
    /**
     * マッピング処理を行うメソッド
     *
     * @param rs     データベースからのレコードセット
     * @param rowNum 処理行数
     * @return Userオブジェクト
     * @throws SQLException レコードセットの読み取りに失敗した場合
     */
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setUserName(rs.getString("userName"));
        user.setPassword(rs.getString("password"));
        user.setIsEnabled(rs.getBoolean("enabled"));
        return user;
    }
}
