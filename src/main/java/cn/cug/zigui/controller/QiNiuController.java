package cn.cug.zigui.controller;


import cn.cug.zigui.service.IFileService;
import cn.cug.zigui.util.ZiguiUtil;
import cn.cug.zigui.util.result.FileData;
import cn.cug.zigui.util.result.FileUpload;
import cn.cug.zigui.util.result.Result;
import cn.cug.zigui.vo.VFile;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.qiniu.util.Auth;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Zi gui
 * @since 2022-05-02
 */
@RestController
@RequestMapping("/zigui/qiniu")
public class QiNiuController {
    @Value("${zigui.qiniu.access-key}")
    private String accessKey;

    @Value("${zigui.qiniu.secret-key}")
    private String secretKey;

    @Value("${zigui.qiniu.bucket}")
    private String bucket;

    @GetMapping("/token")
    public Result<?> createToken() {
        return ZiguiUtil.ok(Auth.create(accessKey, secretKey).uploadToken(bucket));
    }
}
