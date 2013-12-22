package com.viyu.apuzzletrans.util;

import com.viyu.apuzzletrans.R;


public enum DiffLevel {

	Level1(1, R.drawable.role_01, 0), 
	Level2(2, R.drawable.role_02, 0), 
	Level3(3, R.drawable.role_03, 0), 
	Level4(4, R.drawable.role_04, 0), 
	Level5(5, R.drawable.role_05, 0), 
	Level6(6, R.drawable.role_06, 0),
	
	Level7(7, R.drawable.role_07, 1), 
	Level8(8, R.drawable.role_08, 1), 
	Level9(9, R.drawable.role_09, 1),
	Level10(10, R.drawable.role_10, 1), 
	Level11(11, R.drawable.role_11, 1), 
	Level12(12, R.drawable.role_12, 1),
	
	Level13(13, R.drawable.role_13, 2), 
	Level14(14, R.drawable.role_14, 2), 
	Level15(15, R.drawable.role_15, 2), 
	Level16(16, R.drawable.role_16, 2), 
	Level17(17, R.drawable.role_17, 2), 
	Level18(18, R.drawable.role_18, 2);

	private static final String KEY_SHAREDPREFERENCE_RECORDS_PREFIX = "RecordLevel_";
	
	private int level = 1;
	private int levelRes = 0;
	private int diffOffset = 0;

	private DiffLevel(int level, int levelRes, int diffOffset) {
		this.level = level;
		this.levelRes = levelRes;
		this.diffOffset = diffOffset;
	}

	public int getLevel() {
		return level;
	}

	public int getLevelResId() {
		return levelRes;
	}
	
	public int getDiffOffset() {
		return diffOffset;
	}
	
	@Override
	public String toString() {
		return KEY_SHAREDPREFERENCE_RECORDS_PREFIX + String.valueOf(level);
	}
	
	public DiffLevel nextDiffLevel() {
		return getDiffLevel(level % 27);//循环
	}

	public static int LEVEL_SUM = 18;
	
	public static DiffLevel getDiffLevel(int index) {
		switch(index) {
		case 0: return Level1;
		case 1: return Level2;
		case 2: return Level3;
		case 3: return Level4;
		case 4: return Level5;
		case 5: return Level6;
		case 6: return Level7;
		case 7: return Level8;
		case 8: return Level9;
		case 9: return Level10;
		case 10: return Level11;
		case 11: return Level12;
		case 12: return Level13;
		case 13: return Level14;
		case 14: return Level15;
		case 15: return Level16;
		case 16: return Level17;
		case 17: return Level18;
		}
		return Level1;
	}
}
