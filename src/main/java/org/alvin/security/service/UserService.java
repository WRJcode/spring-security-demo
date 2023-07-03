package org.alvin.security.service;

import org.alvin.security.mapper.UserMapper;
import org.alvin.security.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.loadUserByUsername(username);
        if (user != null) {
            user.setRoles(userMapper.getRolesByUserId(user.getId()));
            return user;
        }
        throw new UsernameNotFoundException("用户不存在");
    }
}
