package com.maxiaobu.healthclub.volleykit;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.maxiaobu.healthclub.R;


/**
 * @author 马小布
 *          没数据Fragment
 */
public class NodataFragment extends DialogFragment {

    private TextView mTvMessage;

    public interface onAgainListener  {
        /**重来*/
        public void onAgain(NodataFragment fragment);
    }
    private onAgainListener mListener;

    public void setOnAgainListener(onAgainListener mListener){
        this.mListener=mListener;
    }

    private ImageView mIvNoDataBac;
    private String mMsg = "";


    public NodataFragment() {
    }



    @SuppressLint("InflateParams")
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_nodata, null);

        mIvNoDataBac = (ImageView) view.findViewById(R.id.ivNoDataBac);
        mTvMessage = (TextView) view.findViewById(R.id.tv_message);
        mTvMessage.setText(mMsg);
        mIvNoDataBac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null)
                mListener.onAgain(NodataFragment.this);
            }
        });
        Dialog dialog = new Dialog(getActivity(), R.style.MyNodataDialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);

        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



    @Override
    public void onStart() {
        super.onStart();

    }

    public void setMsg(String msg) {
        if (msg != null) {
         mMsg=msg;
        }
    }

}

