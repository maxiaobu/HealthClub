package com.maxiaobu.healthclub;

import android.app.Application;

import com.maxiaobu.volleykit.IRequest;

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
    }
}
