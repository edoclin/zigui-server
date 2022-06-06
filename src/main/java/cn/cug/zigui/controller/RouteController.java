package cn.cug.zigui.controller;


import cn.cug.zigui.entity.*;
import cn.cug.zigui.mapper.RouteMapper;
import cn.cug.zigui.mapper.RoutePointMapper;
import cn.cug.zigui.service.*;
import cn.cug.zigui.util.ZiguiUtil;
import cn.cug.zigui.util.result.Result;
import cn.cug.zigui.vo.*;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Zi gui
 * @since 2022-04-04
 */
@RestController
@RequestMapping("/zigui/route")
public class RouteController {

    @Resource
    private IRouteService iRouteService;

    @Resource
    private IResourceService iResourceService;

    @Resource
    private IRoutePointService iRoutePointService;

    @Resource
    private IRouteThemeService iRouteThemeService;
    @Resource
    private RouteMapper routeMapper;

    @Resource
    private RoutePointMapper pointMapper;

    @Resource
    private IThemeService iThemeService;

    @Resource
    private IUserRoleService iUserRoleService;

    @Resource
    private IRolePermissionService iRolePermissionService;

    @Resource
    private IPermissionService iPermissionService;

    @GetMapping("/generate")
    public Result<?> generateRoute() {
        Route route = new Route();
        if (iRouteService.save(route)) {
            return ZiguiUtil.ok(route.getRouteId());
        }
        return ZiguiUtil.error();
    }

    @PostMapping("/set-polyline")
    public Result<?> setPolyline(@RequestBody VPostPolyline polyline) {
        if (BeanUtil.isNotEmpty(polyline)) {
            Route route = new Route();
            route.setRouteId(polyline.getRouteId());
            route.setRoutePolyline(ZiguiUtil.buildLineStringText(polyline.getPolyline()));
            routeMapper.updatePolyline(route);
        }
        return ZiguiUtil.ok();
    }

    @PostMapping("")
    public Result<?> post(@RequestBody VPostRoute route) {
        if (BeanUtil.isNotEmpty(route)) {
            Route byId = iRouteService.getById(route.getRouteId());
            if (BeanUtil.isNotEmpty(byId)) {
                BeanUtil.copyProperties(route, byId);
                routeMapper.updateBaseInfo(byId);
                if (CollUtil.isNotEmpty(route.getThemeIds())) {
                    iRouteThemeService.remove(Wrappers.<RouteTheme>lambdaQuery().eq(RouteTheme::getRouteId, byId.getRouteId()));
                    route.getThemeIds().forEach(themeId -> {
                        RouteTheme routeTheme = new RouteTheme();
                        routeTheme.setRouteId(byId.getRouteId());
                        routeTheme.setThemeId(themeId);
                        iRouteThemeService.save(routeTheme);
                    });
                }
                return ZiguiUtil.ok();
            }
        }
        return ZiguiUtil.error();
    }


    @PostMapping("/content")
    public Result<?> postContent(@RequestBody VPostRoute route) {
        if (BeanUtil.isNotEmpty(route)) {
            Route byId = iRouteService.getById(route.getRouteId());

            if (BeanUtil.isNotEmpty(byId)) {
                BeanUtil.copyProperties(route, byId);
                routeMapper.updateContent(byId);
                return ZiguiUtil.ok();
            }
        }
        return ZiguiUtil.error();
    }

    @GetMapping("")
    public Result<?> list() {
        List<Route> list = iRouteService.list(Wrappers.<Route>lambdaQuery().eq(Route::getSaved, Boolean.TRUE));
        List<VRoute> result = CollUtil.list(Boolean.TRUE);

        list.forEach(item -> {
            VRoute route = new VRoute();
            BeanUtil.copyProperties(item, route);
            route.setCreatedTime(ZiguiUtil.formatLocalDateTime_yyyy_MM_dd(item.getCreatedTime()));
            route.setThemeIds(CollUtil.list(Boolean.TRUE));
            iRouteThemeService.list(Wrappers.<RouteTheme>lambdaQuery().eq(RouteTheme::getRouteId, item.getRouteId())).forEach(routeTheme -> {
                route.getThemeIds().add(routeTheme.getThemeId());
            });
            result.add(route);
        });


        return ZiguiUtil.ok(result);
    }


