package taeha.wheelloader.update;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;

import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender.SendIntentException;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BKCUUpdatePopup extends Dialog{
	private static final String TAG = "BKCUUpdatePopup";
	
	private static CAN1CommManager CAN1Comm;
	private static MainActivity ParentActivity;
	private static _Parent_CANUpdateFragment ParentFragment;
	
	protected static Thread threadUpdate = null;
	protected static UpdateThread updateThread = null;
	
	public BKCUUpdatePopup(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public BKCUUpdatePopup(Context context, int theme) {
		// TODO Auto-generated constructor stub
		super(context, theme);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
		//return super.onTouchEvent(event);
	}
	@Override
	public void dismiss(){
		super.dismiss();
		Thread.interrupted();
	}
	
	public static class Builder{
		private Context context;
		
		int mBaseAddr = 0;
		int mDownloadDataLineSize = 0;
		public static int mDownloadTotalSize = 0;
		public static int DESTADDRESS = 52;
		byte[]  mDownload;
		String mDownloadFile;
		
		ProgressBar progressProgram;
		int nProgramProgress = 0;
		int nProgress = 0;
		
		TextView textViewStatusProgram;
		TextView textViewStatusNumber;
		
		ImageButton imgBtnExit;
		TextView	textWarning;
		RelativeLayout layoutCancel;
		
		private DialogInterface.OnDismissListener dismissListener;
		private DialogInterface.OnClickListener exitButtonClickListener;
		
		public Builder(Context context){
			this.context = context;
			ParentActivity = (MainActivity) context;
		}
		
		public Builder setExitButton(DialogInterface.OnClickListener listener){
			this.exitButtonClickListener = listener;
			return this;
		}
		public Builder setDismiss(DialogInterface.OnDismissListener listener){
			this.dismissListener = listener;
			return this;
		}
		
		public BKCUUpdatePopup create(BKCUUpdatePopup.Builder _builder, String _updatefile){
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			final BKCUUpdatePopup dialog = new BKCUUpdatePopup(context, R.style.Dialog);
			
			View layout = inflater.inflate(R.layout.popup_update_bkcu_progress, null);
			
			progressProgram = (ProgressBar)layout.findViewById(R.id.progressBar_popup_update_bkcu_progress_program);
			
			textViewStatusProgram = (TextView)layout.findViewById(R.id.textView_popup_update_bkcu_progress_status);
			textViewStatusNumber = (TextView)layout.findViewById(R.id.textView_popup_update_bkcu_progress_status_number);
			
			imgBtnExit = (ImageButton)layout.findViewById(R.id.imageButton_popup_update_bkcu_progress_exit);
			textWarning = (TextView)layout.findViewById(R.id.textView_popup_update_bkcu_progress_warning);
			textWarning.setVisibility(View.VISIBLE);
			layoutCancel = (RelativeLayout)layout.findViewById(R.id.RelativeLayout_popup_update_bkcu_progress_bottom);
			layoutCancel.setVisibility(View.INVISIBLE);
			
			dialog.addContentView(layout, new LayoutParams(427, 229));
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

			CAN1Comm = CAN1CommManager.getInstance();
			
			Log.d(TAG, "updateFile : " + _updatefile);
			
			mDownloadFile = _updatefile;
			mDownload = new byte[1024 * 1024];
			
			textViewStatusProgram.setText("");
			textViewStatusNumber.setText("");
			
			threadUpdate = new Thread(new UpdateThread(dialog, _builder));
			threadUpdate.start();
			
			if(exitButtonClickListener != null){
				
				imgBtnExit.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.d(TAG, "OKButtonClickListener");
						exitButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
					}
				});
			}
			
	    	if(dismissListener != null){
	    		dismissListener.onDismiss(dialog);
			}
			return dialog;
		}
		public void readFile(String Path) throws IOException{
			InputStream inStream = null;
			try{
				inStream = new FileInputStream(Path);
				
				if(inStream != null){
					InputStreamReader inputReader = new InputStreamReader(inStream);
					BufferedReader bufferReader = new BufferedReader(inputReader);
					
					String line;
					String strData;
					
					int nIndex = 0;
					
					do {
						line = bufferReader.readLine();
						
						byte[] strLineData = line.getBytes();
						int nLength;
						
						if(strLineData[1] == '3'){
							nLength = HexToInt(strLineData[2]) * 16;
							nLength += HexToInt(strLineData[3]);
							
							if(mBaseAddr == 0){
								mBaseAddr = HexToInt(strLineData[8]) * 16;
								mBaseAddr = HexToInt(strLineData[9]);
								
								mBaseAddr = (mBaseAddr << 8);
								mBaseAddr += HexToInt(strLineData[10]) * 16;
								mBaseAddr += HexToInt(strLineData[11]);
							}
								
							mDownloadTotalSize += (nLength - 5);
								
							for(int i = 0; i < (nLength - 3); i++){
								if(i >= 2){
									mDownload[nIndex] = (byte) ((HexToInt(strLineData[8 + (i * 2)]) * 16) + HexToInt(strLineData[8 + ((i*2) + 1)]));
									nIndex++;
								}
							}
							mDownloadDataLineSize++;
						}
						if(strLineData[1] == '7') {
							break;
						}
					}while(line != null);
					progressProgram.setMax(getTotalPacketSize());
					Log.d(TAG, "" + getTotalPacketSize());
				}
			}catch (Exception e) {
				// TODO: handle exception
				
			} finally {
				inStream.close();
			}
		}
		public int getTotalPacketSize() {
			int returnTotalPacketSize = mDownloadTotalSize / 4;
			if ((mDownloadTotalSize % 4) != 0)
				returnTotalPacketSize++;
			return returnTotalPacketSize;
		}
		
		public int getTotalDownalodSize() {
			return mDownloadTotalSize;
		}
		private int HexToInt(byte Num) {
			if (Num >= 'A')
				return ((Num - 'A') + 10);
			else {
				return (Num - '0');
			}
		}
		
		public void requestStartPacketNumber(int _totalSendByte, int _totalSendPacket){
			CAN1Comm.Set_TargetSourceAddress(CAN1CommManager.SA_BKCU);
			CAN1Comm.Set_TotalSendByte_TX_BKCU_RTS_DOWNLOAD_MODE_65280_250_52(_totalSendByte/2);
			CAN1Comm.Set_TotalSendPacket_TX_BKCU_RTS_DOWNLOAD_MODE_65280_250_52(_totalSendPacket);
			CAN1Comm.TxCANToMCU(0x34);
		}
		public void sendPacket(int index, byte[] data){
			byte[] seperateData = new byte[4];
			try{
				seperateData[0] = data[index*4];
				seperateData[1] = data[index*4+1];
				seperateData[2] = data[index*4+2];
				seperateData[3] = data[index*4+3];
				CAN1Comm.Set_SequenceNumber_TX_BKCU_SEND_PACKET_65024_250_52(index+1);
				CAN1Comm.Set_Data_TX_BKCU_SEND_PACKET_65024_250_52(seperateData);
				CAN1Comm.TxCANToMCU(0x35);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
		}
		public boolean completeDownload(){
			if(CAN1Comm.Get_ControlByte_RX_BKCU_DOWNLOAD_COMPLETE_65280_250_52() == 19){
					//CAN1Comm.Get_TotalReceivePacket_RX_BKCU_DOWNLOAD_COMPLETE_65280_250_52() == getTotalPacketSize()){
				return false;
			}else {
				return true;
			}
		}
		
		public void DisplayStatus(final String status){
			ParentActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					textViewStatusProgram.setText(status);
					
				}
			});
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
		
		public void DisplayCompletedStatus(final String status){
			ParentActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					textWarning.setText("Completed download.\n Please restart the machine");
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
	}
	
	public static class UpdateThread implements Runnable{
		private WeakReference<BKCUUpdatePopup.Builder> BuilderRef = null;
		private WeakReference<BKCUUpdatePopup> DialogRef = null;
		public Message msg = null;
		public UpdateThread(BKCUUpdatePopup _dialog, BKCUUpdatePopup.Builder _builder){
			this.BuilderRef = new WeakReference<BKCUUpdatePopup.Builder>(_builder);
			this.DialogRef = new WeakReference<BKCUUpdatePopup>(_dialog);
			msg = new Message();
		}
		@Override
		public void run(){
			int SentPacketIndex = 0;
			int NextStartPacketNumber = 0;
			int OldNextStartPacketNumber = 0;
			try {
				// 파일 읽고 Total Packet Size 계산
				BuilderRef.get().readFile(BuilderRef.get().mDownloadFile);
				// RTS 요청
				BuilderRef.get().requestStartPacketNumber(BuilderRef.get().getTotalDownalodSize(), BuilderRef.get().getTotalPacketSize());
				
				// 다운로드 완료 될때까지 루프 반복
				while(BuilderRef.get().completeDownload()){
					SentPacketIndex = CAN1Comm.Get_SentPacketNumber_RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52();
					NextStartPacketNumber = CAN1Comm.Get_NextStartPacketNumber_RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52();
					//Log.d(TAG, "SendPacket : " + SentPacketIndex + " 1NextPacketNumber : " + NextStartPacketNumber);
					// 0은 무시 ffff 무시
					if(NextStartPacketNumber != 0 && NextStartPacketNumber != 0xffff && SentPacketIndex !=0xff){
						// old 값과 달라지면 패킷 받도록 
						Log.d(TAG, "SendPacket : " + SentPacketIndex + " 2NextPacketNumber : " + CAN1Comm.Get_NextStartPacketNumber_RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52());
						if(OldNextStartPacketNumber != NextStartPacketNumber){
							OldNextStartPacketNumber = NextStartPacketNumber;
							// 400byte씩 보냄
							CAN1Comm.Set_SentPacketNumber_RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52(0xff);
							CAN1Comm.Set_NextStartPacketNumber_RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52(0xffff);
							//Log.d(TAG, "SendPacket : " + SentPacketIndex + " 3NextPacketNumber : " + CAN1Comm.Get_NextStartPacketNumber_RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52());
							for(int i = (NextStartPacketNumber - 1); i < (NextStartPacketNumber + SentPacketIndex - 1); i++)
							{
								
								BuilderRef.get().sendPacket(i, BuilderRef.get().mDownload);
								// 디스플레이 표시
								BuilderRef.get().DisplayProgress(i);
								BuilderRef.get().DisplayStatusNumber("(" + (i+1) + " / " + BuilderRef.get().getTotalPacketSize() + ")");
							}
						}

					}
				}
				// 완료 된 후 UI 표시
				BuilderRef.get().DisplayCompletedStatus("Completed Update");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
}
