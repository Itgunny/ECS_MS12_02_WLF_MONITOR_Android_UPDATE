package taeha.wheelloader.update;

import java.io.FileDescriptor;

import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

public class CAN1CommManager extends ICAN1CommManager.Stub{		// ttySAC1(Linux), UART1(Exynos), UART2(STM32) 
	
	private static final String TAG = "CAN1CommManager";
	
	
	public static int CMD_KEY		= 0x00;
	public static int CMD_LCD		= 0x01;
	public static int CMD_BUZ		= 0x02;
	public static int CMD_RTC		= 0x03;
	public static int CMD_CAM		= 0x04;
	public static int CMD_VERSION	= 0x05;
	public static int CMD_DOWN		= 0x06;
	public static int CMD_SMK		= 0x07;
	public static int CMD_LAMP		= 0x08;
	public static int CMD_STARTCAN	= 0x09;
	public static int CMD_CANUPDATE	= 0x0A;
	public static int CMD_OSUPDATE	= 0x0B;
	
	public static int RES_KEY		= 0x80;
	public static int RES_LCD		= 0x81;
	public static int RES_BUZ		= 0x82;
	public static int RES_RTC		= 0x83;
	public static int RES_CAM		= 0x84;
	public static int RES_VERSION	= 0x85;
	public static int RES_DOWN		= 0x86;
	public static int RES_SMK		= 0x87;
	public static int RES_LAMP		= 0x88;
	public static int RES_STARTCAN	= 0x89;
	public static int RES_CANUPDATE = 0x8A;
	public static int RES_OSUPDATE 	= 0x8B;
	
	public static final int SA_MONITOR 	= 0x28;
	public static final int SA_MCU		= 0x47;
	public static final int SA_CLUSTER 	= 0x17;
	public static final int SA_BKCU		= 0x34;				
	
	
	
	static final RemoteCallbackList<ICAN1CommManagerCallback> callbacks = new RemoteCallbackList<ICAN1CommManagerCallback>();
	
	
	
	private int ReqPopup = 0xFFFF;
	// Singleton Pattern
	//////////////////////Init////////////////////////////////////
	private CommService service;
	private static CAN1CommManager instance = null;
	
	public static CAN1CommManager getInstance(){
		Log.v(TAG,"getInstance1");
		return instance;
	}
	protected static CAN1CommManager getInstance(CommService service){
		Log.v(TAG,"getInstance2");
		if(instance == null){
			instance = new CAN1CommManager(service);
		}
		
		return instance;
	}
	private CAN1CommManager(CommService _service){
		Log.v(TAG,"CAN1CommManager");
		this.service = _service;
	}

	///////////////////////////////////////////////////////////
	
	//////////////////////Register Callback////////////////////
	public boolean unregisterCallback(ICAN1CommManagerCallback callback) throws RemoteException{
		boolean flag = false;
		
		if(callback != null){
			flag = callbacks.unregister(callback);
		}
		return flag;
	}
	
	public boolean registerCallback(ICAN1CommManagerCallback callback) throws RemoteException{
		boolean flag = false;
		
		if(callback != null){
			flag = callbacks.register(callback);
		}
		return flag;
	}
	
