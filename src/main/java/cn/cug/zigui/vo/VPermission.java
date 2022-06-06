package cn.cug.zigui.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author Zi gui
 * @since 2022-06-04
 */
@Data
public class VPermission {
    private String permissionId;
    private String permissionName;
    private String permissionDesc;
    private String routeId;
    private String routeName;
    private String  createdTime;
}
