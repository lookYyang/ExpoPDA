package com.orangehi.expo.modules;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orangehi.expo.R;
import com.orangehi.expo.common.LoadingDialog;
import com.orangehi.expo.common.OHCons;
import com.orangehi.expo.po.ResultBean;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.login)
public class LoginActivity extends AppCompatActivity {

    @ViewInject(R.id.btn_login)
    private Button btn_login;

    @ViewInject(R.id.edit_account)
    private EditText edit_account;

    @ViewInject(R.id.edit_password)
    private EditText edit_password;

    private Dialog loadingDialog;
    private String user_id = "";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    LoadingDialog.closeDialog(loadingDialog);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        if(!userInfo.getString("account", "").isEmpty()){
            Intent intent = new Intent(LoginActivity.this, OperationActivity.class);
            startActivity(intent);
            finish();
        }
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = edit_account.getText().toString().trim();
                String password = edit_password.getText().toString().trim();
                if (account.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, R.string.login_pwdIsNotNull, Toast.LENGTH_SHORT).show();
                }else if (password.length() < 6) {
                    Toast.makeText(LoginActivity.this, R.string.login_pwdIsShort, Toast.LENGTH_SHORT).show();
                }else {
                    loadingDialog = LoadingDialog.showWaitDialog(LoginActivity.this, getString(R.string.common_isLoading), false, false);
                    SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    Editor userInfoEditor = userInfo.edit();
                    userInfoEditor.putString("account", account);
                    userInfoEditor.putString("password", password);
                    userInfoEditor.commit();
                    Intent intent = new Intent(LoginActivity.this, OperationActivity.class);
                    startActivity(intent);
                    finish();
                    mHandler.sendEmptyMessageDelayed(1, 100);
//                    login(account, password);
                }
            }
        });

    }

    private void login(final String account,final String password){
        RequestParams params = new RequestParams(OHCons.URL.LOGIN_URL);
        params.addQueryStringParameter("account",account);
        params.addQueryStringParameter("password",password);
        x.http().get(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //成功
                Gson gson = new Gson();
                ResultBean resultBean = gson.fromJson(result, ResultBean.class);
                Toast.makeText(LoginActivity.this,resultBean.getAppmsg().toString(), Toast.LENGTH_SHORT).show();
                if (resultBean.getAppcode().equals(OHCons.SYS_STATUS.SUCCESS)){
                    SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    Editor userInfoEditor = userInfo.edit();
                    userInfoEditor.putString("account", account);
                    userInfoEditor.putString("password", password);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    mHandler.sendEmptyMessageDelayed(1, 0);
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(LoginActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                mHandler.sendEmptyMessageDelayed(1, 0);
            }
            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }

    private boolean isLogin(){
        SharedPreferences share = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        user_id = share.getString("user_id", "");
        return true;
    }
}
