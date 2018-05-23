package com.orangehi.expo.modules;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orangehi.expo.R;
import com.orangehi.expo.common.JsonUtils;
import com.orangehi.expo.common.LoadingDialog;
import com.orangehi.expo.common.OHCons;
import com.orangehi.expo.common.OHUtils;
import com.orangehi.expo.common.xUtilsHttpsUtils;
import com.orangehi.expo.common.xUtilsImageUtils;
import com.orangehi.expo.po.ResultBean;
import com.orangehi.expo.po.SvCardPO;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.search_info)
public class SeaInfoActivity extends AppCompatActivity {

    @ViewInject(R.id.facial_photo)
    private ImageView facialImage;

    @ViewInject(R.id.textName)
    private TextView textName;

    @ViewInject(R.id.textGender)
    private TextView textGender;

    @ViewInject(R.id.textGroupName)
    private TextView textGroupName;

    @ViewInject(R.id.textIdCardNo)
    private TextView textIdCardNo;

    @ViewInject(R.id.textEmail)
    private TextView textEmail;

    @ViewInject(R.id.textMobilePhone)
    private TextView textMobilePhone;

    @ViewInject(R.id.textIdCardType)
    private TextView textIdCardType;

    @ViewInject(R.id.textCompanyName)
    private TextView textCompanyName;

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
        loadingDialog = LoadingDialog.showWaitDialog(SeaInfoActivity.this, getString(R.string.common_isLoading), false, true);
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            String search = extras.getString("search");
            Map<String, String> params = new HashMap<>();
            params.put("search", search);
            xUtilsHttpsUtils.getInstance().get(OHUtils.getPrefix(getApplicationContext()) + OHCons.URL.GET_CARD_INFO_URL, params, new xUtilsHttpsUtils.XCallBack() {
                @Override
                public void onResponse(String result) {
                    ResultBean resultBean = JsonUtils.fromJson(result, ResultBean.class);
                    if (!resultBean.getAppcode().isEmpty()) {
                        if (OHCons.SYS_STATUS.SUCCESS.equals(resultBean.getAppcode())) {
                            if (resultBean.getData().size() > 0) {
                                Gson gson = new Gson();
                                SvCardPO svCardPO = resultBean.getData().get(0);
                                String json = gson.toJson(resultBean.getData().get(0));
                                Map<String, String> resultMap = gson.fromJson(json, new TypeToken<Map<String, String>>(){}.getType());
                                xUtilsImageUtils.display(facialImage, OHCons.IMAGE_PREFIX + resultMap.get("facial_photo_path"));
                                textName.setText(svCardPO.getName());
                                textGender.setText(svCardPO.getGender());
                                textGroupName.setText(resultMap.get("group_name"));
                                textIdCardNo.setText(resultMap.get("id_card_no"));
                                textIdCardType.setText(resultMap.get("id_card_type"));
                                textCompanyName.setText(resultMap.get("company_name"));
                                textEmail.setText(resultMap.get("email"));
                                textMobilePhone.setText(resultMap.get("mobile_phone"));
                                mHandler.sendEmptyMessageDelayed(1, 100);
                            }
                        } else {
                            Toast.makeText(SeaInfoActivity.this, resultBean.getAppmsg(), Toast.LENGTH_LONG).show();
                            mHandler.sendEmptyMessageDelayed(1, 100);
                            finish();
                            Log.w("警告：", resultBean.getAppmsg());
                        }
                    }
                }
            });
        }
    }
}
