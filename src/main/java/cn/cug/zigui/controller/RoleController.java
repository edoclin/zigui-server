package cn.cug.zigui.controller;


import cn.cug.zigui.entity.Role;
import cn.cug.zigui.entity.RolePermission;
import cn.cug.zigui.entity.UserRole;
import cn.cug.zigui.service.IRolePermissionService;
import cn.cug.zigui.service.IRoleService;
import cn.cug.zigui.service.IUserRoleService;
import cn.cug.zigui.util.ZiguiUtil;
import cn.cug.zigui.util.result.Result;
import cn.cug.zigui.vo.VPostRole;
import cn.cug.zigui.vo.VRole;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Zi gui
 * @since 2022-06-04
 */
@RestController
@RequestMapping("/zigui/role")
public class RoleController {

    @Resource
    private IRoleService iRoleService;

    @Resource
    private IRolePermissionService iRolePermissionService;

    @Resource
    private IUserRoleService iUserRoleService;

    @GetMapping("")
    public Result<?> list() {
        List<VRole> result = CollUtil.list(Boolean.TRUE);
        iRoleService.list().forEach(role -> {
            VRole vRole = new VRole();
            BeanUtil.copyProperties(role, vRole);
            vRole.setCreatedTime(ZiguiUtil.formatLocalDateTime_yyyy_MM_dd(role.getCreatedTime()));
            result.add(vRole);
        });
        return ZiguiUtil.ok(result);
    }

    @PostMapping("")
    public Result<?> post(@RequestBody VPostRole role) {
        if (BeanUtil.isNotEmpty(role)) {
            Role insert = new Role();
            BeanUtil.copyProperties(role, insert);

            if (iRoleService.save(insert)) {
                if (CollUtil.isNotEmpty(role.getPermissionIds())) {
                    role.getPermissionIds().forEach(id -> {
                        RolePermission rolePermission = new RolePermission();
                        rolePermission.setRoleId(insert.getRoleId());
                        rolePermission.setPermissionId(id);

                        iRolePermissionService.save(rolePermission);
                    });
                    return ZiguiUtil.ok();
                }
            }
        }

        return ZiguiUtil.error();
    }

    @DeleteMapping("/{roleId}")
    public Result<?> delete(@PathVariable Integer roleId) {

        if (roleId != null) {
            if (iRoleService.removeById(roleId)) {
                iUserRoleService.remove(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getRoleId, roleId));
                return ZiguiUtil.ok();
            }
        }
        return ZiguiUtil.error();
    }
}
