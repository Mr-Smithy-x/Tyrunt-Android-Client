package co.yoprice.tyrunt.models;

import com.google.gson.GsonBuilder;

/**
 * Created by Charl on 3/25/2016.
 */
public class Response<T> {
    private int code;
    private String message;
    T data;

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(this, getClass());
    }


    public Response(int code, String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public Response setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Response setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public Response setData(T data) {
        this.data = data;
        return this;
    }
}
