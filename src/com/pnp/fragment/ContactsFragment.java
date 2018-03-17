package com.pnp.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.fragment.BaseFragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.pnp.ContactInfoActivity;
import com.pnp.R;
import com.pnp.adapter.ContactsAdapter;
import com.pnp.model.ContactModel;
import com.pnp.utils.CharacterParser;
import com.pnp.utils.PinyinComparator;
import com.pnp.utils.PreferenceConstants;
import com.pnp.widget.ClearEditText;
import com.pnp.widget.SideBar;

public class ContactsFragment extends BaseFragment {

	@BindView(id = R.id.contact_list)
	private ListView sortListView;

	@BindView(id = R.id.contact_sidrbar)
	private SideBar sideBar;

	@BindView(id = R.id.contact_dialog)
	private TextView dialog;

	@BindView(id = R.id.contact_filter_edit)
	private ClearEditText mClearEditText;

	private ContactsAdapter mAdapter;
	private List<ContactModel> SourceDateList;
	private PinyinComparator pinyinComparator = new PinyinComparator();;
	private CharacterParser characterParser = CharacterParser.getInstance();

	@Override
	protected View inflaterView(LayoutInflater inflater, ViewGroup parent,
			Bundle bundle) {
		return inflater.inflate(R.layout.fragment_contacts, null);
	}

	@Override
	protected void initWidget(View parentView) {
		super.initWidget(parentView);
		final TextView titleView = (TextView) getActivity().findViewById(
				R.id.actionbar_title);
		titleView.setText("联系人");
		final Button backButton = (Button) getActivity().findViewById(
				R.id.actionbar_back);
		backButton.setVisibility(View.GONE);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = mAdapter.getPositionForSection(s.charAt(0));
				System.out.println("dsfasdfad:" + position + "  "
						+ (sortListView == null));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});
		sortListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						Intent intent = new Intent();
						intent.setClass(getActivity(),
								ContactInfoActivity.class);
						intent.putExtra(
								PreferenceConstants.SESSION_NAME,
								getActivity().getResources().getStringArray(
										R.array.img_src_name)[position]);
						getActivity().startActivity(intent);
						// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
						// Toast.makeText(getActivity(), ((SortEntry)
						// adapter.getItem(position)).getName(),
						// Toast.LENGTH_SHORT).show();
					}
				});

		SourceDateList = filledData(
				getResources().getStringArray(R.array.img_src_name),
				getResources().getStringArray(R.array.img_src_data));

		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		mAdapter = new ContactsAdapter(getActivity(), SourceDateList);
		sortListView.setAdapter(mAdapter);

		// //根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private List<ContactModel> filledData(String[] date, String[] imgData) {
		List<ContactModel> mSortList = new ArrayList<ContactModel>();

		for (int i = 0; i < date.length; i++) {
			ContactModel sortModel = new ContactModel();
			sortModel.setUserGuid(imgData[i]);
			sortModel.setContactName(date[i]);
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase(
					Locale.getDefault());

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase(Locale
						.getDefault()));
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<ContactModel> filterDateList = new ArrayList<ContactModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (ContactModel sortModel : SourceDateList) {
				String name = sortModel.getContactName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		mAdapter.updateListView(filterDateList);
	}
}
