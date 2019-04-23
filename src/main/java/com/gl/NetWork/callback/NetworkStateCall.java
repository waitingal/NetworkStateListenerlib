package com.gl.NetWork.callback;

import com.gl.NetWork.NetType;

/**
 * Created by waiting on 2019/3/14.
 */

public interface NetworkStateCall {

    void stateChange(NetType netType);
//    void disconnect();
}
