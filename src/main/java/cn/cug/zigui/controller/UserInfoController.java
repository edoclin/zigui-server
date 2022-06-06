package cn.cug.zigui.controller;


import cn.cug.zigui.entity.UserInfo;
import cn.cug.zigui.service.IUserInfoService;
import cn.cug.zigui.util.ZiguiUtil;
import cn.cug.zigui.util.result.Result;
import cn.hutool.core.bean.BeanUtil;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Zi gui
 * @since 2022-04-04
 */
@RestController
@RequestMapping("/zigui/user-info")
public class UserInfoController {
    @Resource
    private IUserInfoService iUserInfoService;

    @PutMapping("")
    public Result<?> update(@RequestBody UserInfo userInfo) {
        if (BeanUtil.isNotEmpty(userInfo)) {
              iUserInfoService.updateById(userInfo);
              return ZiguiUtil.ok();
        }
        return ZiguiUtil.error();
    }
}
