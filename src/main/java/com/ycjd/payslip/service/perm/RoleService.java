package com.ycjd.payslip.service.perm;

import com.ycjd.payslip.dao.perm.IRoleDao;
import com.ycjd.payslip.pojo.ResponseJson;
import com.ycjd.payslip.pojo.SequenceId;
import com.ycjd.payslip.pojo.perm.Perm;
import com.ycjd.payslip.pojo.perm.Role;
import com.ycjd.payslip.pojo.perm.RolePerm;
import com.ycjd.payslip.pojo.perm.WorkRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RoleService {
    @Autowired
    private IRoleDao roleDao;
    @Autowired
    private SequenceId sequenceId;
/*-------------------------------------------------generated code start,do not update-----------------------------------------------------------*/
    @Transactional(readOnly = true)
    public Role findRoleById(String id) {
        return roleDao.findRoleById(id);
    }
    @Transactional
    public void saveRole(Role role) {
        role.setId(sequenceId.nextId());
        roleDao.saveRole(role);
    }
    @Transactional(readOnly = true)
    public List<Role> findRoleListByCondition(Role role) {
        return roleDao.findRoleListByCondition(role);
    }
    @Transactional(readOnly = true)
    public Role findOneRoleByCondition(Role role) {
        return roleDao.findOneRoleByCondition(role);
    }
    @Transactional(readOnly = true)
    public long findRoleCountByCondition(Role role) {
        return roleDao.findRoleCountByCondition(role);
    }
    @Transactional
    public void updateRole(Role role) {
        roleDao.updateRole(role);
    }
    @Transactional
    public void deleteRole(String id) {
        roleDao.deleteRole(id);
    }
    @Transactional
    public void deleteRoleByCondition(Role role) {
        roleDao.deleteRoleByCondition(role);
    }
    @Transactional
    public void batchSaveRole(List<Role> roles){
        roles.forEach(role -> role.setId(sequenceId.nextId()));
        roleDao.batchSaveRole(roles);
    }

    public List<String> findPermChecked(String roleId) {
    return     roleDao.findPermChecked(roleId);
    }


    /*-------------------------------------------------generated code end,do not update-----------------------------------------------------------*/

    @Transactional
    public void delsertRolePerms(Map<String, String> map) {
        String roleId = map.get("roleId");
        String permIds = map.get("permIds");
        roleDao.deleteRolePermByRoleId(roleId);
        if(permIds!=null){
            String[] permArr = permIds.split(",");
            if(permArr.length>0){
                List<RolePerm> rolePerms = new ArrayList<>();
                for (String permId : permArr) {
                    RolePerm rolePerm = new RolePerm();
                    rolePerm.setId(sequenceId.nextId());
                    rolePerm.setRoleId(roleId);
                    rolePerm.setPermId(permId);
                    rolePerms.add(rolePerm);
                }
                roleDao.batchSaveRolePerm(rolePerms);
            }
        }
    }


    public List<String> findCheckedRoloIdsByAdminId(String id) {
       return roleDao.findCheckedRoloIdsByAdminId(id);
    }


    @Transactional
    public void delsertAdminRoles(Map<String,String> map) {
        String adminId = map.get("adminId");

        roleDao.deleteAdminRoleByAdminId(adminId);
        List<WorkRole> list = new ArrayList<>();
        String roleStr = map.get("roleIds");
        if(roleStr==null)return;
        String[] roleIds= roleStr.split(",");
        if(roleIds.length>0){
            for (String roleId : roleIds) {
                WorkRole workRole = new WorkRole();
                workRole.setId(sequenceId.nextId());
                workRole.setWorkId(adminId);
                workRole.setRoleId(roleId);
                list.add(workRole);
            }
            roleDao.batchSaveAdminRole(list);
        }

    }
}
