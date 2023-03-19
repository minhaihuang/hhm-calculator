package com.hhm.calculator.util;

/**
 * @Author huanghm
 * @Date 2023/3/19
 */
public class ResultUtil {

    public static<T> Result success(T t) {
        return new Result(0,null, t);
    }

    public static<T> Result fail(T t) {
        return new Result(-1,null, t);
    }
}
