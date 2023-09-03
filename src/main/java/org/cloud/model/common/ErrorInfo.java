package org.cloud.model.common;

import lombok.Getter;
import lombok.Setter;

/**
 * 2021/9/7 Created by 𝑳𝒚𝒑(l1yp@qq.com),
 */
@Getter
@Setter
public class ErrorInfo {

    private int code;

    private String message;

    private Object data;

    public ErrorInfo() {
    }

    public ErrorInfo(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorInfo(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
