package com.maxiaobu.healthclub;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.chat.EMOptions;
import com.maxiaobu.healthclub.chat.DemoHelper;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.volleykit.IRequest;
import com.maxiaobu.volleykit.NodataFragment;
import com.maxiaobu.volleykit.RequestListener;
import com.maxiaobu.volleykit.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import maxiaobu.easeui.domain.EaseUser;
import maxiaobu.easeui.utils.EaseUserUtils;

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
        map.put("topImgList", SPUtils.getString(getBaseContext(), "topImgList"));
        map.put("topidList", SPUtils.getString(getBaseContext(), "topidlist", ""));
        map.put("moreImgList", (SPUtils.getString(getBaseContext(), "moreImgList", "")));
        map.put("moreidList", SPUtils.getString(getBaseContext(), "moreidlist", ""));
        return map;
    }


    /**
     * 同步服务器个人信息
     */
    public void update_local_myinfo() {
        RequestParams params = new RequestParams("memid", SPUtils.getString(instance, Constant.MEMID));
        getRequestInstance().post(UrlPath.URL_MYINFO, instance, params, new RequestListener() {
            @Override
            public void requestSuccess(String s) {
                try {
                    JSONObject data = new JSONObject(s);
                    if (data == null) {
                        Toast.makeText(App.this, "同步服务器失败！", Toast.LENGTH_SHORT).show();
                    } else {
                        if (data.getString("msgFlag").equals("1")) {
                            JSONObject memberobj = data.getJSONObject("member");
                            String nickname = memberobj.getString("nickname");
                            String brithday = memberobj.getString("birth").toString();
                            String sex = memberobj.getString("gender");
                            String mysign = memberobj.getString("signature");
                            String addressname = memberobj.getString("recname");
                            String addresphone = memberobj.getString("recphone");
                            String address = memberobj.getString("recaddress");
                            String memrole = memberobj.getString("memrole");
                            String headpage =memberobj.getString("headpage");
                            String headImgUrl = UrlPath.URL_RESOURCE + memberobj.getString("imgpfile");
                            SPUtils.putString(getBaseContext(), "nickname",nickname );
                            SPUtils.putString(getBaseContext(), Constant.BRITHDAY, brithday);
                            SPUtils.putString(getBaseContext(), Constant.GENDER, sex);
                            SPUtils.putString(getBaseContext(), Constant.MY_SIGN, mysign);
                            SPUtils.putString(getBaseContext(), Constant.REC_NAME, addressname);
                            SPUtils.putString(getBaseContext(), Constant.REC_PHONE, addresphone);
                            SPUtils.putString(getBaseContext(), Constant.REC_ADDRESS, address);
                            SPUtils.putString(getBaseContext(), "headImgUrl", headImgUrl);
                            SPUtils.putString(getBaseContext(), "memrole", memrole);
                            SPUtils.putString(getBaseContext(), "headpage", headpage);

                            DemoHelper instance = DemoHelper.getInstance();
                            instance.getUserProfileManager().updateCurrentUserNickName(nickname);
                            instance.getUserProfileManager().setCurrentUserAvatar(headImgUrl);
                            JSONArray imglist_top = data.getJSONArray("istopfile");
                            String topImglist = "";
                            String topidlist = "";
                            for (int i = 0; i < imglist_top.length(); i++) {
                                topImglist += imglist_top.getJSONObject(i).getString("imgpfilename");
                                topidlist += imglist_top.getJSONObject(i).getString("medid");
                                if (i != imglist_top.length() - 1) {
                                    topImglist += ",";
                                    topidlist += ",";
                                }
                            }
                            String moreImglist = "";
                            String moreidlist = "";
                            JSONArray imglist_more = data.getJSONArray("imagefile");
                            for (int i = 0; i < imglist_more.length(); i++) {
                                moreImglist += imglist_more.getJSONObject(i).getString("imgpfilename");
                                moreidlist += imglist_more.getJSONObject(i).getString("medid");
                                if (i != imglist_more.length() - 1) {
                                    moreImglist += ",";
                                    moreidlist += ",";
                                }
                            }

                            SPUtils.putString(getInstance(), "topImgList", topImglist);
                            SPUtils.putString(getInstance(),  "moreImgList", moreImglist);
                            SPUtils.putString(getInstance(), "topidlist", topidlist);
                            SPUtils.putString(getInstance(), "topidlist", topidlist);
                            Log.e("MyApplication", "照片墙信息更新成功");

                            Log.e("MyApplication", "个人信息更新成功");

                        } else {
                            Toast.makeText(App.this, "服务器连接不稳定！", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestAgain(NodataFragment nodataFragment) {

            }
        });
    }


}
