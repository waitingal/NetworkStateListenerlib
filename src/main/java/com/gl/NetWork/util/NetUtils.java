package com.gl.NetWork.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import com.gl.NetWork.NetType;
import com.gl.NetWork.NetworkManager;

/**
 * Created by waiting on 2019/3/8.
 */

public class NetUtils {

       //是否有网络
        public static boolean isNetworkAvailable(){
            ConnectivityManager connectivityManager = getConnectivityManager();
            if(connectivityManager == null){
                return false;
            }
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null){
                return networkInfo.isAvailable();
            }
            return false;
        }

        //判断网络类型
        public static NetType getNetType(){
            ConnectivityManager connectivityManager=getConnectivityManager();
            if(connectivityManager == null){
                return NetType.NONE;
            }
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if(networkInfo == null){
                return NetType.NONE;
            }
            int type = networkInfo.getType();
            if(type == ConnectivityManager.TYPE_WIFI){
                    return NetType.WIFI;
            }else if(type == ConnectivityManager.TYPE_MOBILE){
                return NetType.MOBILE;
            }
            return NetType.NONE;
        }

        //获取ConnectivityManager
        public static ConnectivityManager getConnectivityManager(){
            ConnectivityManager connectivityManager=(ConnectivityManager) NetworkManager.getInstance().getApplication()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            return connectivityManager;
        }
}
