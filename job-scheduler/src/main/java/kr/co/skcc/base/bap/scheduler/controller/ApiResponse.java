package kr.co.skcc.base.bap.scheduler.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse implements Serializable {

    private static final long serialVersionUID = 4321761252954619538L;

    private boolean valid;
    private String message;
    private Object data;

    public ApiResponse(boolean valid, String message) {
        super();
        this.valid = valid;
        this.message = message;
    }

    public ApiResponse(boolean valid) {
        super();
        this.valid = valid;
    }

    public static ApiResponse failure(String message) {
        return new ApiResponse(false, message);
    }

    public static ApiResponse failure(Exception e) {
        return new ApiResponse(false, e.getMessage());
    }

    public static ApiResponse failure() {
        return new ApiResponse(false);
    }

    public static ApiResponse success() {
        return new ApiResponse(true);
    }

    public static ApiResponse success(String message) {
        return new ApiResponse(true, message);
    }

    public void setData(Object data) {
        this.valid = true;
        this.data = data;
    }
}
