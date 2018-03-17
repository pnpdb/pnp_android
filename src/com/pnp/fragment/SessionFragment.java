package com.pnp.fragment;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.fragment.BaseFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.pnp.ChatActivity;
import com.pnp.R;
import com.pnp.adapter.SessionAdapter;
import com.pnp.model.SessionModel;
import com.pnp.utils.PreferenceConstants;
import com.pnp.widget.ListViewCompat;

public class SessionFragment extends BaseFragment implements
		OnItemClickListener {

	@BindView(id = R.id.session_list)
	private ListViewCompat mList;

	private List<SessionModel> mData;

	private SessionAdapter mAdapter;

	@Override
	protected View inflaterView(LayoutInflater inflater, ViewGroup parent,
			Bundle bundle) {
		return inflater.inflate(R.layout.fragment_session, null);
	}

	@Override
	protected void initWidget(View parentView) {
		super.initWidget(parentView);
		final TextView titleView = (TextView) getActivity().findViewById(
				R.id.actionbar_title);
		titleView.setText("会话");
		final Button backButton = (Button) getActivity().findViewById(
				R.id.actionbar_back);
		backButton.setVisibility(View.GONE);
		mAdapter = new SessionAdapter(getActivity(), getSessionData());
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(this);
	}

	public void removeFromList(int position) {
		mData.remove(position);
		mAdapter.notifyDataSetChanged();
	}

	private List<SessionModel> getSessionData() {
		mData = new ArrayList<SessionModel>();

		SessionModel sm1 = new SessionModel();
		sm1.setSessionDate("5分钟");
		sm1.setSessionName("李安海");
		sm1.setSessionRecord("今天晚上吃什么");

		SessionModel sm2 = new SessionModel();
		sm2.setSessionDate("一天前");
		sm2.setSessionName("闫闵闵");
		sm2.setSessionRecord("怎么还没回来。。。。");

		SessionModel sm3 = new SessionModel();
		sm3.setSessionDate("星期一");
		sm3.setSessionName("王小二");
		sm3.setSessionRecord("我的18618331000");

		SessionModel sm4 = new SessionModel();
		sm4.setSessionDate("10-09 12:03");
		sm4.setSessionName("刘德华");
		sm4.setSessionRecord("欢迎大家收看我的新电影");

		mData.add(sm1);
		mData.add(sm2);
		mData.add(sm3);
		mData.add(sm4);

		return mData;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {
		Intent intent = new Intent();
		intent.setClass(getActivity(), ChatActivity.class);
		intent.putExtra(PreferenceConstants.SESSION_NAME, mData.get(position)
				.getSessionName());
		getActivity().startActivity(intent);
	}
}
