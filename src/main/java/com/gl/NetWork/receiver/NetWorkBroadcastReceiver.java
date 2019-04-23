package com.gl.NetWork.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.gl.NetWork.Config;
import com.gl.NetWork.NetType;
import com.gl.NetWork.callback.NetworkStateCall;
import com.gl.NetWork.util.NetUtils;

/**
 * Created by waiting on 2019/3/8.
 */

public class NetWorkBroadcastReceiver extends BroadcastReceiver {

    private NetType netType;
    private NetworkStateCall networkStateCall;

    public NetWorkBroadcastReceiver(NetworkStateCall call){
        networkStateCall = call;
        netType = NetType.NONE;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent == null || intent.getAction() == null){
                return;
        }
        if(intent.getAction().equalsIgnoreCase(Config.NET_STATUS_CHANGE)){
            //是否有网络
            if(NetUtils.isNetworkAvailable()){
                //有网络
                netType = NetUtils.getNetType();
            }else{
                //无网络
                netType = NetType.NONE;
            }

            if(networkStateCall !=null){
                networkStateCall.stateChange(netType);
            }else{
                throw new RuntimeException("NetWorkBroadcastReceiver -- NetworkStateCall is null");
            }
        }
    }

}
