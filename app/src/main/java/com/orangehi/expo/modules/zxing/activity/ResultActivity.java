package com.orangehi.expo.modules.zxing.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.orangehi.expo.R;
import com.orangehi.expo.common.JsonUtils;
import com.orangehi.expo.common.OHCons;
import com.orangehi.expo.common.xUtilsHttpsUtils;
import com.orangehi.expo.common.xUtilsImageUtils;
import com.orangehi.expo.po.ResultBean;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.title_list)
public class ResultActivity extends Activity {

	@ViewInject(R.id.facial_photo)
	private ImageView facialImage;

	@ViewInject(R.id.textName)
	private TextView textName;

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
			xUtilsImageUtils.display(facialImage,"http://p4.so.qhmsg.com/sdr/600_900_/t01f4e6c53f0e23d459.jpg");

			String ticket_code = extras.getString("result");

			Map<String, String> params = new HashMap<String, String>();
			params.put("ticket_code", ticket_code);
			xUtilsHttpsUtils.getInstance().get(OHCons.URL.GET_CARD_INFO_URL, params, new xUtilsHttpsUtils.XCallBack(){
				@Override
				public void onResponse(String result) {
					Log.d("返回值", result);
					ResultBean resultBean = JsonUtils.fromJson(result, ResultBean.class);

					if(!resultBean.getAppcode().isEmpty()) {
						if(OHCons.SYS_STATUS.SUCCESS.equals(resultBean.getAppcode())){
							if(resultBean.getData().size() > 0){
								textName.setText(resultBean.getData().get(0).getName());
								Log.d("用户姓名：", resultBean.getData().get(0).getName());
							}
						}else {
							Log.w("警告：", resultBean.getAppmsg().toString());
						}
					}
				}
			});

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
