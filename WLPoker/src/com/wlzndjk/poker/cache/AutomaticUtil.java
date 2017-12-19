package com.wlzndjk.poker.cache;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wlzndjk.poker.bean.BackgroundBean;
import com.wlzndjk.poker.bean.DecorationBean;
import com.wlzndjk.poker.bean.FrameBean;
import com.wlzndjk.poker.bean.MobileShell;
import com.wlzndjk.poker.bean.PageBean;
import com.wlzndjk.poker.bean.PokerBean;
import com.wlzndjk.poker.constants.MyConstants;
import com.wlzndjk.poker.utils.FileUtil;
import com.wlzndjk.poker.utils.JSONUtil;
import com.wlzndjk.poker.utils.ToastUtils;

public class AutomaticUtil {
	
	/**
	 * 
	 * 自动修改模板TheOrder 层级信息
	 * @param id
	 * @param myJsonObject
	 */
	public static void reviseTemplate(String template_id,JSONObject myJsonObject){
		try {
			ToastUtils.show("开始");
			//1步
			PokerBean oldPoker = initPoker(myJsonObject);
			ToastUtils.show("1步Ok");
			//2步
			PokerBean newPoker = newPoker(oldPoker);
			ToastUtils.show("2步Ok");
			//3步
			String savePokerData = savePokerData(newPoker);
			ToastUtils.show("3步Ok");
			//4步
			FileUtil.saveFile(savePokerData, MyConstants.NEW_TEMPLATE, "new_"+template_id, MyConstants.JSON);
			ToastUtils.show("4步Ok");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 2步 
	 * @param oldPoker
	 * @return
	 */
	public static PokerBean newPoker(PokerBean oldPoker) {
		List<MobileShell> listBigace = oldPoker.getListBigace();
		makeListChange(listBigace);
		
		List<MobileShell> listSmallace = oldPoker.getListSmallace();
		makeListChange(listSmallace);
		
		List<MobileShell> listBackup = oldPoker.getListBackup();
		makeListChange(listBackup);
		
		List<MobileShell> listInpage = oldPoker.getListInpage();
		makeListChange(listInpage);

		return oldPoker;
	}

	/**
	 * 
	 * 2.1步 
	 * @param oldPoker
	 * @return
	 */
	private static void makeListChange(List<MobileShell> list) {
		for (int i = 0; i < list.size(); i++) {
			
			MobileShell oldmobileShell = list.get(i);
			
			List<BackgroundBean> oldlistBackground = oldmobileShell.getListBackground();
			for (int j = 0; j < oldlistBackground.size(); j++) {
				BackgroundBean oldgroundBean = oldlistBackground.get(j);
				oldgroundBean.setTheOrder("1");
			}
			
			List<FrameBean> oldlistframe = oldmobileShell.getListframe();
			for (int j = 0; j < oldlistframe.size(); j++) {
				FrameBean oldframeBean = oldlistframe.get(j);
				oldframeBean.setTheOrder("2");
			}
			
			List<DecorationBean> oldlistDecoration = oldmobileShell.getListDecoration();
			for (int j = 0; j < oldlistDecoration.size(); j++) {
				DecorationBean olddecorationBean = oldlistDecoration.get(j);
				olddecorationBean.setTheOrder("3");
			}
			
		}
	}

	/**
	 * 
	 * 1步
	 * 
	 * @param myJsonObject
	 * @return
	 * @throws JSONException
	 */
	public static PokerBean initPoker(JSONObject myJsonObject)
			throws JSONException {
		PokerBean poker = new PokerBean();
		String pagetype = myJsonObject.optString("pagetype");
		poker.setPagetype(pagetype);
		JSONArray bigace = myJsonObject.getJSONArray("bigace");
		List<MobileShell> listbigace = initListMobileShell(bigace);
		poker.setListBigace(listbigace);
		JSONArray smallace = myJsonObject.getJSONArray("smallace");
		List<MobileShell> listsmallace = initListMobileShell(smallace);
		poker.setListSmallace(listsmallace);
		JSONArray backup = myJsonObject.getJSONArray("backup");
		List<MobileShell> listbackup = initListMobileShell(backup);
		poker.setListBackup(listbackup);
		JSONArray inpage = myJsonObject.getJSONArray("inpage");
		List<MobileShell> listinpage = initListMobileShell(inpage);
		poker.setListInpage(listinpage);

		return poker;

	}
	

	/**
	 * 3步 PokerBean 转 String
	 * 
	 * @throws JSONException
	 */
	public static String savePokerData(PokerBean pokerBean)
			throws JSONException {
		String myJson = null;
		JSONObject poker = new JSONObject();

		String pagetype = pokerBean.getPagetype();
		poker.put("pagetype", pagetype);

		List<MobileShell> listBigace = pokerBean.getListBigace();
		JSONArray bigace = listMobileShell2JSONArray(listBigace);
		poker.put("bigace", bigace);

		List<MobileShell> listSmallace = pokerBean.getListSmallace();
		JSONArray smallace = listMobileShell2JSONArray(listSmallace);
		poker.put("smallace", smallace);

		List<MobileShell> listBackup = pokerBean.getListBackup();
		JSONArray backup = listMobileShell2JSONArray(listBackup);
		poker.put("backup", backup);

		List<MobileShell> listInpage = pokerBean.getListInpage();
		JSONArray inpage = listMobileShell2JSONArray(listInpage);
		poker.put("inpage", inpage);

		myJson = poker.toString();

		return myJson;

	}

	/**
	 * 1.1步
	 * 
	 * @param myJsonObject
	 * @return
	 * @throws JSONException
	 */
	public static List<MobileShell> initListMobileShell(JSONArray myJsonObject)
			throws JSONException {
		List<MobileShell> list = new ArrayList<MobileShell>();
		for (int i = 0; i < myJsonObject.length(); i++) {
			String jsonarrayStr= (String) myJsonObject.get(i);
			JSONArray jsonarray =new JSONArray(jsonarrayStr) ;
			MobileShell mobileShell = initMobileShell(jsonarray);
			list.add(mobileShell);
		}
		return list;
	}

	/**
	 * 1.2步
	 * 
	 * @param myJsonObject
	 * @return
	 * @throws JSONException
	 */
	public static MobileShell initMobileShell(JSONArray myJsonObject)
			throws JSONException {
		MobileShell mobileShell = new MobileShell();
		List<FrameBean> listframe = new ArrayList<FrameBean>();
		List<DecorationBean> listDecoration = new ArrayList<DecorationBean>();
		List<BackgroundBean> listBackground = new ArrayList<BackgroundBean>();
		List<PageBean> listPage = new ArrayList<PageBean>();
		for (int i = 0; i < myJsonObject.length(); i++) {
			JSONObject jsonobject = (JSONObject) myJsonObject.get(i);
			String elementType = jsonobject.getString("elementType");
			if ("frame".equals(elementType)) {
				FrameBean frameBean = (FrameBean) JSONUtil.JSONToObj(
						jsonobject.toString(), FrameBean.class);
				listframe.add(frameBean);
			} else if ("decoration".equals(elementType)) {
				DecorationBean decorationBean = (DecorationBean) JSONUtil
						.JSONToObj(jsonobject.toString(), DecorationBean.class);
				listDecoration.add(decorationBean);
			} else if ("background".equals(elementType)) {
				BackgroundBean backgroundBean = (BackgroundBean) JSONUtil
						.JSONToObj(jsonobject.toString(), BackgroundBean.class);
				listBackground.add(backgroundBean);
			} else if ("page".equals(elementType)) {
				PageBean pageBean = (PageBean) JSONUtil.JSONToObj(
						jsonobject.toString(), PageBean.class);
				listPage.add(pageBean);
			}
		}
		mobileShell.setListframe(listframe);
		mobileShell.setListDecoration(listDecoration);
		mobileShell.setListBackground(listBackground);
		mobileShell.setListPage(listPage);

		return mobileShell;
	}


	/**
	 * 3.1步
	 * 
	 * @param list
	 * @return
	 */
	public static JSONArray listMobileShell2JSONArray(List<MobileShell> list) {
		JSONArray arr = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			MobileShell mobileShell = list.get(i);
			String mobileShellStr = mobileShell2JSONArray(mobileShell);
			try {
				arr.put(i, mobileShellStr);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return arr;

	}
	
	/**
	 * 3.22步
	 * 
	 * @param bean
	 * @return
	 */
	public static String mobileShell2JSONArray(MobileShell bean) {
		List<FrameBean> listframe = bean.getListframe();
		List<DecorationBean> listDecoration = bean.getListDecoration();
		List<BackgroundBean> listBackground = bean.getListBackground();
		List<PageBean> listPage = bean.getListPage();
		int cout = -1;
		JSONArray arr = new JSONArray();
		try {

			if (listframe.size() != 0) {
				JSONArray frameStr = list2JSONArray(listframe);
				for (int i = 0; i < frameStr.length(); i++) {
					JSONObject object = (JSONObject) frameStr.get(i);
					cout++;
					arr.put(cout, object);
				}
			}
			if (listDecoration.size() != 0) {
				JSONArray decorationStr = list2JSONArray(listDecoration);
				for (int i = 0; i < decorationStr.length(); i++) {
					JSONObject object = (JSONObject) decorationStr.get(i);
					cout++;
					arr.put(cout, object);
				}
			}
			if (listBackground.size() != 0) {
				JSONArray backgroundStr = list2JSONArray(listBackground);
				for (int i = 0; i < backgroundStr.length(); i++) {
					JSONObject object = (JSONObject) backgroundStr.get(i);
					cout++;
					arr.put(cout, object);
				}
			}
			if (listPage.size() != 0) {
				JSONArray pageStr = list2JSONArray(listPage);
				for (int i = 0; i < pageStr.length(); i++) {
					JSONObject object = (JSONObject) pageStr.get(i);
					cout++;
					arr.put(cout, object);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return arr.toString();
	}

	/**
	 * 3.3步
	 * 
	 * @param list
	 * @return
	 */
	public static <T> JSONArray list2JSONArray(List<T> list) {
		JSONArray arr = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			T t = list.get(i);
			JSONObject jsonobject = JSONUtil.objectToJson(t);
			try {
				arr.put(i, jsonobject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return arr;
	}

}
