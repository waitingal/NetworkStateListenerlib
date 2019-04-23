package com.gl.NetWork;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import com.gl.NetWork.callback.NetworkStateCall;
import com.gl.NetWork.handle.EvenHandler;
import com.gl.NetWork.receiver.NetWorkBroadcastReceiver;
import com.gl.NetWork.receiver.NetWorkCallBackImpl;
import com.gl.NetWork.util.NetUtils;

/**
 * Created by waiting on 2019/3/8.
 */

public class NetworkManager implements NetworkStateCall {

    private static volatile NetworkManager instance;
    private Application application;
    private NetWorkBroadcastReceiver netWorkBroadcastReceiver;
    private ConnectivityManager.NetworkCallback networkCallback;
    private EvenHandler evenHandler;

    private NetworkManager(){
        evenHandler = EvenHandler.getInstance();
    }

    public static NetworkManager getInstance(){
        if(instance == null){
            synchronized (NetworkManager.class){
                 if(instance == null){
                     instance = new NetworkManager();
                 }
            }
        }
        return instance;
    }

    public Application getApplication(){
        if(application == null){
            throw  new RuntimeException("NetworkManager.application == null");
        }
        return application;
    }

    public void init(Application application){
        this.application = application;
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            netWorkBroadcastReceiver = new NetWorkBroadcastReceiver(this);
            IntentFilter intentFilter=new IntentFilter();
            intentFilter.addAction(Config.NET_STATUS_CHANGE);
            application.registerReceiver(netWorkBroadcastReceiver,intentFilter);
        }else{
            networkCallback=new NetWorkCallBackImpl(this);
            NetworkRequest.Builder builder=new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            ConnectivityManager connectivityManager = NetUtils.getConnectivityManager();
            if(connectivityManager!=null){
                connectivityManager.registerNetworkCallback(request,networkCallback);
            }
        }

    }

    public void register(Object obj){
        evenHandler.register(obj);
    }

    public void unRegister(Object obj){
        evenHandler.unRegister(obj);
    }

    public void unRegisterAll(){
        evenHandler.unRegisterAll();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && networkCallback!=null) {
            ConnectivityManager connectivityManager = NetUtils.getConnectivityManager();
            if(connectivityManager!=null){
                connectivityManager.unregisterNetworkCallback(networkCallback);
            }
        }else if(netWorkBroadcastReceiver!=null){
            application.unregisterReceiver(netWorkBroadcastReceiver);
        }
    }

    @Override
    public void stateChange(NetType netType) {
        Log.e(Config.LOG_TAG,"stateChange--"+netType);
        evenHandler.send(netType);
    }

}
