package com.pnp;

import org.kymjs.aframe.ui.activity.BaseSplash;

import android.content.Intent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

import com.pnp.utils.LocalStoreUtil;
import com.pnp.utils.PreferenceConstants;
import com.pnp.utils.PreferenceUtil;

public class SplishActivity extends BaseSplash {

	@Override
	protected void setRootBackground(ImageView imageView) {
		imageView.setImageResource(R.drawable.splish_screen);
	}

	@Override
	protected void initWidget() {
		super.initWidget();
		LocalStoreUtil.DirsCreate(this);
		AnimationSet animationSet = new AnimationSet(true);
		AlphaAnimation alphaAnimation = new AlphaAnimation(1.0F, 1.0F);
		alphaAnimation.setDuration(2000);
		animationSet.addAnimation(alphaAnimation);
		// 监听动画过程
		animationSet.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				checkVersion();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				redirectTo();
			}
		});
		mImageView.startAnimation(animationSet);
	}

	/**
	 * 跳转到...
	 */
	protected void redirectTo() {
		Intent intent = new Intent();
		if (firstsInstall()) {
			intent.setClass(this, GuideActivity.class);
		} else {
			intent.setClass(this, LoginActivity.class);
		}
		startActivity(intent);
		finish();
	}

	/**
	 * 判断首次使用
	 */
	protected boolean firstsInstall() {
		if (PreferenceUtil.getPrefBoolean(this,
				PreferenceConstants.PRE_FIRST_INSTALL, true))
			return true;
		return false;
	}

	/**
	 * 检查更新
	 */
	protected void checkVersion() {
	}

}
