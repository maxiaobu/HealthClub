package com.maxiaobu.healthclub.utils;

import android.util.Log;
import android.widget.Toast;

import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.chat.DemoHelper;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanMindex;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.healthclub.volleykit.NodataFragment;
import com.maxiaobu.healthclub.volleykit.RequestJsonListener;
import com.maxiaobu.healthclub.volleykit.RequestListener;
import com.maxiaobu.healthclub.volleykit.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import maxiaobu.easeui.domain.EaseUser;

/**
 * Created by 马小布 on 2016/9/12.
 */
public class HealthUtil {

    /**
     * 同步服务器个人信息
     */
    public static void update_local_myinfo() {
        RequestParams params = new RequestParams("memid", SPUtils.getString(Constant.MEMID));
        App.getInstance().getRequestInstance().post(UrlPath.URL_MYINFO, App.getInstance(), params, new RequestListener() {
            @Override
            public void requestSuccess(String s) {
                try {
                    JSONObject data = new JSONObject(s);
                    if (data == null) {
                        Toast.makeText(App.getInstance(), "同步服务器失败！", Toast.LENGTH_SHORT).show();
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
                            String headpage = memberobj.getString("headpage");
                            String headImgUrl = UrlPath.URL_RESOURCE + memberobj.getString("imgpfile");
                            SPUtils.putString("nickname", nickname);
                            SPUtils.putString(Constant.BRITHDAY, brithday);
                            SPUtils.putString(Constant.GENDER, sex);
                            SPUtils.putString(Constant.MY_SIGN, mysign);
                            SPUtils.putString(Constant.REC_NAME, addressname);
                            SPUtils.putString(Constant.REC_PHONE, addresphone);
                            SPUtils.putString(Constant.REC_ADDRESS, address);
                            SPUtils.putString("headImgUrl", headImgUrl);
                            SPUtils.putString(Constant.MEMROLE, memrole);
                            SPUtils.putString("headpage", headpage);

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

                            SPUtils.putString("topImgList", topImglist);
                            SPUtils.putString("moreImgList", moreImglist);
                            SPUtils.putString("topidlist", topidlist);
                            SPUtils.putString("topidlist", topidlist);
                            Log.e("MyApplication", "照片墙信息更新成功");

                            Log.e("MyApplication", "个人信息更新成功");

                        } else {
                            Toast.makeText(App.getInstance(), "服务器连接不稳定！", Toast.LENGTH_SHORT).show();
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


    /**
     * 同步服务器照片墙
     */
    public static void update_loacl_imgWall() {
        RequestParams params = new RequestParams("memid", SPUtils.getString(Constant.MEMID));
        App.getRequestInstance().post(App.getInstance(), UrlPath.URL_MPHOTOWALL, params, new RequestListener() {
            @Override
            public void requestSuccess(String s) {
                try {
                    JSONObject data = new JSONObject(s);
                    if (data == null) {
                        Toast.makeText(App.getInstance(), "同步服务器失败！", Toast.LENGTH_SHORT).show();
                    } else {
                        if (data.getString("msgFlag").equals("1")) {
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
                            SPUtils.putString("topImgList", topImglist);
                            SPUtils.putString("moreImgList", moreImglist);
                            SPUtils.putString("topidlist", topidlist);
                            SPUtils.putString("moreidlist", moreidlist);
                            Log.e("MyApplication", "照片墙信息更新成功");
                        } else {
                            Toast.makeText(App.getInstance(), "服务器连接不稳定！", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(App.getInstance(), "json解析失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void requestAgain(NodataFragment nodataFragment) {
                nodataFragment.dismissAllowingStateLoss();
            }
        });
    }

    // 异步更新好友信息
    public static void update_loacl_friend() {
        RequestParams params = new RequestParams("memid", SPUtils.getString(Constant.MEMID));
        App.getRequestInstance().post(App.getInstance(), UrlPath.URL_MGETFRIENDS, params, new RequestListener() {
            @Override
            public void requestSuccess(String s) {
                try {
                    JSONObject data = new JSONObject(s);
                    if (data == null) {
                        Toast.makeText(App.getInstance(), "同步服务器失败！", Toast.LENGTH_SHORT).show();
                    } else {
                        if (data.getString("msgFlag").equals("1")) {
//                            Log.i("djy", data.toJSONString());
                            JSONArray jsonfriends = data.getJSONArray("friendslist");
                            Map<String, EaseUser> localUsers = DemoHelper.getInstance().getContactList();

                            for (int i = 0; i < jsonfriends.length(); i++) {
                                String f_memeid = jsonfriends.getJSONObject(i).getString("memid").toLowerCase();
                                String f_nickname = jsonfriends.getJSONObject(i).getString("nickname");
                                String f_headimg = jsonfriends.getJSONObject(i).getString("imgsfile");
                                EaseUser f_user = DemoHelper.getInstance().getUserInfo(f_memeid);
                                f_user.setAvatar(f_headimg);
                                f_user.setNick(f_nickname);
                                localUsers.put(f_memeid, f_user);
                            }
                            ArrayList<EaseUser> mList = new ArrayList<EaseUser>();
                            mList.addAll(localUsers.values());
                            DemoHelper.getInstance().updateContactList(mList);
                            Log.e("MyApplication", "好友信息更新成功");
                        } else {
                            Toast.makeText(App.getInstance(), "服务器连接不稳定！", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(App.getInstance(), "json解析失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void requestAgain(NodataFragment nodataFragment) {
                nodataFragment.dismissAllowingStateLoss();
            }
        });
    }

    // 异步获取首页信息
    public static void update_loacl_indexdata() {
        RequestParams params = new RequestParams();
        params.put("longitude",SPUtils.getString(Constant.LONGITUDE));
        params.put("latitude",SPUtils.getString(Constant.LATITUDE));
        params.put("memid",SPUtils.getString(Constant.MEMID));
        App.getRequestInstance().post(App.getInstance(), UrlPath.URL_MINDEX, BeanMindex.class, params, new RequestJsonListener<BeanMindex>() {
            @Override
            public void requestSuccess(BeanMindex beanMindex) {
                if (beanMindex.getMsgFlag().equals("1")){
                    List<BeanMindex.PageHImageListBean> small_imgArry = beanMindex.getPageHImageList();
                    String samll_img_url_1 = small_imgArry.get(0).getImgpfile();
                    String samll_img_url_2 = small_imgArry.get(1).getImgpfile();
                    String samll_img_url_3 = small_imgArry.get(2).getImgpfile();
                    String samll_img_url_4 = small_imgArry.get(3).getImgpfile();
                    List<BeanMindex.PageVImageListBean> big_imgArry = beanMindex.getPageVImageList();

                    String big_img = "";
                    for (int i = 0; i < big_imgArry.size(); i++) {
                        big_img += big_imgArry.get(i).getImgpfile();
                        if (i != big_imgArry.size() - 1) {
                            big_img += ",";
                        }
                    }
                    SPUtils.putString("big_img_url",big_img);
                }
            }

            @Override
            public void requestAgain(NodataFragment nodataFragment) {
                nodataFragment.dismissAllowingStateLoss();

            }
        });

    }



}
