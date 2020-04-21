package com.ycjd.payslip.dao.perm;

import java.util.List;

import com.ycjd.payslip.pojo.perm.Role;
import com.ycjd.payslip.pojo.perm.RolePerm;
import com.ycjd.payslip.pojo.perm.WorkRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IRoleDao {
/*-------------------------------------------------generated code start,do not update-----------------------------------------------------------*/
    List<Role> findRoleListByCondition(Role role);

    long findRoleCountByCondition(Role role);

    Role findOneRoleByCondition(Role role);

    Role findRoleById(@Param("id") String id);

    void saveRole(Role role);

    void updateRole(Role role);

    void deleteRole(@Param("id") String id);

    void deleteRoleByCondition(Role role);

    void batchSaveRole(List<Role> roles);


    /*-------------------------------------------------generated code end,do not update-----------------------------------------------------------*/
    List<String> findPermChecked(String roleId);

    void deleteRolePermByRoleId(String roleId);

    void batchSaveRolePerm(List<RolePerm> rolePerms);

    List<String> findCheckedRoloIdsByAdminId(@Param("adminId")String id);

    void deleteAdminRoleByAdminId(@Param("adminId")String adminId);

    void batchSaveAdminRole(List<WorkRole> list);
}
