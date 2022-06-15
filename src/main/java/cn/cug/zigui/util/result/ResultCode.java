package cn.cug.zigui.util.result;

import lombok.Getter;

@Getter
public enum ResultCode {
    OK(2000),
    WS_OK(2100),
    SERVER_ERROR(5000),
    PARAM_ERROR(5001),
    NOT_LOGIN(5100),
    ;

    private final Integer code;
    ResultCode(int code) {
        this.code = code;
    }
}
