package com.pnp.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pnp.ChatActivity;
import com.pnp.R;
import com.pnp.model.ChatMsgModel;
import com.pnp.utils.BaseUtil;

public class ChatAdapter extends BaseAdapter {

	private Context mContext;
	private List<ChatMsgModel> mData;
	private LayoutInflater mInflater;
	ChatActivity sInstance = new ChatActivity();

	public ChatAdapter(Context context, List<ChatMsgModel> data) {
		this.mContext = context;
		this.mData = data;
		mInflater = LayoutInflater.from(mContext);
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
	public View getView(int position, View convertView, ViewGroup parent) {

		final ChatMsgModel model = mData.get(position);

		ViewHolder viewHolder = null;
		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.chatting_item_msg, null);

			viewHolder = new ViewHolder(convertView);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// 接收消息
		if (model.getSource() == -1) {
			viewHolder.rightLayout.setVisibility(View.GONE);
			viewHolder.leftLayout.setVisibility(View.VISIBLE);

			viewHolder.chatTime_l.setText(model.getTime());
			switch (model.getType()) {
			case 0:
				viewHolder.chatImage_l.setVisibility(View.GONE);
				viewHolder.chatAudio_ll_l.setVisibility(View.GONE);
				viewHolder.chatCard_ll_l.setVisibility(View.GONE);
				viewHolder.chatPosition_ll_l.setVisibility(View.GONE);
				viewHolder.chatContent_l.setVisibility(View.VISIBLE);

				// 设置消息文本
				viewHolder.chatContent_l.setText(model.getText());
				break;
			case 1:
				viewHolder.chatContent_l.setVisibility(View.GONE);
				viewHolder.chatAudio_ll_l.setVisibility(View.GONE);
				viewHolder.chatCard_ll_l.setVisibility(View.GONE);
				viewHolder.chatPosition_ll_l.setVisibility(View.GONE);
				viewHolder.chatImage_l.setVisibility(View.VISIBLE);
				// 设置图片消息
				Bitmap bm = BaseUtil.base64ToBitmap(model.getText());
				viewHolder.chatImage_l.setImageDrawable(new BitmapDrawable(
						mContext.getResources(), bm));

				viewHolder.chatImage_l
						.setOnClickListener(sInstance.new ChatItemClickListener(
								model, null));

				break;
			case 2:
				viewHolder.chatContent_l.setVisibility(View.GONE);
				viewHolder.chatAudio_ll_l.setVisibility(View.VISIBLE);
				viewHolder.chatPosition_ll_l.setVisibility(View.GONE);
				viewHolder.chatImage_l.setVisibility(View.GONE);
				viewHolder.chatCard_ll_l.setVisibility(View.GONE);

				viewHolder.chatAudioTime_l.setText(model.getText());

				viewHolder.chatAudio_ll_l
						.setOnClickListener(sInstance.new ChatItemClickListener(
								model, viewHolder.chatVolumnView_l));

				break;
			case 3:
				viewHolder.chatContent_l.setVisibility(View.GONE);
				viewHolder.chatAudio_ll_l.setVisibility(View.GONE);
				viewHolder.chatPosition_ll_l.setVisibility(View.VISIBLE);
				viewHolder.chatImage_l.setVisibility(View.GONE);
				viewHolder.chatCard_ll_l.setVisibility(View.GONE);

				viewHolder.chatPositionValue_l.setText(model.getText());

				viewHolder.chatPosition_ll_l
						.setOnClickListener(sInstance.new ChatItemClickListener(
								model, viewHolder.chatVolumnView_l));
				break;
				
			default:
				break;
			}

			// 发送消息
		} else {
			viewHolder.rightLayout.setVisibility(View.VISIBLE);
			viewHolder.leftLayout.setVisibility(View.GONE);

			viewHolder.chatTime_r.setText(model.getTime());
			switch (model.getType()) {
			case 0:
				viewHolder.chatImage_r.setVisibility(View.GONE);
				viewHolder.chatAudio_ll_r.setVisibility(View.GONE);
				viewHolder.chatPosition_ll_r.setVisibility(View.GONE);
				viewHolder.chatCard_ll_r.setVisibility(View.GONE);
				viewHolder.chatContent_r.setVisibility(View.VISIBLE);
				// 设置消息文本
				viewHolder.chatContent_r.setText(model.getText());
				break;
			case 1:
				viewHolder.chatContent_r.setVisibility(View.GONE);
				viewHolder.chatAudio_ll_r.setVisibility(View.GONE);
				viewHolder.chatCard_ll_r.setVisibility(View.GONE);
				viewHolder.chatPosition_ll_r.setVisibility(View.GONE);
				viewHolder.chatImage_r.setVisibility(View.VISIBLE);
				// 设置图片消息
				Bitmap bm = BaseUtil.base64ToBitmap(model.getText());
				viewHolder.chatImage_r.setImageDrawable(new BitmapDrawable(
						mContext.getResources(), bm));

				viewHolder.chatImage_r
						.setOnClickListener(sInstance.new ChatItemClickListener(
								model, null));
				break;
			case 2:
				viewHolder.chatContent_r.setVisibility(View.GONE);
				viewHolder.chatAudio_ll_r.setVisibility(View.VISIBLE);
				viewHolder.chatPosition_ll_r.setVisibility(View.GONE);
				viewHolder.chatImage_r.setVisibility(View.GONE);
				viewHolder.chatCard_ll_r.setVisibility(View.GONE);

				viewHolder.chatAudioTime_r.setText(model.getText());

				viewHolder.chatAudio_ll_r
						.setOnClickListener(sInstance.new ChatItemClickListener(
								model, viewHolder.chatVolumnView_r));
				break;
			case 3:
				viewHolder.chatContent_r.setVisibility(View.GONE);
				viewHolder.chatAudio_ll_r.setVisibility(View.GONE);
				viewHolder.chatPosition_ll_r.setVisibility(View.VISIBLE);
				viewHolder.chatImage_r.setVisibility(View.GONE);
				viewHolder.chatCard_ll_r.setVisibility(View.GONE);

				viewHolder.chatPositionValue_r.setText(model.getText());

				viewHolder.chatPosition_ll_r
						.setOnClickListener(sInstance.new ChatItemClickListener(
								model, viewHolder.chatVolumnView_r));
				break;
			default:
				break;
			}

		}

		return convertView;
	}

