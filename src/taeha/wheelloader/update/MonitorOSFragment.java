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
import android.content.DialogInterface;
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

public class MonitorOSFragment extends Fragment{
	/////////////////CONSTANT////////////////////////////////////////////
	private static final String TAG = "MonitorOSFragment";

	/////////////////////////////////////////////////////////////////////
	/////////////////////RESOURCE////////////////////////////////////////
	// Fragment Root
	private View mRoot;
	
	TextView textViewBoot;
	TextView textViewKernel;
	TextView textViewSystem;
	TextView textViewRamdisk;
	
	Button btnUpdate;
	

	/////////////////////////////////////////////////////////////////////
	
	/////////////////////VALUABLE////////////////////////////////////////
	// Home
	private MainActivity ParentActivity;
	
	// CAN1CommManager
	protected static CAN1CommManager CAN1Comm = null;
	
	UpdateFileFindClass UpdateFile;
	
	String strBoot;
	String strKernel;
	String strSystem;
	String strRamdisk;
	
	boolean FileOkFlag;
	UpdateQuestionMonitorSTM32Popup.Builder UpdateQuestionBuilder;
	/////////////////////////////////////////////////////////////////////	
	
	///////////////////ANIMATION/////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////
	
	///////////////////Common Function///////////////////////////////////
	private void InitResource(){
		textViewBoot = (TextView)mRoot.findViewById(R.id.textView_screen_monitor_os_boot);
		textViewKernel = (TextView)mRoot.findViewById(R.id.textView_screen_monitor_os_kernel);
		textViewSystem = (TextView)mRoot.findViewById(R.id.textView_screen_monitor_os_system);
		textViewRamdisk = (TextView)mRoot.findViewById(R.id.textView_screen_monitor_os_ramdisk);
		
		btnUpdate = (Button)mRoot.findViewById(R.id.button_screen_monitor_os_update);
		
	}
	private void InitButtonListener(){
		btnUpdate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(FileOkFlag){
					showMonitorOSUpdateQuestionPopup();
				}
			}
		});
	}

	private void InitValuables(){
		Log.d(TAG,"InitValuables");
		CAN1Comm = CAN1CommManager.getInstance();	
		
		UpdateFile = new UpdateFileFindClass(ParentActivity);
		
		strBoot = UpdateFile.GetMonitorBootVersion();
		strKernel = UpdateFile.GetMonitorKernelVersion();
		strSystem = UpdateFile.GetMonitorSystemVersion();
		strRamdisk = UpdateFile.GetMonitorRamdiskVersion();
		
		BootVersionDisplay(strBoot);
		KernelVersionDisplay(strKernel);
		SystemVersionDisplay(strSystem);
		RamdiskVersionDisplay(strRamdisk);
		
//		if(strBoot != null && strKernel != null && strSystem != null && strRamdisk != null){
//			FileOkFlag = true;
//		}
		if(strKernel != null && strSystem != null && strRamdisk != null){
			FileOkFlag = true;
		}
		if(FileOkFlag){
			btnUpdate.setClickable(true);
		}else{
			btnUpdate.setClickable(false);
		}
		UpdateQuestionBuilder = new UpdateQuestionMonitorSTM32Popup.Builder(ParentActivity);

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
		mRoot = inflater.inflate(R.layout.screen_monitor_os, null);
		
		InitResource();
		InitValuables();
		InitButtonListener();
		
		ParentActivity.MenuIndex = ParentActivity.INDEX_MONITOR_ANDROID_OS;
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
	
	public void showMonitorOSUpdateQuestionPopup(){

		if(ParentActivity.MenuDialog != null){
			ParentActivity.MenuDialog.dismiss();
			ParentActivity.MenuDialog = null;
		}
		

		UpdateQuestionBuilder.setOKButton(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.d(TAG,"setOKButton");
				CAN1Comm.TxCMDToMCU(CAN1CommManager.CMD_OSUPDATE);
				ParentActivity.Reboot();
				dialog.dismiss();
			}
		});
		UpdateQuestionBuilder.setCancelButton(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.d(TAG,"setCancelButton");				
				dialog.dismiss();
				
				
			}
		});
		UpdateQuestionBuilder.setDismiss(new DialogInterface.OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				Log.d(TAG,"onDismiss");
				

			}
		});
		
		ParentActivity.MenuDialog = UpdateQuestionBuilder.create(ParentActivity.getResources().getString(string.Do_you_want_update));
		ParentActivity.MenuDialog.show();		
	}
	
	///////////////////////////////////////////////////////
	public void BootVersionDisplay(String str){
		textViewBoot.setText("Boot\n" + str);
	}
	public void KernelVersionDisplay(String str){
		textViewKernel.setText("Kernel\n" + str);
	}
	public void SystemVersionDisplay(String str){
		textViewSystem.setText("System\n" + str);
	}
	public void RamdiskVersionDisplay(String str){
		textViewRamdisk.setText("Ramdisk\n" + str);
	}

}
