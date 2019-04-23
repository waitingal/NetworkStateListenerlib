android设备网络状态监听库
使用
1.application中onCreate方法中调用
    NetworkManager.getInstance().init(this);

2.Activity/Fragment中需要及时获取设备网络状态，
    onCreate方法中调用
      NetworkManager.getInstance().register(this);
    onDestroy方法中调用
      NetworkManager.getInstance().unRegister(this);
	  
3.Activity/Fragment里添加接收网络状态改变的通知方法
    如：
	  @NetWork(netType = NetType.AUTO)
	  public void testNetworkState(NetType netType){
			switch (netType){
            case NetType.WIFI://WiFi网络
                break;
			case NetType.MOBILE://移动网络
                break;
			case NetType.NONE://无网络
                break;	
        }
	  }

4.如需取消所有的设备网络状态监听,需调用（如退出App）
    NetworkManager.getInstance().unRegisterAll()；
   
   