package cn.cug.zigui.controller;


import cn.cug.zigui.entity.UserRole;
import cn.cug.zigui.service.IUserRoleService;
import cn.cug.zigui.util.ZiguiUtil;
import cn.cug.zigui.util.result.Result;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Zi gui
 * @since 2022-06-04
 */
@RestController
@RequestMapping("/zigui/user-role")
public class UserRoleController {

    @Resource
    private IUserRoleService iUserRoleService;

    @GetMapping("/{userId}/{roleId}")
    public Result<?> addUserRole(@PathVariable String userId, @PathVariable Integer roleId) {
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        if (iUserRoleService.save(userRole)) {
            return ZiguiUtil.ok();
        }
        return ZiguiUtil.error();
    }

    @DeleteMapping("/{userId}/{roleId}")
    public Result<?> delUserRole(@PathVariable String userId, @PathVariable Integer roleId) {
        
        if (iUserRoleService.remove(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, userId).eq(UserRole::getRoleId, roleId))) {
            return ZiguiUtil.ok();
        }
        return ZiguiUtil.error();
    }
}
