package com.maxiaobu.healthclub.common;

/**
 * Created by 马小布 on 2016/9/3.
 */
public class UrlPath {
    public static final String TEXT_IMG = "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2449950469,2297536915&fm=80";

    /**
     * 内网
     * http://192.168.1.173:8080/efithealth/
     * 121
     */
    public static final String URL_HOST = "http://192.168.1.182:8080/efithealth/";
    /**
     * 外网
     * http://192.168.1.185:18080/efithealth/
     */
    public static final String WEB_HOST = "http://123.56.195.32:18080/efithealth/";

    /**
     * 图片头
     */
    public static final String URL_RESOURCE = "http://efithealthresource.oss-cn-beijing.aliyuncs.com";
    /**
     * url基类
     * http://192.168.1.121:8080/efithealth/
     */
    public static final String URL_BASE = URL_HOST;//"http://123.56.195.32:8080/efithealth/";

    /**
     * 获取验证码
     * mobphone电话号码
     * http://192.168.1.121:8080/efithealth/msendCode.do?mobphone=18624616670
     */
    public static final String URL_UPDATE = URL_BASE + "msendCode.do";

    /**
     * 登录
     * http://192.168.1.121:8080/efithealth/mlogin.do?mobphone=18624616670&mempass=123456&phonedeviceno=
     * mobphone  用户名
     * mempass      密码
     * phonedeviceno   设备id
     */
    public static final String URL_LOGIN = URL_BASE + "mlogin.do";//"http://123.56.195.32:8080/efithealth/";

    /**
     * 获取验证码
     * mobphone电话号码
     * http://192.168.1.121:8080/efithealth/msendCode.do?mobphone=18624616670
     */
    public static final String URL_SENDCODE = URL_BASE + "msendCode.do";

    /**
     * 验证验证码
     * mobphone电话号码
     * identcode验证码
     * http://192.168.1.121:8080/efithealth/mcheckCode.do?mobphone=18624616670&identcode=1234
     */
    public static final String URL_SENDCODE_CHECK = URL_BASE + "mcheckCode.do";

    /**
     * 注册
     * mempass 密码
     * nickname 昵称
     * gender 性别 0女1男
     * mobphone  电话
     * http://192.168.1.121:8080/efithealth/mregister.do?mempass=123456&nickname=马晓卜&gender=1&mobphone=18624616670&identcode=1234
     */
    public static final String URL_REGISTER = URL_BASE + "mregister.do";

    /**
     * 获取验证码 忘记密码
     * mobphone 电话号码
     * http://192.168.1.121:8080/efithealth/mrsendCode.do?mobphone=18624616670
     */
    public static final String URL_SENDCODE_FORGET = URL_BASE + "mrsendCode.do";

    /**
     * 验证验证码  忘记密码
     * mobphone  电话号码
     * identcode  验证码
     * http://192.168.1.121:8080/efithealth/mrcheckCode.do?mobphone=18624616670&identcode=1234
     */
    public static final String URL_SENDCODE_CHECK_FORGET = URL_BASE
            + "mrcheckCode.do";
    /**
     * 重置密码
     * mobphone  电话号码
     * mempass  新密码
     * http://192.168.1.121:8080/efithealth/mrcheckCode.do?mobphone=18624616670&identcode=1234
     */
    public static final String URL_RESETPASSWORD = URL_BASE
            + "mresetMempass.do";

    /**
     * 订餐列表
     * http://192.168.1.121:8080/efithealth/mbFoodmers.do?memid=M000439&mertype=all&pageIndex=1&sorttype=sorttype
     */
    public static final String URL_CATERING_LIST = URL_BASE + "mbFoodmers.do";

    /**
     * 订餐详情
     * 商品id
     * http://192.168.1.121:8080/efithealth/mgetFoodmers.do?merid=M000019
     */
    public static final String URL_CATERING_DETAIL = URL_BASE + "mgetFoodmers.do";


    /**
     * 订单确认
     * 商品id
     * http://192.168.1.121:8080/efithealth/mmeById.do?memid=M000439
     */
    public static final String URL_USER_INFO_BY_ID = URL_BASE + "mmeById.do";

