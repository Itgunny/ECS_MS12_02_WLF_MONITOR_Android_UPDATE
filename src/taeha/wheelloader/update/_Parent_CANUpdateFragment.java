package taeha.wheelloader.update;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

public class _Parent_CANUpdateFragment extends Fragment{
	/////////////////CONSTANT////////////////////////////////////////////
	private static final String TAG = "_Parent_CANUpdateFragment";
	
	private static final int PACKET_LENGTH = 256;
	private static final int FWINFO_LENGTH = 72;
	

	
	private static final int RESULT_FLASH_CRC_OK		 			= 0x41;
	private static final int RESULT_FLASH_CRC_ERROR		 			= 0x42;
	private static final int RESULT_FLASH_CRC_NOFW		 			= 0x43;
	/////////////////////////////////////////////////////////////////////
	/////////////////////RESOURCE////////////////////////////////////////
	// Fragment Root
	protected View mRoot;

	protected TextView textViewMachineSlaveID;
	protected TextView textViewMachineFWID;
	protected TextView textViewMachineFWName;
	protected TextView textViewMachineFWModel;
	protected TextView textViewMachineFWVer;
	protected TextView textViewMachineProtoVer;
	protected TextView textViewMachineDate;
	protected TextView textViewMachineTime;
	
	protected TextView textViewFileSlaveID;
	protected TextView textViewFileFWID;
	protected TextView textViewFileFWName;
	protected TextView textViewFileFWModel;
	protected TextView textViewFileFWVer;
	protected TextView textViewFileProtoVer;
	protected TextView textViewFileDate;
	protected TextView textViewFileTime;
	
	protected TextView textViewMachineTitle;
	protected TextView textViewFileTitle;
	protected TextView textViewStatus;
	
	protected Button btnUpdate;
	/////////////////////////////////////////////////////////////////////
	
	/////////////////////VALUABLE////////////////////////////////////////
	// Home
	protected MainActivity ParentActivity;

	// CAN1CommManager
	protected static CAN1CommManager CAN1Comm = null;
	
	// Thread
	protected Thread threadRead = null;
	
	// Thread Sleep Time
	private static int ThreadSleepTime;
	
	protected UpdateFileFindClass UpdateFile;
	
	protected boolean FileOkFlag;
	
	FirmwareInfoClass MachineFirmwareInfo;
	FirmwareInfoClass FileFirmwareInfo;
	
	int RetryCount;
	
	int BootloaderStatus;
	/////////////////////////////////////////////////////////////////////	
	
	///////////////////ANIMATION/////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////
	
	///////////////////Common Function///////////////////////////////////
	protected void InitResource(){
		textViewMachineSlaveID = (TextView)mRoot.findViewById(R.id.textView_screen_cluster_machine_slaveid);
		textViewMachineFWID = (TextView)mRoot.findViewById(R.id.textView_screen_cluster_machine_fwid);
		textViewMachineFWName = (TextView)mRoot.findViewById(R.id.textView_screen_cluster_machine_fwname);
		textViewMachineFWModel = (TextView)mRoot.findViewById(R.id.textView_screen_cluster_machine_fwmodel);
		textViewMachineFWVer = (TextView)mRoot.findViewById(R.id.textView_screen_cluster_machine_fwver);
		textViewMachineProtoVer = (TextView)mRoot.findViewById(R.id.textView_screen_cluster_machine_protover);
		textViewMachineDate = (TextView)mRoot.findViewById(R.id.textView_screen_cluster_machine_manufacture_date);
		textViewMachineTime = (TextView)mRoot.findViewById(R.id.textView_screen_cluster_machine_manufacture_time);
		
		
		textViewFileSlaveID = (TextView)mRoot.findViewById(R.id.textView_screen_cluster_file_slaveid);
		textViewFileFWID = (TextView)mRoot.findViewById(R.id.textView_screen_cluster_file_fwid);
		textViewFileFWName = (TextView)mRoot.findViewById(R.id.textView_screen_cluster_file_fwname);
		textViewFileFWModel = (TextView)mRoot.findViewById(R.id.textView_screen_cluster_file_fwmodel);
		textViewFileFWVer = (TextView)mRoot.findViewById(R.id.textView_screen_cluster_file_fwver);
		textViewFileProtoVer = (TextView)mRoot.findViewById(R.id.textView_screen_cluster_file_protover);
		textViewFileDate = (TextView)mRoot.findViewById(R.id.textView_screen_cluster_file_manufacture_date);
		textViewFileTime = (TextView)mRoot.findViewById(R.id.textView_screen_cluster_file_manufacture_time);
		
		textViewFileTitle = (TextView)mRoot.findViewById(R.id.textView_screen_cluster_file_title);
		textViewMachineTitle = (TextView)mRoot.findViewById(R.id.textView_screen_cluster_machine_title);
		textViewStatus = (TextView)mRoot.findViewById(R.id.textView_screen_cluster_status);
		
		
		btnUpdate = (Button)mRoot.findViewById(R.id.button_screen_cluster_update);
	}
	private void InitButtonListener(){
		
	}

