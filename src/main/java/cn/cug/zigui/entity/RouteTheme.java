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
@TableName("t_route_theme")
public class RouteTheme implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "route_theme_id", type = IdType.ASSIGN_UUID)
    private String routeThemeId;

    private String routeId;

    private Integer themeId;

    @TableLogic
    private Boolean deleted;

    private LocalDateTime createdTime;


}
