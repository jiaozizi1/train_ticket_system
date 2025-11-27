package org.example.train_ticket_system.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应结果封装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code; // 状态码：200成功，400失败，500服务器错误
    private String message; // 响应消息
    private Object data; // 响应数据

    // 成功响应（带数据）
    public static Result success(Object data) {
        return new Result(200, "操作成功", data);
    }

    // 成功响应（无数据）
    public static Result success() {
        return new Result(200, "操作成功", null);
    }

    // 失败响应
    public static Result error(String message) {
        return new Result(400, message, null);
    }

    // 服务器错误响应
    public static Result error() {
        return new Result(500, "服务器内部错误", null);
    }
}