package com.orangehi.expo.modules.zxing.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.orangehi.expo.R;
import com.orangehi.expo.modules.zxing.decode.DecodeThread;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_result)
public class ResultActivity extends Activity {

	@ViewInject(R.id.result_image)
	private ImageView mResultImage;

	@ViewInject(R.id.result_text)
	private TextView mResultText;

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
			
			mResultImage.setLayoutParams(lps);

			String result = extras.getString("result");
			mResultText.setText(result);
//			try {
//				EncryptUtil encryptUtil = new EncryptUtil("orangehi", "utf-8");
//				Log.i("ResultMsg", encryptUtil.decode(result));
//				mResultText.setText(encryptUtil.decode(result));
//			}catch (Exception e){
//
//			}
			Bitmap barcode = null;
			byte[] compressedBitmap = extras.getByteArray(DecodeThread.BARCODE_BITMAP);
			if (compressedBitmap != null) {
				barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
				// Mutable copy:
				barcode = barcode.copy(Bitmap.Config.RGB_565, true);
			}

			mResultImage.setImageBitmap(barcode);
		}
	}
}
