package com.maxiaobu.healthclub.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.maxiaobu.healthclub.App;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.adapter.LunchListAdapter;
import com.maxiaobu.healthclub.adapter.RvLunchSelectAdapter;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.common.UrlPath;
import com.maxiaobu.healthclub.common.beangson.BeanGoodsList;
import com.maxiaobu.healthclub.ui.weiget.refresh.LoadMoreFooterView;
import com.maxiaobu.healthclub.ui.weiget.refresh.RefreshHeaderView;
import com.maxiaobu.healthclub.utils.storage.SPUtils;
import com.maxiaobu.healthclub.volleykit.JsonUtils;
import com.maxiaobu.healthclub.volleykit.NodataFragment;
import com.maxiaobu.healthclub.volleykit.RequestListener;
import com.maxiaobu.healthclub.volleykit.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CateringActivity extends BaseAty implements OnRefreshListener, OnLoadMoreListener, View.OnClickListener {


    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.tv_menu_time)
    TextView mTvMenuTime;
    @Bind(R.id.iv_menu_time)
    ImageView mIvMenuTime;
    @Bind(R.id.rl_time_select)
    RelativeLayout mRlTimeSelect;
    @Bind(R.id.tv_menu_type)
    TextView mTvMenuType;
    @Bind(R.id.iv_menu_type)
    ImageView mIvMenuType;
    @Bind(R.id.rl_type_select)
    RelativeLayout mRlTypeSelect;
    @Bind(R.id.ll_select)
    LinearLayout mLlSelect;
    @Bind(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @Bind(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @Bind(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @Bind(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @Bind(R.id.rv_select)
    RecyclerView mRvSelect;
    @Bind(R.id.fl_select)
    FrameLayout mFlSelect;
    @Bind(R.id.iv_detail)
    ImageView mIvDetail;
    @Bind(R.id.container)
    CoordinatorLayout mContainer;
    private LunchListAdapter mAdapter;
    private List<BeanGoodsList.ListBean> mData;
    private int currentPage;
    /**
     * 请求参数：
     * all全部；1增肌；2塑形；3减脂
     */
    private String mertype;
    /**
     * 请求参数：
     * sorttype: merprice(按价格排序)； createtime(按时间排序)； 不排序不传值
     */
    private String sorttype;

    private int[] menuIcons;
    private String[] menuTitles;


    /**
     * 0刷新  1加载
     */
    private int dataType;

    /**
     * 0隐藏；1时间；2类型
     */
    private int menuType;
    /**
     * Hold a reference to the current animator, so that it can be canceled mid-way.
     */
    private Animator mCurrentAnimator;

    /**
     * The system "short" animation time duration, in milliseconds. This duration is ideal for
     * subtle animations or animations that occur very frequently.
     * 时间
     */
    private int mShortAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catering);
        ButterKnife.bind(this);
        currentPage = 1;
        dataType = 0;
        mShortAnimationDuration = 300;
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "营养配餐");
        mData = new ArrayList<>();
        menuIcons = new int[]{};
        menuTitles = new String[]{};
        menuType = 0;
        sorttype = "";
        mertype = "all";
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeTarget.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mSwipeTarget.setLayoutManager(layoutManager);
        mSwipeTarget.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new LunchListAdapter(this, mData);
        mSwipeTarget.setAdapter(mAdapter);

        mRlTimeSelect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mIvMenuTime.setImageResource(R.mipmap.ic_lunch_arrow_select);
                        break;
                    case MotionEvent.ACTION_UP:
                        mIvMenuTime.setImageResource(R.mipmap.ic_lunch_arrow_default);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        mRlTypeSelect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mIvMenuType.setImageResource(R.mipmap.ic_lunch_arrow_select);
                        break;
                    case MotionEvent.ACTION_UP:
                        mIvMenuType.setImageResource(R.mipmap.ic_lunch_arrow_default);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        mAdapter.setOnImageItemClickListener(new LunchListAdapter.OnImageItemClickListener() {
            @Override
            public void onItemClick(View view, String url) {
                zoomImageFromThumb(view, url);
            }
        });
        mAdapter.setOnItemClickListener(new LunchListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String merid) {
                Intent intent = new Intent(CateringActivity.this,
                        CateringDetailActivity.class);
                intent.putExtra("merid", merid);
                ActivityOptionsCompat compat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(CateringActivity.this,
                                view, mActivity.getString(R.string.transition));
                ActivityCompat.startActivity(CateringActivity.this, intent, compat.toBundle());
            }
        });
    }

    @Override
    public void initData() {
        RequestParams params = new RequestParams();
        params.put("memid", SPUtils.getString( Constant.MEMID));
        params.put("pageIndex", String.valueOf(currentPage));
        params.put("sorttype", sorttype);//sorttype: merprice(按价格排序)； createtime(按时间排序)； 不排序不传值
        params.put("mertype", mertype);//mertype:all全部；1增肌；2塑形；3减脂
        App.getRequestInstance().post(this, UrlPath.URL_CATERING_LIST, params, new RequestListener() {
            @Override
            public void requestSuccess(String s) {
                BeanGoodsList object = JsonUtils.object(s, BeanGoodsList.class);
                if (dataType == 0) {//刷新
                    mData.clear();
                    mData.addAll(object.getList());
                    mAdapter.notifyDataSetChanged();
                    if (mSwipeToLoadLayout != null) {
                        mSwipeToLoadLayout.setRefreshing(false);
                    }
                } else if (dataType == 1) {//加载更多
                    int position = mAdapter.getItemCount();
                    mData.addAll(object.getList());
                    mAdapter.notifyItemRangeInserted(position, object.getList().size());
                    mSwipeToLoadLayout.setLoadingMore(false);
                } else {
                    Toast.makeText(mActivity, "刷新什么情况", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void requestAgain(NodataFragment nodataFragment) {
                initData();
            }
        });
    }

    @OnClick({R.id.rl_time_select, R.id.rl_type_select, R.id.fl_select})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_time_select:
                switchMenu(1);
                break;
            case R.id.rl_type_select:
                switchMenu(2);
                break;
            case R.id.fl_select:
                switchMenu(0);
                break;
            case R.id.ivNoDataBac:
                initData();
                break;

            default:
                break;
        }
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        dataType = 1;
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });

    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        dataType = 0;
        if (mSwipeToLoadLayout != null) {
            mSwipeToLoadLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initData();
                }
            }, 2);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (mIvDetail.getVisibility() == View.VISIBLE) {
//            mIvDetail.setVisibility(View.GONE);
            mIvDetail.performClick();

        } else {
            super.onBackPressed();
        }
    }

    /**
     * 显示/隐藏筛选列表
     *
     * @param whichMenuType 0隐藏   1时间  2类型
     */
    private void switchMenu(int whichMenuType) {
//        mFlSelect.getVisibility()
        if (whichMenuType == 0) {//隐藏
            menuType = whichMenuType;
            mRvSelect.setAnimation(AnimationUtils.loadAnimation(this, R.anim.dd_menu_out));
            mFlSelect.setAnimation(AnimationUtils.loadAnimation(this, R.anim.dd_mask_out));
            mFlSelect.setVisibility(View.GONE);
            mRlTimeSelect.setBackgroundResource(R.drawable.bg_catering_select);
            mRlTypeSelect.setBackgroundResource(R.drawable.bg_catering_select);
            mIvMenuTime.setImageResource(R.mipmap.ic_lunch_arrow_default);
            mIvMenuType.setImageResource(R.mipmap.ic_lunch_arrow_default);

        } else if (whichMenuType == menuType) {
            menuType = 0;
            mRvSelect.setAnimation(AnimationUtils.loadAnimation(this, R.anim.dd_menu_out));
            mFlSelect.setAnimation(AnimationUtils.loadAnimation(this, R.anim.dd_mask_out));
            mFlSelect.setVisibility(View.GONE);
            mRlTimeSelect.setBackgroundResource(R.drawable.bg_catering_select);
            mRlTypeSelect.setBackgroundResource(R.drawable.bg_catering_select);
            mIvMenuTime.setImageResource(R.mipmap.ic_lunch_arrow_default);
            mIvMenuType.setImageResource(R.mipmap.ic_lunch_arrow_default);
        } else {//显示
            mFlSelect.setVisibility(View.VISIBLE);
            mRvSelect.setAnimation(AnimationUtils.loadAnimation(this, R.anim.dd_menu_in));
            mFlSelect.setAnimation(AnimationUtils.loadAnimation(this, R.anim.dd_mask_in));
            LinearLayoutManager menuTimeLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRvSelect.setLayoutManager(menuTimeLayoutManager);
            mRvSelect.setItemAnimator(new DefaultItemAnimator());
            if (whichMenuType == 1) {//时间
                menuType = whichMenuType;
                menuIcons = new int[]{R.mipmap.shijian_icon_default, R.mipmap.jiage_icon_default};
                menuTitles = new String[]{"上线时间", "价格"};
                RvLunchSelectAdapter mMenuAdapter = new RvLunchSelectAdapter(this, menuIcons, menuTitles);
                mMenuAdapter.setLunchSelectItemClickListener(new RvLunchSelectAdapter.LunchSelectItemClickListener() {
                    @Override
                    public void onItemClick(View view, int postion) {
                        String[] s = new String[]{"createtime", "merprice"}; //sorttype: merprice(按价格排序)； createtime(按时间排序)； 不排序不传值
                        sorttype = s[postion];
                        mTvMenuTime.setText(menuTitles[postion]);
                        switchMenu(0);
                        initData();
                    }
                });
                mRvSelect.setAdapter(mMenuAdapter);
                mRlTimeSelect.setBackgroundResource(R.drawable.bg_catering_select_focused);
                mRlTypeSelect.setBackgroundResource(R.drawable.bg_catering_select);
                mIvMenuTime.setImageResource(R.mipmap.ic_lunch_arrow_select);
                mIvMenuType.setImageResource(R.mipmap.ic_lunch_arrow_default);
            } else {//类型
                menuType = whichMenuType;
                menuTitles = new String[]{"全部", "增肌", "减脂", "塑性"};
                menuIcons = new int[]{R.mipmap.quanbu_icon_default, R.mipmap.zengji_icon_default, R.mipmap.jianzhi_icon_default,
                        R.mipmap.suxing_icon_default};
                RvLunchSelectAdapter mMenuAdapter = new RvLunchSelectAdapter(this, menuIcons, menuTitles);
                mMenuAdapter.setLunchSelectItemClickListener(new RvLunchSelectAdapter.LunchSelectItemClickListener() {
                    @Override
                    public void onItemClick(View view, int postion) {
                        String[] s = new String[]{"all", "1", "2", "3"};//mertype:all全部；1增肌；2塑形；3减脂
                        mertype = s[postion];
                        mTvMenuType.setText(menuTitles[postion]);
                        switchMenu(0);
                        initData();
                    }
                });
                mRvSelect.setAdapter(mMenuAdapter);
                mRlTypeSelect.setBackgroundResource(R.drawable.bg_catering_select_focused);
                mRlTimeSelect.setBackgroundResource(R.drawable.bg_catering_select);
                mIvMenuType.setImageResource(R.mipmap.ic_lunch_arrow_select);
                mIvMenuTime.setImageResource(R.mipmap.ic_lunch_arrow_default);
            }
        }
    }

    /**
     * 大小图缩放
     * "Zooms" in a thumbnail view by assigning the high resolution image to a hidden "zoomed-in"
     * image view and animating its bounds to fit the entire activity content area. More
     * specifically:
     * <p>
     * <ol>
     * <li>Assign the high-res image to the hidden "zoomed-in" (expanded) image view.</li>
     * <li>Calculate the starting and ending bounds for the expanded view.</li>
     * <li>Animate each of four positioning/sizing properties (X, Y, SCALE_X, SCALE_Y)
     * simultaneously, from the starting bounds to the ending bounds.</li>
     * <li>Zoom back out by running the reverse animation on click.</li>
     * </ol>
     *
     * @param thumbView The thumbnail view to zoom in.
     * @param url       图片网址
     */
    private void zoomImageFromThumb(final View thumbView, String url) {
        // If there's an animation in progress, cancel it immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        Glide.with(mActivity).load(url).placeholder(R.mipmap.ic_place_holder).into(mIvDetail);
        // Calculate the starting and ending bounds for the zoomed-in image. This step
        // involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail, and the
        // final bounds are the global visible rectangle of the container view. Also
        // set the container view's offset as the origin for the bounds, since that's
        // the origin for the positioning animation properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final bounds using the
        // "center crop" technique. This prevents undesirable stretching during the animation.
        // Also calculate the start scaling factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation begins,
        // it will position the zoomed-in view in the place of the thumbnail.
        thumbView.setAlpha(0f);
        mIvDetail.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations to the top-left corner of
        // the zoomed-in view (the default is the center of the view).
        mIvDetail.setPivotX(0f);
        mIvDetail.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and scale properties
        // (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(mIvDetail, View.X, startBounds.left,
                        finalBounds.left))
                .with(ObjectAnimator.ofFloat(mIvDetail, View.Y, startBounds.top,
                        finalBounds.top))
                .with(ObjectAnimator.ofFloat(mIvDetail, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(mIvDetail, View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down to the original bounds
        // and show the thumbnail instead of the expanded image.
        final float startScaleFinal = startScale;
        mIvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel, back to their
                // original values.
                AnimatorSet set = new AnimatorSet();
                set
                        .play(ObjectAnimator.ofFloat(mIvDetail, View.X, startBounds.left))
                        .with(ObjectAnimator.ofFloat(mIvDetail, View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(mIvDetail, View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(mIvDetail, View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        mIvDetail.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        mIvDetail.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

    private void launch(View view) {
        ActivityOptionsCompat compat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        view, getString(R.string.transition));
        ActivityCompat.startActivity(this, new Intent(this,
                CateringActivity.class), compat.toBundle());
    }
}
