package com.pnp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.pnp.adapter.AlbumGridAdapter;
import com.pnp.utils.ChatIdentifier;

/**
 * GridView显示一个相册的图片
 * 
 * @author lianhai
 * 
 */
public class AlbumGridActivity extends Activity {

	private GridView mGridView;
	private List<Item> mData;
	private List<Item> readlData;
	private AlbumGridAdapter mAdapter;

	/** 底部预览的发送按钮 */
	private Button previewButton, sendButton;

	/** 当前是否正在滚动 */
	// private boolean mScrollState = false;

	/** 选中发送的Pic列表 */
	private ArrayList<String> picBeSend = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album_grid);
		String title = getIntent().getStringExtra("album_name");
		if (null != title) {
			final TextView titleView = (TextView) findViewById(R.id.actionbar_title);
			titleView.setText(title);
		}

		initMem();
		findViews();
		registListener();
		setGridView(getIntent().getStringExtra("album_path"));
	}

	private void setGridView(String path) {
		if (path != null) {
			getFiles(path);
			mGridView.setAdapter(mAdapter);
			mGridView.setOnScrollListener(mScrollListener);
			mGridView.setOnItemClickListener(mItemClickListener);
		}
	}

	/**
	 * 根据路径遍历相册，并填充Adapter数据
	 * 
	 * @author lianhai
	 */
	private void getFiles(String path) {
		File root = new File(path);
		if (!root.exists())
			return;
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				// 递归调用
				getFiles(file.getAbsolutePath());
			} else {
				Item item = new Item();
				item.setPicPath(file.getAbsolutePath());
				mData.add(item);
			}
		}
	}

	private void initMem() {
		mData = new ArrayList<AlbumGridActivity.Item>();
		readlData = new ArrayList<AlbumGridActivity.Item>();
		mAdapter = new AlbumGridAdapter(getBaseContext(), getResources(), mData);
	}

	private void findViews() {
		mGridView = (GridView) findViewById(R.id.acv_album_grid_gridview);
		sendButton = (Button) findViewById(R.id.acv_album_grid_button_send);
		previewButton = (Button) findViewById(R.id.acv_album_grid_button_preview);
	}

	private void registListener() {
		sendButton.setOnClickListener(mOnClickListener);
		previewButton.setOnClickListener(mOnClickListener);
	}

	/**
	 * 实现onScrollListener接口
	 * 
	 * @author lianhai
	 */

	private OnScrollListener mScrollListener = new OnScrollListener() {
		private int start_index;
		private int end_index;

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			start_index = firstVisibleItem;
			end_index = firstVisibleItem + visibleItemCount;
			if (end_index >= totalItemCount) {
				end_index = totalItemCount - 1;
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:
				// mScrollState = false;
				pageImgLoad(start_index, end_index);
				break;
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				// mScrollState = true;
				break;
			case OnScrollListener.SCROLL_STATE_FLING:
				// mScrollState = true;
				break;
			}
		}

		private void pageImgLoad(int start_index, int end_index) {
			readlData.clear();
			for (; start_index < end_index; start_index++) {
				readlData.add(mData.get(start_index));
			}
			mAdapter.notifyDataSetChanged();
		}
	};

	/**
	 * 实现OnItemClickListener
	 * 
	 * @author lianhai
	 * 
	 */

	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long id) {
			AlbumGridAdapter.ViewHolder viewHolder = (AlbumGridAdapter.ViewHolder) view
					.getTag();
			boolean bool = mData.get(position).isChoose();
			// 维护待发送图片列表
			if (!bool) {
				picBeSend.add(mData.get(position).getPicPath());
				viewHolder.getChooseView().setVisibility(View.VISIBLE);
			} else {
				picBeSend.remove(mData.get(position).getPicPath());
				viewHolder.getChooseView().setVisibility(View.GONE);
			}
			// 根据选中数量设置Send Button的背景
			if (picBeSend.size() > 0) {
				sendButton
						.setBackgroundResource(R.drawable.common_short_blue_selector);
			} else {
				sendButton
						.setBackgroundResource(R.drawable.button_short_blue_grey);
			}
			// 刷新GridView
			mData.get(position).setChoose(!bool);
			// mAdapter.notifyDataSetChanged();
		}
	};

	/**
	 * onClick事件监听器
	 * 
	 * @author lianhai
	 * 
	 */
	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.acv_album_grid_button_preview:
				if (picBeSend.size() > 0)
					GoPreview();
				break;
			case R.id.acv_album_grid_button_send:
				if (picBeSend.size() > 0)
					GoPhotoList();
				break;
			default:
				break;
			}
		}

		private void GoPhotoList() {
			Intent intent = new Intent(AlbumGridActivity.this,
					AlbumListActivity.class);
			intent.putStringArrayListExtra("picList", picBeSend);
			setResult(ChatIdentifier.REQUEST_IMAGE_FROM_PATH, intent);
			finish();
		}

		private void GoPreview() {
			Intent intent = new Intent(AlbumGridActivity.this,
					PreviewActivity.class);
			intent.putStringArrayListExtra("imageList", picBeSend);
			startActivity(intent);
		}
	};
	
	/**
	 * GridView Item实体项
	 * 
	 * @author lianhai
	 * 
	 */
	public static class Item {
		private String picPath;
		private boolean isChoose = false;

		public String getPicPath() {
			return picPath;
		}

		public void setPicPath(String picPath) {
			this.picPath = picPath;
		}

		public boolean isChoose() {
			return isChoose;
		}

		public void setChoose(boolean isChoose) {
			this.isChoose = isChoose;
		}
	}
}
