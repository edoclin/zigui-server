package cn.cug.zigui.mapper;

import cn.cug.zigui.entity.Route;
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
public interface RouteMapper extends BaseMapper<Route> {
    Integer updatePolyline(Route route);
    String toGeoJsonStr(String routeId);
    Integer updateBaseInfo(Route route);
    Integer updateContent(Route route);
}
