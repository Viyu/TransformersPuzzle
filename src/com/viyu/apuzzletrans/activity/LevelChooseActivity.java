package com.viyu.apuzzletrans.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.viyu.apuzzletrans.APuzzleApp;
import com.viyu.apuzzletrans.R;
import com.viyu.apuzzletrans.util.DiffLevel;
import com.viyu.apuzzletrans.view.LevelSwitcher;

public class LevelChooseActivity extends Activity implements OnTouchListener, ViewFactory {
	public static final String KEY_SHAREDPREFERENCE_RECORDS = "PuzzleRecords";

	public static final String KEY_INTENT_CURRENTLEVELRECORD = "CurrentLevelHistoryRecords";
	
	private LevelSwitcher levelSwitcher = null;

	private int[] recordArray = null;

	private int currentShowIndex = 0;
	private int nextLevel = -1;

	private Animation animationShake = null;
	
	private LayoutInflater infalter = null;

	private Button startButton = null;
	
	private APuzzleApp app = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (APuzzleApp) getApplication();
		setContentView(R.layout.activity_levelchoose);
		infalter = getLayoutInflater();
		animationShake = AnimationUtils.loadAnimation(this, R.anim.shake);
		//
		recordArray = new int[DiffLevel.LEVEL_SUM];
		//
		levelSwitcher = (LevelSwitcher) findViewById(R.id.levelchoose_imageswitcher);
		levelSwitcher.setOnTouchListener(this);
		levelSwitcher.setFactory(this);
		//
		startButton = (Button)findViewById(R.id.levelchoose_startbutton);
		//
		Toast.makeText(this, R.string.text_sliding, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		//
		loadRecords();
		//
		currentShowIndex = nextLevel;
		fillContent();
		//
		levelSwitcher.startAnimation(animationShake);
	}
	
	public void startPuzzle(View view) {
		app.setCurrentLevel(DiffLevel.getDiffLevel(currentShowIndex));
		Intent intent = new Intent(this, PuzzleActivity.class);
		intent.putExtra(KEY_INTENT_CURRENTLEVELRECORD, recordArray[currentShowIndex]);
		startActivity(intent);
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
	}

	@Override
	public View makeView() {
		return infalter.inflate(R.layout.view_levelswitcher, null);
	}

	protected void loadRecords() {
		nextLevel = -1;
		SharedPreferences prefers = getSharedPreferences(KEY_SHAREDPREFERENCE_RECORDS, Context.MODE_PRIVATE);
		for (int i = 0; i < recordArray.length; i++) {
			int record = prefers.getInt(DiffLevel.getDiffLevel(i).toString(), -1);
			if (record > -1) {// 有记录
				recordArray[i] = record;
			} else {// 没记录的
				recordArray[i] = -1;
				if (nextLevel == -1) {
					nextLevel = i;
				}
			}
		}
		//for test
		//nextLevel = 17;
	}

	private float touchDownX;
	private float touchUpX;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			touchDownX = event.getX();
			return true;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			touchUpX = event.getX();
			if (touchUpX - touchDownX > 100 && currentShowIndex > 0) {
				currentShowIndex--;
				levelSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
				levelSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_right));
				fillContent();
			} else if (touchDownX - touchUpX > 100 && currentShowIndex < recordArray.length - 1) {
				currentShowIndex++;
				levelSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_left));
				levelSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_left));
				fillContent();
			}
			return true;
		}
		return false;
	}

	private void fillContent() {
		int resId = 0;
		String text = "";
		boolean canbePlayedNow = false;
		if (currentShowIndex < nextLevel) {// 都有记录
			text = getString(R.string.text_record, recordArray[currentShowIndex]);
			resId = DiffLevel.getDiffLevel(currentShowIndex).getLevelResId();
			canbePlayedNow = true;
		} else if (currentShowIndex == nextLevel) {
			text = getString(R.string.text_notfinish);
			resId = DiffLevel.getDiffLevel(currentShowIndex).getLevelResId();
			canbePlayedNow = true;
		} else if (currentShowIndex > nextLevel) {
			text = getString(R.string.text_notplayed);
			resId = R.drawable.role_locked;
			canbePlayedNow = false;
		}
		levelSwitcher.setLevelInfo(resId, text, (currentShowIndex + 1) + " / " + DiffLevel.LEVEL_SUM);
		startButton.setVisibility(canbePlayedNow ? View.VISIBLE : View.INVISIBLE);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
		finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}