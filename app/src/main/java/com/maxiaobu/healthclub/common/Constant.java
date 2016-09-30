package com.maxiaobu.healthclub.common;

import android.os.Environment;
import android.util.Log;

/**
 * Created by 马小布 on 2016/9/3.
 */
public class Constant {
    public static final String SYSTEM_INIT_FILE_NAME = "/maxiaobu/";
    /**
     * 图片缓存目录
     */
    public static final String CACHE_DIR_IMAGE;
    /**
     * 本地缓存目录
     */
    public static final String CACHE_DIR;
    /**
     * 表情缓存目录
     */
    public static final String CACHE_DIR_EMOJI;

    /**
     * 待上传图片缓存目录
     */
    public static final String CACHE_DIR_UPLOADING_IMG;
    /**
     * 数据库缓存目录
     */
    public static final String CACHE_DIR_DATABASE;

    static {
        // TODO: 2016/7/29 maxiaobu 换项目名
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + SYSTEM_INIT_FILE_NAME;//有sdcard
        } else {
            CACHE_DIR = Environment.getRootDirectory().getAbsolutePath() + SYSTEM_INIT_FILE_NAME;//没有
        }
        CACHE_DIR_EMOJI = CACHE_DIR + "/emoji";
        CACHE_DIR_IMAGE = CACHE_DIR + "/pic";
        CACHE_DIR_UPLOADING_IMG = CACHE_DIR + "/uploading_img";
        CACHE_DIR_DATABASE = CACHE_DIR + "/databases";
    }


    public static final int RESULT_OK=0x0101;
    public static final int RESULT_OK_ONE=0x0102;
    public static final int RESULT_OK_TWO=0x0103;
    public static final int RESULT_REQUEST_ONE=0x0201;
    public static final int RESULT_REQUEST_SECOND=0x0202;
    public static final int RESULT_REQUEST_THIRD=0x0203;




    public static final String JUMP_KEY="jump_key";
    /**
     * 支付2预约
     */
    public static final int PAY_TO_RESERVATION=0x0300;
    /**
     * 团超详情2预约
     */
    public static final int GCOURSE_TO_BESPEAKLIST=0x0301;
    /**
     * 团超详情2团操
     */
    public static final int GCOURSE_TO_GCOURSE=0x0301;


    public static final String PAY_TYPE="payType";
    public static final String PAY_RESULT="payResult";

    /**
     * 经度
     */
    public static final String LONGITUDE = "longitude";
    /**
     * 纬度
     */
    public static final String LATITUDE = "latitude";

    /**
     * ？？？？？？
     */
    public static final String CITY = "city";
    /**
     * 城市名
     */
    public static final String CITY_NAME = "city_name";

    /**
     * 用户id
     */
    public static final String MEMID = "memid";
    public static final String USER_ID = "userId";

    /**
     * 用户昵称
     */
    public static final String NICK_NAME = "nickname";

    /**
     * 用户头像
     */
    public static final String AVATAR = "avatar";


    /**
     * ???
     */
    public static final String MY_SIGN = "mysign";
    /**
     * 收货人姓名
     */
    public static final String REC_NAME = "recname";

    /**
     * 收货人电话
     */
    public static final String REC_PHONE = "recphone";
     /**
     * 收货人地址
     */
    public static final String REC_ADDRESS = "recaddress";
    /**
     * 用户生日
     */
    public static final String BRITHDAY = "brithday";

    /**
     * 用户身份
     */
    public static final String MEMROLE = "memrole";
    /**
     * 用户性别
     */
    public static final String GENDER = "gender";






}
