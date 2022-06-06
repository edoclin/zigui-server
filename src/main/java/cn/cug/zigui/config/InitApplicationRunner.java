package cn.cug.zigui.config;


import cn.cug.zigui.entity.User;
import cn.cug.zigui.entity.UserInfo;
import cn.cug.zigui.service.IUserInfoService;
import cn.cug.zigui.service.IUserService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.Resource;

@Configuration
@Slf4j
@EnableAsync
public class InitApplicationRunner implements ApplicationRunner {


    @Value("${zigui.init-user.username}")
    private String username;

    @Value("${zigui.init-user.password}")
    private String password;

    @Value("${zigui.init-user.role}")
    private String role;

    @Resource
    private IUserService iUserService;

    @Resource
    private IUserInfoService iUserInfoService;
    @Override
    @Async
    public void run(ApplicationArguments args) {
        Page<User> page = new Page<>(1, 1);

        if (CollUtil.isEmpty(iUserService.page(page).getRecords())) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(BCrypt.hashpw(password));
            user.setRole(role);
            if (iUserService.save(user)) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(user.getUserId());
                iUserInfoService.save(userInfo);
                log.info("初始化用户成功");
            }
        }
    }
}
