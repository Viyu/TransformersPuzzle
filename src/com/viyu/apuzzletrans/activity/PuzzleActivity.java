package com.viyu.apuzzletrans.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.viyu.apuzzletrans.APuzzleApp;
import com.viyu.apuzzletrans.R;
import com.viyu.apuzzletrans.util.DiffLevel;
import com.viyu.apuzzletrans.util.Puzzle;
import com.viyu.apuzzletrans.util.SoundUtil;
import com.viyu.apuzzletrans.view.PuzzleView;
import com.viyu.apuzzletrans.view.PuzzleView.ShowNumbers;

public class PuzzleActivity extends Activity implements OnClickListener{
	private SoundUtil soundPlayer =  null; 
	private final int SOUNDID_MOVE = 0;
	private final int SOUNDID_RIGHTPLACE = 1;
	private final int SOUNDID_SOLVED = 2;
	private final int SOUNDID_NEWRECORD = 3;
	
	private static final long VIBRATE_DRAG = 5;
	private static final long VIBRATE_MATCH = 50;
	private static final long VIBRATE_SOLVED = 250;
	
	private PuzzleView puzzleView;
	private Puzzle puzzle;
	private int rowColumnBase = 3;
	private final int HINT_TIME = 5 * 1000;

	private APuzzleApp app = null;
	private DiffLevel level = null;

	//private Button refreshButton = null;
	private Button hintButton = null;
	private TextView hintText = null;
	private TextView moveCountText = null;
	
	private int hintCount = 3;
	
	private int currentLevelRecord = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_puzzle);
		app = (APuzzleApp) getApplication();
		level = app.getCurrentLevel();
		
		//refreshButton = (Button)findViewById(R.id.puzzle_refreshbutton);
		//refreshButton.setOnClickListener(this);
		hintButton = (Button)findViewById(R.id.puzzle_hintbutton);
		hintButton.setOnClickListener(this);
		hintText = (TextView)findViewById(R.id.puzzle_hintnum);
		hintText.setText(String.valueOf(hintCount));
		moveCountText = (TextView)findViewById(R.id.puzzle_movecount);
		moveCountText.setText("0");
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		//
		//
		soundPlayer = new SoundUtil(this);
		soundPlayer.loadSfx(this, R.raw.move, SOUNDID_MOVE);
		soundPlayer.loadSfx(this, R.raw.rightplace, SOUNDID_RIGHTPLACE);
		soundPlayer.loadSfx(this, R.raw.solved, SOUNDID_SOLVED);
		soundPlayer.loadSfx(this, R.raw.newrecord, SOUNDID_NEWRECORD);
		//
		currentLevelRecord = getIntent().getIntExtra(LevelChooseActivity.KEY_INTENT_CURRENTLEVELRECORD, -1);
		
		puzzle = new Puzzle();
		puzzleView = new PuzzleView(this, puzzle);

		FrameLayout container = (FrameLayout)findViewById(R.id.puzzle_container);
		container.addView(puzzleView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		//
		loadPuzzlePicture();
		//
		puzzle.scramble();
		puzzleView.invalidate();
	}

	private void loadPuzzlePicture() {
		Options o = new Options();
		o.inScaled = false;
		o.inJustDecodeBounds = false;

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), level.getLevelResId(), o);
		//
		puzzleView.setBitmap(bitmap);
		//base + 难度级别
		puzzle.init(level.getDiffOffset() + rowColumnBase, level.getDiffOffset() + rowColumnBase);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		/*case R.id.puzzle_refreshbutton: {
			puzzle.scramble();
			puzzleView.invalidate();
			break;
		}*/
		case R.id.puzzle_hintbutton: {
			if(hintCount > 0) {
				hintCount--;
				hintText.setText(String.valueOf(hintCount));
				hintButton.setEnabled(false);
				puzzleView.setShowNumbers(ShowNumbers.ALL);
				puzzleView.invalidate();
				(new AsyncTask<String, Integer, String>() {
					@Override
					protected String doInBackground(String... params) {
						try {
							Thread.sleep(HINT_TIME);
						} catch ( InterruptedException e) {
							e.printStackTrace();
						}
						return null;
					}

					@Override
					protected void onPostExecute(String str) {
						hintButton.setEnabled(true);
						puzzleView.setShowNumbers(ShowNumbers.NONE);
						puzzleView.invalidate();
					}
				}).execute();
			} else {
			}
			
			break;
		}
		}
	}
	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void showResult(boolean newRecord) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(newRecord ? R.string.text_newrecord : R.string.text_wancheng);
		builder.setMessage(getString(R.string.text_stepsconsted, puzzle.getMoveCount()));
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.text_dialog_ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(which == DialogInterface.BUTTON_POSITIVE) {
					finish();
				}
			}
		});
		builder.create().show();
	}

	/**
	 * 成功
	 */
	public void onFinished() {
		if(currentLevelRecord < 0 || puzzle.getMoveCount() < currentLevelRecord) {
			writeNewRecord(puzzle.getMoveCount());
			soundPlayer.play(SOUNDID_NEWRECORD, 0, app.isHintSound());
			showResult(true);
		} else {
			soundPlayer.play(SOUNDID_SOLVED, 0, app.isHintSound());
			showResult(false);
		}
	}
	
	/**
	 * 移动了
	 */
	public void onMoved(int count) {
		soundPlayer.play(SOUNDID_MOVE, 0, app.isHintSound());
		moveCountText.setText(String.valueOf(count));
	}
	
	public void onNoKnown() {
		if(app.isHintVibrate()) {
			vibrate(VIBRATE_DRAG);
		}
	}

	//调用这个不是match就是solve
	public void onMatchOrSolved(boolean isSolved) {
		if(app.isHintVibrate()) {
			vibrate(isSolved ? VIBRATE_SOLVED : VIBRATE_MATCH);
		}
		if(app.isHintSound() && !isSolved) {
			soundPlayer.play(SOUNDID_RIGHTPLACE, 0, true);
		}
	}
	
	private void vibrate(long d) {
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		if (v != null) {
			v.vibrate(d);
		}
	}
	
	public PuzzleView getPuzzleView() {
		return puzzleView;
	}

	public Puzzle getPuzzle() {
		return puzzle;
	}
	
	private void writeNewRecord(int newRecord) {
		SharedPreferences prefers = getSharedPreferences(LevelChooseActivity.KEY_SHAREDPREFERENCE_RECORDS, Context.MODE_PRIVATE);
		Editor edit = prefers.edit();
		//更新数值
		edit.putInt(level.toString(), newRecord);
		edit.commit();
		currentLevelRecord = newRecord;
	}
}
