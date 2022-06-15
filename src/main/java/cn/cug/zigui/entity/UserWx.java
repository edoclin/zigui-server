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
 * @since 2022-05-23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_user_wx")
public class UserWx implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "open_id", type = IdType.ASSIGN_UUID)
    private String openId;

    private String userId;

    private String nickName;

    private Integer gender;

    private String country;

    private String province;

    private String city;

    private String mobile;

    @TableLogic
    private Boolean deleted;

    private LocalDateTime createdTime;


}
