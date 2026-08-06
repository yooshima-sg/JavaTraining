package com.s_giken.training.webapp.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.s_giken.training.webapp.model.entity.User;

/**
 * ユーザ情報のリポジトリクラス(実体クラス)
 */
@Repository
public class UserRepository implements IUserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userRowMapper;

    /**
     * ユーザ情報のリポジトリクラスのコンストラクタ
     *
     * @param jdbcTemplate  SpringのDIコンテナから渡されるJdbcTemplate
     * @param userRowMapper SpringのDIコンテナから渡されるUserのRowMapper
     */
    public UserRepository(JdbcTemplate jdbcTemplate, RowMapper<User> userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
    }

    /**
     * ユーザ名に一致するユーザ情報を取得する。
     *
     * @param userName 検索するユーザ名
     * @return 一致したUserオブジェクト。存在しない場合はnull
     */
    @Override
    public User findByUsername(String userName) {
        String sql = "SELECT * FROM T_USER WHERE userName = ?";
        User user = null;
        try {
            user = jdbcTemplate.queryForObject(sql, userRowMapper, userName);
        } catch (EmptyResultDataAccessException e) {
            user = null;
        }
        return user;
    }
}
