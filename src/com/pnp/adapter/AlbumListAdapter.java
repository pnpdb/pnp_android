package com.pnp.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pnp.R;
import com.pnp.model.ImageModel;

public class AlbumListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<ImageModel> imageInfo;
	private Context context;

	public AlbumListAdapter(Context context, List<ImageModel> imageInfos) {
		this.imageInfo = imageInfos;
		this.context = context;
	}

	public int getCount() {
		return imageInfo.size();
	}

	public Object getItem(int position) {
		return imageInfo.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.album_list_item, null);
			// convertView.setDrawingCacheBackgroundColor(Color.TRANSPARENT);
			holder = new ViewHolder();
			holder.albumIcon = (ImageView) convertView
					.findViewById(R.id.album_list_img);
			holder.albumName = (TextView) convertView
					.findViewById(R.id.album_list_name);
			holder.albumPicCount = (TextView) convertView
					.findViewById(R.id.album_list_count);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (imageInfo == null) {
			return convertView;
		}
		if (imageInfo.get(position) == null) {
			return convertView;
		}
		File f = new File(imageInfo.get(position).path);
		String fName = f.getName();
		holder.albumIcon.setImageBitmap(imageInfo.get(position).icon);
		holder.albumName.setText(fName);
		holder.albumPicCount.setText(imageInfo.get(position).picturecount
				+ "张照片");
		return convertView;
	}

	private class ViewHolder {
		private TextView albumName;
		private TextView albumPicCount;
		private ImageView albumIcon;
	}
}
