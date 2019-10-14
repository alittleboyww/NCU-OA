package com.ncu.oa.admin.mapper;

import com.ncu.oa.admin.pojo.Role;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IDEA
 * author:G.B.Monkey
 * Date:2019/9/26 0026
 * Time:20:41
 */
@Mapper
@Repository
public interface UserRoleMapper {
    @Select("select tr.* from tb_role as tr left join tb_user_role as tur on tr.id = tur.roleId left join tb_user as tu on tu.id=tur.userId where tu.staffId=#{staffId}")
    List<Role> findRoleByUsername(@Param("staffId") int staffId);
}
