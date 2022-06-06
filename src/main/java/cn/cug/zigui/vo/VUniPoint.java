package cn.cug.zigui.vo;

import lombok.Data;

@Data
public class VUniPoint {
    String pointId;
    Double latitude;
    Double longitude;
    String pointName;
    String pointDesc;
    String content;
    String createdTime;
}
