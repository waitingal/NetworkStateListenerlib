package com.gl.NetWork.bean;

import com.gl.NetWork.NetType;

import java.lang.reflect.Method;

/**
 * Created by waiting on 2019/3/8.
 */

public class MethodManager {

    //注解的方法
    private Method method;
    //注解的参数
    private NetType netType;
    //方法的参数
    private Class<?> para;

    public MethodManager(Method method, NetType netType, Class<?> para) {
        this.method = method;
        this.method.setAccessible(true);
        this.netType = netType;
        this.para = para;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public NetType getNetType() {
        return netType;
    }

    public void setNetType(NetType netType) {
        this.netType = netType;
    }

    public Class<?> getPara() {
        return para;
    }

    public void setPara(Class<?> para) {
        this.para = para;
    }
}
