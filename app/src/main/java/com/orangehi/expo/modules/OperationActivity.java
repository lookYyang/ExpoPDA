package com.orangehi.expo.modules;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.orangehi.expo.R;
import com.orangehi.expo.Utils.PermissionUtils;
import com.orangehi.expo.Utils.TopBar;
import com.orangehi.expo.modules.zxing.activity.CaptureActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.operation_main)
public class OperationActivity extends AppCompatActivity {

    @ViewInject(R.id.btn_scanCard)
    private Button btn_scanCard;

    @ViewInject(R.id.btn_searchInfo)
    private Button btn_searchInfo;

    @ViewInject(R.id.btn_setParam)
    private Button btn_setParam;

    @ViewInject(R.id.operation_bar)
    private TopBar topBar;

    private static final String TAG = OperationActivity.class.getSimpleName();

    private EditText alertEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        topBar.setOnTitleClickListener(new TopBar.TitleOnClickListener() {
           @Override
           public void onLeftClick() {
               Toast.makeText(OperationActivity.this, "点击了左侧", Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onRightClick() {
               Intent intent = new Intent(OperationActivity.this, UserInfoActivity.class);
               startActivity(intent);
           }
       });

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
                showInputDialog();
            }
        });
        btn_setParam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OperationActivity.this, SetParamsActivity.class);
                startActivity(intent);
            }
        });
    }
    private void showInputDialog() {
        /*
         *  @setView 装入一个EditView
         */
        final EditText editText = new EditText(OperationActivity.this);
        editText.setHint("门票编号/身份证件号/手机号/邮箱");
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(OperationActivity.this);
        inputDialog.setTitle("请输入").setView(editText);
        inputDialog.setPositiveButton("查询",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        try {
                            if(editText.getText().toString().isEmpty()){
                                Toast.makeText(OperationActivity.this, "查询内容不能为空", Toast.LENGTH_SHORT).show();
                            }else {
                                Map<String, String> params = new HashMap<String, String>();
                                Bundle bundle = new Bundle();
                                bundle.putString("search", editText.getText().toString());
                                Intent intent = new Intent(OperationActivity.this, SeaInfoActivity.class).putExtras(bundle);
                                startActivity(intent);
                                try {
                                    Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                                    field.setAccessible(true);
                                    // 将mShowing变量设为false，表示对话框已关闭
                                    field.set(dialog, true);
                                    dialog.dismiss();
                                } catch (NoSuchFieldException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);
                            // 将mShowing变量设为false，表示对话框已关闭
                            field.set(dialog, true);
                            dialog.dismiss();
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }).show();
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
