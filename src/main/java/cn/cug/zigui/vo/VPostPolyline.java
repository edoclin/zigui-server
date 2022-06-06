package cn.cug.zigui.vo;

import lombok.Data;

import java.util.List;

@Data
public class VPostPolyline {
    String routeId;
    List<List<Double>> polyline;
}
