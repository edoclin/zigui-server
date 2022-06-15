package cn.cug.zigui.controller;


import cn.cug.zigui.entity.User;
import cn.cug.zigui.entity.UserInfo;
import cn.cug.zigui.entity.UserRole;
import cn.cug.zigui.entity.UserWx;
import cn.cug.zigui.service.*;
import cn.cug.zigui.util.ZiguiUtil;
import cn.cug.zigui.util.result.Result;
import cn.cug.zigui.vo.VUser;
import cn.cug.zigui.vo.VUserInfo;
import cn.cug.zigui.vo.VWeixin;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.temp.SaTempUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Zi gui
 * @since 2022-04-04
 */
@RestController
@RequestMapping("/zigui/user")
public class UserController {

    @Resource
    private IUserService iUserService;

    @Resource
    private IUserInfoService iUserInfoService;

    @Resource
    private IUserWxService iUserWxService;

    @Resource
    private IUserRoleService iUserRoleService;

    @Resource
    private IRoleService iRoleService;

    @PostMapping("login")
    public Result<?> login(@RequestBody User user) {
        if (BeanUtil.isNotEmpty(user)) {
            User one = iUserService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));
            if (BeanUtil.isNotEmpty(one)) {
                if (BCrypt.checkpw(user.getPassword(), one.getPassword())) {
                    StpUtil.login(one.getUserId());
                    VUserInfo vUserInfo = new VUserInfo();
                    BeanUtil.copyProperties(one, vUserInfo);
                    vUserInfo.setDisplayRole(ZiguiUtil.toDisplayRoleName(one.getRole()));
                    UserInfo userInfo = iUserInfoService.getById(one.getUserId());
                    BeanUtil.copyProperties(userInfo, vUserInfo);
                    vUserInfo.setCreatedTime(ZiguiUtil.formatLocalDateTime_yyyy_MM_dd(one.getCreatedTime()));
                    vUserInfo.setLastLogin(ZiguiUtil.formatLocalDateTime_yyyy_MM_dd_HH_mm(userInfo.getLastLogin()));
                    userInfo.setLastLogin(LocalDateTime.now());
                    iUserInfoService.updateById(userInfo);
                    return ZiguiUtil.ok(vUserInfo);
                }
            }
        }
        return ZiguiUtil.error("用户名或密码错误");
    }

    @GetMapping("/logout")
    @SaCheckLogin
    public Result<?> logout() {
        StpUtil.logout();
        return ZiguiUtil.ok();
    }

    @GetMapping("")
    public Result<?> list() {
        List<VUser> result = CollUtil.list(Boolean.TRUE);
        iUserService.list().forEach(item -> {
            VUser vUser = new VUser();
            BeanUtil.copyProperties(item, vUser);
            vUser.setRoles(CollUtil.list(Boolean.TRUE));
            iUserRoleService.list(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, item.getUserId())).forEach(userRole -> {
                vUser.getRoles().add(userRole.getRoleId());
            });
            vUser.setDisplayRole(ZiguiUtil.toDisplayRoleName(item.getRole()));
            UserInfo byId = iUserInfoService.getById(item.getUserId());
            BeanUtil.copyProperties(byId, vUser);
            UserWx one = iUserWxService.getOne(Wrappers.<UserWx>lambdaQuery().eq(UserWx::getUserId, item.getUserId()));
            if (BeanUtil.isEmpty(one)) {
                  vUser.setOpenId("");
                  vUser.setNickName("");
            } else {
                BeanUtil.copyProperties(one, vUser);

            }
            vUser.setLastLogin(ZiguiUtil.formatLocalDateTime_yyyy_MM_dd_HH_mm(byId.getLastLogin()));
            vUser.setCreatedTime(ZiguiUtil.formatLocalDateTime_yyyy_MM_dd(item.getCreatedTime()));
            result.add(vUser);
        });
        return ZiguiUtil.ok(result);
    }

    @GetMapping("/change-role/{userId}/{role}")
    public Result<?> changeRoleByUserId(@PathVariable String userId, @PathVariable String role) {

        if (StrUtil.isNotEmpty(userId) && StrUtil.isNotEmpty(role)) {
            User byId = iUserService.getById(userId);
            if (BeanUtil.isNotEmpty(byId)) {
                byId.setRole(role);
                iUserService.updateById(byId);
                return ZiguiUtil.ok();
            }
        }
        return ZiguiUtil.ok();
    }


    @PostMapping("")
    public Result<?> post(@RequestBody VUser user) {

        if (BeanUtil.isNotEmpty(user)) {
         if (StrUtil.isNotEmpty(user.getUserId())) {
             UserInfo byId = iUserInfoService.getById(user.getUserId());
             if (BeanUtil.isNotEmpty(byId)) {
                   BeanUtil.copyProperties(user, byId);
                   iUserInfoService.updateById(byId);
                   return ZiguiUtil.ok();
             }
         }
        }
        return ZiguiUtil.ok();
    }
    
    @GetMapping("/weixin/{code}")
    public Result<?> parseCode(@PathVariable String code) {
        VWeixin vWeixin = JSONUtil.toBean(HttpUtil.get(StrUtil.format("https://api.weixin.qq.com/sns/jscode2session?appid=wxd5a79aff2b14d08d&secret=5d8a025a52218b4bc5a8ea183efdd535&js_code={}&grant_type=authorization_code", code)), VWeixin.class);
        return ZiguiUtil.ok(vWeixin);
    }
}
