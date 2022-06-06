package cn.cug.zigui.service.impl;

import cn.cug.zigui.entity.UserInfo;
import cn.cug.zigui.mapper.UserInfoMapper;
import cn.cug.zigui.service.IUserInfoService;
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
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

}
