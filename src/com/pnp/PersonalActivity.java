package com.pnp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pnp.adapter.PersonalAdapter;
import com.pnp.model.KVModel;

public class PersonalActivity extends Activity implements OnItemClickListener,
		OnClickListener {

	private ListView mListView;
	private PersonalAdapter mAdapter;

	private PopupWindow mpopupWindow;

	private List<KVModel> mData = new ArrayList<KVModel>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);
		final TextView titleView = (TextView) findViewById(R.id.actionbar_title);
		titleView.setText("个人信息");
		final Button backButton = (Button) findViewById(R.id.actionbar_back);
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		initWidget();
	}

	protected void initWidget() {
		mListView = (ListView) findViewById(R.id.personal_list);
		mAdapter = new PersonalAdapter(this, getData());
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	private List<KVModel> getData() {
		mData.add(new KVModel("头像", "avator_tag", true));
		mData.add(new KVModel("账号", "lianhai", false));
		mData.add(new KVModel("昵称", "游泳的鱼", false));
		mData.add(new KVModel("性别", "男", false));
		mData.add(new KVModel("地区", "北京 海淀", false));
		mData.add(new KVModel("个人签名", "一二三四五六七八九十哈哈哈哈", false));
		return mData;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		switch (arg2) {
		case 0:
			showPopMenu();
			break;
		case 1:
			Intent intent1 = new Intent();
			intent1.setClass(PersonalActivity.this, SingleEditActivity.class);
			intent1.putExtra("tag", "账号");
			startActivity(intent1);
			break;
		case 2:
			Intent intent2 = new Intent();
			intent2.setClass(PersonalActivity.this, SingleEditActivity.class);
			intent2.putExtra("tag", "昵称");
			startActivity(intent2);
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			Intent intent5 = new Intent();
			intent5.setClass(PersonalActivity.this, SingleEditActivity.class);
			intent5.putExtra("tag", "个性签名");
			startActivity(intent5);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			switch (requestCode) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			default:
				break;
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void showPopMenu() {
		View view = View.inflate(getApplicationContext(),
				R.layout.popup_avator_menu, null);

		Button rl_album = (Button) view.findViewById(R.id.pop_avator_album);
		Button rl_camera = (Button) view.findViewById(R.id.pop_avator_camera);
		Button rl_cancle = (Button) view.findViewById(R.id.pop_avator_cancle);
		rl_album.setOnClickListener(this);
		rl_camera.setOnClickListener(this);
		rl_cancle.setOnClickListener(this);

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mpopupWindow.dismiss();
			}
		});

		view.startAnimation(AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.fade_in));
		LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		ll_popup.startAnimation(AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.push_bottom_in));

		if (mpopupWindow == null) {
			mpopupWindow = new PopupWindow(this);
			mpopupWindow.setWidth(LayoutParams.MATCH_PARENT);
			mpopupWindow.setHeight(LayoutParams.MATCH_PARENT);
			mpopupWindow.setBackgroundDrawable(new BitmapDrawable());

			mpopupWindow.setFocusable(true);
			mpopupWindow.setOutsideTouchable(true);
		}

		mpopupWindow.setContentView(view);
		mpopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,
				0, 0);
		mpopupWindow.update();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pop_avator_album:
			mpopupWindow.dismiss();
			break;
		case R.id.pop_avator_camera:
			mpopupWindow.dismiss();
			break;
		case R.id.pop_avator_cancle:

			mpopupWindow.dismiss();
			break;
		default:
			break;
		}
	}

}
