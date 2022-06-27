package im.zego.livedemo.feature.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import im.zego.live.ZegoRoomManager;
import im.zego.live.constants.ZegoRoomErrorCode;
import im.zego.live.model.ZegoUserInfo;
import im.zego.livedemo.R;
import im.zego.livedemo.base.BaseActivity;
import im.zego.livedemo.constants.Constants;
import im.zego.livedemo.databinding.ActivityUserLoginBinding;
import im.zego.livedemo.feature.room.RoomListActivity;
import im.zego.livedemo.helper.ToastHelper;
import im.zego.livedemo.token.ZegoTokenManager;

public class UserLoginActivity extends BaseActivity<ActivityUserLoginBinding> {

    public static void start(Context context) {
        Intent intent = new Intent(context, UserLoginActivity.class);
        context.startActivity(intent);
    }

    // login info
    private String userID;
    private String userName;

    @SuppressLint("ResourceType")
    @Override
    protected int getStatusBarColor() {
        return R.color.login_dark_bg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initListener();
    }

    private void initData() {
        binding.etUserId.setText(DeviceUtils.getManufacturer() + (int) (100 + Math.random() * 900));
    }

    private void initListener() {
        binding.etUserId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                enableLoginBtn(editable.length() > 0);
            }
        });

        ClickUtils.applySingleDebouncing(binding.btnLogin, Constants.DEBOUNCING_DEFAULT_VALUE, v -> {
            userID = binding.etUserId.getText().toString();
            userName = binding.etUserName.getText().toString();
            if (TextUtils.isEmpty(userName)) {
                userName = userID;
            }
            ZegoUserInfo user = new ZegoUserInfo();
            if (!(TextUtils.isEmpty(userID))) {
                String regEx = "^[a-zA-Z\\d]+$";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(binding.etUserId.getText().toString());
                if (!m.matches()) {
                    ToastHelper.showNormalToast(R.string.toast_user_id_error);
                    return;
                }
                user.setUserID(userID);
                user.setUserName(userName);
                ZegoTokenManager.getInstance().getToken(userID, (errorCode1, token) -> {
                    if (!TextUtils.isEmpty(token)) {
                        ZegoRoomManager.getInstance().userService.login(user, token, errorCode -> {
                            if (errorCode == ZegoRoomErrorCode.SUCCESS) {
                                RoomListActivity.start(this);
                            } else {
                                ToastHelper.showWarnToast(StringUtils.getString(R.string.toast_login_fail, errorCode));
                            }
                        });
                    }
                });
            } else {
                ToastHelper.showNormalToast(R.string.toast_userid_login_fail);
            }
        });
    }

    private void enableLoginBtn(boolean enable) {
        int color = ColorUtils.getColor(R.color.white);
        binding.btnLogin.setTextColor(enable ? color : ColorUtils.setAlphaComponent(color, 0.4F));
        binding.btnLogin.setEnabled(enable);
    }
}