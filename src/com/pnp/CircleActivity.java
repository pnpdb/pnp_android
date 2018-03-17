package com.pnp;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.activity.BaseActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.pnp.adapter.CircleAdapter;
import com.pnp.model.CircleImgs;
import com.pnp.model.CircleInfo;

public class CircleActivity extends BaseActivity {

	@BindView(id = R.id.circle_list)
	private ListView mListView;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_circle);
	}

	@Override
	protected void initWidget() {
		super.initWidget();
		final TextView titleView = (TextView) findViewById(R.id.actionbar_title);
		titleView.setText("朋友圈");
		final Button backButton = (Button) findViewById(R.id.actionbar_back);
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mListView.addHeaderView(getheadView());
		// mListView.setDividerHeight(0);
		setData();
	}

	private void setData() {
		List<CircleInfo> mList = new ArrayList<CircleInfo>();
		CircleInfo mUserInfo = new CircleInfo();
		CircleImgs m = new CircleImgs();
		m.setUrls("http://m1.img.srcdd.com/farm2/d/2011/0817/01/5A461954F44D8DC67A17838AA356FE4B_S64_64_64.JPEG");
		mUserInfo.getUi().add(m);
		mList.add(mUserInfo);
		// ---------------------------------------------
		CircleInfo mUserInfo2 = new CircleInfo();
		CircleImgs m2 = new CircleImgs();
		m2.setUrls("http://m1.img.srcdd.com/farm2/d/2011/0817/01/5A461954F44D8DC67A17838AA356FE4B_S64_64_64.JPEG");
		mUserInfo2.getUi().add(m2);
		CircleImgs m21 = new CircleImgs();
		m21.setUrls("http://m1.img.srcdd.com/farm2/d/2011/0817/01/5A461954F44D8DC67A17838AA356FE4B_S64_64_64.JPEG");
		mUserInfo2.getUi().add(m21);
		mList.add(mUserInfo2);

		CircleAdapter mWeChatAdapter = new CircleAdapter(this);
		mWeChatAdapter.setData(mList);
		mListView.setAdapter(mWeChatAdapter);
	}

	private View getheadView() {
		View view = LayoutInflater.from(CircleActivity.this).inflate(
				R.layout.circle_header, null);
		return view;
	}

}
