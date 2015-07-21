package taeha.wheelloader.update;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Timer;

import taeha.wheelloader.update.R.string;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	/////////////////////////////CONSTANT////////////////////////////////
	private static final String TAG = "MainActivity";
	
	
	public static final int VERSION_HIGH		= 2;
	public static final int VERSION_LOW			= 0;
	public static final int VERSION_SUB_HIGH	= 0;
	public static final int VERSION_SUB_LOW		= 4;
	
	////////////////////////////////////////////////////////////////////
	////2.0.0.2
	//	Enter DW Mode 후 2초 딜레이 추가 (Cluster Serial Flash Erase Time)
	////2.0.0.3
	// Requeset 후 응답 없을 시 1회 재요청
	// CTS, ACK 응답 없을 시 4회 재요청
	// Status Text 추가 
	// CAN Update Popup 종료 시 Thread도 강제 종료
	// CAN Update 문구 수정
	// Bootloader Status 표시 추가
	// OS Update 시 3초간 LCD Off CMD 추가
	////2.0.0.4
	// BKCU 업데이트 추가 
	////////////////////////////////////////////////////////////////////
	
	public static final int INDEX_MAIN_TOP								= 0X1100;
	
	public static final int INDEX_MONITOR_TOP							= 0X2100;
	public static final int INDEX_MONITOR_STM32_QUESTION				= 0X2110;
	public static final int INDEX_MONITOR_STM32_UPDATE					= 0X2111;
	public static final int INDEX_MONITOR_ANDROID_OS					= 0X2200;
	public static final int INDEX_MONITOR_ANDROID_OS_QUESTION			= 0X2210;
	
	public static final int INDEX_CLUSTER_TOP							= 0X3000;
	public static final int INDEX_CLUSTER_QUESTION						= 0X3110;
	public static final int INDEX_CLUSTER_UPDATE						= 0X3111;
	
	public static final int INDEX_MCU_TOP								= 0X4000;
	public static final int INDEX_MCU_QUESTION							= 0X4110;
	public static final int INDEX_MCU_UPDATE							= 0X4111;
	
	public static final int INDEX_BKCU_TOP								= 0X5000;
	public static final int INDEX_BKCU_QUESTION							= 0X5110;
	public static final int INDEX_BKCU_UPDATE							= 0X5111;
	
	public static final int CMD_DUMMY		= 0xF5;
	
	
	////////////////////////////////////////////////////////////////////
	
	///////////////////////////FRAGMENT/////////////////////////////////
	UpperFragment _UpperFragment;
	MainFragment _MainFragment;
	MonitorFragment _MonitorFragment;
	MonitorOSFragment _MonitorOSFragment;
	ClusterFragment _ClusterFragment;
	MCUFragment _MCUFragment;
	BKCUFragment _BKCUFragment;
	////////////////////////////////////////////////////////////////////
	///////////////////////////POPUP////////////////////////////////////
	UpdaetMonitorSTM32Popup.Builder MonitorSTM32Builder;
	UpdateQuestionMonitorSTM32Popup.Builder MonitorUpdateQuestionBuilder;
	////////////////////////////////////////////////////////////////////
	
	///////////////////////////VALUABLE/////////////////////////////////
	public int MenuIndex;
	
	// CAN1CommManager
	public static CAN1CommManager CAN1Comm;
	
	// DialogFlag
	public Dialog MenuDialog = null;
	
	Process mProcess = null;
	/////////////////////////////////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		InitResource();
		InitFragment();
		InitPopup();
		InitValuable();
		
		showUpper();
		showMain();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StartCommService();
		SetSU();
	}

	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		CAN1Comm.CloseComport();
		stopCommService();
	}

	public void InitResource(){
		
	}
	
	public void InitFragment(){
		_UpperFragment = new UpperFragment();
		_MainFragment = new MainFragment();
		_MonitorFragment = new MonitorFragment();
		_MonitorOSFragment = new MonitorOSFragment();
		_ClusterFragment = new ClusterFragment();
		_MCUFragment = new MCUFragment();
		_BKCUFragment = new BKCUFragment();
	}
	
	public void InitPopup(){
		MonitorSTM32Builder = new UpdaetMonitorSTM32Popup.Builder(this);
		MonitorUpdateQuestionBuilder = new UpdateQuestionMonitorSTM32Popup.Builder(this);
	}
	
	public void InitValuable(){
		MenuIndex = INDEX_MAIN_TOP;
		
	}
	
	
	//////////////////////////////////COMM.///////////////////////////////////////
	// Communication Service Start
	private void StartCommService() {
		Log.v(TAG,"Start Comm Service");
		Intent intent = new Intent(MainActivity.this,CommService.class);
		// Loacal Service
		startService(intent);
		bindService(new Intent(CommService.class.getName()),serConn,Context.BIND_AUTO_CREATE);
		
	}
	
	// Communication Service Stop
	private void stopCommService(){
		Log.v(TAG,"Stop Comm Service");
		unbindService(serConn);
		if(stopService(new Intent(MainActivity.this,CommService.class))){
			Log.v(TAG,"stopService was successful");
		}
		else{
			Log.v(TAG,"stopService was unsuccessful");
		}
		
		try {
			CAN1Comm.unregisterCallback(mCallback);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private ServiceConnection serConn = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			boolean Success;
			boolean temp = true;
			int CNT = 0;
			Log.v(TAG,"onServiceConnected() called");
			
			CAN1Comm = CAN1CommManager.getInstance();
			CAN1Comm.TxCMDToMCU(CAN1Comm.CMD_STARTCAN);
			
			
			try {
				Success = CAN1Comm.registerCallback(mCallback);
				for(int i = 0; i < 10; i++){
					CAN1Comm.TxCMDToMCU(CMD_DUMMY,0);		// DUMMY CMD
					Thread.sleep(100);
				}
				Log.d(TAG,"CallBack Success : " + Boolean.toString(Success));
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	
		}
	
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			Log.v(TAG,"onServiceDisconnected() called");
			
			StartCommService();
		}
	
	};
	
	// Service Callback
	ICAN1CommManagerCallback mCallback = new ICAN1CommManagerCallback.Stub() {
		
		@Override
		public void KeyButtonCallBack(int Data) throws RemoteException {
			// TODO Auto-generated method stub
			Log.i(TAG,"KeyButton Callback : 0x" + Integer.toHexString(Data));
			
		}
		
		@Override
		public void CallbackFunc(int Data) throws RemoteException {
			// TODO Auto-generated method stub
			Log.i(TAG,"Callback");
		}

		@Override
		public void UpdateResponseCallBack(int Data) throws RemoteException {
			// TODO Auto-generated method stub
			Log.i(TAG,"UpdateResponseCallBack : 0x" + Integer.toHexString(Data));
			RecResponse(Data);
		}
	};
	//////////////////////////////////////////////////////////////////////////////
	
	public void showUpper(){
		android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.FrameLayout_fragment_upper, _UpperFragment);
		transaction.commit();
	}
	
	public void showMain(){
		android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.FrameLayout_fragment_body, _MainFragment);
		transaction.commit();
	}
	
	public void showMonitor(){
		android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.FrameLayout_fragment_body, _MonitorFragment);
		transaction.commit();
	}
	public void showMonitorOS(){
		android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.FrameLayout_fragment_body, _MonitorOSFragment);
		transaction.commit();
	}
	
	
	public void showCluster(){
		android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.FrameLayout_fragment_body, _ClusterFragment);
		transaction.commit();
	}
	
	public void showMCU(){
		android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.FrameLayout_fragment_body, _MCUFragment);
		transaction.commit();
	}
	public void showBKCU(){
		android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.FrameLayout_fragment_body, _BKCUFragment);
		transaction.commit();
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////

	public void showMonitorUpdateQuestionPopup(){

		if(MenuDialog != null){
			MenuDialog.dismiss();
			MenuDialog = null;
		}
		

		MonitorUpdateQuestionBuilder.setOKButton(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.d(TAG,"setOKButton");
				
				dialog.dismiss();
				showMonitorSTM32UpdatePopup();
			}
		});
		MonitorUpdateQuestionBuilder.setCancelButton(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.d(TAG,"setCancelButton");				
				dialog.dismiss();
				
				
			}
		});
		MonitorUpdateQuestionBuilder.setDismiss(new DialogInterface.OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				Log.d(TAG,"onDismiss");
				

			}
		});
		
		MenuDialog = MonitorUpdateQuestionBuilder.create(getResources().getString(string.Do_you_want_update));
		MenuDialog.show();		
	}
	public void showMonitorFactoryInitUpdateQuestionPopup(){

		if(MenuDialog != null){
			MenuDialog.dismiss();
			MenuDialog = null;
		}
		

		MonitorUpdateQuestionBuilder.setOKButton(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.d(TAG,"setOKButton");
				
				dialog.dismiss();
				showMonitorSTM32FactoryInitUpdatePopup();
			}
		});
		MonitorUpdateQuestionBuilder.setCancelButton(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.d(TAG,"setCancelButton");				
				dialog.dismiss();
				
				
			}
		});
		MonitorUpdateQuestionBuilder.setDismiss(new DialogInterface.OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				Log.d(TAG,"onDismiss");
				

			}
		});
		
		MenuDialog = MonitorUpdateQuestionBuilder.create(getResources().getString(string.Do_you_want_update_factory_init));
		MenuDialog.show();		
	}
	
	public void showMonitorSTM32UpdatePopup(){

		if(MenuDialog != null){
			MenuDialog.dismiss();
			MenuDialog = null;
		}

		MonitorSTM32Builder.setDismiss(new DialogInterface.OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				Log.d(TAG,"onDismiss");
				

			}
		});
		
		MenuDialog = MonitorSTM32Builder.create(MonitorSTM32Builder,0);
		MenuDialog.show();		
	}
	
	public void showMonitorSTM32FactoryInitUpdatePopup(){

		if(MenuDialog != null){
			MenuDialog.dismiss();
			MenuDialog = null;
		}

		MonitorSTM32Builder.setDismiss(new DialogInterface.OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				Log.d(TAG,"onDismiss");
				

			}
		});
		
		MenuDialog = MonitorSTM32Builder.create(MonitorSTM32Builder,1);
		MenuDialog.show();		
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////
	
	public void RecResponse(int response){
		switch (MenuIndex) {
		case INDEX_MONITOR_STM32_UPDATE:
			MonitorSTM32Builder.recResponse(response);
			break;

		default:
			break;
		}
	}
	
	public void onClickBack(){
		switch (MenuIndex) {
		case INDEX_MAIN_TOP:
			//finish();
			break;
			
		case INDEX_MONITOR_TOP:
			showMain();
			break;
			
		case INDEX_CLUSTER_TOP:
			showMain();
			break;
			
		case INDEX_MCU_TOP:
			showMain();
			break;
			
		case INDEX_MONITOR_ANDROID_OS:
			showMonitor();
			break;

		default:
			break;
		}
	}
	
	public void SetSU(){
		try{
			mProcess =  Runtime.getRuntime().exec("su");
			Log.d(TAG,"Su");
			
		}catch(Exception e){
			Log.e(TAG,"Su Exception");
		}
	}
	
	public void Reboot(){
		Log.d(TAG,"Reboot");

		try {
            OutputStream os = mProcess.getOutputStream();
            String cmd = "/system/bin/reboot update";
            os.write(cmd.getBytes());
            os.flush();
            os.close();
            mProcess.waitFor();
        } catch ( Exception e) {
            Log.d(TAG, "rooting X");
        }   
	
	}
	

}
