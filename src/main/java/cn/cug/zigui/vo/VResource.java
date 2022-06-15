package cn.cug.zigui.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VResource {
    private String resourceId;
    private String pointId;
    private String resourceType;
    private String addition;
    private String originalName;
    private String accessUrl;
    private String createdTime;
}
