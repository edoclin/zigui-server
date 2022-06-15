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
@TableName("t_theme")
public class Theme implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "theme_id", type = IdType.AUTO)
    private Integer themeId;

    private String themeName;

    private String themeDesc;

    @TableLogic
    private Boolean deleted;

    private LocalDateTime createdTime;


}
