package com.fedexu.user.auth.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM USERS WHERE username =#{username}")
    User findByUsername(@Param("username") String username);

    @Select("SELECT * FROM USER_ROLE WHERE username =#{username}")
    List<UserRole> getRoles(@Param("username") String username);

    @Update("UPDATE USERS SET jwt=#{jwt} WHERE username =#{username}")
    void updateJwt(User user);


}