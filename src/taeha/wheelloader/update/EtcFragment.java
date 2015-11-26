package taeha.wheelloader.update;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import taeha.wheelloader.update.R.string;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class EtcFragment extends Fragment{
	private static final String TAG = "EtcFragment";
	/////////////////////////////CONSTANT//////////////////////////
	private static final int STATE_ANDROID_OS	 			= 0;
	private static final int STATE_ANDROID_UPDATE 			= 1;
	private static final int STATE_ANDROID_MIRACAST 		= 2;
	private static final int STATE_PDF_TO_ANDROID  			= 3;
	private static final int STATE_PDF_REMOVE_FILE 			= 4;
	private static final int STATE_STM32	 				= 5;
	private static final int STATE_ANDROID_APP				= 6;
	//private static final int STATE_STM32_FACTORYINIT	= 7;
	public  static String ROOT_PATH = "/storage/emulated/legacy/Help_pdf";
	public  static String ROOT_PATH_USB = "/mnt/usb/UPDATE/Monitor/Help_pdf";
	///////////////////////////////////////////////////////////////
	// Frament Root
	private View mRoot;
	
	// ListView
	ListView listItem;
	TextView textViewStatus;
	//////////////////////////////////////////////////////////////
	// Home
	private MainActivity ParentActivity;
	
	ArrayAdapter<String> listAdapter;
	
	UpdateFileFindClass UpdateFile;
	
	private static final String CHECK_MIRACAST = "com.powerone.wfd.sink";
	/*PackageManager pm = null;
	static boolean installedMiracast = false;
	Handler handler = null;*/
	//////////////////////////////////////////////////////////////
	
	////////////////Common Function///////////////////////////////
	private void InitResource(){
		listItem = (ListView)mRoot.findViewById(R.id.listView_screen_main);
		textViewStatus = (TextView)mRoot.findViewById(R.id.textView_screen_main_status);
	}
	private void InitButtonListener(){
		AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				switch(arg2){
				case STATE_ANDROID_OS:
					ParentActivity.showMonitorOS();
					break;
				case STATE_ANDROID_UPDATE:
					if(UpdateFile.GetUpdateProgramVersion() != null)
						ParentActivity.showMonitorAppQuestionPopup(1);
					break;
				case STATE_ANDROID_MIRACAST:
					if(UpdateFile.GetMiracastVersion() != null)
							ParentActivity.showMonitorAppQuestionPopup(2);
					break;
				case STATE_PDF_TO_ANDROID:
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
				case STATE_PDF_REMOVE_FILE:
					fileRemove(ROOT_PATH);
					Toast.makeText(ParentActivity.getApplicationContext(), "Remove .pdf files ", 50).show();
					break;
				case STATE_STM32:
					if(UpdateFile.GetMonitorSTM32Version() != null)
						ParentActivity.showMonitorUpdateQuestionPopup(0);
					break;
				case STATE_ANDROID_APP:
					if(UpdateFile.GetMonitorVersion() != null)
						ParentActivity.showMonitorAppQuestionPopup(3);
					break;	
				/*case STATE_STM32_FACTORYINIT:
					if(UpdateFile.GetMonitorSTM32FactoryInitVersion() != null)
						ParentActivity.showMonitorFactoryInitUpdateQuestionPopup();
					break;*/
				default:
					break;
				}
			}
		};
		listItem.setOnItemClickListener(mItemClickListener);
	}
	private void InitValuables(){
		Log.d(TAG, "InitValuables");
		UpdateFile = new UpdateFileFindClass(ParentActivity);
		
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> mapSTM32				= new HashMap<String, String>(2);
		Map<String, String> mapApp					= new HashMap<String, String>(2);
		Map<String, String> mapOS					= new HashMap<String, String>(2);
		Map<String, String> mapUpdateProgram 		= new HashMap<String, String>(2);
		Map<String, String>	mapMiracastProgram	 	= new HashMap<String, String>(2);
		Map<String, String> mapPdfCopy				= new HashMap<String, String>(2);
		Map<String, String> mapPdfRemove			= new HashMap<String, String>(2);
		//Map<String, String> mapSTM32FactoryInit		= new HashMap<String, String>(2);
		


        mapOS.put("First Line", ParentActivity.getResources().getString(string.Monitor_Android_OS));
        mapOS.put("Second Line","");
        data.add(mapOS);
		
		mapUpdateProgram.put("First Line", ParentActivity.getResources().getString(string.Monitor_Android_Update));
		if(UpdateFile.GetUpdateProgramVersion() != null)
			mapUpdateProgram.put("Second Line", UpdateFile.GetUpdateProgramVersion());
		data.add(mapUpdateProgram);
		
		mapMiracastProgram.put("First Line", ParentActivity.getResources().getString(string.Monitor_Android_Miracast));
		if(UpdateFile.GetMiracastVersion() != null){
			mapMiracastProgram.put("Second Line", UpdateFile.GetMiracastVersion());
		}
		data.add(mapMiracastProgram);
		
		
		mapPdfCopy.put("First Line", ParentActivity.getResources().getString(string.Monitor_Copy_USB_TO_PDF));
		mapPdfCopy.put("Second Line", "");
		data.add(mapPdfCopy);
		
		mapPdfRemove.put("First Line", ParentActivity.getResources().getString(string.Monitor_Remove_PDF_File));
		mapPdfRemove.put("Second Line", "");
		data.add(mapPdfRemove);
		
		mapSTM32.put("First Line", ParentActivity.getResources().getString(string.Monitor_STM32));
		if(UpdateFile.GetMonitorSTM32Version() != null)
			mapSTM32.put("Second Line",UpdateFile.GetMonitorSTM32Version());
        data.add(mapSTM32);
        
        mapApp.put("First Line", ParentActivity.getResources().getString(string.Monitor_Android_App));
        if(UpdateFile.GetMonitorVersion() != null)
        	mapApp.put("Second Line",UpdateFile.GetMonitorVersion());
        data.add(mapApp);
        
        //Factory Init
        /*mapSTM32FactoryInit.put("First Line", ParentActivity.getResources().getString(string.Monitor_STM32_Factory_Init));
        if(UpdateFile.GetMonitorSTM32FactoryInitVersion() != null)
        	mapSTM32FactoryInit.put("Second Line",UpdateFile.GetMonitorSTM32FactoryInitVersion());
        data.add(mapSTM32FactoryInit);*/
		SimpleAdapter adapter = new SimpleAdapter(ParentActivity, data, 
				android.R.layout.simple_list_item_2,
				new String[] {"First Line", "Second Line"},
				new int[] {android.R.id.text1, android.R.id.text2});
		
		listItem.setAdapter(adapter);
	}
	
	@Override
	public void onInflate(Activity activity, AttributeSet attrs,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onInflate");
		for(int i = 0; i < attrs.getAttributeCount(); i++){
			Log.d(TAG,
					"   " + attrs.getAttributeName(i) + " = "
							+ attrs.getAttributeValue(i));
		}
		super.onInflate(activity, attrs, savedInstanceState);
		
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.ParentActivity = (MainActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		/*pm = getActivity().getPackageManager();*/
		
		if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1){
			ROOT_PATH = "/mnt/sdcard/Help_pdf";
			Log.d(TAG, ROOT_PATH);
		}else {
			ROOT_PATH = "/storage/emulated/legacy/Help_pdf";
			Log.d(TAG, ROOT_PATH);
		}		
		
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
		ParentActivity.MenuIndex = ParentActivity.INDEX_MONITOR_ETC;
		
		return mRoot;

	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}
	//fileList
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
	//fileCopy
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
	// fileRemove
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
