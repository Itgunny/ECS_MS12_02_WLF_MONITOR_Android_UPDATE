package taeha.wheelloader.update;

import java.io.FileDescriptor;


import android.app.Instrumentation;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;

public class CommService extends Service{

	/////////////////VALUABLES///////////////////////////////////////////
	// TAG
	private static final String TAG = "CommService";
	// COMPOART
	private static final int UART1Baudrate = 115200;
	private static final String UART1ComPort = "/dev/ttySAC1";
	private FileDescriptor mFdUART1;
	private static final int UART3Baudrate = 115200;
	private static final String UART3ComPort = "/dev/ttySAC3";
	private FileDescriptor mFdUART3;
	
	// SOUND
	private static SoundPool SoundPoolKeyButton;
	static int	SoundID;
	static float fVolume;
	private static SoundPool SoundPoolKeyButtonEnding;
	static int	SoundIDEnding;
	static float fVolumeEnding;
	// Comm Manager
	static CAN1CommManager CAN1Comm;
	
	// BindFlag
	private static boolean BindFlag = false;
	
	// My App Top Flag
	private static boolean ScreenTopFlag = true;
	
	private static final int MENU 				= 0x41;
	private static final int ESC 				= 0x42;
	private static final int LEFT 				= 0x44;
	private static final int RIGHT 				= 0x48;
	private static final int ENTER 				= 0x50;
	private static final int CAMERA 			= 0x60;
	
	private static final int MAINLIGHT 			= 0x81;
	private static final int WORKLIGHT 			= 0x82;
	private static final int AUTO_GREASE 		= 0x84;
	private static final int QUICK_COUPLER 		= 0x88;
	private static final int RIDE_CONTROL 		= 0x90;
	private static final int WORK_LOAD 			= 0xA0;
	
	private static final int BEACON_LAMP 		= 0xC1;
	private static final int REAR_WIPER 		= 0xC2;
	private static final int MIRROR_HEAT 		= 0xC4;
	private static final int AUTOPOSITION 		= 0xC8;
	private static final int FINEMODULATION		= 0xD0;
	private static final int FN		 			= 0xE0;


	private final int MENU_LEFT = 0X45;
	private final int MENU_RIGHT = 0X49;
	private final int MENU_ENTER = 0X51;
	private final int LEFT_RIGHT = 0X4C;
	private final int LEFT_RIGHT_ENTER = 0X5C;

	private final int LONG_MENU = 0x61;
	private final int LONG_ESC = 0x62;
	private final int LONG_LEFT = 0X64;
	private final int LONG_RIGHT = 0X68;
	private final int LONG_ENTER = 0x70;
	
	private final int LONG_MENU_LEFT = 0x65;
	private final int LONG_MENU_RIGHT = 0x69;
	private final int LONG_MENU_ENTER = 0x71;
	
	private final int LONG_LEFT_RIGHT = 0X6C;
	private final int LONG_LEFT_RIGHT_ENTER = 0x7C;

	
	private static final int POWER_OFF = 0xF5;
	
	
	/////////////////////////////////////////////////////////////////////
	
	//////////////////LOAD NATIVELIBRARY/////////////////////////////////
	static{
		try {
			System.loadLibrary("can_serial_port");
			System.loadLibrary("can_data_parsing");
		} catch (Throwable t) {
			// TODO: handle exception
			Log.e(TAG,"Load Library Error");
		}
		
	}
	/////////////////////////////////////////////////////////////////////
	//////////////////NATIVE METHOD/////////////////////////////////////
	public native FileDescriptor Open_UART1(String path, int baudrate, int flag);
	public native void Close_UART1();				
	public native int Write_UART1(byte[] Data, int size);
	public native int UART1_TxComm(int PS);
	public native FileDescriptor Open_UART3(String path, int baudrate, int flag);
	public native void Close_UART3();				// UART 1¸¸ close µÊ
	public native int Write_UART3(byte[] Data, int size);
	public native int UART3_TxComm(int CMD, int DAT1, int DAT2, int DAT3, int DAT4, int DAT5, int DAT6, int DAT7, int DAT8);
	public native int UART3_UpdatePacketComm(int Index, int CRC, int EOTFlag, byte[] arr);

