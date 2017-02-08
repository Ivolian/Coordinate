package com.unicorn.coordinate.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.unicorn.coordinate.utils.ConfigUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class DriverActivity  extends Activity implements ImagePickerCallback {


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
//        builder.addFormDataPart("teamId",myMatchStatus.getTeamid());
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





}
