package cn.cug.zigui.controller;


import cn.cug.zigui.entity.*;
import cn.cug.zigui.service.*;
import cn.cug.zigui.util.ZiguiUtil;
import cn.cug.zigui.util.result.Result;
import cn.cug.zigui.vo.PermissionJson;
import cn.cug.zigui.vo.VResource;
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
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Zi gui
 * @since 2022-05-02
 */
@RestController
@RequestMapping("/zigui/resource")
public class ResourceController {

    @Resource
    private IResourceService iResourceService;

    @Resource
    private IUserRoleService iUserRoleService;

    @Resource
    private IRolePermissionService iRolePermissionService;

    @Resource
    private IPermissionService iPermissionService;

    @Resource
    private IRoutePointService iRoutePointService;

    @PostMapping("")
    public Result<?> post(@RequestBody cn.cug.zigui.entity.Resource resource) {
        if (BeanUtil.isNotEmpty(resource)) {
            if (iResourceService.save(resource)) {
                VResource vResource = new VResource();
                BeanUtil.copyProperties(resource, vResource);
                vResource.setCreatedTime(ZiguiUtil.formatLocalDateTime_yyyy_MM_dd(resource.getCreatedTime()));
                return ZiguiUtil.ok(vResource);
            }
        }
        return ZiguiUtil.error();
    }

    @GetMapping("/{pointId}")
    public Result<?> listByPointId(@PathVariable String pointId) {
        if (StrUtil.isNotEmpty(pointId)) {
            List<Object> result = CollUtil.list(Boolean.TRUE);
            iResourceService.list(Wrappers.<cn.cug.zigui.entity.Resource>lambdaQuery().eq(cn.cug.zigui.entity.Resource::getPointId, pointId)).forEach(item -> {
                VResource vResource = new VResource();
                BeanUtil.copyProperties(item, vResource);
                vResource.setCreatedTime(ZiguiUtil.formatLocalDateTime_yyyy_MM_dd(item.getCreatedTime()));
                result.add(vResource);
            });
            return ZiguiUtil.ok(result);
        }
        return ZiguiUtil.error();
    }
    
    @DeleteMapping("/{pointId}")
    public Result<?> delete(@PathVariable String pointId) {
        if (StrUtil.isNotEmpty(pointId)) {
            if (iResourceService.removeById(pointId)) {
                return ZiguiUtil.ok();
            }
        }
        return ZiguiUtil.error();
    }

    @GetMapping("/wx/{pointId}/{type}")
    @SaCheckLogin
    public Result<?> getResourceByPointIdAndType(@PathVariable String pointId, @PathVariable String type) {
        AtomicReference<Boolean> allowed = new AtomicReference<>(false);
        if (StrUtil.isNotEmpty(pointId) && StrUtil.isNotEmpty(type)) {
            if (StrUtil.containsAny(type, "text", "image", "audio", "video")) {
                RoutePoint routePoint = iRoutePointService.getById(pointId);
                if (BeanUtil.isNotEmpty(routePoint)) {
                    for (UserRole userRole : iUserRoleService.list(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, StpUtil.getLoginIdAsString()))) {
                        for (RolePermission rolePermission : iRolePermissionService.list(Wrappers.<RolePermission>lambdaQuery().eq(RolePermission::getRoleId, userRole.getRoleId()))) {
                            Permission byId = iPermissionService.getById(rolePermission.getPermissionId());
                            if (BeanUtil.isNotEmpty(byId) && StrUtil.equals(byId.getRouteId(), routePoint.getRouteId())) {
                                PermissionJson permissions = JSONUtil.toBean(byId.getPermissions(), PermissionJson.class);
                                switch (type) {
                                    case "text":
                                        allowed.set(permissions.getText());
                                        break;
                                    case "image":
                                        allowed.set(permissions.getImage());
                                        break;
                                    case "audio":
                                        allowed.set(permissions.getAudio());
                                        break;
                                    case "video":
                                        allowed.set(permissions.getVideo());
                                        break;
                                }

                                if (allowed.get()) {
                                    return ZiguiUtil.ok(iResourceService.list(Wrappers.<cn.cug.zigui.entity.Resource>lambdaQuery().eq(cn.cug.zigui.entity.Resource::getPointId, pointId).eq(cn.cug.zigui.entity.Resource::getResourceType, type)));
                                }
                            }
                        }
                    }
                }
            }
        }

        return ZiguiUtil.ok();
    }
}
