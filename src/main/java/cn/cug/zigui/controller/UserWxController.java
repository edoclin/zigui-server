package cn.cug.zigui.controller;


import cn.cug.zigui.entity.User;
import cn.cug.zigui.entity.UserInfo;
import cn.cug.zigui.entity.UserWx;
import cn.cug.zigui.service.IUserInfoService;
import cn.cug.zigui.service.IUserService;
import cn.cug.zigui.service.IUserWxService;
import cn.cug.zigui.util.ZiguiUtil;
import cn.cug.zigui.util.result.Result;
import cn.cug.zigui.vo.VRegistryUserInfo;
import cn.cug.zigui.vo.VUserInfo;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Zi gui
 * @since 2022-05-23
 */
@RestController
@RequestMapping("/zigui/user-wx")
public class UserWxController {

    @Resource
    private IUserWxService iUserWxService;

    @Resource
    private IUserService iUserService;

    @Resource
    private IUserInfoService iUserInfoService;


    @GetMapping("/user-info/{openid}")
    public Result<?> getUserInfoByOpenId(@PathVariable String openid) {
        if (StrUtil.isNotEmpty(openid)) {
            UserWx byId = iUserWxService.getById(openid);
            if (BeanUtil.isNotEmpty(byId)) {
                StpUtil.login(byId.getUserId());
                VUserInfo vUserInfo = new VUserInfo();
                UserInfo userInfo = iUserInfoService.getById(byId.getUserId());

                BeanUtil.copyProperties(userInfo, vUserInfo);
                vUserInfo.setUserId(byId.getUserId());
                vUserInfo.setTokenName(StpUtil.getTokenName());
                vUserInfo.setTokenValue(StpUtil.getTokenValue());
                
                BeanUtil.copyProperties(byId, vUserInfo);

                return ZiguiUtil.ok(vUserInfo);
            } else {
                return ZiguiUtil.ok();
            }
        }
        return ZiguiUtil.error("参数错误");
    }

    @PutMapping("user-info")
    public Result<?> update(@RequestBody UserWx userWx) {
        if (BeanUtil.isNotEmpty(userWx)) {
            iUserWxService.updateById(userWx);
        }
        return ZiguiUtil.ok();
    }

    @PostMapping("/auto-register")
    public Result<?> autoRegisterByOpenId(@RequestBody UserWx userWx) {
        String password = RandomUtil.randomString(6);
        User user = new User();
        user.setUsername("");
        user.setPassword(BCrypt.hashpw(password));
        if (iUserService.save(user)) {
            userWx.setUserId(user.getUserId());

            if (iUserWxService.save(userWx)) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(user.getUserId());
                userInfo.setName("");
                if (iUserInfoService.save(userInfo)) {
                    VRegistryUserInfo vRegistryUserInfo = new VRegistryUserInfo();
                    vRegistryUserInfo.setUserId(user.getUserId());
                    vRegistryUserInfo.setPassword(password);
                    return ZiguiUtil.ok(vRegistryUserInfo);
                }
            }
        }
        return ZiguiUtil.ok();
    }

    @GetMapping("/check")
    @SaCheckLogin
    public Result<?> checkLogin() {
        return ZiguiUtil.ok();
    }

    @GetMapping("/logout")
    @SaCheckLogin
    public Result<?> logout() {
        StpUtil.logout();
        return ZiguiUtil.ok();
    }
}
