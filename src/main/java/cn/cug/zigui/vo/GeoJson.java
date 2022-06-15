package cn.cug.zigui.vo;

import lombok.Data;

import java.util.List;

@Data
public class GeoJson {
    String type;
    List<List<Double>> coordinates;
}
