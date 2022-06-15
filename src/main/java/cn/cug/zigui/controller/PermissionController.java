package cn.cug.zigui.controller;


import cn.cug.zigui.entity.Permission;
import cn.cug.zigui.entity.RolePermission;
import cn.cug.zigui.entity.UserRole;
import cn.cug.zigui.service.IPermissionService;
import cn.cug.zigui.service.IRolePermissionService;
import cn.cug.zigui.service.IRouteService;
import cn.cug.zigui.service.IUserRoleService;
import cn.cug.zigui.util.ZiguiUtil;
import cn.cug.zigui.util.result.Result;
import cn.cug.zigui.vo.PermissionJson;
import cn.cug.zigui.vo.VPermission;
import cn.cug.zigui.vo.VPostPermission;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Zi gui
 * @since 2022-06-04
 */
@RestController
@RequestMapping("/zigui/permission")
public class PermissionController {

    @Resource
    private IPermissionService iPermissionService;

    @Resource
    private IRouteService iRouteService;

    @Resource
    private IRolePermissionService iRolePermissionService;

    @Resource
    private IUserRoleService iUserRoleService;

    @GetMapping("")
    public Result<?> list() {
        List<VPermission> result = CollUtil.list(Boolean.TRUE);
        iPermissionService.list().forEach(permission -> {
            VPermission vPermission = new VPermission();
            BeanUtil.copyProperties(permission, vPermission, "permissions");
            vPermission.setPermissions(JSONUtil.toBean(permission.getPermissions(), PermissionJson.class));
            vPermission.setRouteName(iRouteService.getById(permission.getRouteId()).getRouteName());
            vPermission.setCreatedTime(ZiguiUtil.formatLocalDateTime_yyyy_MM_dd(permission.getCreatedTime()));
            result.add(vPermission);
        });
        return ZiguiUtil.ok(result);
    }

//    @PostMapping("")
//    public Result<?> post(@RequestBody Permission permission) {
//        if (BeanUtil.isNotEmpty(permission)) {
//            if (iPermissionService.save(permission)) {
//                return ZiguiUtil.ok();
//            }
//        }
//        return ZiguiUtil.error();
//    }

    @PostMapping("")
    public Result<?> post(@RequestBody VPostPermission permission) {
        if (BeanUtil.isNotEmpty(permission)) {

            Permission insert = new Permission();
            BeanUtil.copyProperties(permission, insert);
            insert.setPermissions(JSONUtil.toJsonStr(permission.getPermissions()));

            if (iPermissionService.save(insert)) {
                VPermission vPermission = new VPermission();
                BeanUtil.copyProperties(permission, vPermission, "permissions");
                vPermission.setPermissions(JSONUtil.toBean(insert.getPermissions(), PermissionJson.class));
                vPermission.setRouteName(iRouteService.getById(permission.getRouteId()).getRouteName());
                vPermission.setCreatedTime(ZiguiUtil.formatLocalDateTime_yyyy_MM_dd(insert.getCreatedTime()));
                return ZiguiUtil.ok(vPermission);
            }
        }
        return ZiguiUtil.error();
    }


    @PutMapping("")
    public Result<?> update(@RequestBody VPostPermission permission) {
        if (BeanUtil.isNotEmpty(permission)) {

            Permission update = new Permission();
            BeanUtil.copyProperties(permission, update);
            update.setPermissions(JSONUtil.toJsonStr(permission.getPermissions()));
            if (iPermissionService.updateById(update)) {
                VPermission vPermission = new VPermission();
                BeanUtil.copyProperties(permission, vPermission, "permissions");
                vPermission.setPermissions(JSONUtil.toBean(update.getPermissions(), PermissionJson.class));
                vPermission.setRouteName(iRouteService.getById(permission.getRouteId()).getRouteName());
                vPermission.setCreatedTime(ZiguiUtil.formatLocalDateTime_yyyy_MM_dd(update.getCreatedTime()));
                return ZiguiUtil.ok(vPermission);
            }
        }
        return ZiguiUtil.error();
    }

    @DeleteMapping("/{permissionId}")
    public Result<?> delete(@PathVariable String permissionId) {
        if (StrUtil.isNotEmpty(permissionId)) {
            if (iPermissionService.removeById(permissionId)) {
                iRolePermissionService.remove(Wrappers.<RolePermission>lambdaQuery().eq(RolePermission::getPermissionId, permissionId));
                return ZiguiUtil.ok();
            }
        }

        return ZiguiUtil.error();
    }


    @GetMapping("/check/{routeId}")
    @SaCheckLogin
    public Result<?> checkUserPermission(@PathVariable String routeId) {
        PermissionJson permissionJson = new PermissionJson();
        permissionJson.setText(false);
        permissionJson.setAudio(false);
        permissionJson.setImage(false);
        permissionJson.setVideo(false);
        iUserRoleService.list(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, StpUtil.getLoginIdAsString())).forEach(userRole -> {
            iRolePermissionService.list(Wrappers.<RolePermission>lambdaQuery().eq(RolePermission::getRoleId, userRole.getRoleId())).forEach(rolePermission -> {
                Permission byId = iPermissionService.getById(rolePermission.getPermissionId());
                if (!StrUtil.equals(byId.getRouteId(), routeId)) {
                    return;
                }
                PermissionJson permission = JSONUtil.toBean(byId.getPermissions(), PermissionJson.class);
                if (permission.getAudio()) {
                    permissionJson.setAudio(Boolean.TRUE);
                }
                if (permission.getText()) {
                    permissionJson.setText(Boolean.TRUE);
                }
                if (permission.getVideo()) {
                    permissionJson.setVideo(Boolean.TRUE);
                }
                if (permission.getImage()) {
                    permissionJson.setImage(Boolean.TRUE);
                }
            });
        });
        return ZiguiUtil.ok(permissionJson);
    }
}
