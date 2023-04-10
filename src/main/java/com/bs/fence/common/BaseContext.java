package com.bs.fence.common;

/**
 * @author sjx
 * @Description TODO
 * @create 2022-07-17-19:20
 */
public class BaseContext {
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
    @author sjx
    @Description 设置值
    @since 2022-07-17 19-29
    */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
    @author sjx
    @Description 获取值
    @since 2022-07-17 19-29
    */
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
