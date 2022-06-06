package cn.cug.zigui.controller;


import cn.cug.zigui.entity.Permission;
import cn.cug.zigui.entity.RolePermission;
import cn.cug.zigui.service.IPermissionService;
import cn.cug.zigui.service.IRolePermissionService;
import cn.cug.zigui.service.IRouteService;
import cn.cug.zigui.util.ZiguiUtil;
import cn.cug.zigui.util.result.Result;
import cn.cug.zigui.vo.VPermission;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
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
@RequestMapping("/zigui/permission")
public class PermissionController {

    @Resource
    private IPermissionService iPermissionService;

    @Resource
    private IRouteService iRouteService;

    @Resource
    private IRolePermissionService iRolePermissionService;

    @GetMapping("")
    public Result<?>list() {
        List<VPermission> result = CollUtil.list(Boolean.TRUE);
        iPermissionService.list().forEach(permission -> {
            VPermission vPermission = new VPermission();
            BeanUtil.copyProperties(permission, vPermission);
            vPermission.setRouteName(iRouteService.getById(permission.getRouteId()).getRouteName());
            vPermission.setCreatedTime(ZiguiUtil.formatLocalDateTime_yyyy_MM_dd(permission.getCreatedTime()));
            result.add(vPermission);
        });
        return ZiguiUtil.ok(result);
    }

    @PostMapping("")
    public Result<?> post(@RequestBody Permission permission) {
        if (BeanUtil.isNotEmpty(permission)) {
            if (iPermissionService.save(permission)) {
                return ZiguiUtil.ok();
            }
        }
        return ZiguiUtil.error();
    }

    @PutMapping("")
    public Result<?> update(@RequestBody Permission permission) {
        if (BeanUtil.isNotEmpty(permission)) {
            if (iPermissionService.updateById(permission)) {
                return ZiguiUtil.ok();
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
}
