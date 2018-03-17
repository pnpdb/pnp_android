package com.pnp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pnp.R;
import com.pnp.utils.Utils;

public class ShareAdapter extends BaseAdapter {

	private Activity activity;

	private String[] mTextIds = { "相册", "相机", "我的位置", "名片", "涂鸦", "语音通话" };
	private Integer[] mImageIds = { R.drawable.chat_action_images,
			R.drawable.chat_action_camera, R.drawable.chat_action_position,
			R.drawable.chat_action_card, R.drawable.chat_action_tuya,
			R.drawable.chat_action_dial };

	public ShareAdapter(Activity activity) {
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return mTextIds.length;
	}

	@Override
	public Object getItem(int i) {
		return new Object[] { mTextIds[i], mImageIds[i] };
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(activity).inflate(
					R.layout.share_item, null);
			viewHolder = new ViewHolder(convertView);
			viewHolder.imageView.setLayoutParams(new LinearLayout.LayoutParams(
					Utils.getShareItemSize(activity), Utils
							.getShareItemSize(activity)));
			viewHolder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.imageView.setImageResource(mImageIds[position]);
		viewHolder.nameView.setText(mTextIds[position]);

		return convertView;
	}

	final static class ViewHolder {
		ImageView imageView;
		TextView nameView;

		public ViewHolder(View v) {
			imageView = (ImageView) v.findViewById(R.id.share_item_img);
			nameView = (TextView) v.findViewById(R.id.share_item_text);
		}
	}

}
