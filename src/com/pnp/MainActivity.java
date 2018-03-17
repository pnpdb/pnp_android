package com.pnp;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.activity.KJFragmentActivity;
import org.kymjs.aframe.ui.fragment.BaseFragment;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pnp.fragment.ContactsFragment;
import com.pnp.fragment.MoreFragment;
import com.pnp.fragment.PinDuFragment;
import com.pnp.fragment.SessionFragment;

public class MainActivity extends KJFragmentActivity {

	@BindView(id = R.id.bottombar_content1, click = true)
	public RadioButton mRbtn1;
	@BindView(id = R.id.bottombar_content2, click = true)
	private RadioButton mRbtn2;
	@BindView(id = R.id.bottombar_content3, click = true)
	private RadioButton mRbtn3;
	@BindView(id = R.id.bottombar_content4, click = true)
	private RadioButton mRbtn4;

	private BaseFragment indexFragment = new PinDuFragment();
	private BaseFragment sessionFragment = new SessionFragment();
	private BaseFragment contactsFragment = new ContactsFragment();
	private BaseFragment moreFragment = new MoreFragment();

	private int fragTag = 1;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_main);
	}

	@Override
	public void changeFragment(BaseFragment targetFragment) {
		changeFragment(R.id.content, targetFragment);
	}

	@Override
	protected void initWidget() {
		final TextView titleView = (TextView) findViewById(R.id.actionbar_title);
		titleView.setText(getResources().getString(R.string.app_name));
		final Button backButton = (Button) findViewById(R.id.actionbar_back);
		backButton.setVisibility(View.GONE);

		mRbtn1.setChecked(true);
		changeFragment(indexFragment);
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.bottombar_content1:
			if (fragTag != 1)
				changeFragment(indexFragment);
			fragTag = 1;
			break;
		case R.id.bottombar_content2:
			if (fragTag != 2)
				changeFragment(sessionFragment);
			fragTag = 2;
			break;
		case R.id.bottombar_content3:
			if (fragTag != 3)
				changeFragment(contactsFragment);
			fragTag = 3;
			break;
		case R.id.bottombar_content4:
			if (fragTag != 4)
				changeFragment(moreFragment);
			fragTag = 4;
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

}
