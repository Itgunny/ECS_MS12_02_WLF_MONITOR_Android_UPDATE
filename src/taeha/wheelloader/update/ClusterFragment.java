package taeha.wheelloader.update;

import java.security.PublicKey;

import taeha.wheelloader.update.R.string;
import taeha.wheelloader.update._Parent_CANUpdateFragment.ReadThread;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ClusterFragment extends _Parent_CANUpdateFragment{
	private static final String TAG = "ClusterFragment";
	
	public static final int ClusterTotalCount = 7;
	CANUpdatePopup.Builder ClusterCANUpdateBuilder;
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
					showClusterUpdateQuestionPopup();
				}
			}
		});
	}

	protected void InitValuables(){
		Log.d(TAG,"InitValuables");
		super.InitValuables();
		ClusterCANUpdateBuilder = new CANUpdatePopup.Builder(ParentActivity,this);
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
		
		CAN1Comm.TxCMDToMCU(CAN1CommManager.CMD_CANUPDATE, 1, 0x17);
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CAN1Comm.Set_TargetSourceAddress(CAN1CommManager.SA_CLUSTER);
		CAN1Comm.Set_FWID_TX_REQUEST_FW_N_INFO_61184_250_32(0);
		CAN1Comm.TxCANToMCU(0x20);
		
		
		FileOkFlag = GetFWInfoFromFile(UpdateFile.GetClusterFirmwareUpdateFile());
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
		
		
		ParentActivity.MenuIndex = ParentActivity.INDEX_CLUSTER_TOP;
		textViewMachineTitle.setText(ParentActivity.getResources().getString(string.Cluster));
		return mRoot;

	}
	
	public void showClusterUpdateQuestionPopup(){

		if(ParentActivity.MenuDialog != null){
			ParentActivity.MenuDialog.dismiss();
			ParentActivity.MenuDialog = null;
		}
		

		UpdateQuestionBuilder.setOKButton(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.d(TAG,"setOKButton");
				showClusterCANUpdatePopup();
				dialog.dismiss();
			}
		});
		UpdateQuestionBuilder.setCancelButton(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.d(TAG,"setCancelButton");				
				dialog.dismiss();
				
				
			}
		});
		UpdateQuestionBuilder.setDismiss(new DialogInterface.OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				Log.d(TAG,"onDismiss");
				

			}
		});
		
		ParentActivity.MenuDialog = UpdateQuestionBuilder.create(ParentActivity.getResources().getString(string.Do_you_want_update));
		ParentActivity.MenuDialog.show();		
	}
	
	public void showClusterCANUpdatePopup(){

		if(ParentActivity.MenuDialog != null){
			ParentActivity.MenuDialog.dismiss();
			ParentActivity.MenuDialog = null;
		}

		ClusterCANUpdateBuilder.setExitButton(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.d(TAG,"setExitButton");				
				dialog.dismiss();
				
				
			}
		});
		ClusterCANUpdateBuilder.setDismiss(new DialogInterface.OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				Log.d(TAG,"onDismiss");
				

			}
		});
		ParentActivity.MenuDialog = ClusterCANUpdateBuilder.create(ClusterCANUpdateBuilder,UpdateFile.GetClusterFirmwareUpdateFile(),FileFirmwareInfo);
		ParentActivity.MenuDialog.show();		
	}

}
