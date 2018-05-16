package com.orangehi.expo.modules;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.orangehi.expo.R;
import com.orangehi.expo.common.OHCons;
import com.orangehi.expo.common.PermissionUtils;
import com.orangehi.expo.common.xUtilsHttpsUtils;
import com.orangehi.expo.modules.zxing.activity.CaptureActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.activity_main)
public class OperationActivity extends AppCompatActivity {

    @ViewInject(R.id.btn_scanCard)
    private Button btn_scanCard;

    @ViewInject(R.id.btn_searchInfo)
    private Button btn_searchInfo;

    @ViewInject(R.id.btn_setParam)
    private Button btn_setParam;

    private static final String TAG = OperationActivity.class.getSimpleName();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        btn_scanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(OperationActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Show camera button pressed. Checking permission.");
                    PermissionUtils.requestPermission(OperationActivity.this, PermissionUtils.CODE_CAMERA, mPermissionGrant);
                } else {
                    Intent intent = new Intent(OperationActivity.this, CaptureActivity.class);
                    startActivity(intent);
                }
            }
        });
        btn_searchInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ticket_code = "12345678";

                Map<String, String> params = new HashMap<String, String>();
                params.put("ticket_code", ticket_code);
                xUtilsHttpsUtils.getInstance().get(OHCons.URL.GET_CARD_INFO_URL, params, new xUtilsHttpsUtils.XCallBack(){
                    @Override
                    public void onResponse(String result) {
                        Log.d("返回值：", result);
//                        Gson gson = new Gson();
//                        JsonReader reader = new JsonReader(new StringReader(result));
//                        reader.setLenient(true);
//                        Map<Object, Object> entity = gson.fromJson(reader, new TypeToken<Map<Object, Object>>() {}.getType());
//                        Log.d("转map：", entity.get("appcode").toString());

                        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                        Type type = new TypeToken<Map<String, String>>() {}.getType();
                        String resultJson = gson.toJson(result);
                            Log.d("##resultJson：", resultJson);
                            Map<String, String> resultMap = gson.fromJson(result, type);
                            if(!resultMap.get("appcode").toString().isEmpty()){
                            if(OHCons.SYS_STATUS.SUCCESS.equals(resultMap.get("appcode"))){
                                if(!resultMap.get("data").toString().isEmpty() ){
                                    Map<String, String> data = gson.fromJson(resultMap.get("data").toString(), type);
                                    Log.d("用户姓名：", data.get("name"));
                                }
                            }else {
                                Log.w("警告：", resultMap.get("appmsg").toString());
                            }
                        }
                    }
                });
            }
        });
        btn_setParam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_RECORD_AUDIO:
                    Toast.makeText(OperationActivity.this, "Result Permission Grant CODE_RECORD_AUDIO", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_GET_ACCOUNTS:
                    Toast.makeText(OperationActivity.this, "Result Permission Grant CODE_GET_ACCOUNTS", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_READ_PHONE_STATE:
                    Toast.makeText(OperationActivity.this, "Result Permission Grant CODE_READ_PHONE_STATE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_CALL_PHONE:
                    Toast.makeText(OperationActivity.this, "Result Permission Grant CODE_CALL_PHONE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_CAMERA:
                    Toast.makeText(OperationActivity.this, "Result Permission Grant CODE_CAMERA", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_ACCESS_FINE_LOCATION:
                    Toast.makeText(OperationActivity.this, "Result Permission Grant CODE_ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_ACCESS_COARSE_LOCATION:
                    Toast.makeText(OperationActivity.this, "Result Permission Grant CODE_ACCESS_COARSE_LOCATION", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_READ_EXTERNAL_STORAGE:
                    Toast.makeText(OperationActivity.this, "Result Permission Grant CODE_READ_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                    Toast.makeText(OperationActivity.this, "Result Permission Grant CODE_WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
}