package com.pnp.widget;

import android.support.v4.view.ViewPager;

/**
 * Created by lianhai on 14-6-3.
 */
public interface PageIndicator extends ViewPager.OnPageChangeListener {

	void setViewPager(ViewPager view);

	void setViewPager(ViewPager view, int initialPosition);

	void setCurrentItem(int item);

	void setOnPageChangeListener(ViewPager.OnPageChangeListener listener);

	void notifyDataSetChanged();
}
