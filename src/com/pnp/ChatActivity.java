package com.pnp;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.activity.BaseActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pnp.adapter.ChatAdapter;
import com.pnp.adapter.FaceAdapter;
import com.pnp.adapter.FacePageAdapter;
import com.pnp.adapter.ShareAdapter;
import com.pnp.audio.AudioWrapper;
import com.pnp.bitmap.ImageTools;
import com.pnp.model.ChatMsgModel;
import com.pnp.utils.BaseUtil;
import com.pnp.utils.ChatIdentifier;
import com.pnp.utils.MotionUtils;
import com.pnp.utils.PreferenceConstants;
import com.pnp.widget.CirclePageIndicator;

public class ChatActivity extends BaseActivity {

	@BindView(id = R.id.actionbar_title)
	private TextView titleView;

	@BindView(id = R.id.actionbar_back, click = true)
	private Button backButton;

	@BindView(id = R.id.chat_switch_button, click = true)
	private Button switchButton;

	@BindView(id = R.id.chat_content, click = true)
	private EditText contentView;

	@BindView(id = R.id.chat_speak, click = true)
	private Button speakButton;

	@BindView(id = R.id.chat_send_button, click = true)
	private Button sendButton;

	@BindView(id = R.id.chat_list)
	private ListView chatList;

	@BindView(id = R.id.motion_pager)
	private ViewPager mFaceViewPager;

	@BindView(id = R.id.motion_layout)
	private LinearLayout mFaceRoot;

	@BindView(id = R.id.chat_bottom_bottom)
	private LinearLayout bottomLayout;

	@BindView(id = R.id.chat_record_ll)
	private LinearLayout recordLayout;

	@BindView(id = R.id.acv_chat_audio_tip_time)
	private TextView recordTimeView;

	@BindView(id = R.id.acv_chat_audio_tip_tip)
	private TextView recordMsgView;

	@BindView(id = R.id.chat_bottom_motion_ll)
	private RelativeLayout motionLayout;

	@BindView(id = R.id.chat_bottom_share_ll)
	private LinearLayout shareLayout;

	// share菜单网格
	@BindView(id = R.id.share_grid)
	private GridView shareGrid;

	// 表情按钮
	@BindView(id = R.id.chat_motion_button, click = true)
	private Button motionButton;
	
	// share按钮
	@BindView(id = R.id.chat_share_button, click = true)
	private Button shareButton;

	private int mCurrentPage = 0;

	private List<String> mFaceMapKeys;

	private ChatAdapter chatAdapter;

	// audio
	private AudioWrapper audioWrapper;
	private MyCounter mCounter;
	private int duration = 0;
	private boolean touchFlag = true;
	// 当前正在播放动画的ImageView
	private ImageView animationView;
	private AnimationDrawable animation;
	private PlayCounter pCounter;
	private boolean isInAudioAnimi = true;

	private static final int SCALE = 3;// 照片缩小比例