    @DeleteMapping("/{routeId}/{confirm}")
    public Result<?> delete(@PathVariable String routeId, @PathVariable Boolean confirm) {
        if (StrUtil.isNotEmpty(routeId)) {
            Route byId = iRouteService.getById(routeId);

            if (BeanUtil.isNotEmpty(byId) && (confirm || StrUtil.isEmpty(byId.getRouteName()))) {
                iRouteService.removeById(byId);
                return ZiguiUtil.ok();
            }
        }
        return ZiguiUtil.ok();
    }

    @GetMapping("/{routeId}")
    public Result<?> getRouteById(@PathVariable String routeId) {
        if (StrUtil.isNotEmpty(routeId)) {
            Route byId = iRouteService.getById(routeId);
            GeoJson geoJson = JSONUtil.toBean(routeMapper.toGeoJsonStr(routeId), GeoJson.class);
            VRoute vRoute = new VRoute();
            BeanUtil.copyProperties(byId, vRoute);
            vRoute.setPointIds(CollUtil.list(Boolean.TRUE));
            List<RoutePoint> list = iRoutePointService.list(Wrappers.<RoutePoint>lambdaQuery().eq(RoutePoint::getRouteId, routeId));

            List<List<Double>> points = CollUtil.list(Boolean.TRUE);

            list.forEach(item -> {
                List<List<Double>> coordinates = JSONUtil.toBean(pointMapper.pointAsGeoJson(item.getPointId()), GeoJson.class).getCoordinates();
                points.add(CollUtil.toList(coordinates.get(0).get(0), coordinates.get(1).get(0)));
                vRoute.getPointIds().add(item.getPointId());
            });
            vRoute.setCreatedTime(ZiguiUtil.formatLocalDateTime_yyyy_MM_dd(byId.getCreatedTime()));
            vRoute.setCoordinates(geoJson.getCoordinates());
            vRoute.setPoints(points);
            vRoute.setThemeIds(CollUtil.list(Boolean.TRUE));
            iRouteThemeService.list(Wrappers.<RouteTheme>lambdaQuery().eq(RouteTheme::getRouteId, routeId)).forEach(routeTheme -> {
                vRoute.getThemeIds().add(routeTheme.getThemeId());
            });
            return ZiguiUtil.ok(vRoute);
        }
        return ZiguiUtil.error();
    }

