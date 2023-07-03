package org.alvin.security.config;

import org.springframework.security.crypto.password.PasswordEncoder;

public class Md5PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        //MD5加密
        
        return null;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        //实现密码匹配

        return false;
    }
}
