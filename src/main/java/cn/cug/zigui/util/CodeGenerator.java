package cn.cug.zigui.util;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CodeGenerator {
    static Map<String, String> params = new HashMap<>();
    public static void main(String[] args) {
        params.put("host", "localhost");
        params.put("port", "5432");
        params.put("database", "postgres");
        params.put("username", "postgres");
        params.put("password", "postgres");
        params.put("author", "Zi gui");
        params.put("parent_pkg", "cn.cug");
        params.put("module_name", "zigui");
        params.put("like_table", "t_permission"); // 需要生成的表的公共前缀
        params.put("table_prefix", "t_"); // 生成时忽略前缀
        generate(params);
    }

    public static void generate(Map<String, String> params) {
        String path = System.getProperty("user.dir") + "/src/main";
        String url = String.format("jdbc:postgresql://%s:%s/%s", params.get("host"), params.get("port"), params.get("database"));
        FastAutoGenerator.create(url, params.get("username"), params.get("password"))
                .globalConfig(builder -> {
                    builder.author(params.get("author")) // 设置作者
                            .outputDir(path + "/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent(params.get("parent_pkg")) // 设置父包名
                            .moduleName(params.get("module_name")) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, path + "/resources/mapper"));
                })
                .strategyConfig(builder -> {
                    builder.controllerBuilder().enableRestStyle();
                    builder.mapperBuilder().enableMapperAnnotation();
                    builder.entityBuilder().enableLombok().enableRemoveIsPrefix().enableChainModel().logicDeleteColumnName("deleted").idType(IdType.ASSIGN_UUID);
                    builder.likeTable(new LikeTable(params.get("like_table")))// 设置需要生成的表名
                            .addTablePrefix(params.get("table_prefix")); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