    @GetMapping("/uni")
    public Result<?> listMini() {
        List<VUniRoute> result = CollUtil.list(Boolean.TRUE);

        if (StpUtil.isLogin()) {
            iUserRoleService.list(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, StpUtil.getLoginIdAsString())).forEach(userRole -> {
                iRolePermissionService.list(Wrappers.<RolePermission>lambdaQuery().eq(RolePermission::getRoleId, userRole.getRoleId())).forEach(rolePermission -> {
                    Permission byId = iPermissionService.getById(rolePermission.getPermissionId());
                    Route route = iRouteService.getById(byId.getRouteId());
                    VUniRoute vUniRoute = new VUniRoute();
                    BeanUtil.copyProperties(route, vUniRoute);

                    vUniRoute.setPolyline(CollUtil.list(Boolean.TRUE));
                    vUniRoute.setThemes(CollUtil.list(Boolean.TRUE));
                    iRouteThemeService.list(Wrappers.<RouteTheme>lambdaQuery().eq(RouteTheme::getRouteId, route.getRouteId())).forEach(theme -> {
                        vUniRoute.getThemes().add(iThemeService.getById(theme.getThemeId()).getThemeName());
                    });
                    GeoJson json = JSONUtil.toBean(routeMapper.toGeoJsonStr(route.getRouteId()), GeoJson.class);

                    if (BeanUtil.isNotEmpty(json) && CollUtil.isNotEmpty(json.getCoordinates())) {
                        json.getCoordinates().forEach(point -> {
                            VLocation vLocation = new VLocation();
                            vLocation.setLongitude(point.get(0));
                            vLocation.setLatitude(point.get(1));
                            vUniRoute.getPolyline().add(vLocation);
                        });
                    }

                    List<RoutePoint> points = iRoutePointService.list(Wrappers.<RoutePoint>lambdaQuery().eq(RoutePoint::getRouteId, route.getRouteId()));

                    List<VUniPoint> jsonPoints = CollUtil.list(Boolean.TRUE);

                    points.forEach(point -> {
                        VUniPoint vUniPoint = new VUniPoint();
                        BeanUtil.copyProperties(point, vUniPoint);
                        GeoJson geoJson = JSONUtil.toBean(pointMapper.pointAsGeoJson(point.getPointId()), GeoJson.class);

                        vUniPoint.setLatitude(geoJson.getCoordinates().get(1).get(0));
                        vUniPoint.setLongitude(geoJson.getCoordinates().get(0).get(0));
                        vUniPoint.setCreatedTime(ZiguiUtil.formatLocalDateTime_yyyy_MM_dd(point.getCreatedTime()));
                        jsonPoints.add(vUniPoint);
                    });

                    vUniRoute.setCreatedTime(ZiguiUtil.formatLocalDateTime_yyyy_MM_dd(route.getCreatedTime()));
                    vUniRoute.setPoints(jsonPoints);
                    result.add(vUniRoute);
                });
            });
        } else {
            List<Route> list = iRouteService.list(Wrappers.<Route>lambdaQuery().eq(Route::getSaved, Boolean.TRUE));
            list.forEach(route -> {
                VUniRoute vUniRoute = new VUniRoute();
                BeanUtil.copyProperties(route, vUniRoute);

                vUniRoute.setPolyline(CollUtil.list(Boolean.TRUE));
                vUniRoute.setThemes(CollUtil.list(Boolean.TRUE));
                iRouteThemeService.list(Wrappers.<RouteTheme>lambdaQuery().eq(RouteTheme::getRouteId, route.getRouteId())).forEach(theme -> {
                    vUniRoute.getThemes().add(iThemeService.getById(theme.getThemeId()).getThemeName());
                });
                GeoJson json = JSONUtil.toBean(routeMapper.toGeoJsonStr(route.getRouteId()), GeoJson.class);

                if (BeanUtil.isNotEmpty(json) && CollUtil.isNotEmpty(json.getCoordinates())) {
                    json.getCoordinates().forEach(point -> {
                        VLocation vLocation = new VLocation();
                        vLocation.setLongitude(point.get(0));
                        vLocation.setLatitude(point.get(1));
                        vUniRoute.getPolyline().add(vLocation);
                    });
                }

                List<RoutePoint> points = iRoutePointService.list(Wrappers.<RoutePoint>lambdaQuery().eq(RoutePoint::getRouteId, route.getRouteId()));

                List<VUniPoint> jsonPoints = CollUtil.list(Boolean.TRUE);

                points.forEach(point -> {
                    VUniPoint vUniPoint = new VUniPoint();
                    BeanUtil.copyProperties(point, vUniPoint);
                    GeoJson geoJson = JSONUtil.toBean(pointMapper.pointAsGeoJson(point.getPointId()), GeoJson.class);

                    vUniPoint.setLatitude(geoJson.getCoordinates().get(1).get(0));
                    vUniPoint.setLongitude(geoJson.getCoordinates().get(0).get(0));
                    vUniPoint.setCreatedTime(ZiguiUtil.formatLocalDateTime_yyyy_MM_dd(point.getCreatedTime()));
                    jsonPoints.add(vUniPoint);
                });

                vUniRoute.setCreatedTime(ZiguiUtil.formatLocalDateTime_yyyy_MM_dd(route.getCreatedTime()));
                vUniRoute.setPoints(jsonPoints);
                result.add(vUniRoute);
            });
        }

        return ZiguiUtil.ok(result);
    }

    @GetMapping("/content/{routeId}")
    public Result<?> getContentById(@PathVariable String routeId) {
        if (StrUtil.isNotEmpty(routeId)) {
            Route one = iRouteService.getOne(Wrappers.<Route>lambdaQuery().eq(Route::getRouteId, routeId));
            one.setRouteName("");
            one.setRouteDesc("");
            one.setRoutePolyline("");
            return ZiguiUtil.ok(one);
        }
        return ZiguiUtil.error();
    }

    @GetMapping("/route-name")
    public Result<?> getRouteName() {
        return ZiguiUtil.ok(iRouteService.list(Wrappers.<Route>lambdaQuery().eq(Route::getSaved, Boolean.TRUE).select(Route::getRouteId, Route::getRouteName).orderByAsc(Route::getRouteName)));
    }


}
