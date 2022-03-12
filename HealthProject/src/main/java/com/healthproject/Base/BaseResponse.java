package com.healthproject.Base;

import lombok.Data;

@Data
public class BaseResponse<T> {
    private int code;
    private Object data;
    private String message;

    public BaseResponse(int code, Object data, String message){
        this.code=code;
        this.data=data;
        this.message=message;
    }
}
