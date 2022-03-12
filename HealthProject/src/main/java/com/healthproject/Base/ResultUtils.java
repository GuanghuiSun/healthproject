package com.healthproject.Base;

public class ResultUtils {

    /**
     * 响应成功
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(0,data,"ok");
    }

    public static <T> BaseResponse<T> error(T data){
        return new BaseResponse<>(1,data,"error");
    }

}
