package org.alvin.security.config;

import org.alvin.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 注解开启基于注解的安全配置
 * prePostEnabled = true 会解析@PreAuthorize @PostAuthorize这两个注解
 * securedEnabled = true 会解析@Secured这个注解
 */
//@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled=true,securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService (userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/db/**").hasRole("DBA")
                .antMatchers("/user/**").hasRole("USER")
                .and()
                .formLogin()
                .loginProcessingUrl("/login").permitAll()
                .and()
                .csrf().disable();
    }
}
