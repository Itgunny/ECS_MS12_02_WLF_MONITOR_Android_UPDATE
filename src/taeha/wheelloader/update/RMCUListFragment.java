package taeha.wheelloader.update;

import java.io.File;
import java.util.ArrayList;

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
import android.widget.TextView;
import android.widget.Toast;

public class RMCUListFragment extends Fragment{
	private static final String TAG = "RMCUListFragment";
	
	
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
	}
	private void InitButtonListener(){
		Log.d(TAG, "InitButtonListener");
		AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				UpdateFile.RMCU_FIRMWARE_NAME = arg0.getItemAtPosition(arg2).toString();
				Log.d(TAG, "FIRMWARE_NAME : " + UpdateFile.RMCU_FIRMWARE_NAME);
				ParentActivity.showRMCUSelect();
			}
		};
		listItem.setOnItemClickListener(mItemClickListener);	
	}
	private void InitValuables(){
		Log.d(TAG, "InitValuables");
		
		UpdateFile = new UpdateFileFindClass(ParentActivity);
		ArrayList<String> fileName = new ArrayList<String>();
		File files = new File(UpdateFile.RMCU_FIRMWARE_PATH);
		if(files.exists()){
		if(files.listFiles().length > 0){
			for(File file : files.listFiles()){
				if(file.isFile()){
					if(file.getName().contains("RMCU")){						
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
		InitResource();
		InitValuables();
		InitButtonListener();
		
		ParentActivity.MenuIndex = ParentActivity.INDEX_RMCU_TOP;
		
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
}
