package com.forum.common;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private String message;
    private Object data;
    public static<T> Result<T> success(String msg,T data){
        Result<T> r=new Result<>();
        r.setCode(200);
        r.setMessage(msg);
        r.setData(data);
        return r;
    }
    public static<T> Result<T> success(String msg){
        Result<T> r=new Result<>();
        r.setCode(200);
        r.setMessage(msg);
        return r;
    }
    public static<T> Result<T> error(String msg) {
        Result<T> r = new Result<>();
        r.setCode(500);
        r.setMessage(msg);
        return r;
    }
}
