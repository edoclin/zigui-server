package cn.cug.zigui.controller;
import java.time.LocalDateTime;


import cn.cug.zigui.entity.Route;
import cn.cug.zigui.entity.RoutePoint;
import cn.cug.zigui.mapper.RoutePointMapper;
import cn.cug.zigui.service.IRoutePointService;
import cn.cug.zigui.util.ZiguiUtil;
import cn.cug.zigui.util.result.Result;
import cn.cug.zigui.vo.VPostPoint;
import cn.cug.zigui.vo.VPutDesc;
import cn.cug.zigui.vo.VPutPoint;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Zi gui
 * @since 2022-04-04
 */
@RestController
@RequestMapping("/zigui/route-point")
public class RoutePointController {

    @Resource
    private RoutePointMapper routePointMapper;

    @Resource
    IRoutePointService iRoutePointService;


    @PostMapping("")
    public Result<?> post(@RequestBody VPostPoint point) {
        if (BeanUtil.isNotEmpty(point)) {
            RoutePoint routePoint = new RoutePoint();
            routePoint.setRouteId(point.getRouteId());
            routePoint.setPointLocation(ZiguiUtil.buildPointText(point.getPoint().get(0), point.getPoint().get(1)));
            routePoint.setPointId(ZiguiUtil.generateId(RoutePoint.class));
            routePointMapper.insert(routePoint);
            return ZiguiUtil.ok(routePoint.getPointId());
        }
        return ZiguiUtil.error();
    }

    @PostMapping("/content")
    public Result<?> postContent(@RequestBody VPostPoint point) {
        if (BeanUtil.isNotEmpty(point)) {
            RoutePoint byId = iRoutePointService.getById(point.getPointId());

            if (BeanUtil.isNotEmpty(byId)) {
             byId.setContent(point.getContent());
             if (routePointMapper.updateContent(byId) == 1) {
                 return ZiguiUtil.ok();
             }
            }
        }
        return ZiguiUtil.error();
    }

    @GetMapping("/as-geojson/{routePointId}")
    public Result<?> asGeoJsonById(@PathVariable String routePointId) {
        return ZiguiUtil.ok(routePointMapper.pointAsGeoJson(routePointId));
    }

    @GetMapping("/desc/{pointId}")
    public Result<?> getDescById(@PathVariable String pointId) {
        if (StrUtil.isNotEmpty(pointId)) {
            RoutePoint byId = iRoutePointService.getById(pointId);
            return ZiguiUtil.ok(byId.getPointDesc());
        }
        return ZiguiUtil.error();
    }

    @PutMapping("/desc")
    public Result<?> update(@RequestBody VPutDesc desc) {
        if (BeanUtil.isNotEmpty(desc)) {
            RoutePoint byId = iRoutePointService.getById(desc.getPointId());

            if (BeanUtil.isNotEmpty(byId)) {
                byId.setPointDesc(desc.getContent());
                routePointMapper.updateBaseInfo(byId);
                return ZiguiUtil.ok();
            }
        }

        return ZiguiUtil.error();
    }

    @GetMapping("/content/{pointId}")
    public Result<?> getContentById(@PathVariable String pointId) {
        if (StrUtil.isNotEmpty(pointId)) {
            RoutePoint one = iRoutePointService.getOne(Wrappers.<RoutePoint>lambdaQuery().eq(RoutePoint::getPointId, pointId));
            one.setRouteId("");
            return ZiguiUtil.ok(one);
        }
        return ZiguiUtil.error();
    }

    @DeleteMapping("/{pointId}")
    public Result<?> delete(@PathVariable String pointId) {
        if (StrUtil.isNotEmpty(pointId)) {
            if (iRoutePointService.removeById(pointId)) {
                return ZiguiUtil.ok();
            }
        }
        return ZiguiUtil.error();
    }

    @PutMapping("")
    public Result<?> update(@RequestBody VPutPoint point) {
        if (BeanUtil.isNotEmpty(point)) {
            RoutePoint byId = iRoutePointService.getById(point.getPointId());

            if (BeanUtil.isNotEmpty(byId)) {
                BeanUtil.copyProperties(point, byId);
                  routePointMapper.updateBaseInfo(byId);
                  return ZiguiUtil.ok();
            }
        }

        return ZiguiUtil.error();
    }

    @GetMapping("/base-info/{pointId}")
    public Result<?> getPointBaseInfoById(@PathVariable String pointId) {
        if (StrUtil.isNotEmpty(pointId)){
            RoutePoint byId = iRoutePointService.getById(pointId);

            if (BeanUtil.isNotEmpty(byId)) {
                  return ZiguiUtil.ok(byId);
            }
        }

        return ZiguiUtil.error();
    }
}
