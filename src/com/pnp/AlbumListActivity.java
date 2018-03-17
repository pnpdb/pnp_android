package com.pnp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.pnp.adapter.AlbumDBAdapter;
import com.pnp.adapter.AlbumListAdapter;
import com.pnp.model.ImageModel;
import com.pnp.utils.AlbumUtil;
import com.pnp.utils.ChatIdentifier;

/**
 * ListView显示手机相册和缩略图
 * 
 * @author lianhai
 * 
 */
public class AlbumListActivity extends Activity {

	/** 相册显示ListView */
	private ListView mListView;
	private AlbumDBAdapter dbAdapter;
	private int flag = 1;
	private Cursor internalCursor;
	private Cursor externalCursor;
	private List<ImageModel> mData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album_list);
		final TextView titleView = (TextView) findViewById(R.id.actionbar_title);
		titleView.setText(getResources().getString(R.string.local_album));
		mListView = (ListView) findViewById(android.R.id.list);
		mData = new LinkedList<ImageModel>();
		setList();
		mListView.setOnItemClickListener(mItemClickListener);
	}

	/**
	 * 扫描本机相册，获取相册名称缩略图以及图片数量，填充ListView
	 */

	private void setList() {
		try {
			dbAdapter = new AlbumDBAdapter(this);
			dbAdapter.open();
			getThumbnailsPhotosInfo();
			if (dbAdapter != null)
				dbAdapter.close();
			mListView.setAdapter(new AlbumListAdapter(this, mData));
		} catch (Exception err) {
			if (dbAdapter != null)
				dbAdapter.close();
			err.printStackTrace();
			return;
		}
	}

	/**
	 * 扫描本机填充Data List
	 * 
	 * @author lianhai
	 */

	private void getThumbnailsPhotosInfo() {
		internalCursor = dbAdapter.getAllImages();
		if (internalCursor == null) {
			internalCursor.close();
			return;
		}
		try {
			externalCursor = getContentResolver().query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,
					null, null);
			if (externalCursor == null) {
				return;
			}
		} catch (Exception err) {
			if (externalCursor != null)
				externalCursor.close();
			return;
		}

		HashMap<String, LinkedList<String>> internalAlbums = AlbumUtil
				.getAlbumsInfo(flag, internalCursor);
		HashMap<String, LinkedList<String>> externamAlbums = AlbumUtil
				.getAlbumsInfo(flag, externalCursor);
		setPhotoMap(internalAlbums, false);
		setPhotoMap(externamAlbums, true);

		internalCursor.close();
		externalCursor.close();
	}

	private void setPhotoMap(HashMap<String, LinkedList<String>> map,
			boolean isExternal) {
		ImageModel info = null;
		for (Iterator<?> it = map.entrySet().iterator(); it.hasNext();) {
			@SuppressWarnings("unchecked")
			Map.Entry<String, LinkedList<String>> e = (Map.Entry<String, LinkedList<String>>) it
					.next();
			LinkedList<String> album = (LinkedList<String>) e.getValue();

			if (album != null && album.size() > 0) {
				info = new ImageModel();
				info.displayName = (String) e.getKey();
				info.picturecount = String.valueOf(album.size());

				// 提取首歌记录的信息。
				String id = album.get(0).split("&")[0];
				String albumpath = album.get(0).split("&")[1];

				String name = albumpath
						.substring(albumpath.lastIndexOf("/") + 1);
				albumpath = albumpath.substring(0, albumpath.lastIndexOf("/"));

				if (!isExternal) {
					try {
						Bitmap image = AlbumUtil.readBitmaps(info.displayName
								+ "_" + name);
						if (image == null) {
							Resources res = getResources();
							image = BitmapFactory.decodeResource(res,
									R.drawable.ic_launcher);
						}
						info.icon = image;
					} catch (Exception err) {
						err.printStackTrace();
					}
				} else {
					info.icon = Thumbnails.getThumbnail(getContentResolver(),
							Integer.valueOf(id), Thumbnails.MICRO_KIND,
							new BitmapFactory.Options());
				}
				info.path = albumpath;

				List<String> list = new ArrayList<String>();
				for (String str : album) {
					list.add(str);
				}
				info.tag = list;
				mData.add(info);
			}
		}
	}

	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int position,
				long id) {
			String name = mData.get(position).displayName;
			String path = mData.get(position).path;
			Intent intent = new Intent(AlbumListActivity.this,
					AlbumGridActivity.class);
			intent.putExtra("album_name", name);
			intent.putExtra("album_path", path);
			startActivityForResult(intent,
					ChatIdentifier.REQUEST_IMAGE_FROM_PATH);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			if (resultCode == ChatIdentifier.REQUEST_IMAGE_FROM_PATH) {
				Intent intent = new Intent(AlbumListActivity.this,
						ChatActivity.class);
				intent.putStringArrayListExtra("picList",
						data.getStringArrayListExtra("picList"));
				setResult(ChatIdentifier.MESSAGE_IMAGE, intent);
				finish();
			}
		}
	}

}
