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
import android.os.Handler;
import android.os.Message;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;


public class UpdaetMonitorSTM32Popup extends Dialog{
	private static final String TAG = "UpdaetMonitorSTM32Popup";

	
	private static final int FILE_DOWN_ENABLE 				= 0x01;
	private static final int FILE_DOWN_FINISH 				= 0x02;
	private static final int FILE_DOWN_VERIFY 				= 0x03;
	private static final int FILE_DOWN_PROGRAM_FAIL 		= 0x04;
	private static final int FILE_DOWN_VERIFY_FAIL 			= 0x05;
	private static final int FILE_DOWN_ENABLE_RES 			= 0x01;
	private static final int FILE_DOWN_FINISH_RES 			= 0x02;
	private static final int FILE_DOWN_VERIFY_RES 			= 0x03;
	private static final int FILE_DOWN_PROGRAM_FAIL_RES 	= 0x04;
	private static final int FILE_DOWN_VERIFY_FAIL_RES 		= 0x05;
	
	private static final int STX = 0x02;
	private static final int ETX = 0x03;
	private static final int EOT = 0x04;
	private static final int ACK = 0x06;
	private static final int NAK = 0x15;
	
	
	private static final int TIMEOUT 			= 100;

	// CAN1CommManager
	private static CAN1CommManager CAN1Comm;
	private static MainActivity ParentActivity;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
		//return super.onTouchEvent(event);
	}
	public UpdaetMonitorSTM32Popup(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public UpdaetMonitorSTM32Popup(Context context, int theme) {
		super(context,theme);
		// TODO Auto-generated constructor stub
	}
		
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();	
		ParentActivity.MenuIndex = ParentActivity.INDEX_MONITOR_TOP;
		
	}

	public static class Builder{
		private Context context;
		UpdaetMonitorSTM32Popup dialog;
		
		// Thread
		protected static Thread threadUpdate = null;
		
		// Timeout Timer
		private Timer mTimeoutTimer = null;
		private int Count = 0;

		
		int Response = 0;
		boolean UpdateProgramFlag = false;
		boolean UpdateVerifyFlag = false;
		int FactoryInitFlag;
	
		
		// ProgressBar
		ProgressBar progressErase;
		ProgressBar progressProgram;
		ProgressBar progressVerify;
		int nProgramProgress = 0;
		int nVerifyProgress = 0;
		
		int nProgress = 0;

		private DialogInterface.OnDismissListener DismissListener;
			
		// Constructor
		public Builder(Context context){
			this.context = context;
			
			ParentActivity = (MainActivity)context;
		}
		
	
		// Dismiss
		public Builder setDismiss(DialogInterface.OnDismissListener listener){
			this.DismissListener = listener;
			return this;
		}
		
		
		// Create Dialog
		
		public UpdaetMonitorSTM32Popup create(UpdaetMonitorSTM32Popup.Builder builder, int _FactoryInitFlag){
			ParentActivity.MenuIndex = ParentActivity.INDEX_MONITOR_STM32_UPDATE;
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			dialog = new UpdaetMonitorSTM32Popup(context,R.style.Dialog);
			
			View layout = inflater.inflate(R.layout.popup_update_monitor_stm32, null);
			progressErase = (ProgressBar)layout.findViewById(R.id.progressBar_popup_update_monitor_stm32_erase);
			progressProgram = (ProgressBar)layout.findViewById(R.id.progressBar_popup_update_monitor_stm32_program);
			progressVerify = (ProgressBar)layout.findViewById(R.id.progressBar_popup_update_monitor_stm32_verify);
			progressErase.setVisibility(View.VISIBLE);
			
			dialog.addContentView(layout, new LayoutParams(427,229));
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));		
			
			FactoryInitFlag = _FactoryInitFlag;
			
			CAN1Comm = CAN1CommManager.getInstance();
	
			threadUpdate = new Thread(new UpdateThread(dialog, builder));
			threadUpdate.start();

	    	if(DismissListener != null){
				DismissListener.onDismiss(dialog);
			}
	    	
			return dialog;
		}
		
		public void recResponse(int response){
			Response = response;
			Log.d(TAG,"recResponse : " + Integer.toString(Response));
		}
		public void ClearResponse(){
			Response = 0;
		}
		public void sendCMD(int cmd,int data,int flag){
			CAN1Comm.TxCMDToMCU(cmd,data,flag);
		}	
		
		/////////////////////////////////////Download Seq///////////////////////////////////////////////////////
		// Update Thread
		public static class UpdateThread implements Runnable {
			private WeakReference<UpdaetMonitorSTM32Popup.Builder> BuilderRef = null;
			private WeakReference<UpdaetMonitorSTM32Popup> DialogRef = null;
			public Message msg = null;
			public UpdateThread(UpdaetMonitorSTM32Popup _dialog, UpdaetMonitorSTM32Popup.Builder _bulder){
				this.BuilderRef = new WeakReference<UpdaetMonitorSTM32Popup.Builder>(_bulder);
				this.DialogRef = new WeakReference<UpdaetMonitorSTM32Popup>(_dialog);
				msg = new Message();
			}
		
			@Override
			public void run() {

				try{				
					Log.d(TAG,"UpdateThread");
					if(BuilderRef.get().UpdateProgram() == false){
						Log.e(TAG,"Program Fail");
						BuilderRef.get().DisplayWarningStatus(ParentActivity.getResources().getString(string.Program_Fail));
						DialogRef.get().dismiss();
						return;
					}
					Thread.sleep(1000);
					if(BuilderRef.get().VerifyProgram() == false){
						Log.e(TAG,"Verify Fail");
						BuilderRef.get().DisplayWarningStatus(ParentActivity.getResources().getString(string.Verify_Fail));
						DialogRef.get().dismiss();
						return;
					}	
					BuilderRef.get().DisplayStatus(ParentActivity.getResources().getString(string.Update_Finish));
					DialogRef.get().dismiss();
				}
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Log.e(TAG,"InterruptedException");
				}
				catch(RuntimeException ee){
					Log.e(TAG,"RuntimeException");
				}
					
			}
		
		}
		public void DisplayWarningStatus(final String status){
			ParentActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					ParentActivity._MonitorFragment.StatusWarningDisplay(status);
				}
			});
		}
		public void DisplayStatus(final String status){
			ParentActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					ParentActivity._MonitorFragment.StatusDisplay(status);
				}
			});
		}
		public void showToast(final String str){
			ParentActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(ParentActivity, str, Toast.LENGTH_LONG).show();
				}
			});
		}
		
		// Erase Seq.
		public boolean EraseFlash(){
			sendCMD(CAN1Comm.CMD_DOWN,FILE_DOWN_ENABLE,FactoryInitFlag);
			StartTimeoutTimer();
			Log.d(TAG,"wait Flash Erase Response");
			while(Response != FILE_DOWN_ENABLE_RES){
				//Log.d(TAG,"wait Flash Erase Response");
				if(Count > TIMEOUT){
					ClearResponse();
					Log.e(TAG,"Flash Erase Timeout");
					CancelTimeoutTimer();
					return false;
				}
				
			}
			ClearResponse();
			Log.d(TAG,"Success Flash Erase Response");
			CancelTimeoutTimer();
			return true;
		}
		
		// Verify Seq.
		public boolean Verify(){
			sendCMD(CAN1Comm.CMD_DOWN,FILE_DOWN_VERIFY,FactoryInitFlag);
			StartTimeoutTimer();
			while(Response != FILE_DOWN_VERIFY_RES){
				//Log.d(TAG,"wait Verify Response");
				if(Count > TIMEOUT){
					ClearResponse();
					Log.e(TAG,"Verify Timeout");
					CancelTimeoutTimer();
					return false;
				}
				
			}
			ClearResponse();
			CancelTimeoutTimer();
			return true;
		}
				
		// Program Finish Seq.
		public boolean ProgramFinish(){
			sendCMD(CAN1Comm.CMD_DOWN,FILE_DOWN_FINISH,FactoryInitFlag);
			StartTimeoutTimer();
			Log.d(TAG,"wait Program Finish Response");
			while(Response != FILE_DOWN_FINISH_RES){
				//Log.d(TAG,"wait Program Finish Response");
				if(Count > TIMEOUT){
					ClearResponse();
					Log.e(TAG,"Program Finish Timeout");
					CancelTimeoutTimer();
					return false;
				}
				
			}
			ClearResponse();
			Log.d(TAG,"Success  Program Finish Response");
			CancelTimeoutTimer();
			return true;
		}
		
		
		
		public boolean UpdateProgram(){
			// TODO Auto-generated method stub
			if(EraseFlash() == false){
				return false;
			}
			UpdateFileFindClass UpdateFile;
			UpdateFile = new UpdateFileFindClass(ParentActivity);
			
			File f;
			InputStream is;
			long Length = 0;
			long Index = 0;
			int TotalPacket = 0;
			int nRead = 0;
			int CRC = 0;
			int RetryCount = 0;
			
			if(FactoryInitFlag == 1){
				f = UpdateFile.GetMonitorSTM32FactoryInitUpdateFile();
			}else{
				f = UpdateFile.GetMonitorSTM32UpdateFile();
			}
			if(f == null){
				return false;
			}
			
			try {
				is = new FileInputStream(f);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				Log.e(TAG,"No Files");
				e.printStackTrace();
				return false;
			}
			
			Length = f.length();
			
			
			byte[] FileData = new byte[1024];
			byte[] SendData = new byte[1030];
			
			SendData[0] = STX;
			SendData[1] = 0;
			SendData[2] = 0;
			SendData[1029] = ETX;
			
			if(Length % 1024 == 0){
				TotalPacket = (int) (Length / 1024);
			}else{
				TotalPacket = (int) (Length / 1024) + 1;
			}
		
			
			progressProgram.setMax(TotalPacket);
			progressVerify.setMax(TotalPacket);
			
		
			for(nProgress = 0; nProgress < TotalPacket; nProgress++){
				
				for(int j = 0; j < 1024; j++){
					FileData[j] = (byte)0xFF;
				}
				
				try {
					nRead = is.read(FileData, 0, 1024);
					CRC = MakeCrc16(FileData, Crc16Table, 1024);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				SendData[1] = (byte) (nProgress & 0x00FF);
				SendData[2] = (byte) ((nProgress & 0xFF00) >> 8);
				
				for(int k = 0; k < 1024; k++){
					SendData[k+3] = FileData[k];
				}
				
				SendData[1027] = (byte) (CRC & 0x00FF);
				SendData[1028] = (byte) ((CRC & 0xFF00) >> 8);
				
				if(nProgress == TotalPacket - 1){
					SendData[1029] = EOT;
				}else{
					SendData[1029] = ETX;
				}
				
				if(ProgramUpdateFile(SendData, 1030) == false){
					try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return false;
				}else{
					ParentActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							progressProgram.setProgress(nProgress+1);
							nProgramProgress = nProgress+1;
						}
					});					
				}
			}
			
			return true;
		}
		
		public boolean VerifyProgram(){
			
			
			// TODO Auto-generated method stub
			if(Verify() == false){
				return false;
			}
			UpdateFileFindClass UpdateFile;
			UpdateFile = new UpdateFileFindClass(ParentActivity);
			
			File f;
			InputStream is;
			long Length = 0;
			long Index = 0;
			int TotalPacket = 0;
			int nRead = 0;
			int CRC = 0;
			int RetryCount = 0;
			
			if(FactoryInitFlag == 1){
				f = UpdateFile.GetMonitorSTM32FactoryInitUpdateFile();
			}else{
				f = UpdateFile.GetMonitorSTM32UpdateFile();
			}
			if(f == null){
				return false;
			}
			
			try {
				is = new FileInputStream(f);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				Log.e(TAG,"No Files");
				e.printStackTrace();
				return false;
			}
			
			Length = f.length();
			
			
			byte[] FileData = new byte[1024];
			byte[] SendData = new byte[1030];
			
			SendData[0] = STX;
			SendData[1] = 0;
			SendData[2] = 0;
			SendData[1029] = ETX;
			
			if(Length % 1024 == 0){
				TotalPacket = (int) (Length / 1024);
			}else{
				TotalPacket = (int) (Length / 1024) + 1;
			}
		
			progressProgram.setMax(TotalPacket);
			progressVerify.setMax(TotalPacket);
			
		
			for(nProgress = 0; nProgress < TotalPacket; nProgress++){
				
				for(int j = 0; j < 1024; j++){
					FileData[j] = (byte)0xFF;
				}
				
				try {
					nRead = is.read(FileData, 0, 1024);
					CRC = MakeCrc16(FileData, Crc16Table, 1024);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				SendData[1] = (byte) (nProgress & 0x00FF);
				SendData[2] = (byte) ((nProgress & 0xFF00) >> 8);
				
				for(int k = 0; k < 1024; k++){
					SendData[k+3] = FileData[k];
				}
				
				SendData[1027] = (byte) (CRC & 0x00FF);
				SendData[1028] = (byte) ((CRC & 0xFF00) >> 8);
				
				if(nProgress == TotalPacket - 1){
					SendData[1029] = EOT;
				}else{
					SendData[1029] = ETX;
				}
				
				if(VerifyUpdateFile(SendData, 1030) == false){
					try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return false;
				}else{
					ParentActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							progressVerify.setProgress(nProgress+1);
							nVerifyProgress = nProgress+1;
						}
					});
					
				}
			}
			if(ProgramFinish() == false){
				return false;
			}
					
			
			ParentActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					progressErase.setVisibility(View.INVISIBLE);
				}
			});
					
			return true;
		}
		

		public boolean ProgramUpdateFile(byte[] Data,int size){
			int	RetryCount = 0;
			StartTimeoutTimer();
			CAN1Comm.TxUpdate(Data, size);
			while(true){
				if(Response == ACK){
					CancelTimeoutTimer();
					ClearResponse();
					return true;
				}
				if(Response == NAK){
					CancelTimeoutTimer();
					ClearResponse();
					RetryCount++;
					CAN1Comm.TxUpdate(Data, size);
					if(RetryCount >= 3){
						return false;
					}
				}
				if(Count > TIMEOUT){
					CancelTimeoutTimer();
					ClearResponse();
					Log.e(TAG,"Flash Erase Timeout");
					CancelTimeoutTimer();
					return false;
				}
				
			}
			
		
		}
		
		public boolean VerifyUpdateFile(byte[] Data,int size){
			StartTimeoutTimer();
			CAN1Comm.TxUpdate(Data, size);
			while(true){
				if(Response == ACK){
					CancelTimeoutTimer();
					ClearResponse();
					return true;
				}
				if(Response == NAK){
					CancelTimeoutTimer();
					ClearResponse();
					CAN1Comm.TxUpdate(Data, size);
					return false;
				}
				if(Count > TIMEOUT){
					CancelTimeoutTimer();
					ClearResponse();
					Log.e(TAG,"Flash Erase Timeout");
					CancelTimeoutTimer();
					return false;
				}
				
			}
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
				0x8201, 0x42C0, 0x4380, 0x8341, 0x4100, 0x81C1, 0x8081, 0x4040};
		
	}
}