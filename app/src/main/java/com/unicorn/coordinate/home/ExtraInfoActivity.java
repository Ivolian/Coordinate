package com.unicorn.coordinate.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.f2prateek.dart.InjectExtra;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.SimpleApplication;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.home.model.ExtraInfo;
import com.unicorn.coordinate.home.model.MatchInfo;
import com.unicorn.coordinate.home.model.MyMatchStatus;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.ToastUtils;
import com.unicorn.coordinate.volley.SimpleVolley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.unicorn.coordinate.R.id.photo;

public class ExtraInfoActivity extends BaseActivity implements ImagePickerCallback {


    // ====================== initViews ======================

    @Override
    public void initViews() {
        ivPhoto.setVisibility(type.equals("2") ? View.VISIBLE : View.INVISIBLE);
        tvPickPhoto.setVisibility(type.equals("2") ? View.VISIBLE : View.INVISIBLE);
        getExtra();
    }

    String photoPath;


    // ====================== pickPhotoOnClick ======================

    @BindView(R.id.pickPhoto)
    TextView tvPickPhoto;

    @OnClick(R.id.pickPhoto)
    public void pickPhotoOnClick() {
        new MaterialDialog.Builder(this)
                .title("上传附加信息")
                .items(Arrays.asList("拍照", "从相册中选取"))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (which == 0) {
                            takePicture();
                        }
                        if (which == 1) {
                            pickPhoto();
                        }
                    }
                })
                .show();
    }


    // ====================== pickPhoto ======================

    private ImagePicker imagePicker;

    public void pickPhoto() {
        if (imagePicker == null) {
            imagePicker = new ImagePicker(this);
            imagePicker.setImagePickerCallback(this);
        }
        imagePicker.pickImage();
    }

    @Override
    public void onImagesChosen(List<ChosenImage> images) {
        ChosenImage chosenImage = images.get(0);
        Glide.with(SimpleApplication.getInstance())
                .load(chosenImage.getThumbnailPath())
                .crossFade()
                .into(ivPhoto);
        photoPath = chosenImage.getThumbnailPath();
    }

    @Override
    public void onError(String message) {

    }


    // ====================== cameraPicker ======================

    private CameraImagePicker cameraPicker;

    public void takePicture() {
        if (cameraPicker == null) {
            cameraPicker = new CameraImagePicker(this);
            cameraPicker.setImagePickerCallback(this);
        }
        cameraPicker.pickImage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Picker.PICK_IMAGE_DEVICE) {
            imagePicker.submit(data);
        }
        if (requestCode == Picker.PICK_IMAGE_CAMERA) {
            cameraPicker.submit(data);
        }
    }


    // ====================== complete ======================

    @OnClick(R.id.complete)
    public void completeOnClick() {
        if (ClickHelper.isSafe()) {
            if (TextUtils.isEmpty(etName.getText())) {
                ToastUtils.show("姓名不能为空");
                return;
            }
            if (TextUtils.isEmpty(etNumber.getText())) {
                ToastUtils.show("号码不能为空");
                return;
            }
            if (type.equals("2") && photoPath == null) {
                ToastUtils.show("请上传附加信息");
                return;
            }
            submit();
        }
    }

    private void submit() {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("type", type);
        builder.addFormDataPart("teamid", myMatchStatus.getTeamid());
        builder.addFormDataPart("info1", etName.getText().toString().trim());
        builder.addFormDataPart("info2", etNumber.getText().toString().trim());
        if (type.equals("2")) {
            File photo = new File(photoPath);
            builder.addFormDataPart("Info3", photo.getName(), RequestBody.create(MediaType.parse("image/png"), photo));
        }

        MultipartBody requestBody = builder.build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ConfigUtils.getBaseUrl() + "/api/addextra?")
                .post(requestBody)
                .build();

        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.show("提交失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (ResponseHelper.isWrong(response.body().string())) {
                                return;
                            }
                            ToastUtils.show("提交附加信息成功");
                            finish();
                        } catch (Exception e) {
                            //
                        }
                    }
                });
            }
        });
    }

    // ====================== getExtra ======================

    private void getExtra() {
        String url = extraUrl();
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            copeResponse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);
    }

    private void copeResponse(String responseString) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
        JSONObject response = new JSONObject(responseString);
        JSONArray data = response.getJSONArray(Constant.K_DATA);
        List<ExtraInfo> extraInfoList = new Gson().fromJson(data.toString(), new TypeToken<List<ExtraInfo>>() {
        }.getType());
        if (extraInfoList.size() != 0) {
            ExtraInfo extraInfo = extraInfoList.get(0);
            fillViews(extraInfo);
        }
    }

    private void fillViews(ExtraInfo extraInfo) {
        etName.setText(extraInfo.getInfo1());
        etNumber.setText(extraInfo.getInfo2());
        if (type.equals("2")) {
            Glide.with(SimpleApplication.getInstance())
                    .load(ConfigUtils.getBaseUrl() + extraInfo.getInfo3())
                    .crossFade()
                    .into(ivPhoto);
        }
    }

    private String extraUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/getextra?").buildUpon();
        builder.appendQueryParameter("teamid", myMatchStatus.getTeamid());
        return builder.toString();
    }

    // ====================== views ======================

    @BindView(R.id.name)
    EditText etName;

    @BindView(R.id.number)
    EditText etNumber;

    @BindView(photo)
    ImageView ivPhoto;


    // ====================== ignore ======================

    @InjectExtra(Constant.K_MATCH_INFO)
    MatchInfo matchInfo;

    @InjectExtra(Constant.K_MY_MATCH_STATUS)
    MyMatchStatus myMatchStatus;

    @InjectExtra("type")
    String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_info);
    }

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
