package cn.cug.zigui.vo;

import lombok.Data;

import java.util.List;

@Data
public class VPostRoute {
    String routeId;
    String routeName;
    String routeDesc;
    String content;
    List<Integer> themeIds;
    List<VPostRoutePoint> points;
}
