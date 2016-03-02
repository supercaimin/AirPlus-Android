package cn.homecaught.airplus.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import java.util.ArrayList;

import cn.homecaught.airplus.R;
import cn.homecaught.airplus.util.ImageUtils;
import cn.homecaught.airplus.view.ImageCycleView;
import cn.homecaught.airplus.view.ImageCycleView.ImageCycleViewListener;


public class WelcomeActivity extends Activity implements OnClickListener{
	private ImageCycleView mAdView;

	private ArrayList<Integer> mImages = null;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
        mImages = new ArrayList<Integer>();
        mImages.add(R.drawable.tutorial_background_00);
        mImages.add(R.drawable.tutorial_background_01);

        mImages.add(R.drawable.tutorial_background_02);
        mImages.add(R.drawable.tutorial_background_03);

		mAdView = (ImageCycleView) findViewById(R.id.ad_view);
		mAdView.setLocalImageResources(mImages, mAdCycleViewListener);

        findViewById(R.id.btnSignUp).setOnClickListener(this);

        findViewById(R.id.btnSignIn).setOnClickListener(this);
	}

	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
        @Override
        public void displayImageURL(String imageURL, ImageView imageView) {

        }

        @Override
        public void displayImageResId(Integer resId, ImageView imageView) {
            imageView.setImageBitmap(ImageUtils.readBitMap(getApplication(), resId));
        }

        @Override
        public void onImageClick(int position, View imageView) {

        }
    };

	@Override
	protected void onResume() {
		super.onResume();
		mAdView.startImageCycle();
	};

	@Override
	protected void onPause() {
		super.onPause();
		mAdView.pushImageCycle();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mAdView.pushImageCycle();
	}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignUp:
                Intent intent = new Intent(this, SelectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("type",  "city");
                bundle.putString("action", "register");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btnSignIn:
                Intent intentSignIn = new Intent(this, LoginActivity.class);
                startActivity(intentSignIn);
                break;
            default:
                break;
        }
    }

}