	public static final class ViewHolder {

		LinearLayout leftLayout;
		LinearLayout rightLayout;

		ImageView chatPortrait_l;
		TextView chatTime_l;
		TextView chatContent_l;

		ImageView chatPortrait_r;
		TextView chatTime_r;
		TextView chatContent_r;

		ImageView chatImage_l;
		ImageView chatImage_r;

		// audio
		RelativeLayout chatAudio_ll_l;
		RelativeLayout chatAudio_ll_r;

		TextView chatAudioTime_l;
		TextView chatAudioTime_r;

		ImageView chatVolumnView_l;
		ImageView chatVolumnView_r;

		// position
		RelativeLayout chatPosition_ll_l;
		RelativeLayout chatPosition_ll_r;

		TextView chatPositionValue_l;
		TextView chatPositionValue_r;

		// card
		LinearLayout chatCard_ll_l;
		LinearLayout chatCard_ll_r;
		ImageView chatCard_image_l;
		ImageView chatCard_image_r;
		TextView chatCard_name_l;
		TextView chatCard_name_r;

		public ViewHolder(View v) {

			leftLayout = (LinearLayout) v.findViewById(R.id.chatting_left_id);
			rightLayout = (LinearLayout) v.findViewById(R.id.chatting_right_id);

			chatPortrait_l = (ImageView) v
					.findViewById(R.id.chat_item_left_portrait);
			chatTime_l = (TextView) v.findViewById(R.id.chat_item_left_time);
			chatContent_l = (TextView) v
					.findViewById(R.id.chat_item_left_content);

			chatPortrait_r = (ImageView) v
					.findViewById(R.id.chat_item_right_portrait);
			chatTime_r = (TextView) v.findViewById(R.id.chat_item_right_time);
			chatContent_r = (TextView) v
					.findViewById(R.id.chat_item_right_content);
			// 图片消息
			chatImage_l = (ImageView) v.findViewById(R.id.chat_item_left_image);
			chatImage_r = (ImageView) v
					.findViewById(R.id.chat_item_right_image);

			// audio
			chatAudio_ll_l = (RelativeLayout) v
					.findViewById(R.id.chat_item_left_audioLayout);
			chatAudio_ll_r = (RelativeLayout) v
					.findViewById(R.id.chat_item_right_audioLayout);
			chatAudioTime_l = (TextView) v
					.findViewById(R.id.chat_item_in_audio_lenthView);
			chatAudioTime_r = (TextView) v
					.findViewById(R.id.chat_item_out_audio_lenthView);
			chatVolumnView_l = (ImageView) v
					.findViewById(R.id.chat_item_in_audio_volumnView);
			chatVolumnView_r = (ImageView) v
					.findViewById(R.id.chat_item_out_audio_volumnView);

			chatPosition_ll_l = (RelativeLayout) v
					.findViewById(R.id.chat_item_left_postionLayout);
			chatPosition_ll_r = (RelativeLayout) v
					.findViewById(R.id.chat_item_right_postionLayout);

			chatPositionValue_l = (TextView) v
					.findViewById(R.id.chat_item_left_addressView);
			chatPositionValue_r = (TextView) v
					.findViewById(R.id.chat_item_right_addressView);

			chatCard_ll_l = (LinearLayout) v
					.findViewById(R.id.chat_item_left_cardLayout);
			chatCard_ll_r = (LinearLayout) v
					.findViewById(R.id.chat_item_right_cardLayout);
			chatCard_image_l = (ImageView) v
					.findViewById(R.id.chat_item_left_cardImage);
			chatCard_image_r = (ImageView) v
					.findViewById(R.id.chat_item_right_cardImage);
			chatCard_name_l = (TextView) v
					.findViewById(R.id.chat_item_left_cardName);
			chatCard_name_r = (TextView) v
					.findViewById(R.id.chat_item_right_cardName);
		}

	}

}
