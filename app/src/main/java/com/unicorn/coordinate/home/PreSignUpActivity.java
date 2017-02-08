package com.unicorn.coordinate.home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.f2prateek.dart.InjectExtra;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.EventBusActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.home.event.RefreshPlayerEvent;
import com.unicorn.coordinate.home.model.MatchInfo;
import com.unicorn.coordinate.home.model.MyLine;
import com.unicorn.coordinate.home.model.MyMatchStatus;
import com.unicorn.coordinate.home.model.Player;
import com.unicorn.coordinate.utils.AESUtils;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.DensityUtils;
import com.unicorn.coordinate.utils.ToastUtils;
import com.unicorn.coordinate.volley.SimpleVolley;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class PreSignUpActivity extends EventBusActivity  implements ImagePickerCallback {


    // ====================== injectExtra ======================

    @InjectExtra(Constant.K_MATCH_INFO)
    MatchInfo matchInfo;

    @InjectExtra(Constant.K_MY_MATCH_STATUS)
    MyMatchStatus myMatchStatus;


    // ====================== onCreate ======================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_sign_up);
    }

    @Override
    public void initViews() {
        initRecyclerView();
        initFillInfoAndInvite();
        matchName.setText(matchInfo.getMatch_name());
        teamStatus.setText(statusText());
        getPlayer();
        getMyLine();
    }


    // ====================== getPlayer ======================

    private void getPlayer() {
        String url = playerUrl();
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
        List<Player> playerList = new Gson().fromJson(data.toString(), new TypeToken<List<Player>>() {
        }.getType());
        Player leader = playerList.get(0);
        teamName.setText(AESUtils.decrypt2(leader.getTeamname()));
        playerAdapter.setPlayerList(playerList);
        playerAdapter.notifyDataSetChanged();
    }


    // ======================== getMyLine ========================

    private void getMyLine() {
        String url = myLineUrl();
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            copeResponse2(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);
    }

    private void copeResponse2(String responseString) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
        JSONObject response = new JSONObject(responseString);
        JSONArray data = response.getJSONArray(Constant.K_DATA);
        List<MyLine> myLineList = new Gson().fromJson(data.toString(), new TypeToken<List<MyLine>>() {
        }.getType());
        MyLine myLine = myLineList.get(0);
        lineName.setText(myLine.getName());

        int myLineStatus = myLine.getStatus();
        if (myLineStatus == 1) {
            fillInfo.setText("汽车线路");
            fillInfo.setVisibility(View.VISIBLE);
        } else if (myLineStatus == 2) {
            fillInfo.setText("宝宝线路");
            fillInfo.setVisibility(View.VISIBLE);
        } else {
            fillInfo.setVisibility(View.GONE);
        }
    }


    // ======================== signUp ========================

    @OnClick(R.id.signUp)
    public void signUpOnClick() {
        if (ClickHelper.isSafe()) {
            completeSign();
        }
    }

    private void completeSign() {
        String url = completeSignUrl();
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            copeResponse3(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);
    }

    private void copeResponse3(String responseString) throws Exception {
        if (ResponseHelper.isWrong(responseString)) {
            return;
        }
        ToastUtils.show("预报名成功");
        formalSignUp();
        finish();
    }

    private void formalSignUp() {
        Intent intent = new Intent(this, FormalSignUpActivity.class);
        intent.putExtra(Constant.K_MATCH_INFO, matchInfo);
        intent.putExtra(Constant.K_MY_MATCH_STATUS, myMatchStatus);
        startActivity(intent);
    }


    // ======================== playerAdapter ========================

    final private PlayerAdapter playerAdapter = new PlayerAdapter();

    private void initRecyclerView() {
        rvTeamInfo.setLayoutManager(new LinearLayoutManager(this));
        rvTeamInfo.setAdapter(playerAdapter);
    }


    // ======================== addPlayer ========================

    @OnClick(R.id.invite)
    public void inviteOnClick() {
        if (ClickHelper.isSafe()) {
            Intent intent = new Intent(this, AddPlayerActivity.class);
            intent.putExtra(Constant.K_MATCH_INFO, matchInfo);
            intent.putExtra(Constant.K_MY_MATCH_STATUS, myMatchStatus);
            startActivity(intent);
        }
    }

    //

    @Subscribe
    public void refreshPlayer(RefreshPlayerEvent refreshPlayerEvent) {
        getPlayer();
    }




    @OnClick(R.id.fillInfo)
    public void fillInfoOnClick(){
      pickImageSingle();
    }

    private void tryDriver(File f){



        // mImgUrls为存放图片的url集合
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//            for (int i = 0; i <mImgUrls.size() ; i++) {
//                File f=new File(mImgUrls.get(i));
//                if (f!=null) {
//                    builder.addFormDataPart("img", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
//                }
//            }
        //添加其它信息
        builder.addFormDataPart("teamId",myMatchStatus.getTeamid());
        builder.addFormDataPart("type", "2");
        builder.addFormDataPart("info1","驾照者姓名");
                    builder.addFormDataPart("Info3", f.getName(), RequestBody.create(MediaType.parse("image/png"), f));

//        builder.addFormDataPart("Info3",SharedInfoUtils.getUserName());
//

        MultipartBody requestBody = builder.build();
        //构建请求
//        new Request
//
        okhttp3.Request request =  new okhttp3.Request.Builder()
                .url(ConfigUtils.getBaseUrl() + "/api/addextra?")//地址
                .post(requestBody)//添加请求体
                .build();

        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ressult","faule");
//                ToastUtils.show("faliuee");
//                    System.out.println("上传失败:e.getLocalizedMessage() = " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Log.e("ressult",response.body().string());
//                System.out.println("上传照片成功：response = " + response.body().string());
//                ToastUtils.show("scucess");
//
//  ToastCustom.makeText(PictureListActivity.this, "上传成功", Toast.LENGTH_LONG).show();

            }
        });
    }




    private ImagePicker imagePicker;

    public void pickImageSingle() {
        imagePicker = new ImagePicker(this);
        imagePicker.setFolderName("Random");
        imagePicker.setRequestId(1234);
        imagePicker.ensureMaxSize(500, 500);
        imagePicker.shouldGenerateMetadata(true);
        imagePicker.shouldGenerateThumbnails(true);
        imagePicker.setImagePickerCallback(this);
        Bundle bundle = new Bundle();
        bundle.putInt("android.intent.extras.CAMERA_FACING", 1);
//        imagePicker.setCacheLocation(PickerUtils.getSavedCacheLocation(this));
        imagePicker.pickImage();
    }

    public void pickImageMultiple() {
        imagePicker = new ImagePicker(this);
        imagePicker.setImagePickerCallback(this);
        imagePicker.allowMultiple();
        imagePicker.pickImage();
    }