	public static void CallbackFunc(){
		int N = callbacks.beginBroadcast();
		for(int i = 0; i < N; i++){
			try {
				callbacks.getBroadcastItem(i).CallbackFunc(1);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	///////////////////////////////////////////////////////////
	
	//////////////////////////////////////////////////////////
	public void OpenComport(){
		service.InitComport();
	}
	public void CloseComport(){
		service.CloseComport();
	}
	//////////////////////////////////////////////////////////
	
	@Override
	public int TxCANToMCU(int PS) {
		// TODO Auto-generated method stub
		Log.v(TAG,"TxCommandToMCU");
		return service.UART1_TxComm(PS);
	}
	public int TxCMDToMCU(int CMD, int DAT1, int DAT2, int DAT3, int DAT4, int DAT5, int DAT6, int DAT7, int DAT8) {
		// TODO Auto-generated method stub
		return service.UART3_TxComm(CMD, DAT1, DAT2, DAT3, DAT4, DAT5, DAT6, DAT7, DAT8);
	}
	public int TxCMDToMCU(int CMD, int DAT1, int DAT2, int DAT3, int DAT4, int DAT5, int DAT6, int DAT7) {
		// TODO Auto-generated method stub
		return service.UART3_TxComm(CMD, DAT1, DAT2, DAT3, DAT4, DAT5, DAT6, DAT7, 0xFF);
	}
	public int TxCMDToMCU(int CMD, int DAT1, int DAT2, int DAT3, int DAT4, int DAT5, int DAT6) {
		// TODO Auto-generated method stub
		return service.UART3_TxComm(CMD, DAT1, DAT2, DAT3, DAT4, DAT5, DAT6, 0xFF, 0xFF);
	}
	public int TxCMDToMCU(int CMD, int DAT1, int DAT2, int DAT3, int DAT4, int DAT5) {
		// TODO Auto-generated method stub
		return service.UART3_TxComm(CMD, DAT1, DAT2, DAT3, DAT4, DAT5, 0xFF, 0xFF, 0xFF);
	}
	public int TxCMDToMCU(int CMD, int DAT1, int DAT2, int DAT3, int DAT4) {
		// TODO Auto-generated method stub
		return service.UART3_TxComm(CMD, DAT1, DAT2, DAT3, DAT4, 0xFF, 0xFF, 0xFF, 0xFF);
	}
	public int TxCMDToMCU(int CMD, int DAT1, int DAT2, int DAT3) {
		// TODO Auto-generated method stub
		return service.UART3_TxComm(CMD, DAT1, DAT2, DAT3, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF);
	}
	public int TxCMDToMCU(int CMD, int DAT1, int DAT2) {
		// TODO Auto-generated method stub
		return service.UART3_TxComm(CMD, DAT1, DAT2, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF);
	}
	public int TxCMDToMCU(int CMD, int DAT1) {
		// TODO Auto-generated method stub
		return service.UART3_TxComm(CMD, DAT1, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF);
	}
	public int TxCMDToMCU(int CMD) {
		// TODO Auto-generated method stub
		return service.UART3_TxComm(CMD, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF);
	}
	
	public int TxUpdate(byte[] Data, int size){
		return service.Write_UART3(Data, size);
	}
	
	@Override
	public int UART3_UpdatePacketComm(int Index, int CRC, int EOTFlag,byte[] arr){
		// TODO Auto-generated method stub
		return service.UART3_UpdatePacketComm(Index, CRC, EOTFlag, arr);
	}

	public  void Set_nRecvSendBootloaderStatusFlag_61184_250_17(int Data)
	{ 
		service.Set_nRecvSendBootloaderStatusFlag_61184_250_17(Data);
	}
	public  int Get_nRecvSendBootloaderStatusFlag_61184_250_17(){ 
		return service.Get_nRecvSendBootloaderStatusFlag_61184_250_17();
	}

	public  void Set_MultiPacketErrFlag(int Data)
	{ 
		service.Set_MultiPacketErrFlag(Data);
	}
	public  int Get_MultiPacketErrFlag(){ 
		return service.Get_MultiPacketErrFlag();
	}
	public  void Set_TargetSourceAddress(int Data)
	{ 
		service.Set_TargetSourceAddress(Data);
	}
	public  void Set_nLength_61184_250_66(int Data)
	{ 
		service.Set_nLength_61184_250_66(Data);
	}
	public  void Set_nLength_61184_250_81(int Data)
	{ 
		service.Set_nLength_61184_250_81(Data);
	}
	public  int Get_nRecAckNewFWNInfoFlag_61184_250_82(){ 
		return service.Get_nRecAckNewFWNInfoFlag_61184_250_82();
	}
	public  int Get_nRecvFWInfoFlag_61184_250_48(){ 
		return service.Get_nRecvFWInfoFlag_61184_250_48();
	}
	public  int Get_nRecvRequestPacketMFlag_61184_250_83(){ 
		return service.Get_nRecvRequestPacketMFlag_61184_250_83();
	}
	public  int Get_nRecvFWDLCompleteFlag_61184_250_80(){ 
		return service.Get_nRecvFWDLCompleteFlag_61184_250_80();
	}	
	public  int Get_nRecvFWUpdateCompleteFlag_61184_250_112(){ 
		return service.Get_nRecvFWUpdateCompleteFlag_61184_250_112();
	}	
	public  void Set_nRecAckNewFWNInfoFlag_61184_250_82(int Data)
	{ 
		service.Set_nRecAckNewFWNInfoFlag_61184_250_82(Data);
	}
	public  void Set_nRecvFWInfoFlag_61184_250_48(int Data)
	{ 
		service.Set_nRecvFWInfoFlag_61184_250_48(Data);
	}
	public  void Set_nRecvRequestPacketMFlag_61184_250_83(int Data)
	{ 
		service.Set_nRecvRequestPacketMFlag_61184_250_83(Data);
	}
	public  void Set_nRecvFWDLCompleteFlag_61184_250_80(int Data)
	{ 
		service.Set_nRecvFWDLCompleteFlag_61184_250_80(Data);
	}
	public  void Set_nRecvFWUpdateCompleteFlag_61184_250_112(int Data)
	{ 
		service.Set_nRecvFWUpdateCompleteFlag_61184_250_112(Data);
	}
	
	///////////////////////////////////////////////////////////
	
	///////////////////////////Set////////////////////////////
	public  void Set_FWID_TX_REQUEST_FW_N_INFO_61184_250_32(int Data){ service.Set_FWID_TX_REQUEST_FW_N_INFO_61184_250_32(Data);}
//	public  void Set_FirmwareInformation_SlaveID_TX_ENTER_DL_MODE_61184_250_65(int Data){ service.Set_FirmwareInformation_SlaveID_TX_ENTER_DL_MODE_61184_250_65(Data);}
//	public  void Set_FirmwareInformation_FWID_TX_ENTER_DL_MODE_61184_250_65(int Data){ service.Set_FirmwareInformation_FWID_TX_ENTER_DL_MODE_61184_250_65(Data);}
//	public  void Set_FirmwareInformation_FWName_TX_ENTER_DL_MODE_61184_250_65(byte[] Data){ service.Set_FirmwareInformation_FWName_TX_ENTER_DL_MODE_61184_250_65(Data);}
//	public  void Set_FirmwareInformation_FWModel_TX_ENTER_DL_MODE_61184_250_65(byte[] Data){ service.Set_FirmwareInformation_FWModel_TX_ENTER_DL_MODE_61184_250_65(Data);}
//	public  void Set_FirmwareInformation_FWVersion_TX_ENTER_DL_MODE_61184_250_65(byte[] Data){ service.Set_FirmwareInformation_FWVersion_TX_ENTER_DL_MODE_61184_250_65(Data);}
//	public  void Set_FirmwareInformation_ProtoType_TX_ENTER_DL_MODE_61184_250_65(byte[] Data){ service.Set_FirmwareInformation_ProtoType_TX_ENTER_DL_MODE_61184_250_65(Data);}
//	public  void Set_FirmwareInformation_FWChangeDay_TX_ENTER_DL_MODE_61184_250_65(byte[] Data){ service.Set_FirmwareInformation_FWChangeDay_TX_ENTER_DL_MODE_61184_250_65(Data);}
//	public  void Set_FirmwareInformation_FWChangeTime_TX_ENTER_DL_MODE_61184_250_65(byte[] Data){ service.Set_FirmwareInformation_FWChangeTime_TX_ENTER_DL_MODE_61184_250_65(Data);}
//	public  void Set_FirmwareInformation_FWFileSize_TX_ENTER_DL_MODE_61184_250_65(int Data){ service.Set_FirmwareInformation_FWFileSize_TX_ENTER_DL_MODE_61184_250_65(Data);}
//	public  void Set_FirmwareInformation_SectionUnitSize_TX_ENTER_DL_MODE_61184_250_65(int Data){ service.Set_FirmwareInformation_SectionUnitSize_TX_ENTER_DL_MODE_61184_250_65(Data);}
//	public  void Set_FirmwareInformation_PacketUnitSize_TX_ENTER_DL_MODE_61184_250_65(int Data){ service.Set_FirmwareInformation_PacketUnitSize_TX_ENTER_DL_MODE_61184_250_65(Data);}
//	public  void Set_FirmwareInformation_NumberofSectioninaFile_TX_ENTER_DL_MODE_61184_250_65(int Data){ service.Set_FirmwareInformation_NumberofSectioninaFile_TX_ENTER_DL_MODE_61184_250_65(Data);}
//	public  void Set_FirmwareInformation_NumberofPacketinaSection_TX_ENTER_DL_MODE_61184_250_65(int Data){ service.Set_FirmwareInformation_NumberofPacketinaSection_TX_ENTER_DL_MODE_61184_250_65(Data);}
//	public  void Set_FirmwareInformation_AppStartAddress_TX_ENTER_DL_MODE_61184_250_65(int Data){ service.Set_FirmwareInformation_AppStartAddress_TX_ENTER_DL_MODE_61184_250_65(Data);}
//	public  void Set_FirmwareInformation_CRC_TX_ENTER_DL_MODE_61184_250_65(int Index, int Data){ service.Set_FirmwareInformation_CRC_TX_ENTER_DL_MODE_61184_250_65(Index, Data);}
	public  void Set_FirmwareInformation_SlaveID_TX_SEND_NEW_FW_N_INFO_61184_250_66(int Data){ service.Set_FirmwareInformation_SlaveID_TX_SEND_NEW_FW_N_INFO_61184_250_66(Data);}
	public  void Set_FirmwareInformation_FWID_TX_SEND_NEW_FW_N_INFO_61184_250_66(int Data){ service.Set_FirmwareInformation_FWID_TX_SEND_NEW_FW_N_INFO_61184_250_66(Data);}
	public  void Set_FirmwareInformation_FWName_TX_SEND_NEW_FW_N_INFO_61184_250_66(byte[] Data){ service.Set_FirmwareInformation_FWName_TX_SEND_NEW_FW_N_INFO_61184_250_66(Data);}
	public  void Set_FirmwareInformation_FWModel_TX_SEND_NEW_FW_N_INFO_61184_250_66(byte[] Data){ service.Set_FirmwareInformation_FWModel_TX_SEND_NEW_FW_N_INFO_61184_250_66(Data);}
	public  void Set_FirmwareInformation_FWVersion_TX_SEND_NEW_FW_N_INFO_61184_250_66(byte[] Data){ service.Set_FirmwareInformation_FWVersion_TX_SEND_NEW_FW_N_INFO_61184_250_66(Data);}
	public  void Set_FirmwareInformation_ProtoType_TX_SEND_NEW_FW_N_INFO_61184_250_66(byte[] Data){ service.Set_FirmwareInformation_ProtoType_TX_SEND_NEW_FW_N_INFO_61184_250_66(Data);}
	public  void Set_FirmwareInformation_FWChangeDay_TX_SEND_NEW_FW_N_INFO_61184_250_66(byte[] Data){ service.Set_FirmwareInformation_FWChangeDay_TX_SEND_NEW_FW_N_INFO_61184_250_66(Data);}
	public  void Set_FirmwareInformation_FWChangeTime_TX_SEND_NEW_FW_N_INFO_61184_250_66(byte[] Data){ service.Set_FirmwareInformation_FWChangeTime_TX_SEND_NEW_FW_N_INFO_61184_250_66(Data);}
	public  void Set_FirmwareInformation_FWFileSize_TX_SEND_NEW_FW_N_INFO_61184_250_66(int Data){ service.Set_FirmwareInformation_FWFileSize_TX_SEND_NEW_FW_N_INFO_61184_250_66(Data);}
	public  void Set_FirmwareInformation_SectionUnitSize_TX_SEND_NEW_FW_N_INFO_61184_250_66(int Data){ service.Set_FirmwareInformation_SectionUnitSize_TX_SEND_NEW_FW_N_INFO_61184_250_66(Data);}
	public  void Set_FirmwareInformation_PacketUnitSize_TX_SEND_NEW_FW_N_INFO_61184_250_66(int Data){ service.Set_FirmwareInformation_PacketUnitSize_TX_SEND_NEW_FW_N_INFO_61184_250_66(Data);}
	public  void Set_FirmwareInformation_NumberofSectioninaFile_TX_SEND_NEW_FW_N_INFO_61184_250_66(int Data){ service.Set_FirmwareInformation_NumberofSectioninaFile_TX_SEND_NEW_FW_N_INFO_61184_250_66(Data);}
	public  void Set_FirmwareInformation_NumberofPacketinaSection_TX_SEND_NEW_FW_N_INFO_61184_250_66(int Data){ service.Set_FirmwareInformation_NumberofPacketinaSection_TX_SEND_NEW_FW_N_INFO_61184_250_66(Data);}
	public  void Set_FirmwareInformation_AppStartAddress_TX_SEND_NEW_FW_N_INFO_61184_250_66(int Data){ service.Set_FirmwareInformation_AppStartAddress_TX_SEND_NEW_FW_N_INFO_61184_250_66(Data);}
	public  void Set_FirmwareInformation_CRC_TX_SEND_NEW_FW_N_INFO_61184_250_66(int Index, int Data){ service.Set_FirmwareInformation_CRC_TX_SEND_NEW_FW_N_INFO_61184_250_66(Index,Data);}
	public  void Set_SectionNumber_TX_SEND_PACKET_M_61184_250_67(int Data){ service.Set_SectionNumber_TX_SEND_PACKET_M_61184_250_67(Data);}
	public  void Set_PacketNumber_TX_SEND_PACKET_M_61184_250_67(int Data){ service.Set_PacketNumber_TX_SEND_PACKET_M_61184_250_67(Data);}
	public  void Set_PacketLength_TX_SEND_PACKET_M_61184_250_67(int Data){ service.Set_PacketLength_TX_SEND_PACKET_M_61184_250_67(Data);}
	public  void Set_DistributionFileData_TX_SEND_PACKET_M_61184_250_67(byte[] Data){ service.Set_DistributionFileData_TX_SEND_PACKET_M_61184_250_67(Data);}
	public  void Set_PacketCRC_TX_SEND_PACKET_M_61184_250_67(int Data){ service.Set_PacketCRC_TX_SEND_PACKET_M_61184_250_67(Data);}
//	public  void Set_FirmwareInformation_SlaveID_TX_QUIT_DL_MODE_61184_250_81(int Data){ service.Set_FirmwareInformation_SlaveID_TX_QUIT_DL_MODE_61184_250_81(Data);}
//	public  void Set_FirmwareInformation_FWID_TX_QUIT_DL_MODE_61184_250_81(int Data){ service.Set_FirmwareInformation_FWID_TX_QUIT_DL_MODE_61184_250_81(Data);}
//	public  void Set_FirmwareInformation_FWName_TX_QUIT_DL_MODE_61184_250_81(byte[] Data){ service.Set_FirmwareInformation_FWName_TX_QUIT_DL_MODE_61184_250_81(Data);}
//	public  void Set_FirmwareInformation_FWModel_TX_QUIT_DL_MODE_61184_250_81(byte[] Data){ service.Set_FirmwareInformation_FWModel_TX_QUIT_DL_MODE_61184_250_81(Data);}
//	public  void Set_FirmwareInformation_FWVersion_TX_QUIT_DL_MODE_61184_250_81(byte[] Data){ service.Set_FirmwareInformation_FWVersion_TX_QUIT_DL_MODE_61184_250_81(Data);}
//	public  void Set_FirmwareInformation_ProtoType_TX_QUIT_DL_MODE_61184_250_81(byte[] Data){ service.Set_FirmwareInformation_ProtoType_TX_QUIT_DL_MODE_61184_250_81(Data);}
//	public  void Set_FirmwareInformation_FWChangeDay_TX_QUIT_DL_MODE_61184_250_81(byte[] Data){ service.Set_FirmwareInformation_FWChangeDay_TX_QUIT_DL_MODE_61184_250_81(Data);}
//	public  void Set_FirmwareInformation_FWChangeTime_TX_QUIT_DL_MODE_61184_250_81(byte[] Data){ service.Set_FirmwareInformation_FWChangeTime_TX_QUIT_DL_MODE_61184_250_81(Data);}
//	public  void Set_FirmwareInformation_FWFileSize_TX_QUIT_DL_MODE_61184_250_81(int Data){ service.Set_FirmwareInformation_FWFileSize_TX_QUIT_DL_MODE_61184_250_81(Data);}
//	public  void Set_FirmwareInformation_SectionUnitSize_TX_QUIT_DL_MODE_61184_250_81(int Data){ service.Set_FirmwareInformation_SectionUnitSize_TX_QUIT_DL_MODE_61184_250_81(Data);}
//	public  void Set_FirmwareInformation_PacketUnitSize_TX_QUIT_DL_MODE_61184_250_81(int Data){ service.Set_FirmwareInformation_PacketUnitSize_TX_QUIT_DL_MODE_61184_250_81(Data);}
//	public  void Set_FirmwareInformation_NumberofSectioninaFile_TX_QUIT_DL_MODE_61184_250_81(int Data){ service.Set_FirmwareInformation_NumberofSectioninaFile_TX_QUIT_DL_MODE_61184_250_81(Data);}
//	public  void Set_FirmwareInformation_NumberofPacketinaSection_TX_QUIT_DL_MODE_61184_250_81(int Data){ service.Set_FirmwareInformation_NumberofPacketinaSection_TX_QUIT_DL_MODE_61184_250_81(Data);}
//	public  void Set_FirmwareInformation_AppStartAddress_TX_QUIT_DL_MODE_61184_250_81(int Data){ service.Set_FirmwareInformation_AppStartAddress_TX_QUIT_DL_MODE_61184_250_81(Data);}
//	public  void Set_FirmwareInformation_CRC_TX_QUIT_DL_MODE_61184_250_81(int Index, int Data){ service.Set_FirmwareInformation_CRC_TX_QUIT_DL_MODE_61184_250_81(Index,Data);}

	///////////////////////////Get////////////////////////////
	
	public  int Get_ResultCPUCRC_RX_SEND_BOOTLOADER_STATUS_61184_250_17(){ return service.Get_ResultCPUCRC_RX_SEND_BOOTLOADER_STATUS_61184_250_17();}
	public  int Get_SlaveInformation_SlaveID_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_SlaveID_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  int Get_SlaveInformation_SlaveName_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_SlaveName_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  byte[] Get_SlaveInformation_SerialNumber_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_SerialNumber_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  byte[] Get_SlaveInformation_HWVersion_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_HWVersion_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  byte[] Get_SlaveInformation_ProductDate_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_ProductDate_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  byte[] Get_SlaveInformation_DeliveryDate_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_DeliveryDate_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  int Get_SlaveInformation_NumberofAPP_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_NumberofAPP_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  int Get_SlaveInformation_App1StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_App1StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  int Get_SlaveInformation_App1Size_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_App1Size_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  int Get_SlaveInformation_App2StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_App2StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  int Get_SlaveInformation_App2Size_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_App2Size_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  int Get_SlaveInformation_App3StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_App3StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  int Get_SlaveInformation_App3Size_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_App3Size_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  int Get_SlaveInformation_App4StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_App4StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  int Get_SlaveInformation_App4Size_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_App4Size_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  int Get_SlaveInformation_App5StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_App5StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  int Get_SlaveInformation_App5Size_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_App5Size_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  int Get_SlaveInformation_App6StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_App6StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  int Get_SlaveInformation_App6Size_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_App6Size_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  int Get_SlaveInformation_App7StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_App7StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  int Get_SlaveInformation_App7Size_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_App7Size_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  int Get_SlaveInformation_App8StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_App8StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  int Get_SlaveInformation_App8Size_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_App8Size_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  int Get_SlaveInformation_App9StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_App9StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  int Get_SlaveInformation_App9Size_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_App9Size_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  int Get_SlaveInformation_App10StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_App10StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  int Get_SlaveInformation_App10Size_RX_SEND_SLAVE_INFO_61184_250_49(){ return service.Get_SlaveInformation_App10Size_RX_SEND_SLAVE_INFO_61184_250_49();}
	public  int Get_FirmwareInformation_SlaveID_RX_SEND_FW_N_INFO_61184_250_48(){ return service.Get_FirmwareInformation_SlaveID_RX_SEND_FW_N_INFO_61184_250_48();}
	public  int Get_FirmwareInformation_FWID_RX_SEND_FW_N_INFO_61184_250_48(){ return service.Get_FirmwareInformation_FWID_RX_SEND_FW_N_INFO_61184_250_48();}
	public  byte[] Get_FirmwareInformation_FWName_RX_SEND_FW_N_INFO_61184_250_48(){ return service.Get_FirmwareInformation_FWName_RX_SEND_FW_N_INFO_61184_250_48();}
	public  byte[] Get_FirmwareInformation_FWModel_RX_SEND_FW_N_INFO_61184_250_48(){ return service.Get_FirmwareInformation_FWModel_RX_SEND_FW_N_INFO_61184_250_48();}
	public  byte[] Get_FirmwareInformation_FWVersion_RX_SEND_FW_N_INFO_61184_250_48(){ return service.Get_FirmwareInformation_FWVersion_RX_SEND_FW_N_INFO_61184_250_48();}
	public  byte[] Get_FirmwareInformation_ProtoType_RX_SEND_FW_N_INFO_61184_250_48(){ return service.Get_FirmwareInformation_ProtoType_RX_SEND_FW_N_INFO_61184_250_48();}
	public  byte[] Get_FirmwareInformation_FWChangeDay_RX_SEND_FW_N_INFO_61184_250_48(){ return service.Get_FirmwareInformation_FWChangeDay_RX_SEND_FW_N_INFO_61184_250_48();}
	public  byte[] Get_FirmwareInformation_FWChangeTime_RX_SEND_FW_N_INFO_61184_250_48(){ return service.Get_FirmwareInformation_FWChangeTime_RX_SEND_FW_N_INFO_61184_250_48();}
	public  int Get_FirmwareInformation_FWFileSize_RX_SEND_FW_N_INFO_61184_250_48(){ return service.Get_FirmwareInformation_FWFileSize_RX_SEND_FW_N_INFO_61184_250_48();}
	public  int Get_FirmwareInformation_SectionUnitSize_RX_SEND_FW_N_INFO_61184_250_48(){ return service.Get_FirmwareInformation_SectionUnitSize_RX_SEND_FW_N_INFO_61184_250_48();}
	public  int Get_FirmwareInformation_PacketUnitSize_RX_SEND_FW_N_INFO_61184_250_48(){ return service.Get_FirmwareInformation_PacketUnitSize_RX_SEND_FW_N_INFO_61184_250_48();}
	public  int Get_FirmwareInformation_NumberofSectioninaFile_RX_SEND_FW_N_INFO_61184_250_48(){ return service.Get_FirmwareInformation_NumberofSectioninaFile_RX_SEND_FW_N_INFO_61184_250_48();}
	public  int Get_FirmwareInformation_NumberofPacketinaSection_RX_SEND_FW_N_INFO_61184_250_48(){ return service.Get_FirmwareInformation_NumberofPacketinaSection_RX_SEND_FW_N_INFO_61184_250_48();}
	public  int Get_FirmwareInformation_AppStartAddress_RX_SEND_FW_N_INFO_61184_250_48(){ return service.Get_FirmwareInformation_AppStartAddress_RX_SEND_FW_N_INFO_61184_250_48();}
	public  int Get_FWID_RX_REQUEST_PACKET_M_61184_250_83(){ return service.Get_FWID_RX_REQUEST_PACKET_M_61184_250_83();}
	public  int Get_SectionNumber_RX_REQUEST_PACKET_M_61184_250_83(){ return service.Get_SectionNumber_RX_REQUEST_PACKET_M_61184_250_83();}
	public  int Get_PacketNumber_RX_REQUEST_PACKET_M_61184_250_83(){ return service.Get_PacketNumber_RX_REQUEST_PACKET_M_61184_250_83();}
	public  int Get_ResultFlashCRC_RX_FW_N_DL_COMPLETE_61184_250_80(){ return service.Get_ResultFlashCRC_RX_FW_N_DL_COMPLETE_61184_250_80();}
	public  int Get_Status_RX_FW_UPDATE_STATUS_61184_250_113(){ return service.Get_Status_RX_FW_UPDATE_STATUS_61184_250_113();}
	public  int Get_Progress_ResultFlashCRC_RX_FW_UPDATE_STATUS_61184_250_113(){ return service.Get_Progress_ResultFlashCRC_RX_FW_UPDATE_STATUS_61184_250_113();}
	public  int Get_ResultFlashCRC_RX_FW_UPDATE_COMPLETE_61184_250_112(){ return service.Get_ResultFlashCRC_RX_FW_UPDATE_COMPLETE_61184_250_112();}
	public  int Get_FirmwareInformation_SlaveID_RX_FW_UPDATE_COMPLETE_61184_250_112(){ return service.Get_FirmwareInformation_SlaveID_RX_FW_UPDATE_COMPLETE_61184_250_112();}
	public  int Get_FirmwareInformation_FWID_RX_FW_UPDATE_COMPLETE_61184_250_112(){ return service.Get_FirmwareInformation_FWID_RX_FW_UPDATE_COMPLETE_61184_250_112();}
	public  byte[] Get_FirmwareInformation_FWName_RX_FW_UPDATE_COMPLETE_61184_250_112(){ return service.Get_FirmwareInformation_FWName_RX_FW_UPDATE_COMPLETE_61184_250_112();}
	public  byte[] Get_FirmwareInformation_FWModel_RX_FW_UPDATE_COMPLETE_61184_250_112(){ return service.Get_FirmwareInformation_FWModel_RX_FW_UPDATE_COMPLETE_61184_250_112();}
	public  byte[] Get_FirmwareInformation_FWVersion_RX_FW_UPDATE_COMPLETE_61184_250_112(){ return service.Get_FirmwareInformation_FWVersion_RX_FW_UPDATE_COMPLETE_61184_250_112();}
	public  byte[] Get_FirmwareInformation_ProtoType_RX_FW_UPDATE_COMPLETE_61184_250_112(){ return service.Get_FirmwareInformation_ProtoType_RX_FW_UPDATE_COMPLETE_61184_250_112();}
	public  byte[] Get_FirmwareInformation_FWChangeDay_RX_FW_UPDATE_COMPLETE_61184_250_112(){ return service.Get_FirmwareInformation_FWChangeDay_RX_FW_UPDATE_COMPLETE_61184_250_112();}
	public  byte[] Get_FirmwareInformation_FWChangeTime_RX_FW_UPDATE_COMPLETE_61184_250_112(){ return service.Get_FirmwareInformation_FWChangeTime_RX_FW_UPDATE_COMPLETE_61184_250_112();}
	public  int Get_FirmwareInformation_FWFileSize_RX_FW_UPDATE_COMPLETE_61184_250_112(){ return service.Get_FirmwareInformation_FWFileSize_RX_FW_UPDATE_COMPLETE_61184_250_112();}
	public  int Get_FirmwareInformation_SectionUnitSize_RX_FW_UPDATE_COMPLETE_61184_250_112(){ return service.Get_FirmwareInformation_SectionUnitSize_RX_FW_UPDATE_COMPLETE_61184_250_112();}
	public  int Get_FirmwareInformation_PacketUnitSize_RX_FW_UPDATE_COMPLETE_61184_250_112(){ return service.Get_FirmwareInformation_PacketUnitSize_RX_FW_UPDATE_COMPLETE_61184_250_112();}
	public  int Get_FirmwareInformation_NumberofSectioninaFile_RX_FW_UPDATE_COMPLETE_61184_250_112(){ return service.Get_FirmwareInformation_NumberofSectioninaFile_RX_FW_UPDATE_COMPLETE_61184_250_112();}
	public  int Get_FirmwareInformation_NumberofPacketinaSection_RX_FW_UPDATE_COMPLETE_61184_250_112(){ return service.Get_FirmwareInformation_NumberofPacketinaSection_RX_FW_UPDATE_COMPLETE_61184_250_112();}
	public  int Get_FirmwareInformation_AppStartAddress_RX_FW_UPDATE_COMPLETE_61184_250_112(){ return service.Get_FirmwareInformation_AppStartAddress_RX_FW_UPDATE_COMPLETE_61184_250_112();}
	
	////////////////////////////////////////////////////////////////////
	// CALLBACK METHOD
	///////////////////////////////////////////////////////////////////
	
	@Override
	public void Callback_KeyButton(int Data) {
		// TODO Auto-generated method stub
		int N = callbacks.beginBroadcast();
		Log.d(TAG,"Callback " + Integer.toString(N));
		for(int i = 0; i < N; i++){
			try {
				callbacks.getBroadcastItem(i).KeyButtonCallBack(Data);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		callbacks.finishBroadcast();
	}
	
	@Override
	public void Callback_UpdateResponse(int Data) {
		// TODO Auto-generated method stub
		int N = callbacks.beginBroadcast();
		Log.d(TAG,"Callback " + Integer.toString(N));
		for(int i = 0; i < N; i++){
			try {
				callbacks.getBroadcastItem(i).UpdateResponseCallBack(Data);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		callbacks.finishBroadcast();
	}
	
}
