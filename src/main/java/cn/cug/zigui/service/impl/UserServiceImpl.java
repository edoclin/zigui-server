package cn.cug.zigui.service.impl;

import cn.cug.zigui.entity.User;
import cn.cug.zigui.mapper.UserMapper;
import cn.cug.zigui.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Zi gui
 * @since 2022-04-04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
