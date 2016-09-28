package com.maxiaobu.healthclub.common.beangson;

/**
 * Created by 马小布 on 2016/9/27.
 */
public class BeanUpdata {

    /**
     * latest : 1.0.2
     * url : http://efithealthresource.oss-cn-beijing.aliyuncs.com/AndroidUpdate/app-debug.apk
     * msgContent : 【更新】\r\n\r\n- 测试：极大提升性能及稳定性\r\n- 假话：部分用户无图模式无法分享至微信及朋友圈
     * msgFlag : 1
     */

    private String latest;
    private String url;
    private String msgContent;
    private String msgFlag;

    public String getLatest() {
        return latest;
    }

    public void setLatest(String latest) {
        this.latest = latest;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgFlag() {
        return msgFlag;
    }

    public void setMsgFlag(String msgFlag) {
        this.msgFlag = msgFlag;
    }
}
