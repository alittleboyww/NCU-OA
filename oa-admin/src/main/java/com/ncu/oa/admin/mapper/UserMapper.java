package com.ncu.oa.admin.mapper;

import com.ncu.oa.admin.pojo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IDEA
 * author:G.B.Monkey
 * Date:2019/9/26 0026
 * Time:20:29
 */
@Mapper
@Repository
public interface UserMapper {
    //查询所有的用户 分页

    /**
     * 查询所有的用户，并进行分页
     * @param start   开始缩影
     * @param length
     * @return
     */
    @Select("select * from tb_user limit #{start}, #{length}")
    List<User> getUserList(@Param("start") int start, @Param("length") int length);

    //查询用户数量
    @Select("select COUNT(*) from tb_user")
    int count();

    //添加用户
    @Insert("insert into tb_user values(#{user.staffId},#{user.password},#{user.passwordSalt},#{user.status},#{user.createTime},#{user.updateTime})")
    void addUser(User user);

    //
    @Select("select * from tb_user where staffId=#{staffId}")
    User findUserByStaffId(@Param("staffId") int staffId);

}
