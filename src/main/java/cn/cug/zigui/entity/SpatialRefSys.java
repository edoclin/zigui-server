package cn.cug.zigui.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@TableName("spatial_ref_sys")
public class SpatialRefSys implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "srid", type = IdType.ASSIGN_UUID)
    private Integer srid;

    private String authName;

    private Integer authSrid;

    private String srtext;

    private String proj4text;


}
