package cn.cug.zigui.vo;

import lombok.Data;

import java.util.List;

@Data
public class VRoute {
    String routeId;
    String routeName;
    String routeDesc;
    String createdTime;
    List<List<Double>> coordinates;
    List<List<Double>> points;
    List<String> pointIds;
    List<Integer> themeIds;
}
