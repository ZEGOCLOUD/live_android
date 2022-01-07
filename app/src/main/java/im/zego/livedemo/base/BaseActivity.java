package im.zego.livedemo.base;

import android.os.Bundle;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.gyf.immersionbar.ImmersionBar;

import im.zego.livedemo.R;
import im.zego.livedemo.view.toast.CookieBar;

public abstract class BaseActivity<VB extends ViewBinding> extends BaseBindingActivity<VB> {
    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImmersionBar.with(this)
                .statusBarColor(getStatusBarColor())
                .fitsSystemWindows(true)
                .statusBarDarkFont(false)
                .init();
    }

    @ColorRes
    protected int getStatusBarColor() {
        return R.color.common_bg;
    }

    protected void showNormalToast(String content, boolean autoDismiss) {
        CookieBar.build(this)
                .setTitle(content)
                .setBackgroundColor(R.color.light_green)
                .setEnableAutoDismiss(autoDismiss)
                .setSwipeToDismiss(false)
                .setCookiePosition(CookieBar.TOP)
                .show();
    }

    protected void dismissAllToast() {
        CookieBar.dismiss(this);
    }
}