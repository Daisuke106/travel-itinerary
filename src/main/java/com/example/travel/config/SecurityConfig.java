package com.example.travel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * セキュリティ設定
 *
 * 【設定方針】
 * - 認証は未導入だが CSRF 保護とセキュリティヘッダーを有効化する。
 * - H2コンソール(開発用)は CSRF の対象外とし、フレーム表示を許可する。
 * - Spring Security のデフォルトログインページは無効化する。
 *
 * 【Spring Security が自動で付与するレスポンスヘッダー】
 * - X-Content-Type-Options: nosniff (MIMEスニッフィング攻撃の防止)
 * - X-XSS-Protection: 0 (モダンブラウザ向け推奨値)
 * - Cache-Control: no-cache (認証ページのキャッシュ防止)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 全URLを認証なしで許可 (認証機能は未導入のため)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                // CSRF保護を有効化。H2コンソールはAjax/フォームが独自のため対象外
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                // H2コンソールはiframeを使用するため SAMEORIGIN に変更 (デフォルトは DENY)
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                // Spring Security のデフォルトログイン/Basic認証UIを無効化
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
