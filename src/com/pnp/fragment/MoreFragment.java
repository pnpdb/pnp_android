package com.pnp.fragment;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.fragment.BaseFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pnp.CircleActivity;
import com.pnp.CollectionActivity;
import com.pnp.PersonalActivity;
import com.pnp.R;
import com.pnp.ScanActivity;
import com.pnp.SettingActivity;

public class MoreFragment extends BaseFragment {

	@BindView(id = R.id.more_list1)
	private ListView list1;
	@BindView(id = R.id.more_list2)
	private ListView list2;
	@BindView(id = R.id.more_list3)
	private ListView list3;

	String array1[] = { "个人信息" };
	String array2[] = { "朋友圈", "扫一扫", "我的收藏" };
	String array3[] = { "设置" };

	@Override
	protected View inflaterView(LayoutInflater inflater, ViewGroup parent,
			Bundle bundle) {
		return inflater.inflate(R.layout.fragment_more, null);
	}

	@Override
	protected void initWidget(View parentView) {
		super.initWidget(parentView);
		final TextView titleView = (TextView) getActivity().findViewById(
				R.id.actionbar_title);
		titleView.setText("更多");
		final Button backButton = (Button) getActivity().findViewById(
				R.id.actionbar_back);
		backButton.setVisibility(View.GONE);

		list1.setAdapter(new MyListAdapter(array1));
		list2.setAdapter(new MyListAdapter(array2));
		list3.setAdapter(new MyListAdapter(array3));

		list1.setOnItemClickListener(listListener1);
		list2.setOnItemClickListener(listListener2);
		list3.setOnItemClickListener(listListener3);
	}

	private class MyListAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private String[] array;

		public MyListAdapter(String[] array) {
			inflater = LayoutInflater.from(getActivity());
			this.array = array;
		}

		@Override
		public int getCount() {
			return array.length;
		}

		@Override
		public Object getItem(int position) {
			return array[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = inflater.inflate(R.layout.fragment_more_list_item,
					null);
			TextView tv = (TextView) convertView.findViewById(R.id.text);// 由于圆角listview一般都是固定的迹象，所以在这里没有做优化处理，需要的话可自行
			ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
			if (array[position].equals("个人信息"))
				icon.setImageResource(R.drawable.index_icon);
			if (array[position].equals("朋友圈"))
				icon.setImageResource(R.drawable.fcircle_icon);
			if (array[position].equals("扫一扫"))
				icon.setImageResource(R.drawable.scan_icon);
			if (array[position].equals("我的收藏"))
				icon.setImageResource(R.drawable.collection_icon);
			if (array[position].equals("设置"))
				icon.setImageResource(R.drawable.setting_icon);
			tv.setText(array[position]);
			if (array.length == 1) {
				setBackgroundDrawable(convertView,
						R.drawable.list_round_selector);
			} else if (array.length == 2) {
				if (position == 0) {
					setBackgroundDrawable(convertView,
							R.drawable.list_top_selector);
				} else if (position == array.length - 1) {
					setBackgroundDrawable(convertView,
							R.drawable.list_bottom_selector);
				}
			} else {
				if (position == 0) {
					setBackgroundDrawable(convertView,
							R.drawable.list_top_selector);
				} else if (position == array.length - 1) {
					setBackgroundDrawable(convertView,
							R.drawable.list_bottom_selector);
				} else {
					setBackgroundDrawable(convertView,
							R.drawable.list_rect_selector);
				}
			}
			return convertView;
		}

		@SuppressWarnings("deprecation")
		private void setBackgroundDrawable(View view, int resID) {
			view.setBackgroundDrawable(getResources().getDrawable(resID));
		}
	}

	private OnItemClickListener listListener1 = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent circleIntent = new Intent(getActivity(),
					PersonalActivity.class);
			startActivity(circleIntent);
		}
	};

	private OnItemClickListener listListener3 = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent circleIntent = new Intent(getActivity(),
					SettingActivity.class);
			startActivity(circleIntent);
		}
	};

	private OnItemClickListener listListener2 = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			switch (arg2) {
			case 0:
				Intent circleIntent = new Intent(getActivity(),
						CircleActivity.class);
				startActivity(circleIntent);
				break;
			case 1:
				Intent scanIntent = new Intent(getActivity(),
						ScanActivity.class);
				startActivity(scanIntent);
				break;
			case 2:
				Intent collectionIntent = new Intent(getActivity(),
						CollectionActivity.class);
				startActivity(collectionIntent);
				break;

			default:
				break;
			}
		}
	};

}
