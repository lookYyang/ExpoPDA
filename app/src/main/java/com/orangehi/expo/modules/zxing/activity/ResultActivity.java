package com.orangehi.expo.modules.zxing.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.orangehi.expo.R;
import com.orangehi.expo.common.JsonUtils;
import com.orangehi.expo.common.LoadingDialog;
import com.orangehi.expo.common.OHCons;
import com.orangehi.expo.common.xUtilsHttpsUtils;
import com.orangehi.expo.common.xUtilsImageUtils;
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
		loadingDialog = LoadingDialog.showWaitDialog(ResultActivity.this, getString(R.string.common_isLoading), false, false);
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
				Map<String, String> params = new HashMap<String, String>();
				params.put("ticket_code", ticket_code);
				xUtilsHttpsUtils.getInstance().get(OHCons.URL.GET_CARD_INFO_URL_TC, params, new xUtilsHttpsUtils.XCallBack(){
					@Override
					public void onResponse(String result) {
						ResultBean resultBean = JsonUtils.fromJson(result, ResultBean.class);

						if(!resultBean.getAppcode().isEmpty()) {
							if(OHCons.SYS_STATUS.SUCCESS.equals(resultBean.getAppcode())){
								if(resultBean.getData().size() > 0){
									SvCardPO svCardPO = resultBean.getData().get(0);
									xUtilsImageUtils.display(facialImage, OHCons.IMAGE_PREFIX + svCardPO.getFacial_photo_path());
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
//			try {
//				EncryptUtil encryptUtil = new EncryptUtil("orangehi", "utf-8");
//				Log.i("ResultMsg", encryptUtil.decode(result));
//				mResultText.setText(encryptUtil.decode(result));
//			}catch (Exception e){
//
//			}
//			Bitmap barcode = null;
//			byte[] compressedBitmap = extras.getByteArray(DecodeThread.BARCODE_BITMAP);
//			if (compressedBitmap != null) {
//				barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
//				// Mutable copy:
//				barcode = barcode.copy(Bitmap.Config.RGB_565, true);
//			}
//
//			mResultImage.setImageBitmap(barcode);

		}
	}
}
