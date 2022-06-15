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
@RequestMapping("/zigui/file")
public class FileController {
    @Value("${zigui.file-upload-path}")
    private String fileUploadPath;

    @Value("${zigui.file-name-prefix}")
    private String fileNamePrefix;

    @Value("${zigui.file-server}")
    private String fileServer;

    @Resource
    private IFileService iFileService;

    @PostMapping("/upload")
    public Result<?> upload(@RequestParam("file") MultipartFile file) {
        return saveFile(file);
    }

    @PostMapping("/wang/upload")
    public String wangUpload(@RequestParam("file") MultipartFile file) {
        Result<String> result = saveFile(file);

        FileUpload upload = new FileUpload();

        if (result.getCode() == 2000) {
            upload.setErrno(0);
            FileData fileData = new FileData();
            fileData.setUrl(fileServer + result.getData());
            upload.setData(fileData);
        } else {
            upload.setErrno(1);
            upload.setMessage("上传失败");
        }
        return JSONUtil.toJsonStr(upload);
    }

    @Transactional
    public Result<String> saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            return ZiguiUtil.error("未选择文件");
        }
        File temp = new File(fileUploadPath);
        if (!temp.exists()) {
            if (!temp.mkdirs()) {
                return ZiguiUtil.error("目录创建失败");
            }
        }

        cn.cug.zigui.entity.File save = new cn.cug.zigui.entity.File();
        try {
            save.setFileId(IdUtil.getSnowflake().nextIdStr());
            save.setOriginalName(file.getOriginalFilename());
            String[] split = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
            save.setLocalName(fileNamePrefix + save.getFileId() + "." + split[split.length - 1]);
            File localFile = new File(fileUploadPath + save.getLocalName());
            file.transferTo(localFile);
            iFileService.save(save);

        } catch (IOException e) {
            e.printStackTrace();
            return ZiguiUtil.error("上传失败");
        }

        return ZiguiUtil.ok(save.getFileId());
    }

    @GetMapping("/{fileId}")
    public void download(@PathVariable String fileId, HttpServletRequest request, HttpServletResponse response) {
        try (InputStream inputStream = new FileInputStream(new File(fileUploadPath, iFileService.getById(fileId).getLocalName())); OutputStream outputStream = response.getOutputStream()) {
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DeleteMapping("/{fileId}")
    public Result<?> delete(@PathVariable String fileId) {
        if (StrUtil.isNotEmpty(fileId)) {
            cn.cug.zigui.entity.File byId = iFileService.getById(fileId);

            if (BeanUtil.isNotEmpty(byId)) {
                File remove = new File(fileUploadPath + byId.getLocalName());
                if (iFileService.removeById(byId) && FileUtil.del(remove)) {
                    return ZiguiUtil.ok();
                }
            }
        }
        return ZiguiUtil.error();
    }


    @GetMapping("")
    public Result<?> list() {
        List<VFile> result = CollUtil.list(Boolean.TRUE);
        iFileService.list().forEach(file -> {
            VFile vFile = new VFile();
            BeanUtil.copyProperties(file, vFile);
            vFile.setAccessUrl(fileServer + file.getFileId());
            vFile.setCreatedTime(ZiguiUtil.formatLocalDateTime_yyyy_MM_dd_HH_mm(file.getCreatedTime()));
            result.add(vFile);
        });
        return ZiguiUtil.ok(result);
    }
}
