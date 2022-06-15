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
@TableName("t_resource")
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "resource_id", type = IdType.ASSIGN_UUID)
    private String resourceId;

    private String pointId;

    private String resourceType;
    private String originalName;

    private String addition;
    private String accessUrl;

    @TableLogic
    private Boolean deleted;

    private LocalDateTime createdTime;


}
