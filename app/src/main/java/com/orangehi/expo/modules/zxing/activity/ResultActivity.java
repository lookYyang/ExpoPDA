package com.orangehi.expo.modules.zxing.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.orangehi.expo.R;
import com.orangehi.expo.Utils.xUtilsHttpsUtils;
import com.orangehi.expo.Utils.xUtilsImageUtils;
import com.orangehi.expo.common.JsonUtils;
import com.orangehi.expo.common.LoadingDialog;
import com.orangehi.expo.common.OHCons;
import com.orangehi.expo.common.OHUtils;
import com.orangehi.expo.po.ResultBean;
import com.orangehi.expo.po.SvCardPO;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.scan_info)
public class ResultActivity extends Activity {

	@ViewInject(R.id.facial_photo)
	private ImageView facialImage;

	@ViewInject(R.id.textName)
	private TextView textName;

	@ViewInject(R.id.textBirthDay)
	private TextView textBirthDay;

	@ViewInject(R.id.textGender)
	private TextView textGender;

	@ViewInject(R.id.textNationality)
	private TextView textNationality;

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

	@ViewInject(R.id.textCheckTime)
	private TextView textCheckTime;

	@ViewInject(R.id.textCompanyLocation)
	private TextView textCompanyLocation;

	@ViewInject(R.id.btn_pass)
	private Button btn_pass;

	private Dialog loadingDialog;

	// 扫面用户id
	private static String scan_user_id;

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
		Bundle extras = getIntent().getExtras();
		if (null != extras) {
			int width = extras.getInt("width");
			int height = extras.getInt("height");

			LayoutParams lps = new LayoutParams(width, height);
			lps.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
			lps.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
			lps.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
			facialImage.setLayoutParams(lps);

			String ticket_code = extras.getString("result");
			if(ticket_code.isEmpty() || ticket_code.length() != 8){
				Toast.makeText(getApplicationContext(),"二维码有误，请核查！", Toast.LENGTH_LONG).show();
				finish();
			}else {
				loadingDialog = LoadingDialog.showWaitDialog(ResultActivity.this, getString(R.string.common_isLoading), false, true);
				Map<String, String> params = new HashMap<String, String>();
				params.put("ticket_code", ticket_code);
				params.put("connectTimeout", "5000");
				xUtilsHttpsUtils.getInstance().get(OHUtils.getPrefix(getApplicationContext()) + OHCons.URL.GET_CARD_INFO_URL_TC, params, new xUtilsHttpsUtils.XCallBack(){
					@Override
					public void onResponse(String result) {
						ResultBean resultBean = JsonUtils.fromJson(result, ResultBean.class);
						if(!resultBean.getAppcode().isEmpty()) {
							if(OHCons.SYS_STATUS.SUCCESS.equals(resultBean.getAppcode())){
								if(resultBean.getData().size() > 0){
									SvCardPO svCardPO = resultBean.getData().get(0);
									xUtilsImageUtils.display(facialImage, OHCons.IMAGE_PREFIX + svCardPO.getFacial_photo_path());
									scan_user_id = svCardPO.getCard_id();

									textName.setText(svCardPO.getName());

									textGender.setText(svCardPO.getGender());

									textCheckTime.setText(svCardPO.getCheck_time());

									textNationality.setText(svCardPO.getNationality());

									textBirthDay.setText(svCardPO.getBirth_date());

									textGroupName.setText(svCardPO.getGroup_name());

									textIdCardNo.setText(svCardPO.getId_card_no());

									textIdCardType.setText(svCardPO.getId_card_type());

									textCompanyName.setText(svCardPO.getCompany_name());

									textCompanyLocation.setText(svCardPO.getCompany_country() +
											svCardPO.getCompany_province() + svCardPO.getCompany_city() + svCardPO.getCompany_address());

									textEmail.setText(svCardPO.getEmail());

									textMobilePhone.setText(svCardPO.getMobile_phone());
									mHandler.sendEmptyMessageDelayed(1, 100);
								}
							}else {
								Log.w("警告：", resultBean.getAppmsg());
							}
						}
					}
				});
			}

			btn_pass.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if(scan_user_id.isEmpty()){
						Toast.makeText(getApplicationContext(),"用户信息无效", Toast.LENGTH_LONG).show();
						finish();
					}else {
						loadingDialog = LoadingDialog.showWaitDialog(ResultActivity.this, getString(R.string.common_isLoading), false, true);

						Map<String, String> params = new HashMap<String, String>();
						params.put("card_id", scan_user_id);
						SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
						params.put("check_user_id", String.valueOf(userInfo.getInt("id", 1)));
						xUtilsHttpsUtils.getInstance().get(OHUtils.getPrefix(getApplicationContext()) + OHCons.URL.CHECK_TICKET_URL, params, new xUtilsHttpsUtils.XCallBack(){
							@Override
							public void onResponse(String result) {
								ResultBean resultBean = JsonUtils.fromJson(result, ResultBean.class);
								if(!resultBean.getAppcode().isEmpty()) {
									if(OHCons.SYS_STATUS.SUCCESS.equals(resultBean.getAppcode())){
										mHandler.sendEmptyMessageDelayed(1, 100);
										Toast.makeText(getApplicationContext(),resultBean.getAppmsg(), Toast.LENGTH_LONG).show();
										finish();
									}
								}else {
									mHandler.sendEmptyMessageDelayed(1, 100);
									Toast.makeText(getApplicationContext(),"通过失败，服务器请求异常", Toast.LENGTH_LONG).show();
								}
							}
						});
					}
				}
			});
		}
	}
}
