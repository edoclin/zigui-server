package cn.cug.zigui.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Zi gui
 * @since 2022-05-20
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.ASSIGN_UUID)
    private String userId;

    private String name;

    private LocalDateTime lastLogin;

    @TableLogic
    private Boolean deleted;

    private LocalDateTime createdTime;

    private String workNumber;

    private String post;

    private String mobile;


}
