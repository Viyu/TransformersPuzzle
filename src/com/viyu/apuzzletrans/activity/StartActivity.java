package com.viyu.apuzzletrans.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.viyu.apuzzletrans.APuzzleApp;
import com.viyu.apuzzletrans.R;

/**
 * 
 * @author Viyu_Lu
 * 
 */
public class StartActivity extends Activity implements OnClickListener, OnCheckedChangeListener {
	private APuzzleApp app = null;

	private Button startButton = null;

	private CheckBox soundCheck = null;
	private CheckBox vibrateCheck = null;

	private static final String KEY_PREFERENCE_HINT = "PuzzleHintPreference";
	private static final String KEY_PREFERENCE_HINT_SOUND = "PuzzleHintPreferenceSound";
	private static final String KEY_PREFERENCE_HINT_VIBRATE = "PuzzleHintPreferenceVibrate";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (APuzzleApp) getApplication();
		this.setContentView(R.layout.activity_start);
		startButton = (Button) findViewById(R.id.startboard_startplay);
		startButton.setOnClickListener(this);

		soundCheck = (CheckBox) findViewById(R.id.startboard_soundcheck);
		vibrateCheck = (CheckBox) findViewById(R.id.startboard_vibratecheck);

		soundCheck.setOnCheckedChangeListener(this);
		vibrateCheck.setOnCheckedChangeListener(this);

		SharedPreferences prefers = getSharedPreferences(KEY_PREFERENCE_HINT, Context.MODE_PRIVATE);
		boolean hintVibrate = prefers.getBoolean(KEY_PREFERENCE_HINT_VIBRATE, true);
		boolean hintSound = prefers.getBoolean(KEY_PREFERENCE_HINT_SOUND, true);
		soundCheck.setChecked(hintSound);
		vibrateCheck.setChecked(hintVibrate);
		app.setHintSound(hintSound);
		app.setHintVibrate(hintVibrate);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.startboard_startplay:
			Intent intent = new Intent(this, LevelChooseActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		SharedPreferences prefers = getSharedPreferences(KEY_PREFERENCE_HINT, Context.MODE_PRIVATE);
		Editor edit = prefers.edit();
		edit.putBoolean(KEY_PREFERENCE_HINT_VIBRATE, vibrateCheck.isChecked());
		edit.putBoolean(KEY_PREFERENCE_HINT_SOUND, soundCheck.isChecked());
		edit.commit();
		
		super.onDestroy();
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.startboard_soundcheck: {
			app.setHintSound(buttonView.isChecked());
			break;
		}
		case R.id.startboard_vibratecheck: {
			app.setHintVibrate(buttonView.isChecked());
			break;
		}
		}
	}
}