	protected void InitValuables(){
		Log.d(TAG,"InitValuables");
		
		UpdateFile = new UpdateFileFindClass(ParentActivity);
		FileOkFlag = false;
		
		MachineFirmwareInfo = new FirmwareInfoClass();
		FileFirmwareInfo = new FirmwareInfoClass();;
		
		ThreadSleepTime = 500;
		CAN1Comm = CAN1CommManager.getInstance();	
		
		RetryCount = 0;
		
		threadRead = new Thread(new ReadThread(this));
		threadRead.start();
		
		
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
		mRoot = inflater.inflate(R.layout.screen_cluster, null);
		
		InitResource();
		InitValuables();
		InitButtonListener();
		
		ParentActivity.MenuIndex = ParentActivity.INDEX_CLUSTER_TOP;
		
		StatusDisplay("Request FW New Info");
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
		private WeakReference<_Parent_CANUpdateFragment> fragmentRef = null;
		public Message msg = null;
		public ReadThread(_Parent_CANUpdateFragment fragment){
			this.fragmentRef = new WeakReference<_Parent_CANUpdateFragment>(fragment);
			msg = new Message();
		}
	
		@Override
		public void run() {
			try{
				while(!fragmentRef.get().threadRead.currentThread().isInterrupted())
				{
					fragmentRef.get().GetDataFromNative();
					Thread.sleep(fragmentRef.get().GetThreadSleepTime());
					fragmentRef.get().DisplayUI();
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
	
	public void GetDataFromNative(){
		MachineFirmwareInfo.SlaveID = CAN1Comm.Get_FirmwareInformation_SlaveID_RX_SEND_FW_N_INFO_61184_250_48();
		MachineFirmwareInfo.FWID = CAN1Comm.Get_FirmwareInformation_FWID_RX_SEND_FW_N_INFO_61184_250_48();
		MachineFirmwareInfo.FWName = CAN1Comm.Get_FirmwareInformation_FWName_RX_SEND_FW_N_INFO_61184_250_48();
		MachineFirmwareInfo.FWModel = CAN1Comm.Get_FirmwareInformation_FWModel_RX_SEND_FW_N_INFO_61184_250_48();
		MachineFirmwareInfo.FWVersion = CAN1Comm.Get_FirmwareInformation_FWVersion_RX_SEND_FW_N_INFO_61184_250_48();
		MachineFirmwareInfo.ProtoVer = CAN1Comm.Get_FirmwareInformation_ProtoType_RX_SEND_FW_N_INFO_61184_250_48();
		MachineFirmwareInfo.Date = CAN1Comm.Get_FirmwareInformation_FWChangeDay_RX_SEND_FW_N_INFO_61184_250_48();
		MachineFirmwareInfo.Time = CAN1Comm.Get_FirmwareInformation_FWChangeTime_RX_SEND_FW_N_INFO_61184_250_48();
		MachineFirmwareInfo.FWFileSize = CAN1Comm.Get_FirmwareInformation_FWFileSize_RX_SEND_FW_N_INFO_61184_250_48();
		MachineFirmwareInfo.SectionUnitSize = CAN1Comm.Get_FirmwareInformation_SectionUnitSize_RX_SEND_FW_N_INFO_61184_250_48();
		MachineFirmwareInfo.PacketUnitSize = CAN1Comm.Get_FirmwareInformation_PacketUnitSize_RX_SEND_FW_N_INFO_61184_250_48();
		MachineFirmwareInfo.NumberofSection = CAN1Comm.Get_FirmwareInformation_NumberofSectioninaFile_RX_SEND_FW_N_INFO_61184_250_48();
		MachineFirmwareInfo.NumberofPacket = CAN1Comm.Get_FirmwareInformation_NumberofPacketinaSection_RX_SEND_FW_N_INFO_61184_250_48();
		MachineFirmwareInfo.AppstartAddress = CAN1Comm.Get_FirmwareInformation_AppStartAddress_RX_SEND_FW_N_INFO_61184_250_48();
		
		BootloaderStatus = CAN1Comm.Get_ResultCPUCRC_RX_SEND_BOOTLOADER_STATUS_61184_250_17();
		
		if(CAN1Comm.Get_CheckBKCUComm()==1)
			ParentActivity.CheckBKCU = true;
		
		if(CAN1Comm.Get_CheckRMCUComm()==1)
			ParentActivity.CheckRMCU = true;
	}
	
	public void UpdateUI(){
		if(CAN1Comm.Get_nRecvFWInfoFlag_61184_250_48() == 1){
			CAN1Comm.Set_nRecvFWInfoFlag_61184_250_48(0);
			MachineSlaveIDDisplay(MachineFirmwareInfo.SlaveID);
			MachineFWIDDisplay(MachineFirmwareInfo.FWID);
			MachineFWNameDisplay(MachineFirmwareInfo.FWName);
			MachineFWModelDisplay(MachineFirmwareInfo.FWModel);
			MachineFWVersionDisplay(MachineFirmwareInfo.FWVersion);
			MachineProtoVersionDisplay(MachineFirmwareInfo.ProtoVer);
			MachineDateDisplay(MachineFirmwareInfo.Date);
			MachineTimeDisplay(MachineFirmwareInfo.Time);
			RetryCount = 101;
			
			if(FileOkFlag)
				StatusDisplay("Ready to Download");
		}else{
			
			if(RetryCount < 100){
				RetryCount++;
				if(FileOkFlag)
					StatusWarningDisplay("Do Not Receive Send F/W Info");
			}
			else if(RetryCount == 100){
				CAN1Comm.Set_FWID_TX_REQUEST_FW_N_INFO_61184_250_32(0);
				CAN1Comm.TxCANToMCU(0x20);
				RetryCount = 101;
			}
		}		
		if(CAN1Comm.Get_nRecvSendBootloaderStatusFlag_61184_250_17() == 1){
			CAN1Comm.Set_nRecvSendBootloaderStatusFlag_61184_250_17(0);
			BootLoaderStatusDisplay(BootloaderStatus);
		}
	}
	////////////////////////////////////////////////////////////////////
	public void BootLoaderStatusDisplay(int _Status){
		switch (_Status) {
		case RESULT_FLASH_CRC_OK:
			StatusDisplay("Send Bootloader Status : CRC OK");
			break;
		case RESULT_FLASH_CRC_ERROR:
			StatusWarningDisplay("Send Bootloader Status : CRC Error");
			break;
		case RESULT_FLASH_CRC_NOFW:
			StatusWarningDisplay("Send Bootloader Status : No FW");
			break;
		default:
			break;
		}
	}
	public void MachineSlaveIDDisplay(int _SlaveID){
		textViewMachineSlaveID.setText("Slave ID\n0x" + Integer.toHexString(_SlaveID));
	}
	public void MachineFWIDDisplay(int _FWID){
		textViewMachineFWID.setText("F/W ID\n0x" + Integer.toHexString(_FWID));
	}
	public void MachineFWNameDisplay(byte[] _FWName){
		String str;
		str = new String(_FWName,0,_FWName.length);
		textViewMachineFWName.setText("F/W Name\n" + str);
	}
	public void MachineFWModelDisplay(byte[] _FWModel){
		String str;
		str = new String(_FWModel,0,_FWModel.length);
		textViewMachineFWModel.setText("F/W Model\n" + str);
	}
	public void MachineFWVersionDisplay(byte[] _FWVersion){
		String str;
		str = _FWVersion[0] + "." + _FWVersion[1] + "." + _FWVersion[2] + "." + _FWVersion[3];
		textViewMachineFWVer.setText("F/W Version\n" + str);
	}
	public void MachineProtoVersionDisplay(byte[] _ProtoVersion){
		String str;
		str = _ProtoVersion[0] + "." + _ProtoVersion[1] + "." + _ProtoVersion[2] + "." + _ProtoVersion[3];
		textViewMachineProtoVer.setText("Proto Version\n" + str);
	}
	public void MachineDateDisplay(byte[] _Date){
		String str;
		str = _Date[0] + "/" + _Date[1] + "/" + _Date[2];
		textViewMachineDate.setText("Manufacture Date\n" + str);
	}
	public void MachineTimeDisplay(byte[] _Time){
		String str;
		str = _Time[0] + ":" + _Time[1] + ":" + _Time[2];
		textViewMachineTime.setText("Manufacture Time\n" + str);
	}
	

	public void FileSlaveIDDisplay(int _SlaveID){
		textViewFileSlaveID.setText("Slave ID\n0x" + Integer.toHexString(_SlaveID));
	}
	public void FileFWIDDisplay(int _FWID){
		textViewFileFWID.setText("F/W ID\n0x" + Integer.toHexString(_FWID));
	}
	public void FileFWNameDisplay(byte[] _FWName){
		String str;
		str = new String(_FWName,0,_FWName.length);
		textViewFileFWName.setText("F/W Name\n" + str);
	}
	public void FileFWModelDisplay(byte[] _FWModel){
		String str;
		str = new String(_FWModel,0,_FWModel.length);
		textViewFileFWModel.setText("F/W Model\n" + str);
	}
	public void FileFWVersionDisplay(byte[] _FWVersion){
		String str;
		str = _FWVersion[0] + "." + _FWVersion[1] + "." + _FWVersion[2] + "." + _FWVersion[3];
		textViewFileFWVer.setText("F/W Version\n" + str);
	}
	public void FileProtoVersionDisplay(byte[] _ProtoVersion){
		String str;
		str = _ProtoVersion[0] + "." + _ProtoVersion[1] + "." + _ProtoVersion[2] + "." + _ProtoVersion[3];
		textViewFileProtoVer.setText("Proto Version\n" + str);
	}
	public void FileDateDisplay(byte[] _Date){
		String str;
		str = _Date[0] + "/" + _Date[1] + "/" + _Date[2];
		textViewFileDate.setText("Manufacture Date\n" + str);
	}
	public void FileTimeDisplay(byte[] _Time){
		String str;
		str = _Time[0] + ":" + _Time[1] + ":" + _Time[2];
		textViewFileTime.setText("Manufacture Time\n" + str);
	}
	
	public void StatusDisplay(String str){
		textViewStatus.setText(str);
		textViewStatus.setTextColor(0xFF00FF00);

	}
	public void StatusWarningDisplay(String str){
		textViewStatus.setText(str);
		textViewStatus.setTextColor(0xFFFF0000);
	}
	///////////////////////////////////////////////////////////////////////////
	public boolean GetFWInfoFromFile(File f){		
		if(f == null){
			Log.e(TAG,"No Files");
			return false;
		}
			
		
		InputStream is;
		long Length = 0;
		int TotalPacket = 0;
		int LastData = 0;
		int nRead = 0;
		boolean DelicatorCheck = false;
		byte[] FileData = new byte[PACKET_LENGTH];
		byte[] FileFWInfo = new byte[FWINFO_LENGTH];
		byte[] FileAppSectionCRCData;
		byte[] FileAppCRC = new byte[2];

		
		try {
			is = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e(TAG,"No Files");
			e.printStackTrace();
			return false;
		}
		
		Length = f.length();
//		if(Length % PACKET_LENGTH == 0){
//			TotalPacket = (int) (Length / PACKET_LENGTH);
//		}else{
//			TotalPacket = (int) (Length / PACKET_LENGTH) + 1;
//		}
		TotalPacket = (int) (Length / PACKET_LENGTH);
		
		///////////////////Check Delicator/////////////////////
		for(int i = 0; i < TotalPacket; i++){
			try {
				
				nRead = is.read(FileData, 0, PACKET_LENGTH);
				
				for(int j = 0; j < PACKET_LENGTH; j++){
					
					if(FileData[j] == 0x45){
						DelicatorCheck = true;
					}else{
						DelicatorCheck = false;
						break;
					}
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
			
			if(DelicatorCheck == true)
				break;
		}
		
		if(DelicatorCheck == false){
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
			
		///////////////////////////////////////////////////////
		
		
		try {
			nRead = is.read(FileFWInfo, 0, FWINFO_LENGTH);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FileFirmwareInfo.SlaveID = (FileFWInfo[0] & 0xFF) + ((FileFWInfo[1] & 0xFF) << 8);
		FileFirmwareInfo.FWID = (FileFWInfo[2] & 0xFF) + ((FileFWInfo[3] & 0xFF) << 8);
		for(int i = 0; i < 18; i++){
			FileFirmwareInfo.FWName[i] = FileFWInfo[i + 4];
		}
		for(int i = 0; i < 20; i++){
			FileFirmwareInfo.FWModel[i] = FileFWInfo[i + 22];
		}
		for(int i = 0; i < 4; i++){
			FileFirmwareInfo.FWVersion[i] = FileFWInfo[i + 42];
		}
		for(int i = 0; i < 4; i++){
			FileFirmwareInfo.ProtoVer[i] = FileFWInfo[i + 46];
		}
		for(int i = 0; i < 3; i++){
			FileFirmwareInfo.Date[i] = FileFWInfo[i + 50];
		}
		for(int i = 0; i < 3; i++){
			FileFirmwareInfo.Time[i] = FileFWInfo[i + 53];
		}
		FileFirmwareInfo.FWFileSize = (FileFWInfo[56] & 0xFF) + ((FileFWInfo[57] & 0xFF) << 8) + ((FileFWInfo[58] & 0xFF) << 16) + ((FileFWInfo[59] & 0xFF) << 24);
		FileFirmwareInfo.SectionUnitSize = (FileFWInfo[60] & 0xFF) + ((FileFWInfo[61] & 0xFF) << 8);
		FileFirmwareInfo.PacketUnitSize = (FileFWInfo[62] & 0xFF) + ((FileFWInfo[63] & 0xFF)<< 8);
		FileFirmwareInfo.NumberofSection = (FileFWInfo[64] & 0xFF) + ((FileFWInfo[65] & 0xFF) << 8);
		FileFirmwareInfo.NumberofPacket = (FileFWInfo[66] & 0xFF) + ((FileFWInfo[67] & 0xFF) << 8);
		FileFirmwareInfo.AppstartAddress = (FileFWInfo[68] & 0xFF) + ((FileFWInfo[69] & 0xFF)<< 8) + ((FileFWInfo[70] & 0xFF) << 16) + ((FileFWInfo[71] & 0xFF) << 24);

		Log.d(TAG, "SectionUnitSize"+FileFirmwareInfo.SectionUnitSize);
		Log.d(TAG, "PacketUnitSize"+FileFirmwareInfo.PacketUnitSize);
		Log.d(TAG, "NumberofSection"+FileFirmwareInfo.NumberofSection);
		Log.d(TAG, "NumberofPacket"+FileFirmwareInfo.NumberofPacket);
		FileAppSectionCRCData = new byte[FileFirmwareInfo.NumberofSection * 2];
		
		try {
			nRead = is.read(FileAppSectionCRCData, 0, FileFirmwareInfo.NumberofSection * 2);
			nRead = is.read(FileAppCRC, 0, 2);
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int j = 0;
		for(int i = 0; i < FileFirmwareInfo.NumberofSection; i++){
			FileFirmwareInfo.AppSectionCRC[i] = (short) ((FileAppSectionCRCData[j] & 0xFF)+ ((FileAppSectionCRCData[j + 1] & 0xFF) << 8));
			j += 2;
		}
		FileFirmwareInfo.AppCRC = (short) ((FileAppCRC[0] & 0xFF) + ((FileAppCRC[1] & 0xFF) << 8));
		return true;
	}
}
