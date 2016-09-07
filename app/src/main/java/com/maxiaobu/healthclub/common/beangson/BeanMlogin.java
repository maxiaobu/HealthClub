package com.maxiaobu.healthclub.common.beangson;

/**
 * Created by 马小布 on 2016/9/5.
 */
public class BeanMlogin {

    /**
     * msgFlag : 1
     * msgContent : 登录成功!
     * memid : M000455
     * mobphone : 18624616670
     * nickname : 小当家。
     * gender : 1
     */

    private String msgFlag;
    private String msgContent;
    private String memid;
    private String mobphone;
    private String nickname;
    private String gender;

    public String getMsgFlag() {
        return msgFlag;
    }

    public void setMsgFlag(String msgFlag) {
        this.msgFlag = msgFlag;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMemid() {
        return memid;
    }

    public void setMemid(String memid) {
        this.memid = memid;
    }

    public String getMobphone() {
        return mobphone;
    }

    public void setMobphone(String mobphone) {
        this.mobphone = mobphone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
