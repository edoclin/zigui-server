package cn.cug.zigui.util.kml;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import lombok.Data;

import java.util.List;

@Data
public class KmlPolygon {
    private List<Coordinate> points;
    private String name;
}