	private List<ChatMsgModel> messageData = new ArrayList<ChatMsgModel>();

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_chat);
	}

	@Override
	protected void initWidget() {
		super.initWidget();
		titleView.setText(getIntent().getStringExtra(
				PreferenceConstants.SESSION_NAME));

		chatAdapter = new ChatAdapter(ChatActivity.this, getChatMessage());
		chatList.setAdapter(chatAdapter);

		audioWrapper = AudioWrapper.getInstance();

		initFaceData();// 初始化数据
		initFacePage();// 初始化表情

		// 初始化sharemenu
		ShareAdapter shareAdapter = new ShareAdapter(this);
		shareGrid.setAdapter(shareAdapter);

		shareGrid.setOnItemClickListener(shareListener);
		contentView.addTextChangedListener(mWatcher);

		speakButton.setOnTouchListener(audioOnTouchListener);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.chat_switch_button:
			if (speakButton.getVisibility() == View.GONE) {
				switchButton.setBackgroundResource(R.drawable.keyboard_button);
				contentView.setVisibility(View.GONE);
				speakButton.setVisibility(View.VISIBLE);
			} else {
				switchButton.setBackgroundResource(R.drawable.voice_button);
				contentView.setVisibility(View.VISIBLE);
				speakButton.setVisibility(View.GONE);
			}
			break;
		// 表情按钮
		case R.id.chat_motion_button:
			if (bottomLayout.getVisibility() == View.VISIBLE) {
				if (shareLayout.getVisibility() == View.VISIBLE) {
					shareLayout.setVisibility(View.GONE);
					motionLayout.setVisibility(View.VISIBLE);
				} else {
					bottomLayout.setVisibility(View.GONE);
				}
			} else {
				bottomLayout.setVisibility(View.VISIBLE);
				shareLayout.setVisibility(View.GONE);
				motionLayout.setVisibility(View.VISIBLE);
			}
			break;
		// share按钮
		case R.id.chat_share_button:
			if (bottomLayout.getVisibility() == View.VISIBLE) {
				if (motionLayout.getVisibility() == View.VISIBLE) {
					motionLayout.setVisibility(View.GONE);
					shareLayout.setVisibility(View.VISIBLE);
				} else {
					bottomLayout.setVisibility(View.GONE);
				}
			} else {
				bottomLayout.setVisibility(View.VISIBLE);
				motionLayout.setVisibility(View.GONE);
				shareLayout.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.chat_content:
			bottomLayout.setVisibility(View.GONE);
			break;
		case R.id.actionbar_back:
			finish();
			break;
		case R.id.chat_send_button:
			String c = contentView.getText().toString();
			System.out.println(c);
			ChatMsgModel m = getMessage(c, "一分钟", true, 0, 1);
			this.refreshList(m);
			contentView.setText("");
			break;
		// case R.id.chat_speak:
		// recordLayout.setVisibility(View.VISIBLE);
		// break;
		default:
			break;
		}
	}

	// 添加聊天消息对象，刷新ListView
	private void refreshList(ChatMsgModel model) {
		messageData.add(model);
		chatAdapter.notifyDataSetChanged();
		// chatList.setSelection(messageData.size() - 1);
		chatList.setSelectionFromTop(chatList.getCount() - 1, 0);
	}

	private OnItemClickListener shareListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			switch (position) {
			case 0:
				// Intent openAlbumIntent = new
				// Intent(Intent.ACTION_GET_CONTENT);
				// openAlbumIntent.setType("image/*");
				// Intent openAlbumIntent = new Intent(
				// Intent.ACTION_PICK,
				// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				//
				//
				Intent openAlbumIntent = new Intent(ChatActivity.this,
						AlbumListActivity.class);
				startActivityForResult(openAlbumIntent,
						ChatIdentifier.MESSAGE_IMAGE);
				break;
			case 1:
				Intent openCameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(openCameraIntent,
						ChatIdentifier.MESSAGE_TAKE);
				break;
			case 2:
				// location
				Intent locationIntent = new Intent(ChatActivity.this,
						LocationActivity.class);
				startActivityForResult(locationIntent,
						ChatIdentifier.MESSAGE_LOCATION);
				break;
			default:
				break;
			}
		}
	};

	// 系统回调
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		bottomLayout.setVisibility(View.GONE);
		if (data != null) {
			switch (requestCode) {
			case ChatIdentifier.MESSAGE_IMAGE:
				ArrayList<String> imageList = data
						.getStringArrayListExtra("picList");

				// ContentResolver resolver = getContentResolver();
				// // 照片的原始资源地址
				// Uri originalUri = data.getData();
				// 使用ContentProvider通过URI获取原始图片
				// Bitmap photo =
				// MediaStore.Images.Media.getBitmap(resolver,
				// originalUri);
				for (int i = 0; i < imageList.size(); i++) {
					System.out.println("imagePath:" + imageList.get(i));
					Bitmap photo = ImageTools.getPhotoFromSDCard(imageList
							.get(0));
					if (photo != null) {
						// 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
						Bitmap smallBitmap = ImageTools.zoomBitmap(photo,
								photo.getWidth() / SCALE, photo.getHeight()
										/ SCALE);
						// 释放原始图片占用的内存，防止out of memory异常发生
						photo.recycle();
						ChatMsgModel m = new ChatMsgModel();
						m.setSource(1);
						m.setStatus(true);
						m.setText(BaseUtil.bitmapToBase64(smallBitmap));
						m.setTime("一分前");
						m.setType(1);
						this.refreshList(m);
					}
				}
				break;
			case ChatIdentifier.MESSAGE_TAKE:
				// 将保存在本地的图片取出并缩小后显示在界面上
				Bitmap bitmap = (Bitmap) data.getExtras().get("data");
				// Bitmap newBitmap = ImageTools.zoomBitmap(bitmap,
				// bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
				// // 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
				// bitmap.recycle();
				ChatMsgModel m = new ChatMsgModel();
				m.setSource(1);
				m.setStatus(true);
				m.setText(BaseUtil.bitmapToBase64(bitmap));
				m.setTime("一分前");
				m.setType(1);
				this.refreshList(m);
				break;
			case ChatIdentifier.MESSAGE_LOCATION:
				ChatMsgModel m2 = new ChatMsgModel();
				m2.setSource(1);
				m2.setStatus(true);
				m2.setText(data.getStringExtra("address"));
				m2.setTime("一分前");
				m2.setType(3);
				this.refreshList(m2);
				break;
			}
		}

	}

	private void initFaceData() {
		// 将表情map的key保存在数组中
		Set<String> keySet = MotionUtils.getFaceMap().keySet();
		mFaceMapKeys = new ArrayList<String>();
		mFaceMapKeys.addAll(keySet);
	}

	/**
	 * 初始化表情适配器
	 */
	private void initFacePage() {
		List<View> lv = new ArrayList<View>();
		for (int i = 0; i < PreferenceConstants.NUM_PAGE; ++i)
			lv.add(getGridView(i));
		FacePageAdapter adapter = new FacePageAdapter(lv);
		mFaceViewPager.setAdapter(adapter);
		mFaceViewPager.setCurrentItem(mCurrentPage);
		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.motion_indicator);
		indicator.setViewPager(mFaceViewPager);
		adapter.notifyDataSetChanged();
		// mFaceRoot.setVisibility(View.GONE);
		indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				mCurrentPage = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// do nothing
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// do nothing
			}
		});

	}

	// 防止PageView乱滚动
	private View.OnTouchListener forbidenScroll() {
		return new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					return true;
				}
				return false;
			}
		};
	}

	private GridView getGridView(int i) {
		GridView gv = new GridView(this);
		gv.setNumColumns(7);
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));// 屏蔽GridView默认点击效果
		gv.setBackgroundColor(Color.TRANSPARENT);
		gv.setCacheColorHint(Color.TRANSPARENT);
		gv.setHorizontalSpacing(1);
		gv.setVerticalSpacing(1);
		gv.setLayoutParams(new ViewGroup.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		gv.setGravity(Gravity.CENTER);
		gv.setAdapter(new FaceAdapter(this, i));
		gv.setOnTouchListener(forbidenScroll());
		gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == PreferenceConstants.NUM) {// 删除键的位置
					int selection = contentView.getSelectionStart();
					String text = contentView.getText().toString();
					if (selection > 0) {
						String text2 = text.substring(selection - 1);
						if ("]".equals(text2)) {
							int start = text.lastIndexOf("[");
							int end = selection;
							contentView.getText().delete(start, end);
							return;
						}
						contentView.getText().delete(selection - 1, selection);
					}
				} else {
					int count = mCurrentPage * PreferenceConstants.NUM + arg2;
					// 注释的部分，在EditText中显示字符串
					// String ori = msgEt.getText().toString();
					// int index = msgEt.getSelectionStart();
					// StringBuilder stringBuilder = new StringBuilder(ori);
					// stringBuilder.insert(index, keys.get(count));
					// msgEt.setText(stringBuilder.toString());
					// msgEt.setSelection(index + keys.get(count).length());

					// 下面这部分，在EditText中显示表情
					Bitmap bitmap = BitmapFactory.decodeResource(
							getResources(), (Integer) MotionUtils.getFaceMap()
									.values().toArray()[count]);
					if (bitmap != null) {
						int rawHeigh = bitmap.getHeight();
						int rawWidth = bitmap.getHeight();
						int newHeight = 40;
						int newWidth = 40;
						// 计算缩放因子
						float heightScale = ((float) newHeight) / rawHeigh;
						float widthScale = ((float) newWidth) / rawWidth;
						// 新建立矩阵
						Matrix matrix = new Matrix();
						matrix.postScale(heightScale, widthScale);
						// 设置图片的旋转角度
						// matrix.postRotate(-30);
						// 设置图片的倾斜
						// matrix.postSkew(0.1f, 0.1f);
						// 将图片大小压缩
						// 压缩后图片的宽和高以及kB大小均会变化
						Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
								rawWidth, rawHeigh, matrix, true);
						ImageSpan imageSpan = new ImageSpan(ChatActivity.this,
								newBitmap);
						String emojiStr = mFaceMapKeys.get(count);
						SpannableString spannableString = new SpannableString(
								emojiStr);
						spannableString.setSpan(imageSpan,
								emojiStr.indexOf('['),
								emojiStr.indexOf(']') + 1,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						contentView.append(spannableString);
					} else {
						String ori = contentView.getText().toString();
						int index = contentView.getSelectionStart();
						StringBuilder stringBuilder = new StringBuilder(ori);
						stringBuilder.insert(index, mFaceMapKeys.get(count));
						contentView.setText(stringBuilder.toString());
						contentView.setSelection(index
								+ mFaceMapKeys.get(count).length());
					}
				}
			}
		});
		return gv;
	}

	// 发送语音按钮Touch事件
	private OnTouchListener audioOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				recordLayout.setVisibility(View.VISIBLE);
				audioWrapper.startRecord();
				mCounter = new MyCounter(60000, 1000);
				mCounter.start();
				break;
			case MotionEvent.ACTION_UP:
				mCounter.cancel();
				audioWrapper.stopRecord();
				recordLayout.setVisibility(View.GONE);
				if (touchFlag) {
					if (duration > 1) {
						ChatMsgModel m = getMessage(String.valueOf(duration),
								"刚刚", true, 2, 1);
						System.out.println("暂时path:"
								+ PreferenceConstants.AUDIO_TEMP_PATH);
						m.setAudioPath(PreferenceConstants.AUDIO_TEMP_PATH);
						refreshList(m);
					}
				}
				break;
			case MotionEvent.ACTION_MOVE:
				float x = event.getRawX();
				float y = event.getRawY();
				int[] position1 = new int[2];
				int[] position2 = new int[2];
				recordLayout.getLocationOnScreen(position1);
				position2[0] = position1[0] + recordLayout.getWidth();
				position2[1] = position1[1] + recordLayout.getHeight();
				if (((int) x > position1[0]) && ((int) x < position2[0])
						&& ((int) y > position1[1]) && ((int) y < position2[1])) {
					// mCounter.cancel();
					touchFlag = false;
					recordMsgView.setText("松手取消本次发送");
				} else {
					touchFlag = true;
					recordMsgView.setText("滑动至此取消发送");
				}
				break;
			}
			return false;
		}

	};

	// chat item content 点击事件
	public class ChatItemClickListener implements OnClickListener {

		private ChatMsgModel model;
		private ImageView volumnView;

		public ChatItemClickListener(ChatMsgModel model, ImageView volumnView) {
			this.model = model;
			this.volumnView = volumnView;
		}

		@Override
		public void onClick(View v) {

			switch (model.getType()) {
			case 1:
				System.out.println("click image");
				break;
			case 2:
				System.out.println("click audio");

				if (model.getSource() == 0)
					isInAudioAnimi = true;
				else
					isInAudioAnimi = false;

				String audioPath = model.getAudioPath();
				PreferenceConstants.AUDIO_TEMP_PATH = audioPath;
				animationView = volumnView;
				animation = (AnimationDrawable) animationView.getBackground();
				animation.start();
				pCounter = new PlayCounter(
						Integer.parseInt(model.getText()) * 1000, 1000);
				pCounter.start();
				// setAudioPath(guid);
				System.out.println("kkkk:" + (audioWrapper == null));
				if (audioWrapper == null)
					audioWrapper = AudioWrapper.getInstance();
				audioWrapper.startListen();
			case 3:
				System.out.println("位置消息点击了，哈哈哈哈哈哈哈哈哈");
				break;
			default:
				break;
			}
		}
	};

	/**
	 * Audio播放计时器
	 * 
	 */
	private class PlayCounter extends CountDownTimer {

		public PlayCounter(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			// animation.setOneShot(false);
			stopAudioPlay();
			/** 恢复动画初始背景 */
			if (!isInAudioAnimi)
				animationView.setBackgroundResource(R.anim.audio_out_anim);
			else {
				animationView.setBackgroundResource(R.anim.audio_in_anim);
			}
			/** 停止计时器 */
			pCounter.cancel();
		}

		@Override
		public void onTick(long millisUntilFinished) {
		}

	}

	private void stopAudioPlay() {
		if (animation.isRunning())
			animation.stop();
	}

	/**
	 * 录音计时器
	 * 
	 * @author lianhai
	 */

	private class MyCounter extends CountDownTimer {

		public MyCounter(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			recordTimeView.setText("录音超过60秒，取消此次发送");
		}

		@Override
		public void onTick(long millisUntilFinished) {
			duration = (int) (60 - millisUntilFinished / 1000);
			recordTimeView.setText("录音时长：" + (60 - millisUntilFinished / 1000)
					+ "''");
		}

	}

	private TextWatcher mWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (contentView.getText().length() > 0) {
				shareButton.setVisibility(View.GONE);
				sendButton.setVisibility(View.VISIBLE);
			} else {
				shareButton.setVisibility(View.VISIBLE);
				sendButton.setVisibility(View.GONE);
			}
		}
	};

	private ChatMsgModel getMessage(String text, String time, boolean status,
			int type, int source) {
		ChatMsgModel m = new ChatMsgModel();
		m.setText(text);
		m.setTime(time);
		m.setStatus(status);
		m.setType(type);
		m.setSource(source);

		return m;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (bottomLayout.getVisibility() == View.VISIBLE) {
				bottomLayout.setVisibility(View.GONE);
				return false;
			}
			return super.onKeyDown(keyCode, event);
		}

		return super.onKeyDown(keyCode, event);
	}

	private List<ChatMsgModel> getChatMessage() {
		ChatMsgModel m1 = new ChatMsgModel();
		m1.setText("在吗？");
		m1.setTime("10:52");
		m1.setType(0);
		m1.setStatus(true);
		m1.setSource(-1);

		ChatMsgModel m2 = new ChatMsgModel();
		m2.setText("在啊，干嘛啊？");
		m2.setTime("10:53");
		m2.setType(0);
		m2.setStatus(true);
		m2.setSource(0);

		messageData.add(m1);
		messageData.add(m2);

		return messageData;
	}

}
