package com.training.bloggingsite.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@Order(1)
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/register/**","/add-post/**","/save-post/**").permitAll()
                                .requestMatchers("/js/**", "/css/**","/lib/**","/scss/**","/img/**").permitAll()
                                .requestMatchers("admin/js/**", "admin/css/**","admin/lib/**","admin/scss/**","admin/img/**").hasAnyAuthority("ADMIN")
                                .requestMatchers("user/js/**", "user/css/**","user/lib/**","user/scss/**","admin/img/**").hasAnyAuthority("USER")
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/home").permitAll()
                                .requestMatchers("/user/getAllCategories").anonymous()
                                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                                .requestMatchers("/user/**").hasAnyAuthority("USER","ADMIN")
                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .usernameParameter("email")
                                .defaultSuccessUrl("/home")
                                .permitAll()
                                .successHandler(new UserHandler())
                ).logout(
                logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/")
                        .permitAll()
        );
        return http.build();
    }
}
