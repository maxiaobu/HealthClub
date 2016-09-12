package com.maxiaobu.healthclub.volleykit;


/**
 * 2015/12/17 0017 JSON 数据请求接口
 */
public interface RequestJsonListener<T> {

    /**
     * 成功
     */
    public void requestSuccess(T result);

    /**
     * 重来
     */
    public void requestAgain(NodataFragment nodataFragment);
}
