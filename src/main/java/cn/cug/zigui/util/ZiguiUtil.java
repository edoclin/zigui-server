package cn.cug.zigui.util;

import cn.cug.zigui.util.result.Result;
import cn.cug.zigui.util.result.ResultCode;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ZiguiUtil {
    public static <T> Result<T> build(Integer code, String message, T data, Long count) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        result.setCount(count);
        return result;
    }


    public static <T> Result<T> build(ResultCode resultCode, T data, Integer count) {
        Result<T> result = new Result<>();
        result.setCode(resultCode.getCode());
        result.setMessage(resultCode.name());
        result.setData(data);
        result.setCount(Long.valueOf(count));
        return result;
    }

    public static <T> Result<T> ok(T data, Integer count) {
        return build(ResultCode.OK, data, count);
    }

    public static <T> Result<T> ok(T data, Long count) {
        return build(ResultCode.OK, data, count.intValue());
    }

    public static <T> Result<T> ok(T data) {
        return build(ResultCode.OK, data, 0);
    }

    public static <T> Result<T> ok() {
        return ok(null, 0);
    }

    public static <T> Result<T> error(String message) {
        return build(ResultCode.SERVER_ERROR.getCode(), message, null, null);
    }
    public static <T> Result<T> notLogin() {
        return build(ResultCode.NOT_LOGIN.getCode(), "用户未登录", null, null);
    }


    public static <T> Result<T> error() {
        return error(null);
    }

    public static String formatLocalDateTime_yyyy_MM_dd_HH_mm(LocalDateTime dateTime) {
        if (ObjectUtil.isNull(dateTime)) {
            return "无";
        }
        return dateTime.format( DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分"));
    }

    public static String formatLocalDateTime_yyyy_MM_dd(LocalDateTime dateTime) {
        if (ObjectUtil.isNull(dateTime)) {
            return "无";
        }
        return dateTime.format( DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
    }

    public static String formatLocalDateTime_yyyy_MM_dd(Date dateTime) {
        if (ObjectUtil.isNull(dateTime)) {
            return "无";
        }
        return DateUtil.toLocalDateTime(dateTime).format( DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
    }

    public static String toDisplayRoleName(String role) {
        switch (role) {
            case "ADMIN":
                return "管理员";
            case "USER":
                return "用户";
            default:
                return "未知";
        }
    }

    public static String buildPointText(Double longitude, Double latitude) {
        return "Point(" + longitude + " " + latitude + ")";
    }

    public static String buildLineStringText(List<List<Double>> polyline) {
        StringBuilder result = new StringBuilder();
        result.append("LineString(");

        polyline.forEach(item -> {
            result.append(item.get(0) + " " + item.get(1) + ",");
        });
        result.deleteCharAt(result.length() - 1);
        result.append(")");
        return result.toString();
    }

    public static String generateId(Class clazz) {
        return clazz.getSimpleName().toLowerCase(Locale.ROOT) + '_' + IdUtil.getSnowflakeNextIdStr();
    }
}