    /**
     * 保存订单信息
     * memid（会员编号）
     * mernum （商品数量）
     * ordamt （订单金额）
     * isShopcart（是否加入过购物车）0、1
     * 商品id foodmers ="{ 'foodmers': [{ 'merid': 'xxxx', 'buynum':'1'},{'merid': 'yyyy', 'buynum':'2'},{'merid': 'zzz', 'buynum':'5'}]}"
     * http://192.168.1.121:8080/efithealth/mfordersave.do?memid=M000439&mernum=2&ordamt=1000&isShopcart=0&foodmers={foodmers:[{merid:M000019,buynum:2}]}
     * ，｛｝
     * retrun {"msgFlag":"1","msgContent":"","ordno":["FO-20160826-474"]}
     */
    public static final String URL_SAVE_ORDER_INDO = URL_BASE + "mfordersave.do";

    /**
     * 配餐订单列表
     * pageIndex: pageIndex++, //当前页码forderlist
     * listtype: "forderlist",//写死，就写他，区分出订餐订单
     * http://192.168.1.121:8080/efithealth/morderlist.do?memid=M000455&listtype=corderlist&pageIndex=1
     * memid: getMemid()//用户id
     */
    public static final String URL_FOOD_ORDER_LIST = URL_BASE + "morderlist.do";

    /**
     * 按摩订单列表
     * pageIndex: pageIndex++, //当前页码
     * listtype: "morderlist",//写死，就写他，区分出按摩订单
     * memid: getMemid()//用户id
     * http://192.168.1.121:8080/efithealth/mmassageorderList.do?memid=M000439&listtype=morderlist&pageIndex=1
     */
    public static final String URL_MSJ_ORDER_LIST = URL_BASE + "mmassageorderList.do";//
    /**
     * 用户账户信息
     * http://192.168.1.121:8080/efithealth/maccount.do?memid=M000439
     */
    public static final String URL_ACCOUNT_INFO = URL_BASE + "maccount.do";
    /**
     * 配餐订单的e币支付
     * <p>
     * http://192.168.1.121:8080/efithealth/mfoodoutBYcoin.do?ordno={"ordno":["FO-20160825-305","FO-20160825-306"]}
     * {"msgFlag":"1","msgContent":"配餐支付成功"}
     */
    public static final String URL_EBI_PAY = URL_BASE + "mfoodoutBYcoin.do";
    /**
     * 课程订单的e币支付
     * <p>
     * http://192.168.1.121:8080/efithealth/moutBYcoin.do?ordno=CO-20160905-994
     * {"msgFlag":"1","msgContent":"配餐支付成功"}
     */
    public static final String URL_COURSE_EBI_PAY = URL_BASE + "moutBYcoin.do";

    /**
     * e币+微信支付
     * http://192.168.1.121:8080/efithealth/mwxpay.do?ordno=FO-20160825-305&orderamt=0
     * 参数：ordno（订单id ），orderamt(支付标识)=0    选择弈币支付为0
     * return:{"appid":"wxe774923041503e14","partnerid":"1335056901","prepayid":"wx20160826150627bf3f7c514f0482801153","package":"Sign=WXPay","noncestr":"gA6ffRHZFGkjfBYS","timestamp":"1472195177","sign":"06BD02175979961E8F3C4468B244FEF9"}
     */
    public static final String URL_EBI_WXIN_PAY = URL_BASE + "mwxpay.do";

    /**
     * 微信支付
     * http://192.168.1.121:8080/efithealth/mwxpay.do?ordno=FO-20160825-305&orderamt=1
     * 参数：ordno（订单id ），orderamt(支付标识)=1    选择弈币支付为0 否为1
     * return:{"appid":"wxe774923041503e14","partnerid":"1335056901","prepayid":"wx20160826150627bf3f7c514f0482801153","package":"Sign=WXPay","noncestr":"gA6ffRHZFGkjfBYS","timestamp":"1472195177","sign":"06BD02175979961E8F3C4468B244FEF9"}
     */
    public static final String URL_WXIN_PAY = URL_BASE + "mwxpay.do";

    /**
     * 单个订单修改地址
     * http://192.168.1.121:8080/efithealth/mupdateAddress.do?updateMode=all&region
     * <p>
     * 参数：updateMode（all，tomorrow）
     * region（区域）
     * address（地址）
     * ordno（订单编号）
     */
    public static final String URL_MODIFY_ORDER_ADDRESS = URL_BASE + "mupdateAddress.do";

    /**
     * 延期
     * http://192.168.1.121:8080/efithealth/mextension.do?ordno=FO-20160726-170
     * {"msgFlag":"1","msgContent":"延期成功"}
     */
    public static final String URL_DELAY_ORDER = URL_BASE + "mextension.do";

