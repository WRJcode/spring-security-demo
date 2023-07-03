package org.alvin.security.mapper;

import org.alvin.security.pojo.Role;
import org.alvin.security.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    User loadUserByUsername(String username);
    List<Role> getRolesByUserId(Integer id);
}
