package org.alvin.security.config;

import org.alvin.security.component.CustomAccessDecisionManager;
import org.alvin.security.component.CustomFilterInvocationSecurityMetadataSource;
import org.alvin.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 *  注解开启基于注解的安全配置
 *  prePostEnabled = true 会解析@PreAuthorize @PostAuthorize这两个注解
 *  securedEnabled = true 会解析@Secured这个注解
 *  通过实现FilterInvocationSecurityMetadataSource接口，来实现自定义的权限数据源
 *  通过实现AccessDecisionManager接口，来实现自定义的权限决策器
 *  通过实现AccessDeniedHandler接口，来实现自定义的权限异常处理器
 *  通过实现AuthenticationEntryPoint接口，来实现自定义的匿名用户访问无权限资源时的异常处理器
 *  通过实现AuthenticationSuccessHandler接口，来实现自定义的认证成功处理器
 *  通过实现AuthenticationFailureHandler接口，来实现自定义的认证失败处理器
 *  通过实现LogoutSuccessHandler接口，来实现自定义的注销成功处理器
 *  通过实现SessionRegistry接口，来实现自定义的Session注册表
 *  通过实现SessionInformationExpiredStrategy接口，来实现自定义的Session过期策略
 *  通过实现InvalidSessionStrategy接口，来实现自定义的Session失效策略
 *  通过实现RememberMeServices接口，来实现自定义的记住我服务
 *  通过实现UserDetailsService接口，来实现自定义的认证用户获取服务
 *  通过实现PasswordEncoder接口，来实现自定义的密码加密服务
 *  通过实现SecurityContextRepository接口，来实现自定义的安全上下文存取服务
 *  通过实现SecurityContextHolderStrategy接口，来实现自定义的安全上下文持有者策略
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true,securedEnabled = true)
public class AdvanceSecurityConfig extends SecurityConfig{


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

    /**
     * 动态配置访问权限
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //定义FilterSecurityInterceptor将自定义的两个类放进去
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(cfisms());
                        o.setAccessDecisionManager(cadm());
                        return o;
                    }
                })
                .and()
                .formLogin()
                .loginProcessingUrl("/login").permitAll()
                .and()
                .csrf().disable() ;
    }

    @Bean
    CustomFilterInvocationSecurityMetadataSource cfisms(){
    return new CustomFilterInvocationSecurityMetadataSource();
    }

    @Bean
    CustomAccessDecisionManager cadm (){
        return  new CustomAccessDecisionManager();
    }
}
