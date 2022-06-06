package cn.cug.zigui.util.kml;

import lombok.Data;

import java.util.List;

@Data
public class KmlProperty {
    private List<KmlPoint> kmlPoints;
    private List<KmlLine> kmlLines;
    private List<KmlPolygon> kmlPolygons;
}
