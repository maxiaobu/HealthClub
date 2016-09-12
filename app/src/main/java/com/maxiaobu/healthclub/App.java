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
    private LocationClient mLocationClient;
    public BDLocationListener myListener = new MyLocationListener();

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
        // 声明LocationClient类
        mLocationClient = new LocationClient(instance);
        mLocationClient.registerLocationListener(myListener);
        setLocationOption();
        mLocationClient.start();

    }


    public Map<String, String> getImgWallInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("topImgList", SPUtils.getString("topImgList"));
        map.put("topidList", SPUtils.getString("topidlist", ""));
        map.put("moreImgList", (SPUtils.getString("moreImgList", "")));
        map.put("moreidList", SPUtils.getString( "moreidlist", ""));
        return map;
    }




    //// 百度MAP///////////////////////////////////////////////////////////////////////
    // 设置百度MAP 定位相关参数
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        option.setAddrType("all");
        option.setIsNeedAddress(true);// 位置，一定要设置，否则后面得不到地址
//		option.setOpenGps(true);// 打开GPS

        //默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false);
        option.setScanSpan(60000);// 多长时间进行一次请求       option.setLocationMode(LocationMode.Hight_Accuracy);// 高精度
        mLocationClient.setLocOption(option);// 使用设置
    }

    /**
     * Description:百度MAP 定位成功回调接口方法
     *
     * @author Xushd
     * @since 2016年2月20日上午11:14:36
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // Receive Location
            SPUtils.putString(Constant.LATITUDE,location.getLatitude() + "");
            SPUtils.putString(Constant.LONGITUDE, location.getLongitude() + "");


            String local_city = location.getCity();
            Log.i("myapp","beyond");
//			mLocationClient.stop();
            if (local_city != null) {
                String cityname = local_city.substring(0, local_city.length() - 1).toString();
                SPUtils.putString(Constant.CITY, location.getCity());
            }
            else {
                SPUtils.putString(Constant.CITY, "沈阳");
            }
        }
        public void onReceivePoi(BDLocation location) {

        }
    }

}
