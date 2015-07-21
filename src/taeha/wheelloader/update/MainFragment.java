package taeha.wheelloader.update;

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

public class MainFragment extends Fragment{
	/////////////////CONSTANT////////////////////////////////////////////
	private static final String TAG = "MainFragment";
	
	private static final int STATE_MONIOR 	= 0;
	private static final int STATE_MCU 		= 1;
	private static final int STATE_CLUSTER 	= 2;
	private static final int STATE_BKCU 	= 3;
	/////////////////////////////////////////////////////////////////////
	/////////////////////RESOURCE////////////////////////////////////////
	// Fragment Root
	private View mRoot;
	
	// ListView
	ListView listItem;
	

	/////////////////////////////////////////////////////////////////////
	
	/////////////////////VALUABLE////////////////////////////////////////
	// Home
	private MainActivity ParentActivity;
	
	ArrayAdapter<String> listAdapter;

	/////////////////////////////////////////////////////////////////////	
	
	///////////////////ANIMATION/////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////
	
	///////////////////Common Function///////////////////////////////////
	private void InitResource(){
		listItem = (ListView)mRoot.findViewById(R.id.listView_screen_main);
	}
	private void InitButtonListener(){
		AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {
				case STATE_MONIOR:
					ParentActivity.showMonitor();
					ParentActivity._UpperFragment.setButtonInvisible(View.VISIBLE); // ++, -- 150326 cjg
					break;
				case STATE_MCU:
					ParentActivity.showMCU();
					ParentActivity._UpperFragment.setButtonInvisible(View.VISIBLE); // ++, -- 150326 cjg
					break;
				case STATE_CLUSTER:
					ParentActivity.showCluster();
					ParentActivity._UpperFragment.setButtonInvisible(View.VISIBLE); // ++, -- 150326 cjg
					break;
				case STATE_BKCU:
					ParentActivity.showBKCU();
					ParentActivity._UpperFragment.setButtonInvisible(View.VISIBLE); // ++, -- 150326 cjg
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
		

		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		
		Map<String, String> mapMonitor = new HashMap<String, String>(2);
		Map<String, String> mapMCU = new HashMap<String, String>(2);
		Map<String, String> mapCluster = new HashMap<String, String>(2);
		Map<String, String> mapBKCU = new HashMap<String, String>(2);
		
		mapMonitor.put("First Line", ParentActivity.getResources().getString(string.Monitor));
		mapMonitor.put("Second Line","");
        data.add(mapMonitor);
        
        mapMCU.put("First Line", ParentActivity.getResources().getString(string.MCU));
        mapMCU.put("Second Line","");
        data.add(mapMCU);
        
        mapCluster.put("First Line", ParentActivity.getResources().getString(string.Cluster));
        mapCluster.put("Second Line","");
        data.add(mapCluster);
        // ++, 150401 cjg
		if(ParentActivity.isAvailableBKCU.equals("-")){
		}else{
			mapBKCU.put("First Line", ParentActivity.getResources().getString(string.BKCU));
			mapBKCU.put("Second Line","");
			data.add(mapBKCU);
		}
		// --, 150401 cjg
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
		
		ParentActivity.MenuIndex = ParentActivity.INDEX_MAIN_TOP;
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
	/////////////////////////////////////////////////////////////////////

}