	public native void Set_nRecvSendBootloaderStatusFlag_61184_250_17(int Data);
	public native int Get_nRecvSendBootloaderStatusFlag_61184_250_17();
	public native void Set_MultiPacketErrFlag(int Data);
	public native int Get_MultiPacketErrFlag();
	public native void Set_TargetSourceAddress(int Data);
	public native void Set_nLength_61184_250_66(int Data);
	public native void Set_nLength_61184_250_81(int Data);
	public native int Get_nRecAckNewFWNInfoFlag_61184_250_82();
	public native int Get_nRecvFWInfoFlag_61184_250_48();
	public native int Get_nRecvRequestPacketMFlag_61184_250_83();
	public native int Get_nRecvFWDLCompleteFlag_61184_250_80();
	public native int Get_nRecvFWUpdateCompleteFlag_61184_250_112();	
	public native void Set_nRecAckNewFWNInfoFlag_61184_250_82(int Data);
	public native void Set_nRecvFWInfoFlag_61184_250_48(int Data);
	public native void Set_nRecvRequestPacketMFlag_61184_250_83(int Data);
	public native void Set_nRecvFWDLCompleteFlag_61184_250_80(int Data);
	public native void Set_nRecvFWUpdateCompleteFlag_61184_250_112(int Data);
	/////////////////////////////Set////////////////////////////////////////
	public native void Set_FWID_TX_REQUEST_FW_N_INFO_61184_250_32(int Data);
//	public native void Set_FirmwareInformation_SlaveID_TX_ENTER_DL_MODE_61184_250_65(int Data);
//	public native void Set_FirmwareInformation_FWID_TX_ENTER_DL_MODE_61184_250_65(int Data);
//	public native void Set_FirmwareInformation_FWName_TX_ENTER_DL_MODE_61184_250_65(byte[] Data);
//	public native void Set_FirmwareInformation_FWModel_TX_ENTER_DL_MODE_61184_250_65(byte[] Data);
//	public native void Set_FirmwareInformation_FWVersion_TX_ENTER_DL_MODE_61184_250_65(byte[] Data);
//	public native void Set_FirmwareInformation_ProtoType_TX_ENTER_DL_MODE_61184_250_65(byte[] Data);
//	public native void Set_FirmwareInformation_FWChangeDay_TX_ENTER_DL_MODE_61184_250_65(byte[] Data);
//	public native void Set_FirmwareInformation_FWChangeTime_TX_ENTER_DL_MODE_61184_250_65(byte[] Data);
//	public native void Set_FirmwareInformation_FWFileSize_TX_ENTER_DL_MODE_61184_250_65(int Data);
//	public native void Set_FirmwareInformation_SectionUnitSize_TX_ENTER_DL_MODE_61184_250_65(int Data);
//	public native void Set_FirmwareInformation_PacketUnitSize_TX_ENTER_DL_MODE_61184_250_65(int Data);
//	public native void Set_FirmwareInformation_NumberofSectioninaFile_TX_ENTER_DL_MODE_61184_250_65(int Data);
//	public native void Set_FirmwareInformation_NumberofPacketinaSection_TX_ENTER_DL_MODE_61184_250_65(int Data);
//	public native void Set_FirmwareInformation_AppStartAddress_TX_ENTER_DL_MODE_61184_250_65(int Data);
//	public native void Set_FirmwareInformation_CRC_TX_ENTER_DL_MODE_61184_250_65(int Index, int Data);
	public native void Set_FirmwareInformation_SlaveID_TX_SEND_NEW_FW_N_INFO_61184_250_66(int Data);
	public native void Set_FirmwareInformation_FWID_TX_SEND_NEW_FW_N_INFO_61184_250_66(int Data);
	public native void Set_FirmwareInformation_FWName_TX_SEND_NEW_FW_N_INFO_61184_250_66(byte[] Data);
	public native void Set_FirmwareInformation_FWModel_TX_SEND_NEW_FW_N_INFO_61184_250_66(byte[] Data);
	public native void Set_FirmwareInformation_FWVersion_TX_SEND_NEW_FW_N_INFO_61184_250_66(byte[] Data);
	public native void Set_FirmwareInformation_ProtoType_TX_SEND_NEW_FW_N_INFO_61184_250_66(byte[] Data);
	public native void Set_FirmwareInformation_FWChangeDay_TX_SEND_NEW_FW_N_INFO_61184_250_66(byte[] Data);
	public native void Set_FirmwareInformation_FWChangeTime_TX_SEND_NEW_FW_N_INFO_61184_250_66(byte[] Data);
	public native void Set_FirmwareInformation_FWFileSize_TX_SEND_NEW_FW_N_INFO_61184_250_66(int Data);
	public native void Set_FirmwareInformation_SectionUnitSize_TX_SEND_NEW_FW_N_INFO_61184_250_66(int Data);
	public native void Set_FirmwareInformation_PacketUnitSize_TX_SEND_NEW_FW_N_INFO_61184_250_66(int Data);
	public native void Set_FirmwareInformation_NumberofSectioninaFile_TX_SEND_NEW_FW_N_INFO_61184_250_66(int Data);
	public native void Set_FirmwareInformation_NumberofPacketinaSection_TX_SEND_NEW_FW_N_INFO_61184_250_66(int Data);
	public native void Set_FirmwareInformation_AppStartAddress_TX_SEND_NEW_FW_N_INFO_61184_250_66(int Data);
	public native void Set_FirmwareInformation_CRC_TX_SEND_NEW_FW_N_INFO_61184_250_66(int Index, int Data);
	public native void Set_SectionNumber_TX_SEND_PACKET_M_61184_250_67(int Data);
	public native void Set_PacketNumber_TX_SEND_PACKET_M_61184_250_67(int Data);
	public native void Set_PacketLength_TX_SEND_PACKET_M_61184_250_67(int Data);
	public native void Set_DistributionFileData_TX_SEND_PACKET_M_61184_250_67(byte[] Data);
	public native void Set_PacketCRC_TX_SEND_PACKET_M_61184_250_67(int Data);
//	public native void Set_FirmwareInformation_SlaveID_TX_QUIT_DL_MODE_61184_250_81(int Data);
//	public native void Set_FirmwareInformation_FWID_TX_QUIT_DL_MODE_61184_250_81(int Data);
//	public native void Set_FirmwareInformation_FWName_TX_QUIT_DL_MODE_61184_250_81(byte[] Data);
//	public native void Set_FirmwareInformation_FWModel_TX_QUIT_DL_MODE_61184_250_81(byte[] Data);
//	public native void Set_FirmwareInformation_FWVersion_TX_QUIT_DL_MODE_61184_250_81(byte[] Data);
//	public native void Set_FirmwareInformation_ProtoType_TX_QUIT_DL_MODE_61184_250_81(byte[] Data);
//	public native void Set_FirmwareInformation_FWChangeDay_TX_QUIT_DL_MODE_61184_250_81(byte[] Data);
//	public native void Set_FirmwareInformation_FWChangeTime_TX_QUIT_DL_MODE_61184_250_81(byte[] Data);
//	public native void Set_FirmwareInformation_FWFileSize_TX_QUIT_DL_MODE_61184_250_81(int Data);
//	public native void Set_FirmwareInformation_SectionUnitSize_TX_QUIT_DL_MODE_61184_250_81(int Data);
//	public native void Set_FirmwareInformation_PacketUnitSize_TX_QUIT_DL_MODE_61184_250_81(int Data);
//	public native void Set_FirmwareInformation_NumberofSectioninaFile_TX_QUIT_DL_MODE_61184_250_81(int Data);
//	public native void Set_FirmwareInformation_NumberofPacketinaSection_TX_QUIT_DL_MODE_61184_250_81(int Data);
//	public native void Set_FirmwareInformation_AppStartAddress_TX_QUIT_DL_MODE_61184_250_81(int Data);
//	public native void Set_FirmwareInformation_CRC_TX_QUIT_DL_MODE_61184_250_81(int Index, int Data);

