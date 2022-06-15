package cn.cug.zigui.vo;

import cn.hutool.core.collection.ListUtil;
import lombok.Data;

import java.util.List;

@Data
public class VPostPoint {
    String routeId;
    String pointId;
    String content;
    List<Double> point;
}
