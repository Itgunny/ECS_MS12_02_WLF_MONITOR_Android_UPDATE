package taeha.wheelloader.update;

import java.io.File;
import java.util.ArrayList;

import taeha.wheelloader.update.R.string;
import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BKCUListFragment extends Fragment{
	private static final String TAG = "BKCUListFragment";
	
	BKCUUpdatePopup.Builder BKCUCANUpdateBuilder;
	UpdateQuestionMonitorSTM32Popup.Builder UpdateQuestionBuilder;
	
	private static CAN1CommManager CAN1Comm;
	// Fragment Root
	private View mRoot;
	
	// ListView
	ListView listItem;
	
	// TextView
	TextView textViewStatus;
	////////////////////////////////////////////////////////
	
	/////////////////////////VALUABLE///////////////////////
	private MainActivity ParentActivity;
	
	ArrayAdapter<String> listAdapter;
	
	UpdateFileFindClass UpdateFile;
	////////////////////////////////////////////////////////
	
	private void InitResource(){
		Log.d(TAG, "InitResource");
		listItem = (ListView)mRoot.findViewById(R.id.listView_screen_main);
		textViewStatus = (TextView)mRoot.findViewById(R.id.textView_screen_main_status);
		textViewStatus.setVisibility(View.INVISIBLE);		
	}
	
	private void InitButtonListener(){
		Log.d(TAG, "InitButtonListener");
		AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				UpdateFile.BKCU_FIRMWARE_NAME = arg0.getItemAtPosition(arg2).toString();
				showBKCUUpdateQuestionPopup();
			}
		};
		listItem.setOnItemClickListener(mItemClickListener);	
	}
	
	private void InitValuables(){
		Log.d(TAG, "InitValuables");
		BKCUCANUpdateBuilder = new BKCUUpdatePopup.Builder(ParentActivity);
		UpdateQuestionBuilder = new UpdateQuestionMonitorSTM32Popup.Builder(ParentActivity);
		UpdateFile = new UpdateFileFindClass(ParentActivity);
		ArrayList<String> fileName = new ArrayList<String>();
		File files = new File(UpdateFile.BKCU_FIRMWARE_PATH);
		if(files.exists()){
		if(files.listFiles().length > 0){
			for(File file : files.listFiles()){
				if(file.isFile()){
					if(file.getName().contains(".srec")){
						fileName.add(file.getName());
					}
				}
			}
		}
		}else{
			Toast.makeText(ParentActivity, "You have to make a Update folder to USB.", Toast.LENGTH_SHORT).show();
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ParentActivity, android.R.layout.simple_list_item_1, 
				fileName);
		listItem.setAdapter(adapter);
	}
	
	
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
		
		CAN1Comm = CAN1CommManager.getInstance();
		
		InitResource();
		InitValuables();
		InitButtonListener();
		CAN1Comm.TxCMDToMCU(CAN1CommManager.CMD_CANUPDATE, 1, 0x34);
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ParentActivity.MenuIndex = ParentActivity.INDEX_BKCU_TOP;
		
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
	
	public void showBKCUUpdateQuestionPopup(){

		if(ParentActivity.MenuDialog != null){
			ParentActivity.MenuDialog.dismiss();
			ParentActivity.MenuDialog = null;
		}
		

		UpdateQuestionBuilder.setOKButton(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.d(TAG,"setOKButton");
				showBKCUCANUpdatePopup();
				dialog.dismiss();
				ParentActivity.MenuIndex = ParentActivity.INDEX_BKCU_TOP;
			}
		});
		UpdateQuestionBuilder.setCancelButton(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.d(TAG,"setCancelButton");				
				dialog.dismiss();
				ParentActivity.MenuIndex = ParentActivity.INDEX_BKCU_TOP;
			}
		});
		UpdateQuestionBuilder.setDismiss(new DialogInterface.OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				Log.d(TAG,"onDismiss");
				ParentActivity.MenuIndex = ParentActivity.INDEX_BKCU_TOP;

			}
		});
		ParentActivity.MenuDialog = UpdateQuestionBuilder.create(ParentActivity.getResources().getString(string.Do_you_want_update));
		ParentActivity.MenuDialog.show();		
	}
	
	public void showBKCUCANUpdatePopup(){

		if(ParentActivity.MenuDialog != null){
			ParentActivity.MenuDialog.dismiss();
			ParentActivity.MenuDialog = null;
		}
		BKCUCANUpdateBuilder.setExitButton(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.d(TAG,"setExitButton");				
				dialog.dismiss();
			}
		});
		BKCUCANUpdateBuilder.setDismiss(new DialogInterface.OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				Log.d(TAG,"onDismiss");
			}
		});
		ParentActivity.MenuDialog = BKCUCANUpdateBuilder.create(BKCUCANUpdateBuilder, UpdateFile.BKCU_FIRMWARE_PATH + "/" + UpdateFile.BKCU_FIRMWARE_NAME);
		ParentActivity.MenuDialog.show();		
	}
}
