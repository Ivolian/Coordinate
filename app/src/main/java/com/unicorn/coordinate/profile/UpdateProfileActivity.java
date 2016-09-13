package com.unicorn.coordinate.profile;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.unicorn.coordinate.R;
import com.unicorn.coordinate.base.BaseActivity;
import com.unicorn.coordinate.helper.ClickHelper;
import com.unicorn.coordinate.helper.Constant;
import com.unicorn.coordinate.helper.ResponseHelper;
import com.unicorn.coordinate.user.model.UserInfo;
import com.unicorn.coordinate.utils.ConfigUtils;
import com.unicorn.coordinate.utils.ToastUtils;
import com.unicorn.coordinate.volley.SimpleVolley;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import info.hoang8f.android.segmented.SegmentedGroup;

public class UpdateProfileActivity extends BaseActivity {


    // ======================== onCreate =========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
    }

    @Override
    public void initViews() {
        UserInfo userInfo = ConfigUtils.getUserInfo();
        name.setText(userInfo.getName());
        checkBySexy(userInfo.getSexy());
        initDateTime(userInfo.getBirthday());
        initCardType(userInfo.getCardtype());
        cardNo.setText(userInfo.getCardno());
    }


    // ======================== views =========================

    @BindView(R.id.name)
    EditText name;

    @BindView(R.id.cardNo)
    EditText cardNo;


    // ======================== sgSexy =========================

    @BindView(R.id.sgSexy)
    SegmentedGroup sgSexy;

    private void checkBySexy(String sexy) {
        sgSexy.check(sexy.equals("1") ? R.id.man : R.id.woman);
    }

    private String getSexyChecked() {
        return sgSexy.getCheckedRadioButtonId() == R.id.man ? "1" : "2";
    }


    // ======================== birthday =========================

    @BindView(R.id.birthday)
    TextView tvBirthday;

    DateTime dateTime;

    private void initDateTime(String dateString) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        dateTime = dateTimeFormatter.parseDateTime(dateString);
        tvBirthday.setText(getBirthdayString());
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int month, int day) {
                        dateTime = new DateTime(year, month + 1, day, 0, 0, 0);
                        tvBirthday.setText(getBirthdayString());
                    }
                },
                dateTime.getYear(), dateTime.getMonthOfYear() - 1, dateTime.getDayOfMonth()
        );
        datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
    }

    private String getBirthdayString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        return dateTime.toString(dateTimeFormatter);
    }

    private String getBirthdayFullString() {
       return  getBirthdayString() +" 00:00:00";
    }

    @OnClick(R.id.birthday)
    public void birthdayOnClick() {
        if (ClickHelper.isSafe()) {
            showDatePicker();
        }
    }


    // ======================== cardType =========================

    @BindView(R.id.tvCardType)
    TextView tvCardType;

    @OnClick(R.id.cardType)
    public void cardTypeOnClick() {
        if (ClickHelper.isSafe()) {
            showListDialog();
        }
    }

    private void showListDialog() {
        new MaterialDialog.Builder(this)
                .title("请选择证件类型")
                .items(Arrays.asList("身份证", "护照"))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        tvCardType.setText(text);
                    }
                })
                .show();
    }

    private void initCardType(String cardType) {
        tvCardType.setText(cardType.equals("1") ? "身份证" : "护照");
    }

    private String getCardType() {
        return tvCardType.getText().toString().equals("身份证") ? "1" : "2";
    }


    // ======================== save =========================

    @OnClick(R.id.save)
    public void saveOnClick() {
        if (ClickHelper.isSafe() && isInputValid()) {
            save();
        }
    }

    private void save() {
        String url = getUrl();
        Request request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            copeResponse(response);
                        } catch (Exception e) {
                            //
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
        UserInfo userInfo = ConfigUtils.getUserInfo();
        userInfo.setName(getName());
        userInfo.setSexy(getSexyChecked());
        userInfo.setBirthday(getBirthdayFullString());
        userInfo.setCardtype(getCardType());
        userInfo.setCardno(getCardNo());
        ConfigUtils.saveUserInfo(userInfo);
        ToastUtils.show("修改成功");
        finish();
    }

    private String getUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/update_info?").buildUpon();
        builder.appendQueryParameter(Constant.K_USER_ID, ConfigUtils.getUserId());
        builder.appendQueryParameter("name", getName());
        builder.appendQueryParameter("sexy", getSexyChecked());
        builder.appendQueryParameter("birthday", getBirthdayFullString());
        builder.appendQueryParameter("cardtype", getCardType());
        builder.appendQueryParameter("cardno", getCardNo());
        builder.appendQueryParameter("mobile", ConfigUtils.getUserInfo().getMobile());
        return builder.toString();
    }


    private boolean isInputValid() {
        if (getName().equals("")) {
            ToastUtils.show("姓名不能为空");
            return false;
        }
        if (getCardNo().equals("")) {
            ToastUtils.show("证件号码不能为空");
            return false;
        }
        return true;
    }


    // ======================== 退回 =========================

    @OnClick(R.id.back)
    public void backOnClick() {
        if (ClickHelper.isSafe()) {
            finish();
        }
    }


    // ======================== 基本无视 =========================

    private String getName() {
        return name.getText().toString().trim();
    }

    private String getCardNo() {
        return cardNo.getText().toString().trim();
    }


}

