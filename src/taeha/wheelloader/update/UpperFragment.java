package taeha.wheelloader.update;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class UpperFragment extends Fragment{
	/////////////////CONSTANT////////////////////////////////////////////
	private static final String TAG = "UpperFragment";
	/////////////////////////////////////////////////////////////////////
	/////////////////////RESOURCE////////////////////////////////////////
	// Fragment Root
	private View mRoot;
	
	// Thread
	protected Thread threadRead = null;
	
	// Thread Sleep Time
	private static int ThreadSleepTime;
	
	// ImageButton 
	ImageButton imgbtnBack;
	
	// TextView
	TextView textViewTitle;
	TextView textViewVersion;
	TextView textViewModel;
	/////////////////////////////////////////////////////////////////////
	
	/////////////////////VALUABLE////////////////////////////////////////
	// Home
	private MainActivity ParentActivity;
	
	String strVersion;
	/////////////////////////////////////////////////////////////////////	
	
	///////////////////ANIMATION/////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////
	
	///////////////////Common Function///////////////////////////////////
	private void InitResource(){
		// ImageButton
		imgbtnBack = (ImageButton)mRoot.findViewById(R.id.imageButton_screen_upper_back);
		// TextView
		textViewTitle = (TextView)mRoot.findViewById(R.id.textView_screen_upper_title);
		textViewVersion = (TextView)mRoot.findViewById(R.id.textView_screen_upper_version);
		textViewModel = (TextView)mRoot.findViewById(R.id.textView_screen_upper_Model);

	}
	private void InitButtonListener(){
		imgbtnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG,"imgbtnBack");
				ParentActivity.onClickBack();
			}
		});
	}

	private void InitValuables(){
		Log.d(TAG,"InitValuables");
		// ++, 150401 cjg
		strVersion = Integer.toString(ParentActivity.VERSION_HIGH) + "." + Integer.toString(ParentActivity.VERSION_LOW) + "." 
				+ Integer.toString(ParentActivity.VERSION_SUB_HIGH);
		// --, 150401 cjg
		SetVersion(strVersion);

		ThreadSleepTime = 500;
	}
	protected void SetThreadSleepTime(int Time){
		ThreadSleepTime = Time;
	}
	protected int GetThreadSleepTime(){
		return ThreadSleepTime;
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
		mRoot = inflater.inflate(R.layout.screen_upper, null);
		

		InitResource();
		InitValuables();
		InitButtonListener();
		setButtonInvisible(View.INVISIBLE);
		
		
		threadRead = new Thread(new ReadThread(this));
		threadRead.start();
		
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
		threadRead.interrupt();
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
	private void DisplayUI(){
		ParentActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				UpdateUI();
			}
		});
	}
	
	// Read Thread
	public static class ReadThread implements Runnable {
		private WeakReference<UpperFragment> fragmentRef = null;
		public Message msg = null;
		public ReadThread(UpperFragment fragment){
			this.fragmentRef = new WeakReference<UpperFragment>(fragment);
			msg = new Message();
		}
	
		@Override
		public void run() {
			try{
				while(!fragmentRef.get().threadRead.currentThread().isInterrupted())
				{
					fragmentRef.get().DisplayUI();
					Thread.sleep(1000);
				}
			}
			catch(InterruptedException ie){
				Log.e(TAG,"InterruptedException");
			}	
			catch(RuntimeException ee){
				Log.e(TAG,"RuntimeException");
			}
		}
	
	}
	
	
	public void UpdateUI(){
		MachineFWModelDisplay(ParentActivity.FWModel);
	}
	////////////////////////////////////////////////////////////////////
	public void MachineFWModelDisplay(byte[] _FWModel){
		String str;
		str = new String(_FWModel,0,_FWModel.length);
		
		if(_FWModel[0] != 0 && _FWModel[0] != 0xff)
		{
			textViewModel.setText(str);
			threadRead.interrupt();
		}
		
	}
	public void SetTitle(String title){
		textViewTitle.setText(title);
	}
	public void SetVersion(String version){
		textViewVersion.setText(version);
	}
	public void SetButtonClickable(boolean clickable){
		imgbtnBack.setClickable(clickable);
	}
	//++, 150326 cjg  
	public void setButtonInvisible(int visibility){
		imgbtnBack.setVisibility(visibility);
	}
	//--, 150326 cjg
}
