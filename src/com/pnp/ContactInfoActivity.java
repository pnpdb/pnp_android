package com.pnp;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.activity.BaseActivity;

import com.pnp.utils.PreferenceConstants;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ContactInfoActivity extends BaseActivity {

	@BindView(id = R.id.actionbar_title)
	private TextView titleView;

	@BindView(id = R.id.actionbar_back, click = true)
	private Button backButton;

	@BindView(id = R.id.contactinfo_sendMsg, click = true)
	private Button sendMsgButton;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_contactinfo);
	}

	@Override
	protected void initWidget() {
		super.initWidget();
		titleView.setText(getIntent().getStringExtra(
				PreferenceConstants.SESSION_NAME));
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.actionbar_back:
			finish();
			break;
		case R.id.contactinfo_sendMsg:
			Intent intent = new Intent(this, ChatActivity.class);
			intent.putExtra(PreferenceConstants.SESSION_NAME, titleView
					.getText().toString());
			startActivity(intent);
			finish();
		default:
			break;
		}
	}

}
