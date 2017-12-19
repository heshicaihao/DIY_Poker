package com.wlzndjk.poker.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.wlzndjk.poker.R;

public class StandardPokerDataUtils {
	
	private static List<Integer> mDatas02 = new ArrayList<Integer>(
			Arrays.asList(R.drawable.icon_szt_browse, R.drawable.icon_szt_browse));

	private static List<Integer> mDatas04 = new ArrayList<Integer>(
			Arrays.asList(R.drawable.p001, R.drawable.p002, R.drawable.p003,
					R.drawable.p011));

	private static List<Integer> mDatas16 = new ArrayList<Integer>(
			Arrays.asList(R.drawable.p001, R.drawable.p002, R.drawable.p003,
					R.drawable.p011, R.drawable.p021, R.drawable.p031,
					R.drawable.p041, R.drawable.p051, R.drawable.p061,
					R.drawable.p071, R.drawable.p081, R.drawable.p091,
					R.drawable.p101, R.drawable.p111, R.drawable.p121,
					R.drawable.p131));

	private static List<Integer> mDatas55 = new ArrayList<Integer>(Arrays.asList(
			R.drawable.p001, R.drawable.p002, R.drawable.p003, R.drawable.p011,
			R.drawable.p012, R.drawable.p013, R.drawable.p014, R.drawable.p021,
			R.drawable.p022, R.drawable.p023, R.drawable.p024, R.drawable.p031,
			R.drawable.p032, R.drawable.p033, R.drawable.p034, R.drawable.p041,
			R.drawable.p042, R.drawable.p043, R.drawable.p044, R.drawable.p051,
			R.drawable.p052, R.drawable.p053, R.drawable.p054, R.drawable.p061,
			R.drawable.p062, R.drawable.p063, R.drawable.p064, R.drawable.p071,
			R.drawable.p072, R.drawable.p073, R.drawable.p074, R.drawable.p081,
			R.drawable.p082, R.drawable.p083, R.drawable.p084, R.drawable.p091,
			R.drawable.p092, R.drawable.p093, R.drawable.p094, R.drawable.p101,
			R.drawable.p102, R.drawable.p103, R.drawable.p104, R.drawable.p111,
			R.drawable.p112, R.drawable.p113, R.drawable.p114, R.drawable.p121,
			R.drawable.p122, R.drawable.p123, R.drawable.p124, R.drawable.p131,
			R.drawable.p132, R.drawable.p133, R.drawable.p134));

	public static List<Integer> getData(String page) {
		int pageInt = Integer.parseInt(page);
		List<Integer> data = new ArrayList<Integer>();
		List<Integer> getmDatas55 = mDatas55;
		if (page.equals("2")) {
			data = mDatas02;
		}else if(page.equals("4")){
			data = mDatas04;
		}else if(page.equals("16")){
			data = mDatas16;
		}else if(page.equals("55")){
			data = mDatas55;
		}else {
			for (int i = 0; i < getmDatas55.size(); i++) {
				Integer integer = getmDatas55.get(i);
				if (i < pageInt) {
					data.add(integer);
				}
			}
		}
		return data;
	}

}