	/////////////////////////////Get////////////////////////////////////////

	public native int Get_ResultCPUCRC_RX_SEND_BOOTLOADER_STATUS_61184_250_17();
	public native int Get_SlaveInformation_SlaveID_RX_SEND_SLAVE_INFO_61184_250_49();
	public native int Get_SlaveInformation_SlaveName_RX_SEND_SLAVE_INFO_61184_250_49();
	public native byte[] Get_SlaveInformation_SerialNumber_RX_SEND_SLAVE_INFO_61184_250_49();
	public native byte[] Get_SlaveInformation_HWVersion_RX_SEND_SLAVE_INFO_61184_250_49();
	public native byte[] Get_SlaveInformation_ProductDate_RX_SEND_SLAVE_INFO_61184_250_49();
	public native byte[] Get_SlaveInformation_DeliveryDate_RX_SEND_SLAVE_INFO_61184_250_49();
	public native int Get_SlaveInformation_NumberofAPP_RX_SEND_SLAVE_INFO_61184_250_49();
	public native int Get_SlaveInformation_App1StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49();
	public native int Get_SlaveInformation_App1Size_RX_SEND_SLAVE_INFO_61184_250_49();
	public native int Get_SlaveInformation_App2StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49();
	public native int Get_SlaveInformation_App2Size_RX_SEND_SLAVE_INFO_61184_250_49();
	public native int Get_SlaveInformation_App3StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49();
	public native int Get_SlaveInformation_App3Size_RX_SEND_SLAVE_INFO_61184_250_49();
	public native int Get_SlaveInformation_App4StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49();
	public native int Get_SlaveInformation_App4Size_RX_SEND_SLAVE_INFO_61184_250_49();
	public native int Get_SlaveInformation_App5StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49();
	public native int Get_SlaveInformation_App5Size_RX_SEND_SLAVE_INFO_61184_250_49();
	public native int Get_SlaveInformation_App6StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49();
	public native int Get_SlaveInformation_App6Size_RX_SEND_SLAVE_INFO_61184_250_49();
	public native int Get_SlaveInformation_App7StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49();
	public native int Get_SlaveInformation_App7Size_RX_SEND_SLAVE_INFO_61184_250_49();
	public native int Get_SlaveInformation_App8StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49();
	public native int Get_SlaveInformation_App8Size_RX_SEND_SLAVE_INFO_61184_250_49();
	public native int Get_SlaveInformation_App9StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49();
	public native int Get_SlaveInformation_App9Size_RX_SEND_SLAVE_INFO_61184_250_49();
	public native int Get_SlaveInformation_App10StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49();
	public native int Get_SlaveInformation_App10Size_RX_SEND_SLAVE_INFO_61184_250_49();
	public native int Get_FirmwareInformation_SlaveID_RX_SEND_FW_N_INFO_61184_250_48();
	public native int Get_FirmwareInformation_FWID_RX_SEND_FW_N_INFO_61184_250_48();
	public native byte[] Get_FirmwareInformation_FWName_RX_SEND_FW_N_INFO_61184_250_48();
	public native byte[] Get_FirmwareInformation_FWModel_RX_SEND_FW_N_INFO_61184_250_48();
	public native byte[] Get_FirmwareInformation_FWVersion_RX_SEND_FW_N_INFO_61184_250_48();
	public native byte[] Get_FirmwareInformation_ProtoType_RX_SEND_FW_N_INFO_61184_250_48();
	public native byte[] Get_FirmwareInformation_FWChangeDay_RX_SEND_FW_N_INFO_61184_250_48();
	public native byte[] Get_FirmwareInformation_FWChangeTime_RX_SEND_FW_N_INFO_61184_250_48();
	public native int Get_FirmwareInformation_FWFileSize_RX_SEND_FW_N_INFO_61184_250_48();
	public native int Get_FirmwareInformation_SectionUnitSize_RX_SEND_FW_N_INFO_61184_250_48();
	public native int Get_FirmwareInformation_PacketUnitSize_RX_SEND_FW_N_INFO_61184_250_48();
	public native int Get_FirmwareInformation_NumberofSectioninaFile_RX_SEND_FW_N_INFO_61184_250_48();
	public native int Get_FirmwareInformation_NumberofPacketinaSection_RX_SEND_FW_N_INFO_61184_250_48();
	public native int Get_FirmwareInformation_AppStartAddress_RX_SEND_FW_N_INFO_61184_250_48();
	public native int Get_FWID_RX_REQUEST_PACKET_M_61184_250_83();
	public native int Get_SectionNumber_RX_REQUEST_PACKET_M_61184_250_83();
	public native int Get_PacketNumber_RX_REQUEST_PACKET_M_61184_250_83();
	public native int Get_ResultFlashCRC_RX_FW_N_DL_COMPLETE_61184_250_80();
	public native int Get_Status_RX_FW_UPDATE_STATUS_61184_250_113();
	public native int Get_Progress_ResultFlashCRC_RX_FW_UPDATE_STATUS_61184_250_113();
	public native int Get_ResultFlashCRC_RX_FW_UPDATE_COMPLETE_61184_250_112();
	public native int Get_FirmwareInformation_SlaveID_RX_FW_UPDATE_COMPLETE_61184_250_112();
	public native int Get_FirmwareInformation_FWID_RX_FW_UPDATE_COMPLETE_61184_250_112();
	public native byte[] Get_FirmwareInformation_FWName_RX_FW_UPDATE_COMPLETE_61184_250_112();
	public native byte[] Get_FirmwareInformation_FWModel_RX_FW_UPDATE_COMPLETE_61184_250_112();
	public native byte[] Get_FirmwareInformation_FWVersion_RX_FW_UPDATE_COMPLETE_61184_250_112();
	public native byte[] Get_FirmwareInformation_ProtoType_RX_FW_UPDATE_COMPLETE_61184_250_112();
	public native byte[] Get_FirmwareInformation_FWChangeDay_RX_FW_UPDATE_COMPLETE_61184_250_112();
	public native byte[] Get_FirmwareInformation_FWChangeTime_RX_FW_UPDATE_COMPLETE_61184_250_112();
	public native int Get_FirmwareInformation_FWFileSize_RX_FW_UPDATE_COMPLETE_61184_250_112();
	public native int Get_FirmwareInformation_SectionUnitSize_RX_FW_UPDATE_COMPLETE_61184_250_112();
	public native int Get_FirmwareInformation_PacketUnitSize_RX_FW_UPDATE_COMPLETE_61184_250_112();
	public native int Get_FirmwareInformation_NumberofSectioninaFile_RX_FW_UPDATE_COMPLETE_61184_250_112();
	public native int Get_FirmwareInformation_NumberofPacketinaSection_RX_FW_UPDATE_COMPLETE_61184_250_112();
	public native int Get_FirmwareInformation_AppStartAddress_RX_FW_UPDATE_COMPLETE_61184_250_112();

