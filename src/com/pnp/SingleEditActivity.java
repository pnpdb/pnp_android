package com.pnp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SingleEditActivity extends Activity {

	private TextView doneButton;
	private EditText contentText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_edit);
		final TextView titleView = (TextView) findViewById(R.id.actionbar_title);
		titleView.setText(getIntent().getStringExtra("tag"));
		final Button backButton = (Button) findViewById(R.id.actionbar_back);
		// 返回事件监听
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		contentText = (EditText) findViewById(R.id.single_text);
		doneButton = (TextView) findViewById(R.id.actionbar_action);
		doneButton.setVisibility(View.VISIBLE);
		doneButton.setText("完成");
		// 完成事件监听
		doneButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String content = contentText.getText().toString().trim();
				Intent intent = new Intent(SingleEditActivity.this,
						PersonalActivity.class);
				intent.putExtra("tag", content);
				if (titleView.getText().equals("账号")) {
					setResult(0, intent);
				} else if (titleView.getText().equals("昵称")) {
					setResult(1, intent);
				} else if (titleView.getText().equals("个性签名")) {
					setResult(2, intent);
				}
			}
		});
	}
}
