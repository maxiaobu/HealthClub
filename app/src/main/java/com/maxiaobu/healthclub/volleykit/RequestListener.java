package com.maxiaobu.healthclub.volleykit;


/**
 * Created by maxiaobu on 2015/12/17.
 * 请求监听   string
 */
public interface RequestListener  {

    /** 成功 */
    public void requestSuccess(String json);

    /**重来*/
    public void requestAgain(NodataFragment fragment);
}
