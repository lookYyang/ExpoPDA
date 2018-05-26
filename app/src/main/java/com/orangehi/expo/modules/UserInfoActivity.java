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
import android.widget.TextView;

import com.orangehi.expo.R;
import com.orangehi.expo.common.LoadingDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.user_info)
public class UserInfoActivity extends AppCompatActivity {

    @ViewInject(R.id.btn_signout)
    private Button btn_signout;

    @ViewInject(R.id.textName)
    private TextView textName;

    @ViewInject(R.id.textAccount)
    private TextView textAccount;

    @ViewInject(R.id.textOrgName)
    private TextView textOrgName;

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
        SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        textName.setText(userInfo.getString("name", ""));
        textAccount.setText(userInfo.getString("account", ""));
        textOrgName.setText(userInfo.getString("org_name", ""));
        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                Editor userInfoEditor = userInfo.edit();
                userInfoEditor.putInt("id", 0);
                userInfoEditor.commit();
                Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
