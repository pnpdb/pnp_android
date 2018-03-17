package com.pnp.adapter;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.pnp.R;
import com.pnp.model.ContactModel;

public class ContactsAdapter extends BaseAdapter implements SectionIndexer {

	private Context mContext;
	private List<ContactModel> mData;

	public ContactsAdapter(Context context, List<ContactModel> data) {
		this.mContext = context;
		this.mData = data;
	}

	public void updateListView(List<ContactModel> data) {
		this.mData = data;
		notifyDataSetChanged();
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

		ViewHolder viewHolder = null;

		if (convertView == null) {
			LayoutInflater mInflater = LayoutInflater.from(mContext);
			convertView = mInflater.inflate(R.layout.contacts_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final ContactModel mContent = mData.get(position);

		// 根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);

		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			viewHolder.contactLetter.setVisibility(View.VISIBLE);
			viewHolder.contactLetter.setText(mContent.getSortLetters());
		} else {
			viewHolder.contactLetter.setVisibility(View.GONE);
		}

		viewHolder.contactName.setText(this.mData.get(position)
				.getContactName());
		// viewHolder.tvTitle.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线

		if (position == 0)
			viewHolder.contactIcon.setImageResource(R.drawable.lianhai);

		if (position == 1)
			viewHolder.contactIcon.setImageResource(R.drawable.yanminmin);

		if (position == 2)
			viewHolder.contactIcon.setImageResource(R.drawable.huangjiaju);

		if (position == 3)
			viewHolder.contactIcon.setImageResource(R.drawable.haosiyun);

		return convertView;
	}

	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = mData.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase(Locale.getDefault()).charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public int getSectionForPosition(int i) {
		return mData.get(i).getSortLetters().charAt(0);
	}

	@Override
	public Object[] getSections() {
		return new Object[0];
	}

	static final class ViewHolder {
		ImageView contactIcon;
		TextView contactName;
		TextView contactLetter;

		ViewHolder(View view) {
			contactIcon = (ImageView) view.findViewById(R.id.contact_icon);
			contactName = (TextView) view.findViewById(R.id.contact_name);
			contactLetter = (TextView) view.findViewById(R.id.contact_letter);
		}
	}

}