	///////////////USER DEFINE METHOD///////////////////////////////////////
	// Open Comport
	public void InitComport(){
		Log.v(TAG,"InitComport");		
		mFdUART1 = Open_UART1(UART1ComPort,UART1Baudrate,0);
		mFdUART3 = Open_UART3(UART3ComPort,UART3Baudrate,0);
	}
	// close Comport
	public void CloseComport(){
		Log.v(TAG,"Closecomport");
		Close_UART1();
		Close_UART3();
	}
	// Sound Init
	private void InitSound(){
		SoundPoolKeyButton = new SoundPool(1,AudioManager.USE_DEFAULT_STREAM_TYPE,0);
		SoundID = SoundPoolKeyButton.load(this, R.raw.touch, 1);
		fVolume = (float)0.4;
		
		SoundPoolKeyButtonEnding = new SoundPool(1,AudioManager.USE_DEFAULT_STREAM_TYPE,0);
		SoundIDEnding = SoundPoolKeyButtonEnding.load(this, R.raw.ending, 1);
		fVolumeEnding = (float)0.4;
		
	}
	
	
	public static void KeyButtonCallBack(int Data){		
		Log.d(TAG,"KeyButtonCallBack : " + Integer.toString(Data));
		CAN1Comm.Callback_KeyButton(Data);
		
		switch (Data) {
		case POWER_OFF:
			SoundPoolKeyButtonEnding.play(SoundID, fVolume, fVolume, 0, 0, 1);
			break;

		default:
			SoundPoolKeyButton.play(SoundID, fVolume, fVolume, 0, 0, 1);
			break;
		}
		
	}
	
