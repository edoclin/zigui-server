package cn.cug.zigui.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VTheme {
    private Integer themeId;
    private String themeName;
    private String themeDesc;
    private String  createdTime;
}
