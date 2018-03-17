package com.pnp.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pnp.R;
import com.pnp.model.KVModel;
import com.pnp.widget.CircleImageView;

public class PersonalAdapter extends BaseAdapter {

	private Context mContext;
	private List<KVModel> mData;

	public PersonalAdapter(Context context, List<KVModel> data) {
		this.mContext = context;
		this.mData = data;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;

		if (convertView == null) {
			LayoutInflater mInflater = LayoutInflater.from(mContext);
			convertView = mInflater.inflate(R.layout.personal_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final KVModel model = mData.get(position);
		viewHolder.keyView.setText(model.getKey());
		if (model.isAvator()) {
			viewHolder.valueView.setVisibility(View.GONE);
			viewHolder.avatorView.setVisibility(View.VISIBLE);
			viewHolder.avatorView.setImageResource(R.drawable.portrait_test);
		} else {
			viewHolder.valueView.setVisibility(View.VISIBLE);
			viewHolder.avatorView.setVisibility(View.GONE);
			viewHolder.valueView.setText(model.getValue());
		}

		return convertView;
	}

	static class ViewHolder {
		TextView keyView;
		TextView valueView;
		CircleImageView avatorView;

		public ViewHolder(View v) {
			this.keyView = (TextView) v.findViewById(R.id.personal_key);
			this.valueView = (TextView) v.findViewById(R.id.personal_value);
			this.avatorView = (CircleImageView) v.findViewById(R.id.personal_image);
		}
	}

}
