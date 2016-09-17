package com.maxiaobu.healthclub.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanCoachesListAty;
import com.maxiaobu.healthclub.ui.weiget.GlideCircleTransform;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 马小布 on 2016/8/31.
 */
public class AdapterUnbindClubListfrg extends RecyclerView.Adapter {



    public interface OnItemClickListener {
        public void onItemClick(View view, String tarid);
    }

    public OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private Context mContext;
    private List<BeanCoachesListAty.CoachListBean> mData;

    public AdapterUnbindClubListfrg(Context context, List<BeanCoachesListAty.CoachListBean> mData) {
        mContext = context;
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_bind_club_list_frg, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
//        BeanCoachesListAty.CoachListBean listBean = mData.get(position);
        Glide.with(mContext).load(UrlPath.TEXT_IMG).transform(new GlideCircleTransform(mContext)).placeholder(R.mipmap.ic_place_holder).into(viewHolder.mIvHead);
        viewHolder.mTvName.setText("马小布");
        viewHolder.mTvDistance.setText("距您" + "12KM");
        viewHolder.mTvAddress.setText("快捷方式的回复可见很快就会失控垃圾发电");
        viewHolder.mTvContent.setText("场地参考价格: 250元/次");
        viewHolder.mLyRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(viewHolder.mIvHead, "");//mData.get(position).getMemid()
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return 15;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_head)
        ImageView mIvHead;
        @Bind(R.id.tv_name)
        TextView mTvName;
        @Bind(R.id.tv_distance)
        TextView mTvDistance;
        @Bind(R.id.tv_address)
        TextView mTvAddress;
        @Bind(R.id.tv_content)
        TextView mTvContent;
        @Bind(R.id.ly_root)
        MaterialRippleLayout mLyRoot;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
