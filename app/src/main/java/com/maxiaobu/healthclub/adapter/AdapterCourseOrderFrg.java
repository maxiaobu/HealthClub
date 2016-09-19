package com.maxiaobu.healthclub.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.beangson.BeanCorderList;
import com.maxiaobu.healthclub.utils.TimesUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2016/8/25.
 */
public class AdapterCourseOrderFrg extends RecyclerView.Adapter {




    public interface OnCancelItemClickListener {
        public void onItemClick(View view, String what);
    }

    public OnCancelItemClickListener mListener;

    public void setOnCancelItemClickListener(OnCancelItemClickListener listener) {
        mListener = listener;
    }

    public interface OnDelayItemClickListener {
        public void onItemClick(View view, String what);
    }

    public OnDelayItemClickListener mDelayListener;

    public void setOnDelayItemClickListener(OnDelayItemClickListener listener) {
        mDelayListener = listener;
    }

    public interface OnAgainItemClickListener {
        public void onItemClick(View view, String what);
    }

    public OnAgainItemClickListener mAgainListener;

    public void setOnAgainItemClickListener(OnAgainItemClickListener listener) {
        mAgainListener = listener;
    }


    private Activity mActivity;
    private List<BeanCorderList.CorderListBean> mData;

    public AdapterCourseOrderFrg(Activity activity, List<BeanCorderList.CorderListBean> mData) {
        mActivity = activity;
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_course_order_list_frg, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        BeanCorderList.CorderListBean listBean = mData.get(position);
        //0 代付款；1 待收货；2已完成
        Glide.with(mActivity).load(listBean.getCa_imgsfilename()).placeholder(R.mipmap.ic_place_holder).into(viewHolder.mIvPhoto);
        viewHolder.mTvTitle.setText(listBean.getCa_nickname());

        viewHolder.mTvCourseName.setText(listBean.getCoursename());
        viewHolder.mTvClubName.setText(listBean.getClubname());
        viewHolder.mTvEndTime.setText("截至日期"+
                TimesUtil.stringsToTimestamp(String.valueOf(listBean.getOrdenddate().getTime()),"yyyy/MM/dd"));
        viewHolder.mTvResidueTimes.setText( "剩余" + listBean.getOrdcoursetimes() + "次");
        if (listBean.getPaystatus() == "0") {
            viewHolder.mTvComplete.setText("待付款");
            viewHolder.mTvAppointment.setVisibility(View.GONE);
            corder.querySelector("#orderExeDiv1").style.display = "none";
            corder.querySelector("#orderExeDiv2").style.display = "";
            corder.querySelector("#orderExe2").innerHTML = "继续支付";
            corder.querySelector("#orderExe2").href = "javascript:commitExe('payOrder','" + corderData.ordno + "')";
            corder.querySelector("#orderDelExe").href = "javascript:commitExe('del','" + corderData.ordno + "')";
        } else if (listBean.getOrdstatus() == "1") {
            corder.querySelector("#paystatus").innerHTML = "已完成";
            corder.querySelector("#orderExeDiv1").style.display = "none";
            corder.querySelector("#orderExeDiv2").style.display = "";
            corder.querySelector("#orderExe2").innerHTML = "再来一单";
            corder.querySelector("#orderExe2").href = "javascript:commitExe('orderAgain','" + corderData.ordno + "')";
            corder.querySelector("#orderDelExe").href = "javascript:commitExe('del','" + corderData.ordno + "')";
        } else if (corderData.bespeaknum == corderData.coursenum) {
            corder.querySelector("#paystatus").innerHTML = "待预约";
            corder.querySelector("#orderExe1").innerHTML = "现在预约";
            corder.querySelector("#orderExe1").href = "javascript:commitExe('bespeak','" + corderData.ordno + "','" + corderData.coachid + "')";
        } else if (corderData.bespeaknum > corderData.coursenum) {
            corder.querySelector("#paystatus").innerHTML = "已预约";
            corder.querySelector("#orderExe1").href = "javascript:commitExe('查看预约','" + corderData.ordno + "')";
        }
        if (corderData.paystatus == "0") {
            corder.querySelector("#ordbegindatestr").innerHTML = "";
            corder.querySelector("#ordcoursetimes").innerHTML = corderData.coursedays + "天/" + corderData.ordcoursetimes + "次"
        } else if (corderData.ordstatus == "1") {
            var datestr = "截止到" + formatdate(corderData.ordenddate);
            var t = corder.querySelector("#ordbegindatestr");
            corder.querySelector("#ordbegindatestr").innerHTML = datestr;
            corder.querySelector("#ordcoursetimes").innerHTML = corderData.coursedays + "天/" + corderData.ordcoursetimes + "次"
        } else {
            corder.querySelector("#ordbegindatestr").innerHTML = "截止到" + formatdate(corderData.ordenddate);
            corder.querySelector("#ordcoursetimes").innerHTML = "剩余" + corderData.ordcoursetimes + "次";
        }
        if (corderData.paystatus === "2") {
            corder.querySelector("#ordamt").innerHTML = "凭会员卡免费预约";
        } else {
            corder.querySelector("#ordamt").innerHTML = "共计：" + corderData.ordamt + "元";
        }



    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_photo)
        ImageView mIvPhoto;
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.tv_content)
        TextView mTvContent;
        @Bind(R.id.tv_complete)
        TextView mTvComplete;
        @Bind(R.id.tv_price)
        TextView mTvPrice;
        @Bind(R.id.tv_course_name)
        TextView mTvCourseName;
        @Bind(R.id.tv_club_name)
        TextView mTvClubName;
        @Bind(R.id.tv_end_time)
        TextView mTvEndTime;
        @Bind(R.id.tv_residue_times)
        TextView mTvResidueTimes;
        @Bind(R.id.tv_appointment)
        TextView mTvAppointment;
        @Bind(R.id.tv_pay)
        TextView mTvPay;
        @Bind(R.id.tv_delete)
        TextView mTvDelete;
        @Bind(R.id.ly_nopay)
        LinearLayout mLyNopay;
        @Bind(R.id.ly_root)
        LinearLayout mLyRoot;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