    /**
     * 取消订单
     * http://192.168.1.121:8080/efithealth/mcancelForder.do?ordno=FO-20160726-170
     * {"msgFlag":"1","msgContent":"取消订单成功"}
     */
    public static final String URL_CANCEL_ORDER = URL_BASE + "mcancelForder.do";
    /**
     * 取消课程订单
     * http://192.168.1.121:8080/efithealth/mdeleteByList.do?ordno=FO-20160726-170&listtype=corderlist
     * ordno: "'" + orderid + "'",
     * listtype: "corderlist"
     * {"msgFlag":"1","msgContent":"取消订单成功"}
     */
    public static final String URL_MDELETEBYLIST = URL_BASE + "mdeleteByList.do";

    /**
     * 订单详情
     * http://192.168.1.121:8080/efithealth/mselectForderByOrdno.do?ordno=FO-20160726-170
     */
    public static final String URL_FOOD_ORDER_DETAIL = URL_BASE + "mselectForderByOrdno.do";

    /**
     * 查看配送详情
     * <p>
     * http://192.168.1.121:8080/efithealth/mselectDelivery.do?ordno=FO-20160726-170
     */
    public static final String URL_FOOD_DISPATCH_DETAIL = URL_BASE + "mselectDelivery.do";

    /**
     * 教练列表coaches_list
     * pageIndex:页码(1开始),
     * memid: 当前用户id ,
     * latitude: 纬度,
     * longitude: 经度,
     * sorttype: 不限；按距离、按好评(evascore)、按热度(coursetimes),
     * gender:不限；男(1)；女(0)
     * <p>
     * http://192.168.1.121:8080/efithealth/mcoachList.do?memid=M000439&pageIndex=1&latitude=41.811237&longitude=123.432856&sorttytpe=distance&gender=all
     */
    public static final String URL_COACHES_LIST = URL_BASE + "mcoachList.do";

    /**
     * 会员动态列表
     * pageIndex:页码(1开始),
     * tarid:教练ID,
     * memid:用户ID
     * http://192.168.1.121:8080/efithealth/mDynamicList.do?pageIndex=1&tarid=M000440&memid=M000439
     */
    public static final String URL_MEMBER_DYNAMIC_LIST = URL_BASE + "mDynamicList.do";

    /**
     * 教练详情
     * pageIndex:页码(1开始),
     * tarid:教练ID,
     * http://192.168.1.121:8080/efithealth/mbcoach.do?pageIndex=1&tarid=M000440
     */
    public static final String URL_COACHES_DETAIL = URL_BASE + "mbcoach.do";

    /**
     * 俱乐部详情
     * pageIndex:页码(1开始),
     * tarid:教练ID,
     * http://192.168.1.182:8080/efithealth/mbclub.do?pageIndex=1&clubmemid=M000438
     */
    public static final String URL_CLUB_DETAIL = URL_BASE + "mbclub.do";


    /**
     * 修改个人信息
     * http://192.168.1.121:8080/efithealth/mupdateMember.do?memid=M000440&nickname=%E9%A9%AC%E5%B0%8F%E5%B8%83&signature=&recaddress=asdfsdf&recname=sdafsdf&recphone=18624616670&birthday=1988/12/12&gender=0&dimg==
     */
    public static final String URL_MYINFO_UPDATE = URL_BASE
            + "mupdateMember.do";


    /**
     * 同步服务器个人信息
     * http://192.168.1.182:8080/efithealth/mtoupdateMember.do?memid=M000439
     */
    public static final String URL_MYINFO = URL_BASE + "mtoupdateMember.do";

    /**
     * 我的预约
     * http://192.168.1.182:8080/efithealth/mmyBespeak.do?memid=M000439
     */
    public static final String URL_MY_BESPEAK = URL_BASE + "mmyBespeak.do";


    /**
     * 申请认证
     * http://192.168.1.182:8080/efithealth/mcoachcertApply.do?memid=M000439&nickname=马小布&coachprice=100&coachadept=快捷方式的恢复开机后&applydescr=的萨科罚金电视里看见弗兰克
     */
    public static final String URL_MCOACHCERTAPPLY = URL_BASE + "mcoachcertApply.do";
    /**
     * 健腰腿背
     * http://192.168.1.182:8080/efithealth/mgettrainingitem.do
     */
    public static final String URL_MGETTRAININGITEM = URL_BASE + "mgettrainingitem.do";


