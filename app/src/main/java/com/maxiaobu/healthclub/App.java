package com.maxiaobu.healthclub;

import android.app.Application;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hyphenate.chat.EMOptions;
import com.maxiaobu.healthclub.chat.DemoHelper;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.healthclub.volleykit.IRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 马小布 on 2016/9/2.
 */
public class App extends Application {
    private static App instance;
    private static IRequest sIRequest;

    public static IRequest getRequestInstance(){
        return sIRequest;
    }
    public static App getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        sIRequest=new IRequest(this);

        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);
        DemoHelper.getInstance().init(this);
    }

    public Map<String, String> getImgWallInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("topImgList", SPUtils.getString("topImgList"));
        map.put("topidList", SPUtils.getString("topidlist", ""));
        map.put("moreImgList", (SPUtils.getString("moreImgList", "")));
        map.put("moreidList", SPUtils.getString( "moreidlist", ""));
        return map;
    }
}
