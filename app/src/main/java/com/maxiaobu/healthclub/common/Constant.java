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
    /**
     * 用户昵称
     */
    public static final String NICK_NAME = "nickname";
}
