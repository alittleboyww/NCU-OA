package com.ncu.oa.admin.shiro;

import com.ncu.oa.admin.mapper.UserMapper;
import com.ncu.oa.admin.mapper.UserPermissionMapper;
import com.ncu.oa.admin.mapper.UserRoleMapper;
import com.ncu.oa.admin.pojo.Permission;
import com.ncu.oa.admin.pojo.Role;
import com.ncu.oa.admin.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IDEA
 * author:G.B.Monkey
 * Date:2019/9/26 0026
 * Time:10:56
 */
public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    public UserMapper userMapper;

    @Autowired
    public UserRoleMapper roleMapper;

    @Autowired
    public UserPermissionMapper permissionMapper;
    /**
     * 获取权限或权限判断时调用这个方法
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String staffId = (String) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        List<Role> roles = roleMapper.findRoleByUsername(Integer.parseInt(staffId));
        List<Permission> permissions = permissionMapper.findPermissionByUsername(Integer.parseInt(staffId));
        //用户对应的角色
        Set<String> roleSet = new HashSet<>();
        for (Role role: roles){
            roleSet.add(role.getRoleName());
        }
        info.setRoles(roleSet);
        //用户对应的权限
        Set<String> permissionSet = new HashSet<>();
        for (Permission permission : permissions) {
            permissionSet.add(permission.getPermissionName());
        }
        info.setStringPermissions(permissionSet);
        return info;
    }

    /**
     * 登录时执行的方法
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String staffId = (String) authenticationToken.getPrincipal();
        String password = new String((char []) authenticationToken.getCredentials());
        User user = userMapper.findUserByStaffId(Integer.parseInt(staffId));
        if(user == null){
            throw new UnknownAccountException();
        }
        ByteSource salt = ByteSource.Util.bytes(""+staffId);
        return new SimpleAuthenticationInfo(staffId,password,salt,getName());
    }
}