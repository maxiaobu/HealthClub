package com.maxiaobu.healthclub.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.beangson.BeanCoachesDetail;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2016/9/1.
 */
public class AdapterGcourseFrg extends RecyclerView.Adapter {
    private List<BeanCoachesDetail.PcourseListBean> mData;
    private Activity mActivity;

    public AdapterGcourseFrg(Activity activity, List<BeanCoachesDetail.PcourseListBean> data) {
        mActivity = activity;
        mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_pcourser_frg, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BeanCoachesDetail.PcourseListBean listBean = mData.get(position);
        MyViewHolder viewHolder = (MyViewHolder) holder;
        Glide.with(mActivity).load(listBean.getImgsfilename()).placeholder(R.mipmap.ic_place_holder).into(viewHolder.mIvHead);
        viewHolder.mTvTitle.setText(listBean.getPcoursename());
        viewHolder.mTvNum.setText(listBean.getPcoursetimes()+"次/"+listBean.getPcoursedays()+"天");
        viewHolder.mTvClubName.setText(listBean.getClubname());
        viewHolder.mTvAddress.setText(listBean.getAddress());
        viewHolder.mTvPrice.setText(listBean.getPcourseprice());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_head)
        ImageView mIvHead;
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.tv_price)
        TextView mTvPrice;
        @Bind(R.id.tv_num)
        TextView mTvNum;
        @Bind(R.id.tv_club_name)
        TextView mTvClubName;
        @Bind(R.id.tv_address)
        TextView mTvAddress;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
