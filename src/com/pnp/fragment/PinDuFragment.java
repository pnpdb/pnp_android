package com.pnp.fragment;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.fragment.BaseFragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pnp.R;

/**
 * Created by lianhai on 14-5-27.
 */
public class PinDuFragment extends BaseFragment {

	@BindView(id = R.id.viewpagerLayout)
	private ViewPager viewPager;

	private List<View> gridViewList;
	private List<String> listData;
	private int pageCount;
	private ImageView imageView;
	private ImageView[] imageViews;
	@BindView(id = R.id.pagerIndicator)
	private LinearLayout mViewPoints;

	public static String K_ITEM;

	@Override
	protected View inflaterView(LayoutInflater inflater, ViewGroup parent,
			Bundle bundle) {
		return inflater.inflate(R.layout.fragment_pindu, null);
	}

	@Override
	protected void initWidget(View parentView) {
		super.initWidget(parentView);
		final TextView titleView = (TextView) getActivity().findViewById(
				R.id.actionbar_title);
		titleView.setText("品度");
		final Button backButton = (Button) getActivity().findViewById(
				R.id.actionbar_back);
		backButton.setVisibility(View.GONE);
		initViewPager();
		initIndicator();
	}

	private void initIndicator() {
		imageViews = new ImageView[pageCount];
		// 添加小圆点的图片
		for (int i = 0; i < pageCount; i++) {
			imageView = new ImageView(getActivity());
			// 设置小圆点imageview的参数
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					10, 10);
			layoutParams.setMargins(5, 0, 5, 0);
			imageView.setLayoutParams(layoutParams);// 创建一个宽高均为20 的布局
			// 将小圆点layout添加到数组中
			imageViews[i] = imageView;
			// 默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
			if (i == 0) {
				imageViews[i].setBackgroundResource(R.drawable.dot_blue);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.dot_gray);
			}
			// 将imageviews添加到小圆点视图组
			mViewPoints.addView(imageViews[i]);
		}

	}

	private void initViewPager() {
		listData = new ArrayList<String>();
		gridViewList = new ArrayList<View>();
		initGridData();
		int size = listData.size();
		// 確定viewpager的頁數
		int len = size / 9;
		if (size % 9 == 0) {
			pageCount = len;
		} else {
			pageCount = len + 1;
		}
		viewPager.setAdapter(new MyPagerAdapter());
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
	}

	class MyPagerAdapter extends PagerAdapter {

		public MyPagerAdapter() {
			for (int i = 0; i < pageCount; i++) {
				GridView gridView = new GridView(getActivity());
				// 去掉GridView Item点击的默认黄色背景
				gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
				gridView.setNumColumns(3);
				gridView.setAdapter(new MyListAdapter(i));
				gridViewList.add(gridView);
			}
		}

		@Override
		public int getCount() {
			return pageCount;// 总页数
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = gridViewList.get(position);
			container.addView(view);
			return view;
		}

	}

	class MyPageChangeListener implements ViewPager.OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[position].setBackgroundResource(R.drawable.dot_blue);
				// 不是当前选中的page，其小圆点设置为未选中的状态
				if (position != i) {
					imageViews[i].setBackgroundResource(R.drawable.dot_gray);
				}
			}
		}

	}

	public class MyListAdapter extends BaseAdapter {

		private ArrayList<String> gridData;

		public MyListAdapter(int page) {
			gridData = new ArrayList<String>();
			for (int i = page * 9; i < page * 9 + 9; i++) {
				if (i == listData.size())
					break;
				gridData.add(listData.get(i));
			}
		}

		@Override
		public int getCount() {
			return gridData == null ? 0 : gridData.size();
		}

		@Override
		public Object getItem(int position) {
			return gridData == null ? null : gridData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		ViewHolder holder = null;

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.index_item, parent, false);
				holder.itemLayout = (LinearLayout) convertView
						.findViewById(R.id.index_item_layout);
				holder.itemIcon = (ImageView) convertView
						.findViewById(R.id.index_item_image);
				holder.itemText = (TextView) convertView
						.findViewById(R.id.index_item_text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if ((position + 1) % 3 == 0)
				holder.itemLayout
						.setBackgroundResource(R.drawable.grid_item_selector_2);
			else
				holder.itemLayout
						.setBackgroundResource(R.drawable.grid_item_selector_1);

			holder.itemText.setText(gridData.get(position));
			holder.itemLayout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			});
			return convertView;
		}

		class ViewHolder {
			LinearLayout itemLayout;
			ImageView itemIcon;
			TextView itemText;
		}
	}

	private void initGridData() {
		listData.add("餐饮");
		listData.add("团购");
		listData.add("机票");
		listData.add("酒店");
		listData.add("娱乐");
		listData.add("彩票");
		listData.add("电影");
		listData.add("精选商品");
		listData.add("旅游");
		listData.add("充值");
		listData.add("游戏平台");
		listData.add("一卡通");
	}
}
