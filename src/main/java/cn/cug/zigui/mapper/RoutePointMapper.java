package cn.cug.zigui.mapper;

import cn.cug.zigui.entity.RoutePoint;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Zi gui
 * @since 2022-04-04
 */
@Mapper
public interface RoutePointMapper extends BaseMapper<RoutePoint> {
    String pointAsGeom(String point);

    int insert(RoutePoint routePoint);

    String pointAsGeoJson(String routePointId);
    Integer updateBaseInfo(RoutePoint point);
    Integer updateContent(RoutePoint point);
}