//    private CameraImagePicker cameraPicker;
//
//    public void takePicture() {
//        cameraPicker = new CameraImagePicker(this);
//        cameraPicker.setDebugglable(true);
//        cameraPicker.setCacheLocation(CacheLocation.EXTERNAL_STORAGE_APP_DIR);
//        cameraPicker.setImagePickerCallback(this);
//        cameraPicker.shouldGenerateMetadata(true);
//        cameraPicker.shouldGenerateThumbnails(true);
//        pickerPath = cameraPicker.pickImage();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == Picker.PICK_IMAGE_DEVICE) {
                if (imagePicker == null) {
                    imagePicker = new ImagePicker(this);
                    imagePicker.setImagePickerCallback(this);
                }
                imagePicker.submit(data);
            }

    }

    @Override
    public void onImagesChosen(List<ChosenImage> images) {
        ChosenImage chosenImage = images.get(0);
                tryDriver(new File(chosenImage.getThumbnailPath()));
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }





    // ======================== views ========================

    @BindView(R.id.matchName)
    TextView matchName;

    @BindView(R.id.teamName)
    TextView teamName;

    @BindView(R.id.teamStatus)
    TextView teamStatus;

    @BindView(R.id.lineName)
    TextView lineName;

    @BindView(R.id.rvTeamInfo)
    RecyclerView rvTeamInfo;

    @BindView(R.id.fillInfo)
    TextView fillInfo;

    @BindView(R.id.invite)
    TextView invite;

    @BindView(R.id.signUp)
    TextView signUp;


    // ======================== 底层方法 ========================

    private String playerUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/getplayer?").buildUpon();
        builder.appendQueryParameter("teamid", myMatchStatus.getTeamid());
        return builder.toString();
    }

    private String myLineUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/getmyline?").buildUpon();
        builder.appendQueryParameter("teamid", myMatchStatus.getTeamid());
        return builder.toString();
    }

    private String completeSignUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/CompleteSign?").buildUpon();
        builder.appendQueryParameter("teamid", myMatchStatus.getTeamid());
        builder.appendQueryParameter(Constant.K_USER_ID, ConfigUtils.getUserId());
        return builder.toString();
    }

    private String statusText() {
        switch (myMatchStatus.getStatus()) {
            case "1":
                return "未报名参赛";
            case "2":
                return "已设定队名";
            case "3":
                return "已选择线路";
            case "4":
                return "被邀请，未操作";
            case "5":
                return "预报名完成";
            case "6":
                return "正式报名完成";
            default:
                return "";
        }
    }

    private void initFillInfoAndInvite() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color.parseColor("#65C0F2"));
        gradientDrawable.setCornerRadius(DensityUtils.dip2px(this, 3));
        fillInfo.setBackgroundDrawable(gradientDrawable);
        invite.setBackgroundDrawable(gradientDrawable);
    }


    // ======================== back ========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


}
