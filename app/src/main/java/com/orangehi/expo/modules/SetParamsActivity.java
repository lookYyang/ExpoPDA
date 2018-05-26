package com.orangehi.expo.modules;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.orangehi.expo.R;
import com.orangehi.expo.common.JsonUtils;
import com.orangehi.expo.common.LoadingDialog;
import com.orangehi.expo.common.OHCons;
import com.orangehi.expo.Utils.xUtilsHttpsUtils;
import com.orangehi.expo.po.ResultBean;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.set_params)
public class SetParamsActivity extends AppCompatActivity {

    @ViewInject(R.id.edit_host)
    private EditText edit_host;

    @ViewInject(R.id.edit_port)
    private EditText edit_port;

    @ViewInject(R.id.btn_saveSetParams)
    private Button btn_saveSetParams;

    private static final String TAG = SetParamsActivity.class.getSimpleName();

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
        SharedPreferences settings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        edit_host.setText(settings.getString("host", ""));
        edit_port.setText(settings.getString("port", ""));

        btn_saveSetParams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("".equals(edit_host.getText()) || null == edit_host.getText()){
                    Toast.makeText(SetParamsActivity.this, R.string.hostIsNotNull, Toast.LENGTH_SHORT).show();
                }else if("".equals(edit_port.getText()) || null == edit_port.getText()){
                    Toast.makeText(SetParamsActivity.this, R.string.portIsNotNull, Toast.LENGTH_SHORT).show();
                }else {
                    final String host = edit_host.getText().toString();
                    final String port = edit_port.getText().toString();
                    loadingDialog = LoadingDialog.showWaitDialog(SetParamsActivity.this, getString(R.string.common_isLoading), false, true);
                    Map<String, String> params = new HashMap<>();
                    xUtilsHttpsUtils.getInstance().get(OHCons.PREFIX_1 + host + ":" + port + OHCons.PREFIX_2 + OHCons.URL.TEST_CONN, params, new xUtilsHttpsUtils.XCallBack() {

                        @Override
                        public void onResponse(String result) {
                            ResultBean resultBean = JsonUtils.fromJson(result, ResultBean.class);
                            if (!resultBean.getAppcode().isEmpty()) {
                                if (OHCons.SYS_STATUS.SUCCESS.equals(resultBean.getAppcode())) {

                                    SharedPreferences settings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor settingsEditor = settings.edit();
                                    settingsEditor.putString("host", host);
                                    settingsEditor.putString("port", port);
                                    settingsEditor.commit();
                                    mHandler.sendEmptyMessageDelayed(1, 100);
                                    Toast.makeText(SetParamsActivity.this, R.string.saveSuccess, Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(SetParamsActivity.this, R.string.connectTimeOut, Toast.LENGTH_SHORT).show();
                                    mHandler.sendEmptyMessageDelayed(1, 100);
                                }
                            }else {
                                Toast.makeText(SetParamsActivity.this, R.string.connectTimeOut, Toast.LENGTH_SHORT).show();
                                mHandler.sendEmptyMessageDelayed(1, 100);
                            }
                        }
                    });
                }
            }
        });
    }
}
