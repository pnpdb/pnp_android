package com.pnp;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.activity.BaseActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SettingActivity extends BaseActivity {

	@BindView(id = R.id.setting_list1)
	private ListView list1;

	@BindView(id = R.id.setting_list2)
	private ListView list2;

	@BindView(id = R.id.setting_list3)
	private ListView list3;

	String array1[] = { "账号设置" };
	String array2[] = { "消息设置", "聊天设置", "隐私设置", "安全设置" };
	String array3[] = { "清除缓存", "意见反馈", "关于品度" };

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_setting);
	}

	@Override
	protected void initWidget() {
		super.initWidget();
		final TextView titleView = (TextView) findViewById(R.id.actionbar_title);
		titleView.setText("设置");
		final Button backButton = (Button) findViewById(R.id.actionbar_back);
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SettingActivity.this.finish();
			}
		});
		list1.setAdapter(new MyListAdapter(array1));
		list2.setAdapter(new MyListAdapter(array2));
		list3.setAdapter(new MyListAdapter(array3));
	}

	private class MyListAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private String[] array;

		public MyListAdapter(String[] array) {
			inflater = LayoutInflater.from(SettingActivity.this);
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
			convertView = inflater.inflate(R.layout.setting_list_item, null);
			TextView tv = (TextView) convertView.findViewById(R.id.text);
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

}
