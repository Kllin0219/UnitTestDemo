package com.jimmy.test.demo;

public class Result {
    public static final int OK = 1;
    public static final int CRITICAL = 2;

    private int _status;
    private String _msg;

    public Result() {
        _status = OK;
        _msg = "OK";
    }

    public Result(int status, String msg) {
        _status = status;
        _msg = msg;
    }

    public int getStatus() {
        return _status;
    }

    public String getMessage() {
        return _msg;
    }
}
