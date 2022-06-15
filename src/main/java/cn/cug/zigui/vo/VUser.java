package cn.cug.zigui.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class VUser {
    String userId;
    String username;
    String role;
    String displayRole;
    String nickName;
    String mobile;
    String openId;
    String country;
    String province;
    String name;
    String lastLogin;
    String createdTime;
    String workNumber;
    String post;
    List<Integer> roles;
}
