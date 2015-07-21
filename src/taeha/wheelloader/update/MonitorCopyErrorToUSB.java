// ++, 150313 cjg
package taeha.wheelloader.update;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;

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
import android.widget.ProgressBar;

public class MonitorCopyErrorToUSB extends Dialog{
	public final static String TAG			= "MonitorCopyErrorToUSB";
	
	public final static String ROOT_PATH	= "/mnt/sdcard/Alarms";
	public final static String ROOT_PATH_USB = "/mnt/usb/Alarms"; 
	public static boolean pauseLock = false;
	private static MainActivity ParentActivity;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
		//return super.onTouchEvent(event);
	}
	public MonitorCopyErrorToUSB(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public MonitorCopyErrorToUSB(Context context, int theme) {
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
		MonitorCopyErrorToUSB dialog;
		
		// ProgressBar
		static ProgressBar progressCopyToUSB;
		
		// Thread
		protected static Thread CopyThread = null;
		
		boolean CopyToUSBFlag = false;
		
		int nCopyFile = 0;
		
		static int nProgress = 0;
		
		static int nFileCount = 0;
		
		private DialogInterface.OnDismissListener DismissListener;
		
		public Builder(Context context){
			this.context = context;
			ParentActivity = (MainActivity)context;
		}
		public Builder setDismiss(DialogInterface.OnDismissListener listener){
			this.DismissListener = listener;
			return this;
		}
		public MonitorCopyErrorToUSB create(MonitorCopyErrorToUSB.Builder builder){
			ParentActivity.MenuIndex = ParentActivity.INDEX_MONITOR_COPY_TO_USB;
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			dialog = new MonitorCopyErrorToUSB(context, R.style.Dialog);
			
			View layout = inflater.inflate(R.layout.popup_update_file_copytousb, null);
			progressCopyToUSB = (ProgressBar)layout.findViewById(R.id.progressBar_popup_update_file_copytousb_progress);
			progressCopyToUSB.setVisibility(View.VISIBLE);
			
			dialog.addContentView(layout, new LayoutParams(427, 229));
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			
			CopyThread = new Thread(new CopyThread(dialog, builder));
			CopyThread.start();
			
			if(DismissListener != null){
				DismissListener.onDismiss(dialog);
			}
			return dialog;
		}
		/////////////////////////////////////////Copy Seq/////////////////////////////////////////
		// Copy Thread
		public static class CopyThread implements Runnable{
			private WeakReference<MonitorCopyErrorToUSB.Builder> BuilderRef = null;
			private WeakReference<MonitorCopyErrorToUSB> DialogRef = null;
			private Object mPauseLock;
			private boolean mPaused;
			private boolean mFinished;
			
			public Message msg = null;
			

			public CopyThread(MonitorCopyErrorToUSB _dialog, MonitorCopyErrorToUSB.Builder _builder) {
				// TODO Auto-generated constructor stub
				this.BuilderRef = new WeakReference<MonitorCopyErrorToUSB.Builder>(_builder);
				this.DialogRef = new WeakReference<MonitorCopyErrorToUSB>(_dialog);
				mPauseLock = new Object();
				mPaused = false;
				mFinished = false;
				msg = new Message();
				fileRemove(ROOT_PATH_USB);
			}
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(!mFinished){
					if(isConnectedUsb() == true){
						String inputFilePath = ROOT_PATH;
						String outputFilePath = ROOT_PATH_USB;
						List<File> dirList = getDirFileList(inputFilePath);
						File dir = new File(outputFilePath);
						Log.d(TAG, ""+ dir.getAbsolutePath());
						if(dir.exists()){
						}else{
							Log.d(TAG, "dir is not exist1");
							dir.mkdirs();
						}
						try{
							progressCopyToUSB.setMax(dirList.size());
							String fileName = dirList.get(nFileCount).getName();
							fileCopy(ROOT_PATH + "/" + fileName, ROOT_PATH_USB + "/" + fileName);
							Log.d(TAG, "i = " + nFileCount + " fileName = " + fileName);
							nProgress = nFileCount;
							nFileCount++;
							if(nFileCount == dirList.size()){
								mFinished = true;
								DialogRef.get().dismiss();
								nFileCount = 0;
							}else{
								mFinished = false;
								DialogRef.get().dismiss();
							}
						}catch(Exception e){
							Log.d(TAG, "File is empty");
							mFinished = true;
							DialogRef.get().dismiss();
							
						}
						ParentActivity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								progressCopyToUSB.setProgress(nProgress + 1);
							}
						});
						synchronized (mPauseLock) {
							while(mPaused){
								try{
									mPauseLock.wait();
								}	catch(InterruptedException e){
									e.printStackTrace();
									mFinished = false;
									DialogRef.get().dismiss();
								}
							}
						}
					}
				}
			}
			public void onPause(){
				// true
				synchronized (mPauseLock) {
					mPaused = MainActivity.isDisConnected;	
				}
			}
			public void onResume(){
				// false
				synchronized (mPauseLock) {
					mPaused = MainActivity.isDisConnected;
					mPauseLock.notifyAll();
				}
			}
		}
		public static boolean isConnectedUsb(){
			File file = new File("/mnt/usb");
			if(file != null){
				if(file.length() > 0){
					Log.d(TAG, "Connected");
					return true;
				}else{
					Log.d(TAG, "disConnected");
					return false;
				}
			}
			return false;
		}
		public static List<File> getDirFileList(String dirPath){
			List<File> dirFileList = null;
			
			File dir = new File(dirPath);

			if(dir.exists()){ 
				Log.d(TAG, ""+"dir is exist.");
				File[] files = dir.listFiles();	
				dirFileList = Arrays.asList(files);
			}else{
				Log.d(TAG, "" + "dir is not exist.");
				dir.mkdir();
				File[] files = dir.listFiles();
				dirFileList = Arrays.asList(files);
			}
			return dirFileList;
		}
		public static void fileCopy(String inputFilePath, String outputFilePath){
			try{
				FileInputStream fis = new FileInputStream(inputFilePath);
				FileOutputStream fos = new FileOutputStream(outputFilePath);
				
				FileChannel fcin = fis.getChannel();
				FileChannel fcout = fos.getChannel();
				
				long size = fcin.size();
				fcin.transferTo(0, size, fcout);
				Log.d(TAG," "+ inputFilePath + " " + outputFilePath);
				
				fcout.close();
				fcin.close();
				
				fos.close();
				fis.close();
			} catch(IOException e){
				e.printStackTrace();
			}
		}
		public static void fileRemove(String FilePath){
			File file = new File(FilePath);
			
			File[] childFileList = file.listFiles();
			if(childFileList != null){
				for(File childFile : childFileList){
					if(childFile.isDirectory()){
						fileRemove(childFile.getAbsolutePath());
					}else{
						childFile.delete();
					}
				}	
			}
			
		}
		public Context getContext(){
			return context;
		}
	}
}
//--, 150313 cjg