package cn.cug.zigui.controller;


import cn.cug.zigui.entity.RouteTheme;
import cn.cug.zigui.entity.Theme;
import cn.cug.zigui.service.IRouteService;
import cn.cug.zigui.service.IRouteThemeService;
import cn.cug.zigui.service.IThemeService;
import cn.cug.zigui.util.ZiguiUtil;
import cn.cug.zigui.util.result.Result;
import cn.cug.zigui.vo.VTheme;
import cn.cug.zigui.vo.VThemeRoute;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
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
 * @since 2022-05-20
 */
@RestController
@RequestMapping("/zigui/theme")
public class ThemeController {

    @Resource
    private IThemeService iThemeService;

    @Resource
    private IRouteService iRouteService;

    @Resource
    private IRouteThemeService iRouteThemeService;

    @GetMapping("/routes/{themeId}")
    public Result<?> getRoutesByThemeId(@PathVariable Integer themeId) {
        if (themeId != null) {
            List<VThemeRoute> result = CollUtil.list(Boolean.TRUE);
            iRouteThemeService.list(Wrappers.<RouteTheme>lambdaQuery().eq(RouteTheme::getThemeId, themeId)).forEach(item -> {
                VThemeRoute vThemeRoute = new VThemeRoute();
                BeanUtil.copyProperties(item, vThemeRoute);
                vThemeRoute.setRouteName(iRouteService.getById(item.getRouteId()).getRouteName());
                vThemeRoute.setCreatedTime(ZiguiUtil.formatLocalDateTime_yyyy_MM_dd(item.getCreatedTime()));
                result.add(vThemeRoute);
            });
            return ZiguiUtil.ok(result);
        }
        return ZiguiUtil.error();
    }

    @GetMapping("")
    public Result<?> list() {
        List<Theme> list = iThemeService.list();
        List<VTheme> result = CollUtil.list(Boolean.TRUE);
        list.forEach(theme -> {
            VTheme vTheme = new VTheme();
            BeanUtil.copyProperties(theme, vTheme);
            vTheme.setCreatedTime(ZiguiUtil.formatLocalDateTime_yyyy_MM_dd(theme.getCreatedTime()));
            result.add(vTheme);
        });
        return ZiguiUtil.ok(result);
    }

    @PostMapping("")
    public Result<?> post(@RequestBody Theme theme) {
        if (BeanUtil.isNotEmpty(theme)) {
            Theme one = iThemeService.getOne(Wrappers.<Theme>lambdaQuery().eq(Theme::getThemeName, theme.getThemeName()));
            if (BeanUtil.isNotEmpty(one)) {
                return ZiguiUtil.error("主题名已存在");
            }
            iThemeService.save(theme);
            return ZiguiUtil.ok();
        }
        return ZiguiUtil.error();
    }

    @PutMapping("")
    public Result<?> update(@RequestBody Theme theme) {
        if (BeanUtil.isNotEmpty(theme)) {
            Theme one = iThemeService.getOne(Wrappers.<Theme>lambdaQuery().eq(Theme::getThemeName, theme.getThemeName()));
            if (BeanUtil.isNotEmpty(one) && one.getThemeId().intValue() != theme.getThemeId().intValue()) {
                return ZiguiUtil.error("主题名已存在");
            }
            iThemeService.updateById(theme);
            return ZiguiUtil.ok();
        }
        return ZiguiUtil.error();
    }

    @DeleteMapping("/{themeId}")
    public Result<?> delete(@PathVariable Integer themeId) {
        if (themeId != null) {
            iThemeService.removeById(themeId);
            iRouteThemeService.remove(Wrappers.<RouteTheme>lambdaQuery().eq(RouteTheme::getThemeId, themeId));
            return ZiguiUtil.ok();
        }
        return ZiguiUtil.error();
    }
}
