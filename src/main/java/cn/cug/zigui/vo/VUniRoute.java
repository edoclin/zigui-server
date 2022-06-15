package cn.cug.zigui.vo;

import lombok.Data;

import java.util.List;

@Data
public class VUniRoute {
    String routeId;
    String routeName;
    String routeDesc;
    List<VLocation> polyline;
    List<VUniPoint> points;
    List<String> themes;
    String content;
    String createdTime;
}