    /**
     * 俱乐部列表
     * http://192.168.1.121:8080/efithealth/mbclubList.do?pageIndex=1&memid=M000440&latitude=123&longitude=123&sorttype=
     * pageIndex: pageindex++, //当前页码
     * memid: memid, //当前用户id
     * latitude: latitude,
     * longitude: longitude,
     * sorttype: sortType
     */
    public static final String URL_MBCLUBLIST = URL_BASE + "mbclubList.do";
    /**
     * 绑定俱乐部列表
     * pageIndex: bindpageIndex++, //当前页码
     * memid: memid //当前用户id
     * http://192.168.1.121:8080/efithealth/mbindList.do?pageIndex=1&memid=M000440
     */
    public static final String URL_MBINDLIST = URL_BASE + "mbindList.do";
    /**
     * 未绑定俱乐部列表
     * pageIndex: bindpageIndex++, //当前页码
     * memid: memid //当前用户id
     * http://192.168.1.121:8080/efithealth/munbindList.do?pageIndex=1&memid=M000440
     */
    public static final String URL_MUNBINDLIST = URL_BASE + "munbindList.do";

    /**
     * 课程管理
     * http://192.168.1.121:8080/efithealth/mcourseList.do?linestatus=0&coachid=M000440
     * map.put("coachid", (String) SharedPreferencesUtils.getParam(
     * getActivity(), "memid", "")); // 会员id
     * map.put("linestatus", "1");// 状态 0 历史 1 上线
     * coachid=M000440  会员id
     * linestatus=1
     */
    public static final String URL_MCOURSELIST = URL_BASE + "mcourseList.do";// 课程管理->上线课程

    /**
     * 同步服务器照片墙
     * http://192.168.1.121:8080/efithealth/mphotowall.do?memid=M000440
     */
    public static final String URL_MPHOTOWALL = URL_BASE + "mphotowall.do";

    /**
     * 异步更新好友信息
     * http://192.168.1.121:8080/efithealth/mgetfriends.do?memid=M000440
     */
    public static final String URL_MGETFRIENDS = URL_BASE + "mgetfriends.do";

    /**
     * 异步获取首页信息
     * http://192.168.1.121:8080/efithealth/mindex.do?memid=M000440&longitude=&latitude=
     */
    public static final String URL_MINDEX = URL_BASE + "mindex.do";

    /**
     * 课程管理->发布课程
     */
    public static final String URL_ISSUE_COURSE = URL_BASE + "mpcoursesave.do";//

    /**
     * 课程管理->获得教练绑定的俱乐部信息
     * http://192.168.1.121:8080/efithealth/mbindList.do?memid=M000440
     */
    public static final String URL_CLUB_MESSAGE = URL_BASE + "mbindList.do";//

    /**
     * 课程修改
     * http://192.168.1.121:8080/efithealth/mupdateCourse.do?memid=M000440
     */
    public static final String URL_UPDATECOURSE = URL_BASE + "mupdateCourse.do";//
    /**
     * 私课详情
     * http://192.168.1.121:8080/efithealth/mbpcourse.do?pcourseid=PC000082
     */
    public static final String URL_MBPCOURSE = URL_BASE + "mbpcourse.do";// 课程删除

    /**
     * 我的教学预约
     * pageIndex: pageindex++, //当前页码
     * memid: memid //当前用户id
     * http://192.168.1.121:8080/efithealth/mcoachBespeak.do?memid=M000440&pageIndex=1
     */
    public static final String URL_MCOACHBESPEAK = URL_BASE + "mcoachBespeak.do";

    /**
     * http://192.168.1.121:8080/efithealth/mdeleteCourse.do?pcourseid=课程编号
     */
    public static final String URL_DELETECOURSE = URL_BASE + "mdeleteCourse.do";

    /**
     * 档期管理
     * http://192.168.1.121:8080/efithealth/mmanager.do?coachid=M000444
     */
    public static final String URL_MMANAGER = URL_BASE + "mmanager.do";

    /**
     * 提交数据录入
     */
    public static final String URL_MCONFIRMLESSION = URL_BASE + "mconfirmLession.do";


    /**
     * 俱乐部绑定关系
     * http://192.168.1.121:8080/efithealth/mrelation.do?memid=M000440&tarid=M000504&tarrole=clubadmin
     */
    public static final String URL_MRELATION = URL_BASE + "mrelation.do";

    /**
     * 版本更新
     * http://192.168.1.121:8080/efithealth/mversionupd.do
     */
    public static final String URL_MVERSIONUPD = URL_BASE + "mversionupd.do";

    /**
     * 版本更新
     * http://192.168.1.121:8080/efithealth/mcoachGallisthList.do
     */
    public static final String URL_mcoachGallisthList = URL_BASE + "mcoachGallisthList.do";

}
