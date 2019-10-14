package com.ncu.oa.admin.mapper;

import com.ncu.oa.admin.pojo.Permission;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IDEA
 * author:G.B.Monkey
 * Date:2019/9/26 0026
 * Time:20:52
 */
@Mapper
@Repository
public interface UserPermissionMapper {
    @Select("select tp.* from tb_permissions as tp left join tb_role_permissions as trp on tp.id=trp.permissionId left join tb_role as tr on tr.id=trp.roleId left join tb_user_role as tur on tr.id = tur.roleId left join tb_user as tu on tu.id=tur.userId where tu.staffId=#{staffId}")
    List<Permission> findPermissionByUsername(@Param("staffId") int staffId);
}
