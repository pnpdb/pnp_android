package com.pnp.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;

import com.pnp.R;

public class LProgressDialog extends Dialog {

	private static LProgressDialog pnpProgressDialog = null;

	public LProgressDialog(Context context) {
		super(context);
	}

	public LProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public static LProgressDialog createDialog(Context context) {
		pnpProgressDialog = new LProgressDialog(context, R.style.ProgressDialog);
		pnpProgressDialog.setContentView(R.layout.pnp_progress);
		pnpProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		// pnpProgressDialog.setCancelable(false);

		return pnpProgressDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {

		if (pnpProgressDialog == null) {
			return;
		}

		ImageView imageView = (ImageView) pnpProgressDialog
				.findViewById(R.id.loadingImageView);
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView
				.getBackground();
		animationDrawable.start();
	}

	public LProgressDialog setTitile(String strTitle) {
		return pnpProgressDialog;
	}

	/*
	 * public PNPProgressDialog setMessage(String strMessage) { TextView tvMsg =
	 * (TextView) pnpProgressDialog .findViewById(R.id.id_tv_loadingmsg);
	 * 
	 * if (tvMsg != null) { tvMsg.setText(strMessage); }
	 * 
	 * return pnpProgressDialog; }
	 */

}
