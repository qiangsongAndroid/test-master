package com.xiaoma.taobao;

/**
 * Created by Administrator on 2017/6/25.
 */
public interface HttpCallBackListener {

    void onFinish(String response);
    void onError(Exception e);
}
