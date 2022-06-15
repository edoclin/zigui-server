package cn.cug.zigui.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VFile {
    private String fileId;
    private String originalName;
    private String localName;
    private String accessUrl;
    private String  createdTime;
}
