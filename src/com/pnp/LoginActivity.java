package com.pnp;

import java.lang.ref.WeakReference;

import org.kymjs.aframe.ui.BindView;
import org.kymjs.aframe.ui.activity.BaseActivity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pnp.message.XStreamUtil;
import com.pnp.model.CommandModel;
import com.pnp.network.NettyManager;
import com.pnp.utils.ToastUtil;
import com.pnp.widget.LProgressDialog;

public class LoginActivity extends BaseActivity {

	@BindView(id = R.id.accountText)
	private EditText accountText;

	@BindView(id = R.id.passwordText)
	private EditText passwordText;

	@BindView(id = R.id.accountDelete, click = true)
	private Button accountDel;

	@BindView(id = R.id.passwordDelete, click = true)
	private Button passwordDel;

	@BindView(id = R.id.loginButton, click = true)
	private Button loginButton;

	private static LoginHandler mHandler;

	private LProgressDialog progressDialog;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_login);
	}

	@Override
	protected void initWidget() {
		super.initWidget();
		// 设置当前Activity标示
		PNPApplication.setCurrentActivity(LoginActivity.class.getName());
		final TextView titleView = (TextView) findViewById(R.id.actionbar_title);
		titleView.setText(getResources().getString(R.string.login));
		final Button backButton = (Button) findViewById(R.id.actionbar_back);
		backButton.setVisibility(View.GONE);

		mHandler = new LoginHandler(LoginActivity.this);

		accountText.addTextChangedListener(mTextWatcher);
		passwordText.addTextChangedListener(mTextWatcher);
	}

	@Override
	public void widgetClick(View v) {
		switch (v.getId()) {
		case R.id.accountDelete:
			accountText.setText(null);
			break;
		case R.id.passwordDelete:
			passwordText.setText(null);
			break;
		default:
//			String userName = accountText.getText().toString().trim();
//			String userPassword = passwordText.getText().toString().trim();
//			if (userName.equals("")) {
//				Toast.makeText(getApplicationContext(), "账号不能为空",
//						Toast.LENGTH_SHORT).show();
//				return;
//			}
//			if (userPassword.equals("")) {
//				Toast.makeText(getApplicationContext(), "密码不能为空",
//						Toast.LENGTH_SHORT).show();
//				return;
//			}
//			startProgressDialog();
//			new Thread(new LoginTask(getLoginMessage(userName, userPassword)))
//					.start();
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			break;
		}
	}

	private String getLoginMessage(String account, String password) {
		CommandModel command = new CommandModel();
		command.setName("Login");
		command.setCaption("Server");
		command.setState("5");
		command.setCommand(password);
		command.setUser(account);
		command.setFrom("3");
		command.setValue("11");
		command.setBlock("0");
		String res = XStreamUtil.CommandToXml(command);
		System.out.println("res:" + res);
		return res;
	}

	private class LoginTask implements Runnable {

		private String loginXML;

		public LoginTask(String loginXML) {
			this.loginXML = loginXML;
		}

		@Override
		public void run() {
			NettyManager.getChannelToSend(loginXML);
		}
	}

	private static class LoginHandler extends Handler {
		WeakReference<LoginActivity> mActivity;

		public LoginHandler(LoginActivity activity) {
			mActivity = new WeakReference<LoginActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			LoginActivity activity = mActivity.get();
			activity.stopProgressDialog();
			CommandModel command = (CommandModel) msg.obj;
			switch (Integer.parseInt(command.getState())) {
			case -1:
				ToastUtil.ToastShow(activity, command.getTag(),
						Toast.LENGTH_SHORT);
				break;
			default:
				break;
			}
		}
	}

	public static void reflaction(CommandModel command) {
		Message msg = mHandler.obtainMessage();
		msg.obj = command;
		// msg.what = result.getState();
		// msg.obj = result.getDescribe();
		mHandler.sendMessage(msg);
	}

	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = LProgressDialog.createDialog(LoginActivity.this);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(true);
		}
		progressDialog.show();
	}

	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (accountText.getText().toString().trim().length() > 0)
				accountDel.setVisibility(View.VISIBLE);
			else
				accountDel.setVisibility(View.GONE);

			if (passwordText.getText().toString().trim().length() > 0)
				passwordDel.setVisibility(View.VISIBLE);
			else
				passwordDel.setVisibility(View.GONE);
		}
	};

}
