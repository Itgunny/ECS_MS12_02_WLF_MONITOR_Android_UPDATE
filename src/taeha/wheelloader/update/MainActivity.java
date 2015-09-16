package taeha.wheelloader.update;

import java.io.File;
import java.lang.ref.WeakReference;

import taeha.wheelloader.update.R.string;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	/////////////////////////////CONSTANT////////////////////////////////
	private static final String TAG = "MainActivity";


	public static final int VERSION_HIGH		= 3;
	public static final int VERSION_LOW			= 1;
	public static final int VERSION_SUB_HIGH	= 0;
	//	public static final int VERSION_SUB_LOW		= 0;

	////////////////////////////////////////////////////////////////////
	////2.0.0.2
	//	Enter DW Mode 후 2초 딜레이 추가 (Cluster Serial Flash Erase Time)
	////2.0.0.3
	// Request 후 응답 없을 시 1회 재요청
	// CTS, ACK 응답 없을 시 4회 재요청
	// Status Text 추가 
	// CAN Update Popup 종료 시 Thread도 강제 종료
	// CAN Update 문구 수정
	// Bootloader Status 표시 추가
	// OS Update 시 3초간 LCD Off CMD 추가
	////2.0.0.4
	// BKCU 업데이트 추가 
	////2.2.0.0
	// Miracast 업데이트 추가
	// Update 프로그램 자체 업데이트 추가
	// PDF Copy 복사 기능 추가
	////2.3.0.0
	// MCU Update 방법 변경.
	////2.4.0.0
	// Can Update 실패시 0x0040 제거
	// 히든키 변경
	// OS 업데이트 추가
	// PDF 예외처리 추가
	// Update 버전 3자리로  변경
	////2.4.0 15.06.23
	// Icon 적용
	// pdf 파일 복사 버그 수정
	////2.5.0 15.06.30
	// sync 추가(파일복사)
	// Exit -> Cancel 변경
	// Update 폴더 없을 경우 뻗는 현상 개선	
	////2.6.0 15.07.15
	// Can Update 관련 프로그레스바 수정.
	////3.0.0 15.08.22
	// 1. CAN Update 1.3 적용
	//  - UPD Format 추가
	//	- 이전 버전 호환을 위해 UPD format 5초 time out 후 1회 재시도 -> pass 함
	//	- Cancel 버튼 파일전송 시에만 Enable
	//	- 사용자가 cancel 했을 경우 프로토콜 반영
	// 2. Enter DL Mode 후 2초 delay 삭제
	// 3. 파일 복사 후 Format 및 Copy 할 때 "Do not try to turn off the machine" 메시지 추가(HHI 임혁준씨 요청)
	// 4. MCU F/W Info 요청하여 F/W Model 우측 상단 항시 표기(HHI 임혁준씨 요청)
	////3.1.0 15.09.03
	// 1. CAN UPDATE RMCU 추가
	////3.1.0 15.09.15
	// 1. RMCU 업데이트 중 뻑나는 현상 개선
	// 2. Main List에 RMCU, BKCU 패킷 확인해서 Display(공유 데이터로 확인하는 부분 삭제)
	// 3. List 높이 293 -> 320으로 변경
	// 4. SendPacket부분 1024 고정 -> 1024 이하 되게 수정
	// 5. BKCU File 읽는 부분, 초기 시작부분 수정
	// 6. RMCU Update 파일 선택해서 업데이트
	// 7. 기존 F/W Info, UPD Format Start, Send new F/W Info, APP DL Start, APP DL Cancel 5초 4회 재시도
	// 8. 모델 요청 메인 페이지에서만 요청하도록 변경(벤치 테스트에서 MCU가 안달린 경우 다른 장비의 F/W Info를 요청함)
	////3.1.0 15.09.16
	// 1. 모니터 메뉴트리 변경
	//	- FW+APP + ETC
	//	- ETC : PDF, OS, APP, FW 등등
	//	- PDF 폴더 삭제 기능 추가
	////////////////////////////////////////////////////////////////////

	public static final int INDEX_MAIN_TOP								= 0X1100;

	public static final int INDEX_MONITOR_TOP							= 0X2100;
	public static final int INDEX_MONITOR_STM32_APP_QUESTION			= 0X2101;
	public static final int INDEX_MONITOR_STM32_APP_UPDATE				= 0X2102;
	public static final int INDEX_MONITOR_STM32_QUESTION				= 0X2110;
	public static final int INDEX_MONITOR_STM32_UPDATE					= 0X2111;
	public static final int INDEX_MONITOR_ANDROID_OS					= 0X2200;
	public static final int INDEX_MONITOR_ANDROID_OS_QUESTION			= 0X2210;
	public static final int INDEX_MONITOR_COPY_TO_USB					= 0X2220;
	public static final int INDEX_MONITOR_ETC							= 0X2230;

	public static final int INDEX_CLUSTER_TOP							= 0X3000;
	public static final int INDEX_CLUSTER_QUESTION						= 0X3110;
	public static final int INDEX_CLUSTER_UPDATE						= 0X3111;

	public static final int INDEX_MCU_TOP								= 0X4000;
	public static final int INDEX_MCU_SELECT							= 0X4100;
	public static final int INDEX_MCU_QUESTION							= 0X4110;
	public static final int INDEX_MCU_UPDATE							= 0X4111;

	public static final int INDEX_BKCU_TOP								= 0X5000;
	public static final int INDEX_BKCU_QUESTION							= 0X5110;
	public static final int INDEX_BKCU_UPDATE							= 0X5111;

	public static final int INDEX_RMCU_TOP								= 0x6000;
	public static final int INDEX_RMCU_SELECT							= 0x6100;
	public static final int INDEX_RMCU_QUESTION							= 0x6110;
	public static final int INDEX_RMCU_UPDATE							= 0x6111;

	public static final int CMD_DUMMY		= 0xF5;

	public static boolean isDisConnected 				= true;
	public int countCopy = 0;
	////////////////////////////////////////////////////////////////////

	///////////////////////////FRAGMENT/////////////////////////////////
	UpperFragment 		_UpperFragment;
	MainFragment 		_MainFragment;
	MonitorFragment 	_MonitorFragment;
	MonitorOSFragment 	_MonitorOSFragment;
	ClusterFragment 	_ClusterFragment;
	MCUFragment 		_MCUFragment;
	MCUListFragment 	_MCUListFragment; // ++, --, cjg 150601
	BKCUFragment 		_BKCUFragment;
	RMCUFragment		_RMCUFragment;
	RMCUListFragment    _RMCUListFragment;
	EtcFragment			_EtcFragment;
	////////////////////////////////////////////////////////////////////
	///////////////////////////POPUP////////////////////////////////////
	UpdateMonitorSTM32AndAppPopup.Builder MonitorSTM32AndAppBuilder;
	UpdateQuestionMonitorSTM32AndAppPopup.Builder MonitorSTM32AndAppQuestionBuilder;
	UpdateQuestionMonitorAppPopup.Builder MonitorAppQuestionBuilder;
	UpdaetMonitorSTM32Popup.Builder MonitorSTM32Builder;
	UpdateQuestionMonitorSTM32Popup.Builder MonitorUpdateQuestionBuilder;
	MonitorCopyErrorToUSB.Builder MonitorCopyErrorToUSBBuilder;
	////////////////////////////////////////////////////////////////////
	Handler HandleKeyButton;
	///////////////////////////VALUABLE/////////////////////////////////
	public int MenuIndex;

	// CAN1CommManager
	public static CAN1CommManager CAN1Comm = null;
	
	// Thread
	private Thread threadRead = null;

	// DialogFlag
	public Dialog MenuDialog = null;

	Process mProcess = null;
	
	byte []FWModel = new byte[20];
	public String strFMModel;
	int RetryCount;
	
	public boolean CheckBKCU=false;
	public boolean CheckRMCU=false;

	/////////////////////////////////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		InitResource();
		InitFragment();
		InitPopup();
		InitValuable();

		isConnectedUsb();
		showUpper();
		showMain();

		// ++, 150326 cjg	
		HandleKeyButton = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == CAN1CommManager.LONG_8_9){
					if(MonitorCopyErrorToUSBBuilder.isConnectedUsb() == true){
						try{
							if(countCopy == 0){
								showMonitorCopyFileToUSBPopup();
								countCopy = 1;
								// ++, 150630 cjg	
								Runtime runtime = Runtime.getRuntime();
								Process process;
								try{
									String cmd = "sync";
									process = runtime.exec(cmd);
									Log.d(TAG, "sync");
								}catch(Exception e){
									e.fillInStackTrace();
								}
								// --, 150630 cjg	
							}

						}catch(Exception e){
							e.printStackTrace();
							//Toast.makeText(getApplicationContext(), "Please Connect USB to device.", 50).show();
						}
					}else{
						//Toast.makeText(getApplicationContext(), "Please Connect USB to device.", 50).show();
					}
				}
			}
		};
		IntentFilter f = new IntentFilter();
		f.addAction(Intent.ACTION_MEDIA_MOUNTED);
		f.addAction(Intent.ACTION_MEDIA_EJECT);
		f.addDataScheme("file");
		registerReceiver(mUsbBroadcastReceiver, f);
		// --, 150326 cjg
	}
	// ++, 150326 cjg
	private BroadcastReceiver mUsbBroadcastReceiver = new BroadcastReceiver(){
		public static final String TAG = "UsbBroadcastReceiver";
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if(action.equals(Intent.ACTION_MEDIA_MOUNTED)){
				Toast.makeText(getApplicationContext(), "USB is connected.", 500).show();
				isDisConnected = false;
			}else if(action.equals(Intent.ACTION_MEDIA_EJECT)){
				Toast.makeText(getApplicationContext(), "USB is disconnected.", 500).show();
				isDisConnected = true;
			}
		}
	};
	public static void isConnectedUsb(){
		File file = new File("/mnt/usb");
		if(file != null){
			if(file.length() > 0){
				Log.d(TAG, "Connected");
				isDisConnected = false;
			}else{
				Log.d(TAG, "disConnected");
				isDisConnected = true;
			}
		}
	}
	// --, 150326 cjg
	// ++, 150601 cjg
	public boolean getisDisConnected(){
		return isDisConnected;
	}
	// --, 150601 cjg

	// ++, 150326 cjg
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		// ++, 150326 cjg
		unregisterReceiver(mUsbBroadcastReceiver);
		// --, 150326 cjg
	}
	// --, 150326 cjg
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
		threadRead = new Thread(new ReadThread(this));
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
		_MCUListFragment = new MCUListFragment();// ++, --, 150601 cjg
		_BKCUFragment = new BKCUFragment();
		_RMCUFragment = new RMCUFragment();
		_RMCUListFragment = new RMCUListFragment();
		_EtcFragment = new EtcFragment();
	}

	public void InitPopup(){
		MonitorSTM32AndAppBuilder = new UpdateMonitorSTM32AndAppPopup.Builder(this);
		MonitorSTM32AndAppQuestionBuilder = new UpdateQuestionMonitorSTM32AndAppPopup.Builder(this);
		MonitorSTM32Builder = new UpdaetMonitorSTM32Popup.Builder(this);
		MonitorUpdateQuestionBuilder = new UpdateQuestionMonitorSTM32Popup.Builder(this);
		MonitorCopyErrorToUSBBuilder = new MonitorCopyErrorToUSB.Builder(this);
		MonitorAppQuestionBuilder = new UpdateQuestionMonitorAppPopup.Builder(this);
	}

	public void InitValuable(){
		MenuIndex = INDEX_MAIN_TOP;
		strFMModel = "";
		RetryCount = 0;
		CheckBKCU = false;
		CheckRMCU = false;
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
		threadRead.interrupt();
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
			
			threadRead.start();

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
			if(Data == CAN1CommManager.LONG_8_9){
				if(MenuIndex == INDEX_MONITOR_TOP){
					HandleKeyButton.sendMessage(HandleKeyButton.obtainMessage(Data));
				}
			}
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
	/////////////////////////////////////////////////////////////////////
	// Thread Class
		public  class ReadThread implements Runnable {
			private WeakReference<MainActivity> activityRef = null;
			public Message msg = null;
			public ReadThread(MainActivity activity){
				this.activityRef = new WeakReference<MainActivity>(activity);
				msg = new Message();
			}

			
			@Override
			public void run() {
				try{
					while(!activityRef.get().threadRead.currentThread().isInterrupted())
					{
						activityRef.get().SetDataFromNative();
						Thread.sleep(1000);
						activityRef.get().GetDataFromNative();
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


		public void SetDataFromNative(){
			if(MenuIndex == INDEX_MAIN_TOP){
				if(strFMModel == "" && RetryCount < 5){
					CAN1Comm.Set_TargetSourceAddress(CAN1CommManager.SA_MCU);
					CAN1Comm.Set_FWID_TX_REQUEST_FW_N_INFO_61184_250_32(0);
					CAN1Comm.TxCANToMCU(0x20);
					RetryCount++;
				}
			}
		}
		
		public void GetDataFromNative(){
			if((CAN1Comm.Get_TargetSourceAddress() == CAN1CommManager.SA_MCU) && (CAN1Comm.Get_nRecvFWInfoFlag_61184_250_48() == 1)){
				CAN1Comm.Set_nRecvFWInfoFlag_61184_250_48(0);
				FWModel = CAN1Comm.Get_FirmwareInformation_FWModel_RX_SEND_FW_N_INFO_61184_250_48();
				strFMModel = new String(FWModel,0,FWModel.length);
				Log.d(TAG, "strFMModel"+strFMModel+"Length"+strFMModel.length());
				if(FWModel[0] == 0xff || FWModel[0] == 0)
					strFMModel = "";
			}
			if(CAN1Comm.Get_CheckBKCUComm()==1)
				CheckBKCU = true;
			if(CAN1Comm.Get_CheckRMCUComm()==1)
				CheckRMCU = true;
		}

	/////////////////////////////////////////////////////////////////////
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

	public void showMCUList(){
		android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.FrameLayout_fragment_body, _MCUListFragment);
		transaction.commit();
	}

	public void showMCUSelect(){
		android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.FrameLayout_fragment_body, _MCUFragment);
		transaction.commit();
	}

	public void showBKCU(){
		android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.FrameLayout_fragment_body, _BKCUFragment);
		transaction.commit();
	}

	public void showRMCUSelect(){
		android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.FrameLayout_fragment_body, _RMCUFragment);
		transaction.commit();
	}
	public void showRMCUList(){
		android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.FrameLayout_fragment_body, _RMCUListFragment);
		transaction.commit();
	}
	public void showEtc(){
		android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.FrameLayout_fragment_body, _EtcFragment);
		transaction.commit();
	}
	//////////////////////////////////////////////////////////////////////////////////////
	public void showMonitorUpdateQuestionPopup(int flag){//public void showMonitorUpdateQuestionPopup(){
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
		if(flag == 0){
			MenuDialog = MonitorUpdateQuestionBuilder.create(getResources().getString(string.Do_you_want_update_firmware));
			MenuIndex = INDEX_MONITOR_ETC;
		}else if(flag == 1){
			MenuDialog = MonitorUpdateQuestionBuilder.create(getResources().getString(string.Do_you_want_update_only_firmware));
			MenuIndex = INDEX_MONITOR_TOP;
		}
		MenuDialog.show();		
	}
	public void showMonitorAppQuestionPopup(final int flag){
		if(MenuDialog != null){
			MenuDialog.dismiss();
			MenuDialog = null;
		}


		MonitorAppQuestionBuilder.setOKButton(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.d(TAG,"setOKButton");
				UpdateFileFindClass UpdateFile;
				UpdateFile = new UpdateFileFindClass(MainActivity.this);
				if(flag == 0){
					MenuIndex = INDEX_MONITOR_TOP;
					if(UpdateFile.GetMonitorVersion() != null)
						UpdateFile.MonitorAndroidAppUpdate();
				}else if(flag == 1){
					MenuIndex = INDEX_MONITOR_ETC;
					if(UpdateFile.GetUpdateProgramVersion() != null){
						UpdateFile.MonitorUpdateProgramUpdate();
					}
				}else if(flag == 2){
					MenuIndex = INDEX_MONITOR_ETC;
					if(UpdateFile.GetMiracastVersion() != null){
						UpdateFile.MonitorMiracastAppUpdate();
					}
				}else if(flag == 3){
					MenuIndex = INDEX_MONITOR_ETC;
					if(UpdateFile.GetMonitorVersion() != null)
						UpdateFile.MonitorAndroidAppUpdate();
				}
				dialog.dismiss();
			}
		});
		MonitorAppQuestionBuilder.setCancelButton(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.d(TAG,"setCancelButton");
				if(flag == 0){
					MenuIndex = INDEX_MONITOR_TOP;
					
				}else if(flag == 1){
					MenuIndex = INDEX_MONITOR_ETC;

				}else if(flag == 2){
					MenuIndex = INDEX_MONITOR_ETC;

				}else if(flag == 3){
					MenuIndex = INDEX_MONITOR_ETC;
				}
				dialog.dismiss();
			}
		});
		MonitorAppQuestionBuilder.setDismiss(new DialogInterface.OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				Log.d(TAG,"onDismiss");
				if(flag == 0){
					MenuIndex = INDEX_MONITOR_TOP;
					
				}else if(flag == 1){
					MenuIndex = INDEX_MONITOR_ETC;

				}else if(flag == 2){
					MenuIndex = INDEX_MONITOR_ETC;

				}else if(flag == 3){
					MenuIndex = INDEX_MONITOR_ETC;
				}
			}
		});
		//Application
		if(flag == 0){
			MenuDialog = MonitorAppQuestionBuilder.create(getResources().getString(string.Do_you_want_update_only_app));	
		}else if(flag == 1){
			MenuDialog = MonitorAppQuestionBuilder.create(getResources().getString(string.Do_you_want_update_update));
		}else if(flag == 2){
			MenuDialog = MonitorAppQuestionBuilder.create(getResources().getString(string.Do_you_want_update_smartterminal));
		}else if(flag == 3){
			MenuDialog = MonitorAppQuestionBuilder.create(getResources().getString(string.Do_you_want_update_app));
		}
		
		MenuDialog.show();		
	}
	//150914, cjg ++
	public void showMonitorSTM32AndAppQuestionPopup(){
		if(MenuDialog != null){
			MenuDialog.dismiss();
			MenuDialog = null;
		}


		MonitorSTM32AndAppQuestionBuilder.setOKButton(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.d(TAG,"setOKButton");

				dialog.dismiss();
				showMonitorSTM32AndAppUpdatePopup();
			}
		});
		MonitorSTM32AndAppQuestionBuilder.setCancelButton(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.d(TAG,"setCancelButton");				
				dialog.dismiss();
			}
		});
		MonitorSTM32AndAppQuestionBuilder.setDismiss(new DialogInterface.OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				Log.d(TAG,"onDismiss");


			}
		});

		MenuDialog = MonitorSTM32AndAppQuestionBuilder.create(getResources().getString(string.Do_you_want_update_firmware_and_app));
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
	public void showMonitorSTM32AndAppUpdatePopup(){

		if(MenuDialog != null){
			MenuDialog.dismiss();
			MenuDialog = null;
		}

		MonitorSTM32AndAppBuilder.setDismiss(new DialogInterface.OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				Log.d(TAG,"onDismiss");


			}
		});
		MenuDialog = MonitorSTM32AndAppBuilder.create(MonitorSTM32AndAppBuilder,0);
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

	public void showMonitorCopyFileToUSBPopup(){

		if(MenuDialog != null){
			MenuDialog.dismiss();
			MenuDialog = null;
		}
		MonitorCopyErrorToUSBBuilder.setDismiss(new DialogInterface.OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				Log.d(TAG,"onDismiss");


			}
		});
		MenuDialog = MonitorCopyErrorToUSBBuilder.create(MonitorCopyErrorToUSBBuilder);
		MenuDialog.show();		
	}
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////

	public void RecResponse(int response){
		switch (MenuIndex) {
		case INDEX_MONITOR_STM32_UPDATE:
			MonitorSTM32Builder.recResponse(response);
			break;
		case INDEX_MONITOR_STM32_APP_UPDATE:
			MonitorSTM32AndAppBuilder.recResponse(response);

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
			_UpperFragment.setButtonInvisible(View.INVISIBLE); // ++, -- 150326 cjg
			break;

		case INDEX_CLUSTER_TOP:
			showMain();
			_UpperFragment.setButtonInvisible(View.INVISIBLE); // ++, -- 150326 cjg
			break;

		case INDEX_MCU_TOP:
			_UpperFragment.setButtonInvisible(View.INVISIBLE); // ++, -- 150326 cjg
			showMain();
			break;

		case INDEX_BKCU_TOP:
			_UpperFragment.setButtonInvisible(View.INVISIBLE); // ++, -- 150326 cjg
			showMain();
			break;
			
		case INDEX_RMCU_TOP:
			_UpperFragment.setButtonInvisible(View.INVISIBLE); 
			showMain();
			break;

		case INDEX_MCU_SELECT:
			// ++, -- 150326 cjg
			if(getisDisConnected() == true){
				showMain();
				_UpperFragment.setButtonInvisible(View.INVISIBLE);
			}else{
				showMCUList();
			}
			break;
		case INDEX_RMCU_SELECT:
			if(getisDisConnected() == true){
				showMain();
				_UpperFragment.setButtonInvisible(View.INVISIBLE);
			}else{
				showRMCUList();
			}
			break;
		case INDEX_MONITOR_ANDROID_OS:
			showEtc();
			break;
		case INDEX_MONITOR_ETC:
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

	/*public void Reboot(){
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

	}*/

}
