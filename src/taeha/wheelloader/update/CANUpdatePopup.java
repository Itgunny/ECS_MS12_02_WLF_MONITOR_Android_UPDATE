package taeha.wheelloader.update;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import taeha.wheelloader.update.R.string;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class CANUpdatePopup extends Dialog{
	private static final String TAG = "CANUpdatePopup";

	
	private static final int PACKET_LENGTH 		= 256;
	private static final int FWINFO_LENGTH 		= 72;
	
	private static final int FW_UPDATE_STATUS_FORMAT_UPD_MEMORY 	= 0x01;
	private static final int FW_UPDATE_STATUS_FORMAT_CPU_MEMORY 	= 0x11;
	private static final int FW_UPDATE_STATUS_COPY_UPD_CPU	 		= 0x12;
	
	
	private static final int RESULT_FLASH_CRC_OK		 			= 0x41;
	private static final int RESULT_FLASH_CRC_ERROR		 			= 0x42;
	private static final int RESULT_FLASH_CRC_NOFW		 			= 0x43;
	// CAN1CommManager
	private static CAN1CommManager CAN1Comm;
	private static MainActivity ParentActivity;
	private static _Parent_CANUpdateFragment ParentFragment;
	//++, 150716 cjg
	private  static int TotalCount = 9;
	//--, 150716 cjg
	// Thread
	protected static Thread threadUpdate = null;
	
	protected static int TIMEOUT 			= 100;
	
	protected static int RETURN_FAIL_TIMEOUT 					= 0;
	protected static int RETURN_SUCCESS 						= 1;
	protected static int RETURN_FAIL_EXIT 						= 2;
	protected static int RETURN_FAIL_CRCERROR					= 3;
	protected static int RETURN_FAIL_NOFW 						= 4;
	protected static int RETURN_FAIL_INCORRECT_STATUS 			= 5;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
		//return super.onTouchEvent(event);
	}
	public CANUpdatePopup(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public CANUpdatePopup(Context context, int theme) {
		super(context,theme);
		// TODO Auto-generated constructor stub
	}
		
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();	
		threadUpdate.interrupt();
		//ParentActivity.MenuIndex = ParentActivity.INDEX_MONITOR_TOP;
	}

	public static class Builder{
		private Context context;
		UpdaetMonitorSTM32Popup dialog;
		
		
		
		// Timeout Timer
		private Timer mTimeoutTimer = null;
		private int Count = 0;

		boolean UpdateProgramFlag = true;
	
		File UpdateFile;
		FirmwareInfoClass FirmwareInfo;
		
		// ProgressBar
		ProgressBar progressProgram;
		int nProgramProgress = 0;
		int nProgress = 0;
		
		// Status
		TextView textViewStatusProgram;
		TextView textViewStatusNumber;
		
		// ImageButton
		ImageButton imgbtnExit;

		private DialogInterface.OnDismissListener DismissListener;
		private DialogInterface.OnClickListener ExitButtonClickListener;
		
		// Constructor
		public Builder(Context context, _Parent_CANUpdateFragment _fragment){
			this.context = context;
			
			ParentActivity = (MainActivity)context;
			ParentFragment = _fragment;
		}
		
		// Button Function
		public Builder setExitButton(DialogInterface.OnClickListener listener){
			this.ExitButtonClickListener = listener;
			return this;
		}
	
		// Dismiss
		public Builder setDismiss(DialogInterface.OnDismissListener listener){
			this.DismissListener = listener;
			return this;
		}
		
		
		// Create Dialog
		
		public CANUpdatePopup create(CANUpdatePopup.Builder builder, File _updatefile, FirmwareInfoClass _firmwareinfo){
			//ParentActivity.MenuIndex = ParentActivity.INDEX_MONITOR_STM32_UPDATE;
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			final CANUpdatePopup dialog = new CANUpdatePopup(context,R.style.Dialog);
			
			View layout = inflater.inflate(R.layout.popup_update_can_progress, null);
		
			progressProgram = (ProgressBar)layout.findViewById(R.id.progressBar_popup_update_can_progress_program);
			textViewStatusProgram = (TextView)layout.findViewById(R.id.textView_popup_update_can_progress_status);
			textViewStatusNumber = (TextView)layout.findViewById(R.id.textView_popup_update_can_progress_status_number);
			imgbtnExit = (ImageButton)layout.findViewById(R.id.imageButton_popup_update_can_progress_exit);
			
			
			dialog.addContentView(layout, new LayoutParams(427,229));
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));			
			
			CAN1Comm = CAN1CommManager.getInstance();
			UpdateFile = _updatefile;
			FirmwareInfo = _firmwareinfo;
			
			UpdateProgramFlag = true;
			progressProgram.setMax(FirmwareInfo.NumberofSection * FirmwareInfo.NumberofPacket);
			textViewStatusProgram.setText("");
			textViewStatusNumber.setText("");
			
			CAN1Comm.Set_nRecAckNewFWNInfoFlag_61184_250_82(0);
			CAN1Comm.Set_nRecvFWDLCompleteFlag_61184_250_80(0);
				
			threadUpdate = new Thread(new UpdateThread(dialog, builder));
			threadUpdate.start();
			
			if(ExitButtonClickListener != null){
				
				imgbtnExit.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.d(TAG,"OKButtonClickListener");
						ExitButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
						CAN1Comm.Set_nRecAckNewFWNInfoFlag_61184_250_82(1);
						CAN1Comm.Set_nRecvFWDLCompleteFlag_61184_250_80(1);
						UpdateProgramFlag = false;
					}
				});
			}
			
	    	if(DismissListener != null){
				DismissListener.onDismiss(dialog);
			}
	    	
			return dialog;
		}
		//////////////////////////
		public void EnterDownloadMode(){
			CAN1Comm.TxCANToMCU(0x41);
			DisplayStatus("Enter DL Mode"); //++, --, 150716 cjg
			DisplayStatusNumber("(" + 1 + " / " + TotalCount + ")");
		}
		
		
		//////////////////////////////
		public int CheckApplication(FirmwareInfoClass _firmwareinfo){
			Log.d(TAG,"CheckApplication");
			int AckNewFWInfo = 0;
			SendNewFWInfo(_firmwareinfo);
			DisplayStatus("Send New FW Info"); //++, --, 150716 cjg
			DisplayStatusNumber("(" + 2 + " / " + TotalCount + ")");
			StartTimeoutTimer();
			while(AckNewFWInfo == 0){
				AckNewFWInfo = CAN1Comm.Get_nRecAckNewFWNInfoFlag_61184_250_82();	
				if(Count >= TIMEOUT){
					CancelTimeoutTimer();
					return RETURN_FAIL_TIMEOUT;
				}
				if(UpdateProgramFlag == false)
					return RETURN_FAIL_EXIT;
			}
			DisplayStatus("Ack New FW Info"); //++, --, 150716 cjg
			DisplayStatusNumber("(" + 3 + " / " + TotalCount + ")");
			CAN1Comm.Set_nRecAckNewFWNInfoFlag_61184_250_82(0);
			CancelTimeoutTimer();
			return RETURN_SUCCESS;
		}
		
		public int SendApplication(File _updatefile, FirmwareInfoClass _firmwareinfo){
			int DLComplete = 0;
			int FWID;
			int SectionIndex;
			int PacketIndex;
			int ResultFlashCRC;
			int RetryCount;
			RetryCount = 0;
			CAN1Comm.Set_nRecvRequestPacketMFlag_61184_250_83(0);
			
			CAN1Comm.TxCANToMCU(0x40);
			StartTimeoutTimer();
			DisplayStatus("App DL Start"); //++, --, 150716 cjg
			DisplayStatusNumber("(" + 4 + " / " + TotalCount + ")");
			while(DLComplete == 0){
				DLComplete = CAN1Comm.Get_nRecvFWDLCompleteFlag_61184_250_80();
				
				if(CAN1Comm.Get_nRecvRequestPacketMFlag_61184_250_83() == 1){
					CAN1Comm.Set_nRecvRequestPacketMFlag_61184_250_83(0);
					FWID = CAN1Comm.Get_FWID_RX_REQUEST_PACKET_M_61184_250_83();
					SectionIndex = CAN1Comm.Get_SectionNumber_RX_REQUEST_PACKET_M_61184_250_83();
					PacketIndex = CAN1Comm.Get_PacketNumber_RX_REQUEST_PACKET_M_61184_250_83();

					SendPacket(GetPacketfromFile(_updatefile, _firmwareinfo, SectionIndex, PacketIndex),SectionIndex,PacketIndex,_firmwareinfo.PacketUnitSize);

					DisplayProgress((SectionIndex * _firmwareinfo.NumberofPacket) + PacketIndex);
					DisplayStatus("Section : " + Integer.toString(SectionIndex) + ", Packet : " + Integer.toString(PacketIndex));
					DisplayStatusNumber("(" + 5 + " / " + TotalCount + ")");
					CancelTimeoutTimer();
					StartTimeoutTimer();
				}
				
				if(Count >= TIMEOUT){
					if(RetryCount == 0){
						Log.d(TAG, "RetryCount"+RetryCount);
						RetryCount++;
						StartTimeoutTimer();
						//CAN1Comm.TxCANToMCU(0x40);
						//DisplayStatus("App DL Start");
					}else{
						Log.d(TAG, "RetryCount"+RetryCount);
						CancelTimeoutTimer();
						return RETURN_FAIL_TIMEOUT;
					}
					
				}
				if(UpdateProgramFlag == false)
					return RETURN_FAIL_EXIT;
			}
			
			CancelTimeoutTimer();
			
			CAN1Comm.Set_nRecvFWDLCompleteFlag_61184_250_80(0);
			ResultFlashCRC = CAN1Comm.Get_ResultFlashCRC_RX_FW_N_DL_COMPLETE_61184_250_80();
			
			if(ResultFlashCRC == RESULT_FLASH_CRC_OK){
				Log.d(TAG,"ResultFlash  CRC OK");
				DisplayStatus("FW DL Complete : CRC OK");
				return RETURN_SUCCESS;
			}
			else if(ResultFlashCRC == RESULT_FLASH_CRC_ERROR){
				Log.e(TAG,"ResultFlash  CRC Error");
				DisplayWarningStatus("FW DL Complete : CRC Error");
				return RETURN_FAIL_CRCERROR;
			}else if(ResultFlashCRC == RESULT_FLASH_CRC_NOFW){
				Log.e(TAG,"ResultFlash No F/W");
				DisplayWarningStatus("FW DL Complete : No F/W");
				return RETURN_FAIL_NOFW;
			}
			else{
				Log.e(TAG,"ResultFlash ???");
				DisplayStatus("F/W DL Complete, ???");
				return RETURN_FAIL_INCORRECT_STATUS;
			}
	
		}
		
		public void CheckFWUpdateComplete(){
			int UpdateComplete;
			int oldUpdateStatus = 0;
			int UpdateStatus;
			int UpdateProgress_CRC;
			int countFlag = 0;
			//++, 150716 cjg
			progressProgram.setProgress(0);
			progressProgram.setMax(100);
			//--, 150716 cjg
			DisplayStatusNumber("(" + 8 + " / " + TotalCount + ")");
			while(UpdateProgramFlag == true){
				UpdateComplete = CAN1Comm.Get_nRecvFWUpdateCompleteFlag_61184_250_112();
				if(UpdateComplete == 1){
					DisplayStatusNumber("(" + 9 + " / " + TotalCount + ")");
					Log.d(TAG, "9");
					if(CAN1Comm.Get_ResultFlashCRC_RX_FW_UPDATE_COMPLETE_61184_250_112() == RESULT_FLASH_CRC_OK){
						DisplayStatus("FW Update Complete, CRC OK\nRestart Machine!!");
					}else if(CAN1Comm.Get_ResultFlashCRC_RX_FW_UPDATE_COMPLETE_61184_250_112() == RESULT_FLASH_CRC_ERROR){
						DisplayStatus("FW Update Complete, CRC Error\nUpdate Fail");
					}else if(CAN1Comm.Get_ResultFlashCRC_RX_FW_UPDATE_COMPLETE_61184_250_112() == RESULT_FLASH_CRC_NOFW){
						DisplayStatus("FW Update Complete, No F/W\nUpdate Fail");
					}
					UpdateProgramFlag = false;
				}else{
					UpdateStatus = CAN1Comm.Get_Status_RX_FW_UPDATE_STATUS_61184_250_113();
					UpdateProgress_CRC = CAN1Comm.Get_Progress_ResultFlashCRC_RX_FW_UPDATE_STATUS_61184_250_113();
					switch (UpdateStatus) {
					case FW_UPDATE_STATUS_FORMAT_UPD_MEMORY:
						DisplayProgress(UpdateProgress_CRC); //++, --,  150715 cjg 
						DisplayStatus("Format UPD region : " + Integer.toString(UpdateProgress_CRC)); //++, --, 150716 cjg
						//DisplayStatusNumber("(" + 10 + " / " + TotalCount + ")"); //++, --, 150717 cjg
						break;
					case FW_UPDATE_STATUS_FORMAT_CPU_MEMORY:
						DisplayProgress(UpdateProgress_CRC); //++, --,  150715 cjg
						DisplayStatus("Format CPU region : " + Integer.toString(UpdateProgress_CRC)); //++, --, 150716 cjg
						//DisplayStatusNumber("(" + 9 + " / " + TotalCount + ")");//++, --, 150717 cjg
						break;
					case FW_UPDATE_STATUS_COPY_UPD_CPU:
						DisplayProgress(UpdateProgress_CRC); //++, --,  150715 cjg
						DisplayStatus("Copy UPD region to CPU region: " + Integer.toString(UpdateProgress_CRC)); //++, --, 150716 cjg
						//DisplayStatusNumber("(" + 8 + " / " + TotalCount + ")"); //++, --, 150717 cjg
						break;	
					default:
						//DisplayStatus("Preparing the ECU Updates");
						break;
					}
				}
			}
			CAN1Comm.Set_nRecvFWUpdateCompleteFlag_61184_250_112(0);
		}
			
		
		/////////////////////////////
		public void SendDownloadModeFinish(){			
			CAN1Comm.TxCANToMCU(0x51);
			DisplayStatus("Quit DL Mode"); //++, --, 150716 cjg
			DisplayStatusNumber("(" + 6 + " / " + TotalCount + ")");
		}
		
		////////////////////////////
		public void EnterAppUpdate(){
			CAN1Comm.TxCANToMCU(0x60);
			DisplayStatus("Preparing the ECU Updates"); //++, --, 150716 cjg
			DisplayStatusNumber("(" + 7 + " / " + TotalCount + ")");
		}
		
		////////////////////////////
		public void SendNewFWInfo(FirmwareInfoClass _firmwareinfo){
			CAN1Comm.Set_FirmwareInformation_SlaveID_TX_SEND_NEW_FW_N_INFO_61184_250_66(_firmwareinfo.SlaveID);
			CAN1Comm.Set_FirmwareInformation_FWID_TX_SEND_NEW_FW_N_INFO_61184_250_66(_firmwareinfo.FWID);
			CAN1Comm.Set_FirmwareInformation_FWName_TX_SEND_NEW_FW_N_INFO_61184_250_66(_firmwareinfo.FWName);
			CAN1Comm.Set_FirmwareInformation_FWModel_TX_SEND_NEW_FW_N_INFO_61184_250_66(_firmwareinfo.FWModel);
			CAN1Comm.Set_FirmwareInformation_FWVersion_TX_SEND_NEW_FW_N_INFO_61184_250_66(_firmwareinfo.FWVersion);
			CAN1Comm.Set_FirmwareInformation_ProtoType_TX_SEND_NEW_FW_N_INFO_61184_250_66(_firmwareinfo.ProtoVer);
			CAN1Comm.Set_FirmwareInformation_FWChangeDay_TX_SEND_NEW_FW_N_INFO_61184_250_66(_firmwareinfo.Date);
			CAN1Comm.Set_FirmwareInformation_FWChangeTime_TX_SEND_NEW_FW_N_INFO_61184_250_66(_firmwareinfo.Time);
			CAN1Comm.Set_FirmwareInformation_FWFileSize_TX_SEND_NEW_FW_N_INFO_61184_250_66(_firmwareinfo.FWFileSize);
			CAN1Comm.Set_FirmwareInformation_SectionUnitSize_TX_SEND_NEW_FW_N_INFO_61184_250_66(_firmwareinfo.SectionUnitSize);
			CAN1Comm.Set_FirmwareInformation_PacketUnitSize_TX_SEND_NEW_FW_N_INFO_61184_250_66(_firmwareinfo.PacketUnitSize);
			CAN1Comm.Set_FirmwareInformation_NumberofSectioninaFile_TX_SEND_NEW_FW_N_INFO_61184_250_66(_firmwareinfo.NumberofSection);
			CAN1Comm.Set_FirmwareInformation_NumberofPacketinaSection_TX_SEND_NEW_FW_N_INFO_61184_250_66(_firmwareinfo.NumberofPacket);
			CAN1Comm.Set_FirmwareInformation_AppStartAddress_TX_SEND_NEW_FW_N_INFO_61184_250_66(_firmwareinfo.AppstartAddress);
			int i;
			for(i = 0; i < _firmwareinfo.NumberofSection; i++)
				CAN1Comm.Set_FirmwareInformation_CRC_TX_SEND_NEW_FW_N_INFO_61184_250_66(i, _firmwareinfo.AppSectionCRC[i]);
			
			CAN1Comm.Set_FirmwareInformation_CRC_TX_SEND_NEW_FW_N_INFO_61184_250_66(i, _firmwareinfo.AppCRC);
			
			CAN1Comm.Set_nLength_61184_250_66(FWINFO_LENGTH + 5 + ((_firmwareinfo.NumberofSection + 1) * 2));
			
			
			CAN1Comm.TxCANToMCU(0x42);
			
			Log.d(TAG,"SendNewFWInfo");
		}
		
		
		public byte[] GetPacketfromFile(File _file, FirmwareInfoClass _firmwareinfo, int _sectionindex, int _packetindex){
			byte[] Packet = new byte[_firmwareinfo.PacketUnitSize];
			InputStream is;
			int nRead;
			int FileOffset;
			
			for(int i = 0; i < _firmwareinfo.PacketUnitSize; i++)
				Packet[i] = (byte)0xFF;
			
			FileOffset = (_firmwareinfo.SectionUnitSize * 1024 * _sectionindex) + (FirmwareInfo.PacketUnitSize * _packetindex);
			
			try {
				is = new FileInputStream(_file);
				is.skip(FileOffset);
				nRead = is.read(Packet, 0, _firmwareinfo.PacketUnitSize);
				is.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				Log.e(TAG,"GetPacketfromFile, No Files");
				e.printStackTrace();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(TAG,"GetPacketfromFile, Read Faile!!!");
				e.printStackTrace();
			}
			return Packet;
		}
		
		public void SendPacket(byte[] _packet, int _sectionindex, int _packetindex, int _packetlength){
			int CRC;
			CRC = MakeCrc16(_packet, Crc16Table, _packetlength);
			
			CAN1Comm.Set_SectionNumber_TX_SEND_PACKET_M_61184_250_67(_sectionindex);
			CAN1Comm.Set_PacketNumber_TX_SEND_PACKET_M_61184_250_67(_packetindex);
			CAN1Comm.Set_PacketLength_TX_SEND_PACKET_M_61184_250_67(_packetlength);
			CAN1Comm.Set_DistributionFileData_TX_SEND_PACKET_M_61184_250_67(_packet);
			CAN1Comm.Set_PacketCRC_TX_SEND_PACKET_M_61184_250_67(CRC);
			
			CAN1Comm.TxCANToMCU(0x43);
		}
		
		public void DisplayProgress(final int progress){
			ParentActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					progressProgram.setProgress(progress);
				}
			});
		}
		
		public void DisplayStatus(final String status){
			ParentActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					textViewStatusProgram.setText(status);
					ParentFragment.StatusDisplay(status);
				}
			});
		}
		public void DisplayWarningStatus(final String status){
			ParentActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					textViewStatusProgram.setText(status);
					ParentFragment.StatusWarningDisplay(status);
				}
			});
		}
		
		public void DisplayStatusNumber(final String status){
			ParentActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					textViewStatusNumber.setText(status);
				}
			});
		}
		/////////////////////////////////////////////////////////
		
		/////////////////////////////////////Download Seq///////////////////////////////////////////////////////
		// Update Thread
		public static class UpdateThread implements Runnable {
			private WeakReference<CANUpdatePopup.Builder> BuilderRef = null;
			private WeakReference<CANUpdatePopup> DialogRef = null;
			public Message msg = null;
			public UpdateThread(CANUpdatePopup _dialog, CANUpdatePopup.Builder _bulder){
				this.BuilderRef = new WeakReference<CANUpdatePopup.Builder>(_bulder);
				this.DialogRef = new WeakReference<CANUpdatePopup>(_dialog);
				msg = new Message();
			}
		
			@Override
			public void run() {

				try{	
					int ReturnValue;
					Log.d(TAG,"UpdateThread");
					
					/////////////////////////////////Step 1. Enter Download Mode////////////////////////////
					BuilderRef.get().EnterDownloadMode();
					Thread.sleep(2000);
					/////////////////////////////////Step 2. Check Application//////////////////////////////
					ReturnValue = BuilderRef.get().CheckApplication(BuilderRef.get().FirmwareInfo);
					if(ReturnValue == RETURN_FAIL_TIMEOUT){
						Log.e(TAG,"CheckApplication Fail 1");
						ReturnValue = BuilderRef.get().CheckApplication(BuilderRef.get().FirmwareInfo);
						if(ReturnValue == RETURN_FAIL_TIMEOUT){
							Log.e(TAG,"CheckApplication Fail 2 TimeOut");
							BuilderRef.get().showToast(ParentActivity.getResources().getString(string.Update_Fail));
							BuilderRef.get().DisplayWarningStatus(ParentActivity.getResources().getString(string.Update_Fail)+"\nDo Not Receive Ack new F/W Info");
							DialogRef.get().dismiss();
							return;
						}else if(ReturnValue == RETURN_FAIL_EXIT){
							Log.e(TAG,"CheckApplication Fail Exit");
							BuilderRef.get().showToast(ParentActivity.getResources().getString(string.Update_Fail));
						//	BuilderRef.get().DisplayStatus(ParentActivity.getResources().getString(string.Update_Fail)+"\nSend New F/W Info Exit");
							DialogRef.get().dismiss();
							return;
						}
					}else if(ReturnValue == RETURN_FAIL_EXIT){
						Log.e(TAG,"CheckApplication Fail Exit");
						BuilderRef.get().showToast(ParentActivity.getResources().getString(string.Update_Fail));
					//	BuilderRef.get().DisplayStatus(ParentActivity.getResources().getString(string.Update_Fail)+"\nSend New F/W Info Exit");
						DialogRef.get().dismiss();
						return;
					}
					
					/////////////////////////////////Step 3. Send Application//////////////////////////////
					ReturnValue = BuilderRef.get().SendApplication(BuilderRef.get().UpdateFile, BuilderRef.get().FirmwareInfo);
					if(ReturnValue == RETURN_FAIL_TIMEOUT){
						Log.e(TAG,"SendApplication Fail RETURN_FAIL_TIMEOUT");
						BuilderRef.get().showToast(ParentActivity.getResources().getString(string.Update_Fail));
						BuilderRef.get().DisplayWarningStatus(ParentActivity.getResources().getString(string.Update_Fail)+"\nDo Not Receive Request Packet");
						DialogRef.get().dismiss();
						return;
					}else if(ReturnValue == RETURN_FAIL_EXIT){
						Log.e(TAG,"SendApplication Fail RETURN_FAIL_EXIT");
						BuilderRef.get().showToast(ParentActivity.getResources().getString(string.Update_Fail));
						//BuilderRef.get().DisplayStatus(ParentActivity.getResources().getString(string.Update_Fail)+"\nSend Application Exit");
						DialogRef.get().dismiss();
						return;
					}
					else if(ReturnValue == RETURN_FAIL_NOFW){
						Log.e(TAG,"SendApplication Fail RETURN_FAIL_NOFW");
						BuilderRef.get().showToast(ParentActivity.getResources().getString(string.Update_Fail));
						//BuilderRef.get().DisplayStatus(ParentActivity.getResources().getString(string.Update_Fail)+"\nSend Application No Firmware");
						DialogRef.get().dismiss();
						return;
					}
					else if(ReturnValue == RETURN_FAIL_CRCERROR){
						Log.e(TAG,"SendApplication Fail RETURN_FAIL_CRCERROR");
						BuilderRef.get().showToast(ParentActivity.getResources().getString(string.Update_Fail));
						//BuilderRef.get().DisplayStatus(ParentActivity.getResources().getString(string.Update_Fail)+"\nSend Application No Firmware");
						DialogRef.get().dismiss();
						return;
					}
					else if(ReturnValue == RETURN_FAIL_INCORRECT_STATUS){
						Log.e(TAG,"SendApplication Fail RETURN_FAIL_INCORRECT_STATUS");
						BuilderRef.get().showToast(ParentActivity.getResources().getString(string.Update_Fail));
						BuilderRef.get().DisplayWarningStatus(ParentActivity.getResources().getString(string.Update_Fail)+"\nSend Application Incorrect Status");
						DialogRef.get().dismiss();
						return;
					}
					
					/////////////////////////////////Step 4. Send Download Mode Finish////////////////////////////
					BuilderRef.get().SendDownloadModeFinish();
					
					/////////////////////////////////Step 5. Send Enter Application Update////////////////////////
					BuilderRef.get().EnterAppUpdate();
					
					/////////////////////////////////Step 6. Check Firmware Update Status/////////////////////////
					BuilderRef.get().CheckFWUpdateComplete();

					/////////////////////////////////Step 7. Finish CAN Update////////////////////////////////////
					BuilderRef.get().showToast(ParentActivity.getResources().getString(string.Update_Finish));
					DialogRef.get().dismiss();
				}

				catch(RuntimeException ee){
					Log.e(TAG,"RuntimeException");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e(TAG,"InterruptedException");
				}
					
			}
		
		}
		
		public void showToast(final String str){
			ParentActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(ParentActivity, str, Toast.LENGTH_LONG).show();
					Log.d(TAG,"Update Complete!!!");
				}
			});
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		///////////////////////////////////// Timer////////////////////////////////////////////////////////////
		
		public class TimeoutTimerClass extends TimerTask{

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ParentActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Count++;
					}
				});
				
			}
			
		}
		
		public void StartTimeoutTimer(){
			CancelTimeoutTimer();
			mTimeoutTimer = new Timer();
			mTimeoutTimer.schedule(new TimeoutTimerClass(),0,100);	
		}
		
		public void CancelTimeoutTimer(){
			Count = 0;
			if(mTimeoutTimer != null){
				mTimeoutTimer.cancel();
				mTimeoutTimer.purge();
				mTimeoutTimer = null;
			}
			
		}
		//////////////////////////////////////////////////////////////////////////////////////////////////
		
		public int MakeCrc16(byte[] data, int[] Crc16Table, int nLen)
		{
			int i = 0;
			int crc16 = 0;
			while(nLen != 0)
			{
				
				crc16 =  (Crc16Table[ (((crc16 & 0xFFFF) ^ ((int)data[i] & 0xFF)) & 0xFF)] ^ ((crc16 & 0xFFFF) >> 8));
				crc16 = crc16 & 0xFFFF;
				nLen--;
				i++;
			}
			
			return crc16;
		}
		
		int Crc16Table[] = {
				0x0000, 0xC0C1, 0xC181, 0x0140, 0xC301, 0x03C0, 0x0280, 0xC241,
				   0xC601, 0x06C0, 0x0780, 0xC741, 0x0500, 0xC5C1, 0xC481, 0x0440,
				   0xCC01, 0x0CC0, 0x0D80, 0xCD41, 0x0F00, 0xCFC1, 0xCE81, 0x0E40,
				   0x0A00, 0xCAC1, 0xCB81, 0x0B40, 0xC901, 0x09C0, 0x0880, 0xC841,
				   0xD801, 0x18C0, 0x1980, 0xD941, 0x1B00, 0xDBC1, 0xDA81, 0x1A40,
				   0x1E00, 0xDEC1, 0xDF81, 0x1F40, 0xDD01, 0x1DC0, 0x1C80, 0xDC41,
				   0x1400, 0xD4C1, 0xD581, 0x1540, 0xD701, 0x17C0, 0x1680, 0xD641,
				   0xD201, 0x12C0, 0x1380, 0xD341, 0x1100, 0xD1C1, 0xD081, 0x1040,
				   0xF001, 0x30C0, 0x3180, 0xF141, 0x3300, 0xF3C1, 0xF281, 0x3240,
				   0x3600, 0xF6C1, 0xF781, 0x3740, 0xF501, 0x35C0, 0x3480, 0xF441,
				   0x3C00, 0xFCC1, 0xFD81, 0x3D40, 0xFF01, 0x3FC0, 0x3E80, 0xFE41,
				   0xFA01, 0x3AC0, 0x3B80, 0xFB41, 0x3900, 0xF9C1, 0xF881, 0x3840,
				   0x2800, 0xE8C1, 0xE981, 0x2940, 0xEB01, 0x2BC0, 0x2A80, 0xEA41,
				   0xEE01, 0x2EC0, 0x2F80, 0xEF41, 0x2D00, 0xEDC1, 0xEC81, 0x2C40,
				   0xE401, 0x24C0, 0x2580, 0xE541, 0x2700, 0xE7C1, 0xE681, 0x2640,
				   0x2200, 0xE2C1, 0xE381, 0x2340, 0xE101, 0x21C0, 0x2080, 0xE041,
				   0xA001, 0x60C0, 0x6180, 0xA141, 0x6300, 0xA3C1, 0xA281, 0x6240,
				   0x6600, 0xA6C1, 0xA781, 0x6740, 0xA501, 0x65C0, 0x6480, 0xA441,
				   0x6C00, 0xACC1, 0xAD81, 0x6D40, 0xAF01, 0x6FC0, 0x6E80, 0xAE41,
				   0xAA01, 0x6AC0, 0x6B80, 0xAB41, 0x6900, 0xA9C1, 0xA881, 0x6840,
				   0x7800, 0xB8C1, 0xB981, 0x7940, 0xBB01, 0x7BC0, 0x7A80, 0xBA41,
				   0xBE01, 0x7EC0, 0x7F80, 0xBF41, 0x7D00, 0xBDC1, 0xBC81, 0x7C40,
				   0xB401, 0x74C0, 0x7580, 0xB541, 0x7700, 0xB7C1, 0xB681, 0x7640,
				   0x7200, 0xB2C1, 0xB381, 0x7340, 0xB101, 0x71C0, 0x7080, 0xB041,
				   0x5000, 0x90C1, 0x9181, 0x5140, 0x9301, 0x53C0, 0x5280, 0x9241,
				   0x9601, 0x56C0, 0x5780, 0x9741, 0x5500, 0x95C1, 0x9481, 0x5440,
				   0x9C01, 0x5CC0, 0x5D80, 0x9D41, 0x5F00, 0x9FC1, 0x9E81, 0x5E40,
				   0x5A00, 0x9AC1, 0x9B81, 0x5B40, 0x9901, 0x59C0, 0x5880, 0x9841,
				   0x8801, 0x48C0, 0x4980, 0x8941, 0x4B00, 0x8BC1, 0x8A81, 0x4A40,
				   0x4E00, 0x8EC1, 0x8F81, 0x4F40, 0x8D01, 0x4DC0, 0x4C80, 0x8C41,
				   0x4400, 0x84C1, 0x8581, 0x4540, 0x8701, 0x47C0, 0x4680, 0x8641,
				   0x8201, 0x42C0, 0x4380, 0x8341, 0x4100, 0x81C1, 0x8081, 0x4040
		};
		
	}
}