	public static void CMDCallback(byte CMD, byte Data){		
		Log.d(TAG,"CMDCallBack, CMD : " + Integer.toString(CMD) + ", Data" + Integer.toString(Data));
	}
	
	public static void UpdateResponseCallBack(int Data){		
		Log.d(TAG,"UpdateResponseCallback : " + Integer.toString(Data));
		CAN1Comm.Callback_UpdateResponse(Data);
	}
	
	public static void BackKeyEvent(){
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_BACK);
				new Instrumentation().sendKeySync(event);
				KeyEvent event2 = new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_BACK);
				new Instrumentation().sendKeySync(event2);
				Log.d(TAG,"BackKeyEvent");
			}
			
		}).start();
	}
	public static void MenuKeyEvent(){
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.d(TAG,"MenuKeyEvent");
				KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_MENU);
				new Instrumentation().sendKeySync(event);
				KeyEvent event2 = new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_MENU);
				new Instrumentation().sendKeySync(event2);
				
			}
			
		}).start();
	}
	
	public static void SetScreenTopFlag(boolean flag){
		ScreenTopFlag = flag;
	}
	public static boolean GetScreenTopFlag(){
		return ScreenTopFlag;
	}
	
	////////////////////////////////////////////////////////////////////////
		
	/////////////////OVERRIDE METHOD///////////////////////////////////////
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.v(TAG,"onBind");
		BindFlag = true;
		return CAN1CommManager.getInstance(this);
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Log.v(TAG,"onUnbind");
		BindFlag = false;
		return super.onUnbind(intent);
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.v(TAG,"onCreate");
		InitComport();
		CAN1Comm = CAN1CommManager.getInstance(this);

		InitSound();
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.v(TAG,"onDestroy");
		CloseComport();
		super.onDestroy();
	}
	
	
	////////////////////////////////////////////////////////////////////////


}
