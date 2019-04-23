package com.gl.NetWork.handle;

import android.util.Log;

import com.gl.NetWork.Config;
import com.gl.NetWork.NetType;
import com.gl.NetWork.annotation.NetWork;
import com.gl.NetWork.bean.MethodManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by waiting on 2019/3/8.
 */

public class EvenHandler {

    private Map<Object,List<MethodManager>> methodMap;

    private static volatile EvenHandler instance;

    private EvenHandler(){
        methodMap = new HashMap<>();
    }

    public static EvenHandler getInstance(){
        if(instance == null){
            instance = new EvenHandler();
        }
        return  instance;
    }

    public void register(Object obj) {
        List<MethodManager> list = methodMap.get(obj);
        if(list == null){
            list = findAnnotationMethod(obj);
            methodMap.put(obj,list);
        }
    }

    public void unRegister(Object obj) {
        if(!methodMap.isEmpty()){
            methodMap.remove(obj);
        }
        Log.e(Config.LOG_TAG,obj.getClass().getName()+"网络监听注销成功");
    }

    public void unRegisterAll() {
        if (!methodMap.isEmpty()) {
            methodMap.clear();
        }
        Log.e(Config.LOG_TAG,"网络监听注销所有");
    }

    private List<MethodManager> findAnnotationMethod(Object obj){
        List<MethodManager>list =new ArrayList<>();
        Method[] methodArray = obj.getClass().getDeclaredMethods();
        for (Method method:methodArray){
            NetWork network  = method.getAnnotation(NetWork.class);
            if(network == null){
                continue;
            }
            Class<?>[] paraArray=method.getParameterTypes();
            if(paraArray == null || paraArray.length != 1 || paraArray[0].getClass().isAssignableFrom(NetType.class.getClass()) == false){
                Log.e(Config.LOG_TAG,method.getName()+" 方法 只能有一个参数，并且参数类型 必须为:"+NetType.class.getName());
                continue;
            }
            MethodManager methodManager = new MethodManager(method,network.netType(),paraArray[0]);
            list.add(methodManager);
        }
        return list;
    }

    public void send(NetType netType){
        if(methodMap == null || methodMap.isEmpty()){
            Log.e(Config.LOG_TAG,"send--methodMap == null");
            return;
        }
        Set<Object> set= methodMap.keySet();
        for (final Object obj : set){
            List<MethodManager> list= methodMap.get(obj);
            Log.e(Config.LOG_TAG,"send-- List<MethodManager> list=="+list.size());
            if(list!=null){
                for (MethodManager methodManage : list){
                    switch (methodManage.getNetType()){
                        case AUTO:
                            Log.e(Config.LOG_TAG,"send--AUTO==");
                            invoke(methodManage,obj,netType);
                            break;
                        case WIFI:
                            Log.e(Config.LOG_TAG,"send--WIFI==");
                            if(netType == NetType.WIFI || netType == NetType.NONE){
                                invoke(methodManage,obj,netType);
                            }
                            break;
                        case MOBILE:
                            Log.e(Config.LOG_TAG,"send--MOBILE==");
                            if(netType == NetType.MOBILE || netType == NetType.NONE){
                                invoke(methodManage,obj,netType);
                            }
                            break;
                        case NONE:
                            Log.e(Config.LOG_TAG,"send--NONE==");
                            invoke(methodManage,obj,netType);
                            break;
                    }
                }
            }
        }
    }

    private void invoke(MethodManager methodManage,Object obj,NetType netType){
        try {
            Method execute= methodManage.getMethod();
            execute.invoke(obj,netType);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.e(Config.LOG_TAG,"invoke--e=="+e.toString());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Log.e(Config.LOG_TAG,"invoke--e=="+e.toString());
        }
    }
}
