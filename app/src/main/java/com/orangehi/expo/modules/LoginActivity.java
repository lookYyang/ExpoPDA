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
import com.orangehi.expo.common.OHUtils;
import com.orangehi.expo.Utils.xUtilsHttpsUtils;
import com.orangehi.expo.po.AosUserPO;
import com.orangehi.expo.po.LoginBean;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.login)
public class LoginActivity extends AppCompatActivity {

    @ViewInject(R.id.btn_login)
    private Button btn_login;

    @ViewInject(R.id.edit_account)
    private EditText edit_account;

    @ViewInject(R.id.edit_password)
    private EditText edit_password;

    private Dialog loadingDialog;

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
        if(isLogin()){
            Intent intent = new Intent(LoginActivity.this, OperationActivity.class);
            startActivity(intent);
            finish();
        }else {
            SharedPreferences settings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor settingsEditor = settings.edit();
            settingsEditor.putString("host", "192.168.2.150");
            settingsEditor.putString("port", "20888");
            settingsEditor.commit();
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
                    login(account, password);
                }
            }
        });

    }

    private void login(final String account, final String password){
        Map<String, String> params = new HashMap<>();
        params.put("account", account);
        params.put("password", password);
        loadingDialog = LoadingDialog.showWaitDialog(LoginActivity.this, getString(R.string.common_isLoading), false, true);
        xUtilsHttpsUtils.getInstance().post(OHUtils.getPrefix(getApplicationContext()) + OHCons.URL.LOGIN_URL, params, new xUtilsHttpsUtils.XCallBack() {
            @Override
            public void onResponse(String result) {
                Gson gson = new Gson();
                LoginBean loginBean = gson.fromJson(result, LoginBean.class);
                    if (loginBean.getAppcode().equals(OHCons.SYS_STATUS.SUCCESS)){
                        if (loginBean.getData().size() > 0) {
                            AosUserPO userPO = loginBean.getData().get(0);
                            SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                            Editor userInfoEditor = userInfo.edit();
                            userInfoEditor.putInt("id", userPO.getId());
                            userInfoEditor.putString("name", userPO.getName());
                            userInfoEditor.putString("sex", userPO.getSex());
                            userInfoEditor.putInt("org_id", userPO.getOrg_id());
                            userInfoEditor.putString("org_name", userPO.getOrg_name());
                            userInfoEditor.putString("email", userPO.getEmail());
                            userInfoEditor.putString("mobile", userPO.getMobile());
                            userInfoEditor.putString("idno", userPO.getIdno());
                            userInfoEditor.putString("account", account);
                            userInfoEditor.putString("password", password);
                            userInfoEditor.commit();
                            mHandler.sendEmptyMessageDelayed(1, 0);
                            Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, OperationActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            mHandler.sendEmptyMessageDelayed(1, 0);
                            Toast.makeText(LoginActivity.this,"服务器返回信息异常，请联系管理员", Toast.LENGTH_SHORT).show();
                        }
                }else {
                    mHandler.sendEmptyMessageDelayed(1, 0);
                    Toast.makeText(LoginActivity.this,loginBean.getAppmsg().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isLogin(){
        SharedPreferences share = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        int user_id = share.getInt("id",0);
        if(user_id == 0){
            return false;
        }else {
            return true;
        }
    }
}
