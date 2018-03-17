package com.pnp;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.activity.BaseActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegistActivity extends BaseActivity {

	@BindView(id = R.id.regist_button, click = true)
	private Button registButton;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_regist);
	}

	@Override
	protected void initWidget() {
		super.initWidget();
		final TextView titleView = (TextView) findViewById(R.id.actionbar_title);
		titleView.setText("用户注册");
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.regist_button) {

		}
	}

}
