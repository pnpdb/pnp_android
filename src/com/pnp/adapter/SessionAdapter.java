package com.pnp.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pnp.R;
import com.pnp.model.SessionModel;
import com.pnp.widget.SlideView;

public class SessionAdapter extends BaseAdapter {

	private Context mContext;
	private List<SessionModel> mData;
	private SlideView mLastSlideViewWithStatusOn;

	public SessionAdapter(Context context, List<SessionModel> data) {
		this.mContext = context;
		this.mData = data;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		SlideView slideView = (SlideView) convertView;

		if (slideView == null) {
			LayoutInflater mInflater = LayoutInflater.from(mContext);
			View itemView = mInflater.inflate(R.layout.session_item, null);
			slideView = new SlideView(mContext);
			slideView.setContentView(itemView);
			holder = new ViewHolder(slideView);
			slideView.setOnSlideListener(new SlideView.OnSlideListener() {

				@Override
				public void onSlide(View view, int status) {
					if (mLastSlideViewWithStatusOn != null
							&& mLastSlideViewWithStatusOn != view) {
						mLastSlideViewWithStatusOn.shrink();
					}
					if (status == SLIDE_STATUS_ON) {
						mLastSlideViewWithStatusOn = (SlideView) view;
					}
				}
			});
			slideView.setTag(holder);
		} else {
			holder = (ViewHolder) slideView.getTag();
		}

		final SessionModel item = mData.get(position);

		item.setSlideView(slideView);
		item.getSlideView().shrink();

		if (position == 0)
			holder.sessionIcon.setImageResource(R.drawable.lianhai);

		if (position == 1)
			holder.sessionIcon.setImageResource(R.drawable.yanminmin);

		if (position == 2)
			holder.sessionIcon.setImageResource(R.drawable.huangjiaju);

		if (position == 3)
			holder.sessionIcon.setImageResource(R.drawable.haosiyun);

		holder.sessionName.setText(mData.get(position).getSessionName());
		holder.sessionRecord.setText(mData.get(position).getSessionRecord());
		holder.sessionDate.setText(mData.get(position).getSessionDate());

		holder.deleteHolder.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.holder) {
					// Log.e("LLPP::::", "onClick v=" + v + "      " +
					// position);
					mData.remove(position);
					SessionAdapter.this.notifyDataSetChanged();
				}
			}
		});

		return slideView;
	}

	static final class ViewHolder {
		ImageView sessionIcon;
		TextView sessionName;
		TextView sessionRecord;
		TextView sessionDate;
		ViewGroup deleteHolder;

		ViewHolder(View view) {
			sessionIcon = (ImageView) view.findViewById(R.id.session_icon);
			sessionName = (TextView) view.findViewById(R.id.session_name);
			sessionRecord = (TextView) view.findViewById(R.id.session_record);
			sessionDate = (TextView) view.findViewById(R.id.session_date);
			deleteHolder = (ViewGroup) view.findViewById(R.id.holder);
		}
	}

}
