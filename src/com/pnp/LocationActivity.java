package com.pnp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.pnp.utils.ChatIdentifier;

public class LocationActivity extends Activity implements
		OnGetGeoCoderResultListener, View.OnClickListener {

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	// private LocationMode mCurrentMode;
	private BitmapDescriptor mCurrentMarker;
	private GeoCoder mSearch = null;
	private Projection mProjection;

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private TextView titleView;
	private TextView locationView;
	private TextView sendButton;

	private String address = null;

	// UI相关
	OnCheckedChangeListener radioButtonListener;
	boolean isFirstLoc = true;// 是否首次定位

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		titleView = (TextView) findViewById(R.id.actionbar_title);
		titleView.setText("我的位置");
		sendButton = (TextView) findViewById(R.id.actionbar_action);
		sendButton.setVisibility(View.VISIBLE);
		sendButton.setTextSize(15.6f);
		sendButton.setText("发送");
		sendButton.setOnClickListener(this);

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		locationView = (TextView) findViewById(R.id.locationTv);
		MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(18);
		mBaiduMap.animateMapStatus(u);

		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);

		// mCurrentMode = LocationMode.NORMAL;
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(null,
				false, mCurrentMarker));

		mBaiduMap.setOnMapStatusChangeListener(mapStatusListener);

		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}

			/*************************************/
			// StringBuffer sb = new StringBuffer(256);
			// sb.append("time : ");
			// sb.append(location.getTime());
			// sb.append("\nerror code : ");
			// sb.append(location.getLocType());
			// sb.append("\nlatitude : ");
			// sb.append(location.getLatitude());
			// sb.append("\nlontitude : ");
			// sb.append(location.getLongitude());
			// sb.append("\nradius : ");
			// sb.append(location.getRadius());
			// if (location.getLocType() == BDLocation.TypeGpsLocation) {
			// sb.append("\nspeed : ");
			// sb.append(location.getSpeed());
			// sb.append("\nsatellite : ");
			// sb.append(location.getSatelliteNumber());
			// } else if (location.getLocType() ==
			// BDLocation.TypeNetWorkLocation) {
			// sb.append("\naddr : ");
			// sb.append(location.getAddrStr());
			// }
			// locationView.setText(location.getAddrStr());
			// System.out.println(sb.toString());
			System.out.println(location.getLatitude() + "   "
					+ location.getLongitude());

			LatLng ptCenter = new LatLng(location.getLatitude(),
					location.getLongitude());
			mSearch.reverseGeoCode(new ReverseGeoCodeOption()
					.location(ptCenter));

			/*************************************/
			mBaiduMap.setMyLocationEnabled(false);
			mLocClient.stop();

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	BaiduMap.OnMapStatusChangeListener mapStatusListener = new BaiduMap.OnMapStatusChangeListener() {

		@Override
		public void onMapStatusChangeStart(MapStatus arg0) {
		}

		@Override
		public void onMapStatusChange(MapStatus arg0) {
		}

		@Override
		public void onMapStatusChangeFinish(MapStatus status) {
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			int point_X = dm.widthPixels / 2;
			int point_Y = dm.heightPixels / 2;
			Point point = new Point(point_X, point_Y);
			if ((mProjection = mBaiduMap.getProjection()) != null) {
				LatLng ll = mProjection.fromScreenLocation(point);
				System.out.println(ll.latitude + "   " + ll.longitude);
				mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
			}
		}

	};

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			locationView.setText("未收录地址");
			return;
		}
		// mBaiduMap.clear();
		// mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
		// .getLocation()));
		locationView.setText(result.getAddress());
		address = result.getAddress();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mLocClient.stop();
		mLocClient = null;
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(LocationActivity.this, ChatActivity.class);
		intent.putExtra("address", address);
		setResult(ChatIdentifier.MESSAGE_LOCATION, intent);
		finish();
	}

}
