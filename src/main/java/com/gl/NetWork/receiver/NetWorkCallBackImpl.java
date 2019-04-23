package com.gl.NetWork.receiver;

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
 *
 * 监听网络连接与断开，应该在 onAvailable 与 onLost 方法中，
 * 如果是一个生硬的断开，可能不会 回调 onLosing方法，如手动断开网络连接
 * onCapabilitiesChanged 会回调多次，谨慎使用，避免多次重复操作
 * NetworkCapabilities.TRANSPORT_WIFI 判断网络属性
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetWorkCallBackImpl extends ConnectivityManager.NetworkCallback {

    private NetworkStateCall networkStateCall;

    public NetWorkCallBackImpl(NetworkStateCall call){
        networkStateCall = call;
    }

    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        Log.e(Config.LOG_TAG,"网络已连接");
        if(networkStateCall != null){
            NetType netType = NetUtils.getNetType();
            networkStateCall.stateChange(netType);
        }else{
            throw new RuntimeException("NetWorkCallBackImpl -- NetworkStateCall is null");
        }

    }

    @Override
    public void onLosing(Network network, int maxMsToLive) {
        super.onLosing(network, maxMsToLive);
    }

    @Override
    public void onLost(Network network) {
        super.onLost(network);
        Log.e(Config.LOG_TAG,"网络已中断");
        NetType netType = NetUtils.getNetType();
        if(netType != NetType.NONE){
            return;
        }
        if(networkStateCall != null){
            networkStateCall.stateChange(NetType.NONE);
        }else{
            throw new RuntimeException("NetWorkCallBackImpl -- NetworkStateCall is null");
        }
    }

    @Override
    public void onUnavailable() {
        super.onUnavailable();
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        if(networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)){
            if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                Log.e(Config.LOG_TAG,"网络状态发送变化,类型为WIFI");
            }else{
                Log.e(Config.LOG_TAG,"网络状态发送变化,类型为其他");
            }
        }

    }

    @Override
    public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties);
    }
}
