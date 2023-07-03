package org.alvin.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 注解开启基于注解的安全配置
 * prePostEnabled = true 会解析@PreAuthorize @PostAuthorize这两个注解
 * securedEnabled = true 会解析@Secured这个注解
 */
//@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled=true,securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").access( "hasAnyRole('ADMIN','USER') ")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .loginPage("/login_page")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler((req, resp, authentication) -> {
                    Object principal = authentication.getPrincipal();
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter out = resp.getWriter();
                    resp.setStatus(200);
                    Map<String,Object> map = new HashMap<>();

                    map.put("status",200);
                    map.put("msg",principal);

                    ObjectMapper om = new ObjectMapper();
                    out.write(om.writeValueAsString(map));
                    out.flush();
                    out.close();
                })
                .failureHandler((req, resp, e) -> {
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter out = resp.getWriter();
                    resp.setStatus(401);
                    Map<String,Object> map = new HashMap<>();

                    map.put("status",401);
                    map.put("msg","登录失败");
                    if (e instanceof LockedException) {
                        map.put("msg","账户被锁定，登录失败!");
                    } else if (e instanceof BadCredentialsException) {
                        map.put("msg","账户名或密码输入错误，登录失败!");
                    } else if (e instanceof DisabledException) {
                        map.put("msg","账户被禁用，登录失败!");
                    } else if (e instanceof AccountExpiredException) {
                        map.put("msg","账户已过期，登录失败!");
                    } else if (e instanceof CredentialsExpiredException) {
                        map.put("msg","密码已过期，登录失败!");
                    } else {
                        map.put("msg","登录失败!");
                    }

                    ObjectMapper om = new ObjectMapper();
                    out.write(om.writeValueAsString(map));
                    out.flush();
                    out.close();
                })
                .and()
                .logout()
                //注销登录请求url
                .logoutUrl("/logout")
                //清除身份认证信息
                .clearAuthentication(true)
                //使 Session失效
                .invalidateHttpSession(true)
                //定义注销成功的业务逻辑，这里返回一段json
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        out.write("logout success");
                        out.flush();
                    }
                })
                .permitAll()
                .and()
                .csrf()
                .disable();

    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("zhangsan").roles("ADMIN").password("$2a$10$dQLFreAJHM0F.4XAWQMTA.kB5W3H2.hjA6xBUJFHTFT7iHRzO0flm")
                .and()
                .withUser("lisi").roles("USER").password("$2a$10$dQLFreAJHM0F.4XAWQMTA.kB5W3H2.hjA6xBUJFHTFT7iHRzO0flm");
    }

}
