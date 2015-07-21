package taeha.wheelloader.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import taeha.wheelloader.update.R.string;


import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MonitorFragment extends Fragment{
	/////////////////CONSTANT////////////////////////////////////////////
	private static final String TAG = "MonitorFragment";
	
	private static final int STATE_STM32 				= 0;
	private static final int STATE_ANDROID_OS	 		= 1;
	private static final int STATE_ANDROID_APP 			= 2;
	private static final int STATE_STM32_FACTORYINIT	= 3;
	private static final int STATE_ERRORREPORT_TO_USB   = 4;
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
	
	MonitorCopyErrorToUSB monitorCopyErrorToUSB;
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
				
				case STATE_STM32:
					if(UpdateFile.GetMonitorSTM32Version() != null)
						ParentActivity.showMonitorUpdateQuestionPopup();
					
					break;
				case STATE_ANDROID_OS:
					ParentActivity.showMonitorOS();
					break;
				case STATE_ANDROID_APP:
					if(UpdateFile.GetMonitorVersion() != null)
						UpdateFile.MonitorAndroidAppUpdate();
					break;
				case STATE_STM32_FACTORYINIT:
					if(UpdateFile.GetMonitorSTM32FactoryInitVersion() != null)
						ParentActivity.showMonitorFactoryInitUpdateQuestionPopup();
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
		
		Map<String, String> mapSTM32 = new HashMap<String, String>(2);
		Map<String, String> mapOS = new HashMap<String, String>(2);
		Map<String, String> mapApp = new HashMap<String, String>(2);
		//Factory Init
		Map<String, String> mapSTM32FactoryInit = new HashMap<String, String>(2); 
		
		mapSTM32.put("First Line", ParentActivity.getResources().getString(string.Monitor_STM32));
		if(UpdateFile.GetMonitorSTM32Version() != null)
			mapSTM32.put("Second Line",UpdateFile.GetMonitorSTM32Version());
        data.add(mapSTM32);
        
        mapOS.put("First Line", ParentActivity.getResources().getString(string.Monitor_Android_OS));
        mapOS.put("Second Line","");
        data.add(mapOS);
        
        
        mapApp.put("First Line", ParentActivity.getResources().getString(string.Monitor_Android_App));
        if(UpdateFile.GetMonitorVersion() != null)
        	mapApp.put("Second Line",UpdateFile.GetMonitorVersion());
        data.add(mapApp);
        //Factory Init
        mapSTM32FactoryInit.put("First Line", ParentActivity.getResources().getString(string.Monitor_STM32_Factory_Init));
        if(UpdateFile.GetMonitorSTM32FactoryInitVersion() != null)
        	mapSTM32FactoryInit.put("Second Line",UpdateFile.GetMonitorSTM32FactoryInitVersion());
        data.add(mapSTM32FactoryInit);
        
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
	public void StatusDisplay(String str){
		textViewStatus.setText(str);
		textViewStatus.setTextColor(0xFF00FF00);
	}
	public void StatusWarningDisplay(String str){
		textViewStatus.setText(str);
		textViewStatus.setTextColor(0xFFFF0000);
	}
	/////////////////////////////////////////////////////////////////////
	public void TestADBShellCommand() {
		Runtime runtime = Runtime.getRuntime();

		Process process;

		String res = "-0-";

		try {

			String cmd = "top -n 1";

			process = runtime.exec(cmd);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					process.getInputStream()));

			String line;

			while ((line = br.readLine()) != null) {

				Log.i("test", line);

			}

		} catch (Exception e) {

			e.fillInStackTrace();

			Log.e("Process Manager", "Unable to execute top command");

		}
	}
	
	
}
