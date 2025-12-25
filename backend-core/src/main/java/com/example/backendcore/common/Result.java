package com.example.backendcore.common;

import lombok.Data;

/**
 * 统一返回结果类
 * 所有接口都返回这个对象，方便前端统一处理
 */
@Data
public class Result<T> {

    private String code; // 状态码: "200"成功, "-1"失败
    private String msg;  // 提示信息
    private T data;      // 数据

    // 成功的方法（带数据）
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode("200");
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    // 成功的方法（不带数据）
    public static <T> Result<T> success() {
        return success(null);
    }

    // 失败的方法
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.setCode("-1");
        result.setMsg(msg);
        return result;
    }
}