package com.maxiaobu.healthclub.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.android.volley.VolleyError;
import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.common.Constant;
import com.maxiaobu.healthclub.volleykit.IRequest;
import com.maxiaobu.healthclub.volleykit.RequestParams;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class CoachesReleaseActivity extends BaseAty implements EasyPermissions.PermissionCallbacks,View.OnClickListener{
    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.iv_top)
    ImageView mIvTop;
    @Bind(R.id.et_name)
    AppCompatEditText mEtName;
    @Bind(R.id.et_times)
    AppCompatEditText mEtTimes;
    @Bind(R.id.ly_times)
    LinearLayout mLyTimes;
    @Bind(R.id.et_days)
    AppCompatEditText mEtDays;
    @Bind(R.id.ly_days)
    LinearLayout mLyDays;
    @Bind(R.id.tv_club)
    TextView mTvClub;
    @Bind(R.id.ly_club)
    LinearLayout mLyClub;
    @Bind(R.id.et_price)
    AppCompatEditText mEtPrice;
    @Bind(R.id.ly_price)
    LinearLayout mLyPrice;
    @Bind(R.id.ed_content)
    AppCompatEditText mEdContent;
    @Bind(R.id.tv_release)
    TextView mTvRelease;

    private static final int IMAGE_REQUEST_CODE = 0;

    private static final int CAMERA_REQUEST_CODE = 1;

    private static final int RESULT_REQUEST_CODE = 2;
    private static final int MY_CAMMER = 122;
    private String mFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coaches_release);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "发布课程");
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.iv_top})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_top:
                selectImage();
                break;
            default:
                break;
        }
    }

    private void selectImage() {
        new MaterialDialog.Builder(this)
                .title("修改头像")
                .items(new String[]{"相册","拍照"})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        switch (position) {
                            //相册
                            case 0:
                                Intent intentFromGallery = new Intent();
                                intentFromGallery.setType("image/*"); // 设置文件类型
                                intentFromGallery.setAction(Intent.ACTION_PICK);
                                startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
                                dialog.dismiss();
                                break;
                            //拍照
                            case 1:
                                getCamera();
                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                }).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_REQUEST_CODE:
                if (resultCode == this.RESULT_CANCELED) {
                } else {
                    startPhotoZoom(data.getData());
                }
                break;
            case CAMERA_REQUEST_CODE:
                if (resultCode == this.RESULT_CANCELED) {
                } else {
                    File tempFile = new File(Constant.CACHE_DIR + "temp.jpg");
                    startPhotoZoom(Uri.fromFile(tempFile));
                }
                break;
            case RESULT_REQUEST_CODE:
                if (resultCode == this.RESULT_CANCELED) {
                } else {
                    if (data != null) {
                        getImageToView(data);
                    }
                }
                break;
        }
    }
    /**
     * 裁剪
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }
    /**
     * 显示图片
     *
     * @param data
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            mFileName = saveBmp(photo);
            mIvTop.setImageURI(Uri.parse(mFileName));
            saveImage();
        }
    }
    /**
     * 保存图片到本地
     *
     * @param bmp
     * @return
     */
    private String saveBmp(Bitmap bmp) {
        String fileName = Constant.CACHE_DIR + System.currentTimeMillis() + ".jpg";
        FileOutputStream stream = null;
        try {
            File parent =new File(Constant.CACHE_DIR);
            if(!parent.exists()){
                parent.mkdirs();
            }
            stream = new FileOutputStream(fileName);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.flush();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bmp.recycle();
        return fileName;
    }

    /**
     * 上传图片
     */
    private void saveImage() {
        try {
            FileInputStream inputStream = new FileInputStream(mFileName);
            RequestParams requestParams = new RequestParams();
            requestParams.put("file", inputStream, mFileName);
           /* IRequest.postImg(this, Constants.URL_UPLOADIMG, requestParams, new RequestListener() {
                @Override
                public void requestSuccess(String json) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        String imgHead = jsonObject.getString("name");
                        updateHead(imgHead);
                    } catch (Exception e) {

                    }
                }

                @Override
                public void requestError(VolleyError e) {

                }
            });*/
        } catch (Exception e) {

        }
    }
    @AfterPermissionGranted(MY_CAMMER)
    private void getCamera() {
        if (EasyPermissions.hasPermissions(myApplication, Manifest.permission.CAMERA)) {
            Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Constant.CACHE_DIR + "temp.jpg")));
            startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
        } else {
            EasyPermissions.requestPermissions(this, "需要相机权限",
                    MY_CAMMER, Manifest.permission.CAMERA);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
