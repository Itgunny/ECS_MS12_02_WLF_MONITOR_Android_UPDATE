package taeha.wheelloader.update;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import taeha.wheelloader.update.R.string;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MonitorFragment extends Fragment{
	
	/////////////////CONSTANT////////////////////////////////////////////
	private static final String TAG = "MonitorFragment";
	
	private static final int STATE_STM32_ANDOROID_APP	= 0;
	private static final int STATE_LANGUAGE				= 1;
	private static final int STATE_LANGUAGE_INIT		= 2;
	private static final int STATE_ETC					= 3;
	
	public  static String ROOT_PATH = "/storage/emulated/legacy/Language";
	public  static String ROOT_PATH_USB = "/mnt/usb/UPDATE/Monitor/Language";
	/////////////////////////////////////////////////////////////////////
	/////////////////////RESOURCE////////////////////////////////////////
	// Fragment Root
	private View mRoot;
	
	// ListView
	ListView listItem;
	
	TextView textViewStatus;
	/////////////////////////////////////////////////////////////////////
	
	/////////////////////VALUABLE////////////////////////////////////////
	// Home
	private MainActivity ParentActivity;
	
	ArrayAdapter<String> listAdapter;
	
	UpdateFileFindClass UpdateFile;

	/////////////////////////////////////////////////////////////////////	
	
	///////////////////ANIMATION/////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////
	
	///////////////////Common Function///////////////////////////////////
	private void InitResource(){
		listItem = (ListView)mRoot.findViewById(R.id.listView_screen_main);
		textViewStatus = (TextView)mRoot.findViewById(R.id.textView_screen_main_status);
	}
	private void InitButtonListener(){
		AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {
				case STATE_STM32_ANDOROID_APP:
					if((UpdateFile.GetMonitorSTM32Version() != null) && (UpdateFile.GetMonitorVersion() != null)){
						ParentActivity.showMonitorSTM32AndAppQuestionPopup();
						Log.d(TAG, "STM32 AND APP");
					}else if((UpdateFile.GetMonitorSTM32Version() == null) && (UpdateFile.GetMonitorVersion() != null)){
						ParentActivity.showMonitorAppQuestionPopup(0);
						Log.d(TAG, "APP");
					}else if((UpdateFile.GetMonitorSTM32Version() != null) && (UpdateFile.GetMonitorVersion() == null)){
						ParentActivity.showMonitorUpdateQuestionPopup(1);
						Log.d(TAG, "STM32");
					}
					break;
				case STATE_LANGUAGE:
					if(ParentActivity.getisDisConnected() == false){
						List<File> dirList = getDirFileList(ROOT_PATH_USB);
						File dir = new File(ROOT_PATH);
						if(!dir.exists()){
							dir.mkdirs();
						}
						try{
							for(int i = 0; i < dirList.size(); i++){
								String fileName = dirList.get(i).getName();
								fileCopy(ROOT_PATH_USB + "/" + fileName, ROOT_PATH + "/" + fileName);
								Toast.makeText(ParentActivity.getApplicationContext(), "Copy Sucess : \n" + fileName + "(" + Integer.valueOf(i+1) + " / " + dirList.size() + ")", 50).show();
							}	
						}catch(Exception e){
							Log.d(TAG, "exception");
						}
					} else{
						Toast.makeText(ParentActivity.getApplicationContext(), "Please Connect USB into device.", 50).show();
					}
					break;
				case STATE_LANGUAGE_INIT:
					fileRemove(ROOT_PATH);
					Toast.makeText(ParentActivity.getApplicationContext(), "Remove string.xls file", 50).show();
					break;
				case STATE_ETC:
					ParentActivity.showEtc();
					break;
				default:
					break;
				}
			}
		};		
		listItem.setOnItemClickListener(mItemClickListener);
	}

	private void InitValuables(){
		Log.d(TAG,"InitValuables");
		
		UpdateFile = new UpdateFileFindClass(ParentActivity);

		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		
		Map<String, String> mapSTM32AndApp = new HashMap<String, String>(2);
		Map<String, String> mapLanguage = new HashMap<String, String>(2);
		Map<String, String> mapLanguageInit = new HashMap<String, String>(2);
		Map<String, String> mapEtc = new HashMap<String, String>(2);
		
		
		mapSTM32AndApp.put("First Line", ParentActivity.getResources().getString(string.Monitor_STM32_APP));
		if((UpdateFile.GetMonitorSTM32Version() != null ) && (UpdateFile.GetMonitorVersion() != null)){
			mapSTM32AndApp.put("Second Line", "Firmware : " + UpdateFile.GetMonitorSTM32Version() +
					" / Application : " + UpdateFile.GetMonitorVersion());
		}else if((UpdateFile.GetMonitorSTM32Version() == null) && (UpdateFile.GetMonitorVersion() != null)){
			mapSTM32AndApp.put("Second Line", "Application : " + UpdateFile.GetMonitorVersion());
		}else if((UpdateFile.GetMonitorSTM32Version() != null) && (UpdateFile.GetMonitorVersion() == null)){
			mapSTM32AndApp.put("Second Line", "Firmware : " + UpdateFile.GetMonitorSTM32Version());
		}
		data.add(mapSTM32AndApp);
		
		mapLanguage.put("First Line", ParentActivity.getResources().getString(string.Monitor_Copy_LANGUAGE));
		mapLanguage.put("Second Line", "");
		data.add(mapLanguage); 
		
		mapLanguageInit.put("First Line", ParentActivity.getResources().getString(string.Monitor_LANGUAGE_Init));
		mapLanguageInit.put("Second Line", "");
		data.add(mapLanguageInit); 
		
        mapEtc.put("First Line", ParentActivity.getResources().getString(string.Monitor_ETC));
        mapEtc.put("Second Line","");
        data.add(mapEtc);        

        SimpleAdapter adapter = new SimpleAdapter(ParentActivity, data,
                android.R.layout.simple_list_item_2, 
                new String[] {"First Line", "Second Line" }, 
                new int[] {android.R.id.text1, android.R.id.text2 });

		listItem.setAdapter(adapter);

	}
	////////////////////////////////////////////////////////////////////
	

	////////////////////Life Cycle Function/////////////////////////////
	@Override
	public void onInflate(Activity activity, AttributeSet attrs,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onInflate");
		for (int i = 0; i < attrs.getAttributeCount(); i++) {
			Log.v(TAG,
					"    " + attrs.getAttributeName(i) + " = "
							+ attrs.getAttributeValue(i));
		}
		super.onInflate(activity, attrs, savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onAttach");
		super.onAttach(activity);
		this.ParentActivity = (MainActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onCreateView");
		mRoot = inflater.inflate(R.layout.screen_main, null);
		
		InitResource();
		InitValuables();
		InitButtonListener();
		ParentActivity.MenuIndex = ParentActivity.INDEX_MONITOR_TOP;
		
		return mRoot;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onActivityCreated");
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onDestroy");
		super.onDestroy();

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onDetach");
		super.onDetach();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onPause");
		super.onPause();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onResume");
		super.onResume();

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onStart");
		super.onStart();

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onStop");
		super.onStop();

	}
	public static List<File> getDirFileList(String dirPath){
		List<File> dirFileList = null;
		
		File dir = new File(dirPath);

		if(dir.exists()){ 
			Log.d(TAG, ""+"dir is exist.");
			File[] files = dir.listFiles();	
			dirFileList = Arrays.asList(files);
		}else{
			Log.d(TAG, "" + "dir is not exist.");
			dir.mkdir();
			File[] files = dir.listFiles();
			dirFileList = Arrays.asList(files);
		}
		return dirFileList;
	}
	public static void fileCopy(String inputFilePath, String outputFilePath){
		try{
			FileInputStream fis = new FileInputStream(inputFilePath);
			FileOutputStream fos = new FileOutputStream(outputFilePath);
			
			FileChannel fcin = fis.getChannel();
			FileChannel fcout = fos.getChannel();
			
			long size = fcin.size();
			fcin.transferTo(0, size, fcout);
			Log.d(TAG," "+ inputFilePath + " " + outputFilePath);
			
			fcout.close();
			fcin.close();
			
			fos.close();
			fis.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	public static void fileRemove(String FilePath){
		File file = new File(FilePath);
		
		File[] childFileList = file.listFiles();
		if(childFileList != null){
			for(File childFile : childFileList){
				if(childFile.isDirectory()){
					fileRemove(childFile.getAbsolutePath());
				}else{
					childFile.delete();
				}
			}	
		}	
	}
	
	public void StatusDisplay(String str){
		textViewStatus.setText(str);
		textViewStatus.setTextColor(0xFF00FF00);
	}
	public void StatusWarningDisplay(String str){
		textViewStatus.setText(str);
		textViewStatus.setTextColor(0xFFFF0000);
	}
}
