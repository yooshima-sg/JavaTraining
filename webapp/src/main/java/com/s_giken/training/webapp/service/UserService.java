package com.s_giken.training.webapp.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.s_giken.training.webapp.model.entity.User;
import com.s_giken.training.webapp.repository.IUserRepository;

/**
 * 認証機能のサービスクラス
 *
 * Spring Securityの{@link UserDetailsService}を実装し、ユーザ名からユーザ情報を取得する。
 */
@Service
public class UserService implements UserDetailsService {
    private final IUserRepository userRepository;

    /**
     * 認証機能のサービスクラスのコンストラクタ
     *
     * @param userRepository ユーザ情報のリポジトリクラス(SpringのDIコンテナから渡される)
     */
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * ユーザ名をもとにユーザ情報を取得し、認証に使用するUserDetailsを生成する。
     *
     * @param userName ログイン画面で入力されたユーザ名
     * @return 認証に使用するUserDetailsオブジェクト
     * @throws UsernameNotFoundException 指定したユーザ名のユーザが存在しない場合
     */
    @Override
    public UserDetails loadUserByUsername(String userName) {
        User user = userRepository.findByUsername(userName);
        if (user == null) {
            throw new UsernameNotFoundException("Username is not found.");
        }
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .disabled(!user.getIsEnabled())
                .roles("USER")
                .build();
    }
}
