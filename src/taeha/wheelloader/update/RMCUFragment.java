package taeha.wheelloader.update;

import taeha.wheelloader.update.R.string;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RMCUFragment extends _Parent_CANUpdateFragment{
	private static final String TAG = "RMCUFragment";
	
	CANUpdatePopup.Builder RMCUCANUpdateBuilder;
	UpdateQuestionMonitorSTM32Popup.Builder UpdateQuestionBuilder;
	
	protected void InitResource(){
		super.InitResource();
	}
	private void InitButtonListener(){
		btnUpdate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(FileOkFlag == true){
					showRMCUUpdateQuestionPopup();
				}
			}
		});
	}

	protected void InitValuables(){
		Log.d(TAG,"InitValuables");
		super.InitValuables();
		RMCUCANUpdateBuilder = new CANUpdatePopup.Builder(ParentActivity,this);
		UpdateQuestionBuilder = new UpdateQuestionMonitorSTM32Popup.Builder(ParentActivity);
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
		
		ParentActivity.MenuIndex = MainActivity.INDEX_RMCU_SELECT;
		
		CAN1Comm.TxCMDToMCU(CAN1CommManager.CMD_CANUPDATE, 1, 0x4A);
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CAN1Comm.Set_TargetSourceAddress(CAN1CommManager.SA_RMCU);
		CAN1Comm.Set_FWID_TX_REQUEST_FW_N_INFO_61184_250_32(0);
		CAN1Comm.TxCANToMCU(0x20);
		
		
		FileOkFlag = GetFWInfoFromFile(UpdateFile.GetRMCUFirmwareUpdateFile());
		if(FileOkFlag){
			FileSlaveIDDisplay(FileFirmwareInfo.SlaveID);
			FileFWIDDisplay(FileFirmwareInfo.FWID);
			FileFWNameDisplay(FileFirmwareInfo.FWName);
			FileFWModelDisplay(FileFirmwareInfo.FWModel);
			FileFWVersionDisplay(FileFirmwareInfo.FWVersion);
			FileProtoVersionDisplay(FileFirmwareInfo.ProtoVer);
			FileDateDisplay(FileFirmwareInfo.Date);
			FileTimeDisplay(FileFirmwareInfo.Time);
			btnUpdate.setClickable(true);
		}else{
			StatusWarningDisplay(ParentActivity.getResources().getString(string.Update_File_Error));
			btnUpdate.setClickable(false);
		}
		
		
		textViewMachineTitle.setText(ParentActivity.getResources().getString(string.RMCU));
		return mRoot;

	}
	
	public void showRMCUUpdateQuestionPopup(){

		if(ParentActivity.MenuDialog != null){
			ParentActivity.MenuDialog.dismiss();
			ParentActivity.MenuDialog = null;
		}
		

		UpdateQuestionBuilder.setOKButton(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.d(TAG,"setOKButton");
				showRMCUCANUpdatePopup();
				dialog.dismiss();
				ParentActivity.MenuIndex = MainActivity.INDEX_RMCU_SELECT;
			}
		});
		UpdateQuestionBuilder.setCancelButton(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.d(TAG,"setCancelButton");				
				dialog.dismiss();
				ParentActivity.MenuIndex = MainActivity.INDEX_RMCU_SELECT;
				
			}
		});
		UpdateQuestionBuilder.setDismiss(new DialogInterface.OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				Log.d(TAG,"onDismiss");
				ParentActivity.MenuIndex = MainActivity.INDEX_RMCU_SELECT;

			}
		});
		ParentActivity.MenuDialog = UpdateQuestionBuilder.create(ParentActivity.getResources().getString(string.Do_you_want_update));
		ParentActivity.MenuDialog.show();		
	}
	
	public void showRMCUCANUpdatePopup(){

		if(ParentActivity.MenuDialog != null){
			ParentActivity.MenuDialog.dismiss();
			ParentActivity.MenuDialog = null;
		}
		RMCUCANUpdateBuilder.setExitButton(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.d(TAG,"setExitButton");				
				dialog.dismiss();
				
				
			}
		});
		RMCUCANUpdateBuilder.setDismiss(new DialogInterface.OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				Log.d(TAG,"onDismiss");
				

			}
		});
		ParentActivity.MenuDialog = RMCUCANUpdateBuilder.create(RMCUCANUpdateBuilder,UpdateFile.GetRMCUFirmwareUpdateFile(),FileFirmwareInfo);
		ParentActivity.MenuDialog.show();		
	}

}
