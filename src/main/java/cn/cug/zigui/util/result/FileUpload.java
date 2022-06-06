package cn.cug.zigui.util.result;

import lombok.Data;

@Data
public class FileUpload {
    Integer errno;
    FileData data;
    String message;
}
