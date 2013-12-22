package com.viyu.apuzzletrans;

import android.app.Application;

import com.viyu.apuzzletrans.util.DiffLevel;

/**
 * 
 * @author Viyu_Lu
 * 
 */
public class APuzzleApp extends Application {

	private DiffLevel currentLevel = null;
	private boolean hintVibrate = true;
	private boolean hintSound = true;

	public void setCurrentLevel(DiffLevel level) {
		currentLevel = level;
	}

	public DiffLevel getCurrentLevel() {
		return currentLevel;
	}

	public boolean isHintVibrate() {
		return hintVibrate;
	}

	public boolean isHintSound() {
		return hintSound;
	}

	public void setHintVibrate(boolean hintVibrate) {
		this.hintVibrate = hintVibrate;
	}

	public void setHintSound(boolean hintSound) {
		this.hintSound = hintSound;
	}

}