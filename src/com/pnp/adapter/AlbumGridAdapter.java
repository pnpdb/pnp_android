package com.pnp.adapter;

import java.lang.ref.WeakReference;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.pnp.AlbumGridActivity;
import com.pnp.AlbumGridActivity.Item;
import com.pnp.bitmap.ImageTools;
import com.pnp.R;
import com.pnp.widget.OptimizeGridView;

public class AlbumGridAdapter extends BaseAdapter implements
		OptimizeGridView.OptimizeGridAdapter<AlbumGridActivity.Item> {

	private List<AlbumGridActivity.Item> mData;
	public static Item NULL_ITEM = new Item();
	public Bitmap mPlaceHolderBitmap;
	private Context context;
	private Resources res;

	public AlbumGridAdapter(Context context, Resources res,
			List<AlbumGridActivity.Item> mData) {
		this.context = context;
		this.mData = mData;
		this.res = res;
		mPlaceHolderBitmap = ImageTools.getBitmapThumbnail(
				context.getResources(), R.drawable.image_replace, 60, 60);
	}

	@Override
	public List<Item> getItems() {
		return mData;
	}

	@Override
	public void setItems(List<Item> items) {
		mData = items;
		notifyDataSetChanged();
	}

	@Override
	public Item getNullItem() {
		return NULL_ITEM;
	}

	@Override
	public boolean isNullItem(Item item) {
		return item == NULL_ITEM;
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
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.album_grid_item, null);
			viewHolder.pictureView = (ImageView) convertView
					.findViewById(R.id.album_grid_item_icon);
			viewHolder.chooseView = (ImageView) convertView
					.findViewById(R.id.album_grid_checked_icon);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Item item = (Item) getItem(position);
		if (isNullItem(item)) {
			convertView.setBackgroundResource(R.drawable.album_grid_bg);
			viewHolder.pictureView.setVisibility(View.INVISIBLE);
			return convertView;
		}
		viewHolder.pictureView.setVisibility(View.VISIBLE);
		convertView.setBackgroundResource(R.drawable.album_grid_item_border);
		// Bitmap bm =
		// BitmapTool.getImageThumbnail(mData.get(position).getPicPath(), 80,
		// 80);
		// BitmapDrawable drawable = new BitmapDrawable(res, bm);
		// viewHolder.pictureView.setImageDrawable(drawable);
		loadBitmap(mData.get(position).getPicPath(), viewHolder.pictureView);

		if (mData.get(position).isChoose())
			viewHolder.chooseView.setVisibility(View.VISIBLE);
		else
			viewHolder.chooseView.setVisibility(View.GONE);
		return convertView;
	}

	/**
	 * ImageView����ͼƬ
	 * 
	 * @param resId
	 * @param imageView
	 */
	public void loadBitmap(String imgPath, ImageView imageView) {
		if (cancelPotentialWork(imgPath, imageView)) {
			final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
			final AsyncDrawable asyncDrawable = new AsyncDrawable(res,
					mPlaceHolderBitmap, task);
			imageView.setImageDrawable(asyncDrawable);
			task.execute(imgPath);
		}
	}

	static class AsyncDrawable extends BitmapDrawable {
		private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

		public AsyncDrawable(Resources res, Bitmap bitmap,
				BitmapWorkerTask bitmapWorkerTask) {
			super(res, bitmap);
			bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(
					bitmapWorkerTask);
		}

		public BitmapWorkerTask getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}
	}

	public static boolean cancelPotentialWork(String data, ImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

		if (bitmapWorkerTask != null) {
			final String bitmapData = bitmapWorkerTask.data;
			if (bitmapData != data) {
				// Cancel previous task
				bitmapWorkerTask.cancel(true);
			} else {
				// The same work is already in progress
				return false;
			}
		}
		// No task associated with the ImageView, or an existing task was
		// cancelled
		return true;
	}

	private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
		private final WeakReference<ImageView> imageViewReference;
		private String data;

		public BitmapWorkerTask(ImageView imageView) {
			// Use a WeakReference to ensure the ImageView can be garbage
			// collected
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		// Decode image in background.
		@Override
		protected Bitmap doInBackground(String... params) {
			data = params[0];
			return ImageTools.getImageThumbnail(data, 80, 80);
		}

		// Once complete, see if ImageView is still around and set bitmap.
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (imageViewReference != null && bitmap != null) {
				final ImageView imageView = imageViewReference.get();
				if (imageView != null) {
					BitmapDrawable drawable = new BitmapDrawable(res, bitmap);
					imageView.setImageDrawable(drawable);
					// imageView.setImageBitmap(bitmap);
					System.gc();
				}
			}
		}
	}

	public static class ViewHolder {
		private ImageView pictureView;
		private ImageView chooseView;

		public ImageView getPictureView() {
			return pictureView;
		}

		public void setPictureView(ImageView pictureView) {
			this.pictureView = pictureView;
		}

		public ImageView getChooseView() {
			return chooseView;
		}

		public void setChooseView(ImageView chooseView) {
			this.chooseView = chooseView;
		}
	}

}
