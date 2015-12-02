package taeha.wheelloader.update;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;




import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.text.method.MovementMethod;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

public class UpdateFileFindClass {
	public static final String TAG = "UpdateFileFindClass";

	public static String ROOT_USB_PATH	= "/mnt/usb/";
	public static String ROOT_MMC_PATH	= "/mnt/sdcard";
	
	public static String MONITOR_BOOT_PATH = "/mnt/usb/UPDATE/Monitor/BootLoader";
	public static String MONITOR_BOOT_NAME = "u-boot_v";
	public static String MONITOR_BOOT_EXT = ".bin";
	
	public static String MONITOR_KERNEL_PATH = "/mnt/usb/UPDATE/Monitor/Kernel";
	public static String MONITOR_KERNEL_NAME = "zImage_v";
	public static String MONITOR_KERNEL_EXT = "";
	
	public static String MONITOR_SYSTEM_PATH = "/mnt/usb/UPDATE/Monitor/Android";
	public static String MONITOR_SYSTEM_NAME = "system_ext4_v";
	public static String MONITOR_SYSTEM_EXT = ".img";
	
	public static String MONITOR_RAMDISK_PATH = "/mnt/usb/UPDATE/Monitor/Android";
	public static String MONITOR_RAMDISK_NAME = "ramdisk.img.ub_v";
	public static String MONITOR_RAMDISK_EXT = "";
	
	public static String MONITOR_APP_PATH = "/mnt/usb/UPDATE/Monitor/Application";
	public static String MONITOR_APP_NAME = "Wheel_Loader_F_Series_UI_v";
	public static String MONITOR_APP_EXT = ".apk";
	
	public static String MONITOR_STM32_PATH = "/mnt/usb/UPDATE/Monitor/Firmware";
	public static String MONITOR_STM32_NAME = "WLF_Monitor_APP_v";
	public static String MONITOR_STM32_EXT = ".bin";
	
	public static String MONITOR_STM32_FACTORYINIT_NAME = "WLF_Monitor_APP_FI_v";
	
	public static String CLUSTER_FIRMWARE_PATH = "/mnt/usb/UPDATE/Cluster/Firmware";
	public static String CLUSTER_FIRMWARE_NAME = "WLF_Cluster_APP_v";
	public static String CLUSTER_FIRMWARE_EXT = ".THM";
	
//	public static String MCU_FIRMWARE_PATH = "/mnt/usb/UPDATE/MCU/Firmware";
//	public static String MCU_FIRMWARE_NAME = "HL7xx-F_v";
//	public static String MCU_FIRMWARE_EXT = ".THM";
	
	public static String MCU_FIRMWARE_PATH = "/mnt/usb/UPDATE";
	public static String MCU_FIRMWARE_NAME = "HL9xx_v";
	public static String MCU_FIRMWARE_EXT = ".THM";
	
	// ++, cjg 150601
	public static String BKCU_FIRMWARE_PATH = "/mnt/usb/UPDATE/BKCU/Firmware";
	public static String BKCU_FIRMWARE_NAME = "BKCU_v";
	public static String BKCU_FIRMWARE_EXT = ".THM";
	// --, cjg 150601
	
	// ++, cjg 150509
	public static String MONITOR_UPDATE_PATH = "/mnt/usb/UPDATE/Monitor/Update";
	public static String MONITOR_UPDATE_NAME = "Wheel_Loader_F_Series_Update_v";
	public static String MONITOR_UPDATE_EXT = ".apk";
	// --, cjg 150509
	
	// ++, cjg 150521
	public static String MONITOR_MIRACAST_PATH = "/mnt/usb/UPDATE/Monitor/Miracast";
	public static String MONITOR_MIRACAST_NAME = "PowerCast_v";
	public static String MONITOR_MIRACAST_EXT = ".apk";
	// --, cjg 150521

	public static String RMCU_FIRMWARE_PATH = "/mnt/usb/UPDATE/RMCU";
	public static String RMCU_FIRMWARE_NAME = "RMCU_v";
	public static String RMCU_FIRMWARE_EXT = ".THM";
	
	public static String MONITOR_LANGUAGE_PATH = "/mnt/usb/UPDATE/Monitor/Language";
	public static String MONITOR_LANGUAGE_NAME = "string_v";
	public static String MONITOR_LANGUAGE_EXT = ".xls";

	String strPath;
	String strFileNameHead;
	String strExtension;

	Context context;
	
	public UpdateFileFindClass(Context _context) {
		context = _context;
	}
	
	
	public File GetLastVersionProgram(String strRootPath, String strAppName, String strExtension){
	//public void GetLastVersionProgram(String strRootPath, String strAppName, String strExtension){
		int Num = 0;
		
		File rootDir = new File(strRootPath);
		File[] files = rootDir.listFiles();
		
		if(files != null){
			for (int i = 0; i < files.length; i++) {
				String str = files[i].getPath();
				if (str.contains(strAppName) == true) {
					if(str.endsWith(strExtension)){
						Num++;
					}
				}
				else
					Log.d(TAG, "Not Match Path : " + str);
			}
			
			if(Num > 0){
				String[] strFileName = new String[Num];
				File[] Program = new File[Num];
				int[] nVersion = new int[Num];
				int nLastVersion;
				int LastVersionIndex;
				Num = 0;
				for (int i = 0; i < files.length; i++) {
					String str = files[i].getPath();
					if (str.contains(strAppName) == true) {
						if(str.endsWith(strExtension)){
							strFileName[Num] = str;
							Program[Num] = files[i];
							Num++;
						}
					}
					else
						Log.d(TAG, "Not Match Path : " + str);
				}
				
				nLastVersion = GetVersion(strRootPath,strAppName,strExtension,strFileName[0]);
				LastVersionIndex = 0;
				for(int i = 0; i < Num; i++){
					nVersion[i] = GetVersion(strRootPath,strAppName,strExtension,strFileName[i]);
					if(nVersion[i] > nLastVersion){
						nLastVersion = nVersion[i];
						LastVersionIndex = i;
					}
				}
				return Program[LastVersionIndex];
			}
			
		}
		return null;
	}
	
	public int GetVersion(String strRootPath, String strAppName, String strExtension, String strFileName){
		int[] nVersion;
		char[] cVersion;

		int VersionStartPosition;
		
		nVersion = new int[4];
		cVersion = new char[4];
		
		
		VersionStartPosition = strRootPath.length() + strAppName.length();
		

		if(strFileName.length() < VersionStartPosition + strExtension.length() + 5){
			return 0;
		}

		cVersion[0] = strFileName.charAt(VersionStartPosition+1);
		cVersion[1] = strFileName.charAt(VersionStartPosition+3);
		cVersion[2] = strFileName.charAt(VersionStartPosition+5);
		//++ cjg 150217
		try{
			cVersion[3] = strFileName.charAt(VersionStartPosition+7);
		}catch(StringIndexOutOfBoundsException e){
			cVersion[3] = '0';
		}
		
		
		for(int i = 0; i < 4; i++){
			nVersion[i] = (int)cVersion[i] - 0x30;
		}
		
		Log.d(TAG,"Version : " + Integer.toString(nVersion[0])+ Integer.toString(nVersion[1])
				+ Integer.toString(nVersion[2])+ Integer.toString(nVersion[3]));
		return (nVersion[0]*1000 + nVersion[1]*100 + nVersion[2]*10 + nVersion[3]);
	}
	public int GetThreeVersion(String strRootPath, String strAppName, String strExtension, String strFileName){
		int[] nVersion;
		char[] cVersion;

		int VersionStartPosition;
		
		nVersion = new int[3];
		cVersion = new char[3];
		
		
		VersionStartPosition = strRootPath.length() + strAppName.length();
		

		if(strFileName.length() < VersionStartPosition + strExtension.length() + 5){
			return 0;
		}

		cVersion[0] = strFileName.charAt(VersionStartPosition+1);
		cVersion[1] = strFileName.charAt(VersionStartPosition+3);
		cVersion[2] = strFileName.charAt(VersionStartPosition+5);

		
		for(int i = 0; i < 3; i++){
			nVersion[i] = (int)cVersion[i] - 0x30;
		}
		
		Log.d(TAG,"Version : " + Integer.toString(nVersion[0])+ Integer.toString(nVersion[1])
				+ Integer.toString(nVersion[2]));
		return (nVersion[0]*1000 + nVersion[1]*100 + nVersion[2]*10);
	}
	//////////////////////////////////////// Monitor STM32 Install///////////////////////////////////////
	public File GetMonitorSTM32UpdateFile(){
		File f = GetLastVersionProgram(MONITOR_STM32_PATH,MONITOR_STM32_NAME,MONITOR_STM32_EXT);
		
		return f;
	}
	public File GetMonitorSTM32FactoryInitUpdateFile(){
		File f = GetLastVersionProgram(MONITOR_STM32_PATH,MONITOR_STM32_FACTORYINIT_NAME,MONITOR_STM32_EXT);
		
		return f;
	}
	
	public boolean SendPacket(CAN1CommManager CAN1Comm, byte[] Data){
		CAN1Comm.TxUpdate(Data, 1030);
		return true;
	}
	
	
	public String GetMonitorSTM32Version(){
		int[] nVersion;
		int Version;
		String strVersion;
		nVersion = new int[4];
		
		File f = GetLastVersionProgram(MONITOR_STM32_PATH,MONITOR_STM32_NAME,MONITOR_STM32_EXT);
		if(f == null)
			return null;
		Version = GetVersion(MONITOR_STM32_PATH,MONITOR_STM32_NAME,MONITOR_STM32_EXT,f.getPath());
		
		nVersion[0] = (Version / 1000) % 10;
		nVersion[1] = (Version / 100) % 10;
		nVersion[2] = (Version / 10) % 10;
		nVersion[3] = Version % 10;
		
		strVersion = Integer.toString(nVersion[0]) + "." + Integer.toString(nVersion[1]) 
				+ "." + Integer.toString(nVersion[2]) + "." + Integer.toString(nVersion[3]);
		
		return strVersion;
	}
	
	public String GetMonitorSTM32FactoryInitVersion(){
		int[] nVersion;
		int Version;
		String strVersion;
		nVersion = new int[4];
		
		File f = GetLastVersionProgram(MONITOR_STM32_PATH,MONITOR_STM32_FACTORYINIT_NAME,MONITOR_STM32_EXT);
		if(f == null)
			return null;
		Version = GetVersion(MONITOR_STM32_PATH,MONITOR_STM32_FACTORYINIT_NAME,MONITOR_STM32_EXT,f.getPath());
		
		nVersion[0] = (Version / 1000) % 10;
		nVersion[1] = (Version / 100) % 10;
		nVersion[2] = (Version / 10) % 10;
		nVersion[3] = Version % 10;
		
		strVersion = Integer.toString(nVersion[0]) + "." + Integer.toString(nVersion[1]) 
				+ "." + Integer.toString(nVersion[2]) + "." + Integer.toString(nVersion[3]);
		
		return strVersion;
	}
	
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
	/////////////////////////////////////////////////////////////////////////////////////////////////////
		
	
	//////////////////////////////////////// Android Application Install/////////////////////////////////
	public void MonitorAndroidAppUpdate(){
		File f = GetLastVersionProgram(MONITOR_APP_PATH,MONITOR_APP_NAME,MONITOR_APP_EXT);
		if(f == null)
			return;
		apkInstall(f);
	}
	/*public void MonitorRemoveMiracast(){
		try {
	       Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE)
	       
	       .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
	    		    | Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_SINGLE_TOP)
           .setData(Uri.parse("package:" + "com.powerone.wfd.sink"));
	       
	       context.startActivity(intent);
		} catch (Exception e) {

		}
	}*/
	public String GetMonitorVersion(){
		int[] nVersion;
		int Version;
		String strVersion;
		nVersion = new int[4];
		
		File f = GetLastVersionProgram(MONITOR_APP_PATH,MONITOR_APP_NAME,MONITOR_APP_EXT);
		if(f == null)
			return null;
		Version = GetVersion(MONITOR_APP_PATH,MONITOR_APP_NAME,MONITOR_APP_EXT,f.getPath());
		
		nVersion[0] = (Version / 1000) % 10;
		nVersion[1] = (Version / 100) % 10;
		nVersion[2] = (Version / 10) % 10;
		nVersion[3] = Version % 10;
		
		strVersion = Integer.toString(nVersion[0]) + "." + Integer.toString(nVersion[1])
				+ "." + Integer.toString(nVersion[2]) + "." + Integer.toString(nVersion[3]);
		
		return strVersion;
	}
	public void apkInstall(File apkfile) {
		Uri apkUri = Uri.fromFile(apkfile);
		try {

			Intent packageinstaller = new Intent(Intent.ACTION_VIEW);
			packageinstaller.setDataAndType(apkUri,
					"application/vnd.android.package-archive");
			context.startActivity(packageinstaller);

		} catch (Exception e) {

		}
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////
		
	
	//////////////////////////////////////// Miracast Application Install/////////////////////////////////
	public void MonitorMiracastAppUpdate(){
		File f = GetLastVersionProgram(MONITOR_MIRACAST_PATH,MONITOR_MIRACAST_NAME,MONITOR_MIRACAST_EXT);
		if(f == null)
			return;
		apkInstall(f);
	}
	
	public String GetMiracastVersion(){
		int[] nVersion;
		int Version;
		String strVersion;
		nVersion = new int[4];
		
		File f = GetLastVersionProgram(MONITOR_MIRACAST_PATH,MONITOR_MIRACAST_NAME,MONITOR_MIRACAST_EXT);
		if(f == null)
			return null;
		Version = GetThreeVersion(MONITOR_MIRACAST_PATH,MONITOR_MIRACAST_NAME,MONITOR_MIRACAST_EXT,f.getPath());
		
		nVersion[0] = (Version / 1000) % 10;
		nVersion[1] = (Version / 100) % 10;
		nVersion[2] = (Version / 10) % 10;
		nVersion[3] = Version % 10;
		
		strVersion = Integer.toString(nVersion[0]) + "." + Integer.toString(nVersion[1])
				+ "." + Integer.toString(nVersion[2]) + "." + Integer.toString(nVersion[3]);
		
		return strVersion;
	}	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////
		
	//////////////////////////////////////// Language Application Install/////////////////////////////////
	public void MonitorLanguageUpdate(){
		File f = GetLastVersionProgram(MONITOR_LANGUAGE_PATH,MONITOR_LANGUAGE_NAME,MONITOR_LANGUAGE_EXT);
		fileCopy(MONITOR_LANGUAGE_PATH + "/" + f.getName(), "/storage/emulated/legacy/Language" + "/string.xls");
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
	public String GetLanguageVersion(){
		int[] nVersion;
		int Version;
		String strVersion;
		nVersion = new int[4];
		
		File f = GetLastVersionProgram(MONITOR_LANGUAGE_PATH,MONITOR_LANGUAGE_NAME,MONITOR_LANGUAGE_EXT);
		if(f == null)
			return null;
		Version = GetThreeVersion(MONITOR_LANGUAGE_PATH,MONITOR_LANGUAGE_NAME,MONITOR_LANGUAGE_EXT,f.getPath());
		
		nVersion[0] = (Version / 1000) % 10; // 2
		nVersion[1] = (Version / 100) % 10;  // 2
		nVersion[2] = (Version / 10) % 10;
		
		strVersion = Integer.toString(nVersion[0]) + "." + Integer.toString(nVersion[1])
				+ "." + Integer.toString(nVersion[2]);
		
		return strVersion;
	}	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////// Update Program Application Install/////////////////////////////////
	
	public void MonitorUpdateProgramUpdate(){
		File f = GetLastVersionProgram(MONITOR_UPDATE_PATH,MONITOR_UPDATE_NAME,MONITOR_UPDATE_EXT);
		if(f == null)
			return;
		UpdateProgrmaApkInstall(f);
	}
	
	public String GetUpdateProgramVersion(){
		int[] nVersion;
		int Version;
		String strVersion;
		nVersion = new int[3];
		
		File f = GetLastVersionProgram(MONITOR_UPDATE_PATH,MONITOR_UPDATE_NAME,MONITOR_UPDATE_EXT);
		if(f == null)
			return null;
		Version = GetThreeVersion(MONITOR_UPDATE_PATH,MONITOR_UPDATE_NAME,MONITOR_UPDATE_EXT,f.getPath());
		Log.d(TAG, "Version 1 : " + Version);
		nVersion[0] = (Version / 1000) % 10; // 2
		nVersion[1] = (Version / 100) % 10;  // 2
		nVersion[2] = (Version / 10) % 10;   
		
		strVersion = Integer.toString(nVersion[0]) + "." + Integer.toString(nVersion[1])
				+ "." + Integer.toString(nVersion[2]);
		
		return strVersion;
	}	
	public void UpdateProgrmaApkInstall(File apkfile) {
		Uri apkUri = Uri.fromFile(apkfile);
		try {
			
			Intent packageinstaller = new Intent(Intent.ACTION_VIEW);
			packageinstaller.setDataAndType(apkUri,
					"application/vnd.android.package-archive");
			context.startActivity(packageinstaller);
			System.exit(0);
		} catch (Exception e) {

		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////	
	////////////////////////////////////OS//////////////////////////////////////////////////////////////////
	public String GetMonitorBootVersion(){
		int[] nVersion;
		int Version;
		String strVersion;
		nVersion = new int[4];
		
		File f = GetLastVersionProgram(MONITOR_BOOT_PATH,MONITOR_BOOT_NAME,MONITOR_BOOT_EXT);
		if(f == null)
			return null;
		Version = GetVersion(MONITOR_BOOT_PATH,MONITOR_BOOT_NAME,MONITOR_BOOT_EXT,f.getPath());
		
		nVersion[0] = (Version / 1000) % 10;
		nVersion[1] = (Version / 100) % 10;
		nVersion[2] = (Version / 10) % 10;
		nVersion[3] = Version % 10;
		
		strVersion = Integer.toString(nVersion[0]) + "." + Integer.toString(nVersion[1])
				+ "." + Integer.toString(nVersion[2]) + "." + Integer.toString(nVersion[3]);
		
		return strVersion;
	}
	
	public String GetMonitorKernelVersion(){
		int[] nVersion;
		int Version;
		String strVersion;
		nVersion = new int[4];
		
		File f = GetLastVersionProgram(MONITOR_KERNEL_PATH,MONITOR_KERNEL_NAME,MONITOR_KERNEL_EXT);
		if(f == null)
			return null;
		Version = GetVersion(MONITOR_KERNEL_PATH,MONITOR_KERNEL_NAME,MONITOR_KERNEL_EXT,f.getPath());
		
		nVersion[0] = (Version / 1000) % 10;
		nVersion[1] = (Version / 100) % 10;
		nVersion[2] = (Version / 10) % 10;
		nVersion[3] = Version % 10;
		
		strVersion = Integer.toString(nVersion[0]) + "." + Integer.toString(nVersion[1])
				+ "." + Integer.toString(nVersion[2]) + "." + Integer.toString(nVersion[3]);
		
		return strVersion;
	}
	
	public String GetMonitorSystemVersion(){
		int[] nVersion;
		int Version;
		String strVersion;
		nVersion = new int[4];
		
		File f = GetLastVersionProgram(MONITOR_SYSTEM_PATH,MONITOR_SYSTEM_NAME,MONITOR_SYSTEM_EXT);
		if(f == null)
			return null;
		Version = GetVersion(MONITOR_SYSTEM_PATH,MONITOR_SYSTEM_NAME,MONITOR_SYSTEM_EXT,f.getPath());
		
		nVersion[0] = (Version / 1000) % 10;
		nVersion[1] = (Version / 100) % 10;
		nVersion[2] = (Version / 10) % 10;
		nVersion[3] = Version % 10;
		
		strVersion = Integer.toString(nVersion[0]) + "." + Integer.toString(nVersion[1])
				+ "." + Integer.toString(nVersion[2]) + "." + Integer.toString(nVersion[3]);
		
		return strVersion;
	}
	
	public String GetMonitorRamdiskVersion(){
		int[] nVersion;
		int Version;
		String strVersion;
		nVersion = new int[4];
		
		File f = GetLastVersionProgram(MONITOR_RAMDISK_PATH,MONITOR_RAMDISK_NAME,MONITOR_RAMDISK_EXT);
		if(f == null)
			return null;
		Version = GetVersion(MONITOR_RAMDISK_PATH,MONITOR_RAMDISK_NAME,MONITOR_RAMDISK_EXT,f.getPath());
		
		nVersion[0] = (Version / 1000) % 10;
		nVersion[1] = (Version / 100) % 10;
		nVersion[2] = (Version / 10) % 10;
		nVersion[3] = Version % 10;
		
		strVersion = Integer.toString(nVersion[0]) + "." + Integer.toString(nVersion[1])
				+ "." + Integer.toString(nVersion[2]) + "." + Integer.toString(nVersion[3]);
		
		return strVersion;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////Cluster/////////////////////////////////////////////////////////////
	public String GetClusterFirmwareVersion(){
		int[] nVersion;
		int Version;
		String strVersion;
		nVersion = new int[4];
		
		File f = GetLastVersionProgram(CLUSTER_FIRMWARE_PATH,CLUSTER_FIRMWARE_NAME,CLUSTER_FIRMWARE_EXT);
		if(f == null)
			return null;
		Version = GetVersion(CLUSTER_FIRMWARE_PATH,CLUSTER_FIRMWARE_NAME,CLUSTER_FIRMWARE_EXT,f.getPath());
		
		nVersion[0] = (Version / 1000) % 10;
		nVersion[1] = (Version / 100) % 10;
		nVersion[2] = (Version / 10) % 10;
		nVersion[3] = Version % 10;
		
		strVersion = Integer.toString(nVersion[0]) + "." + Integer.toString(nVersion[1]) 
				+ "." + Integer.toString(nVersion[2]) + "." + Integer.toString(nVersion[3]);
		
		return strVersion;
	}
	public File GetClusterFirmwareUpdateFile(){
		File f = GetLastVersionProgram(CLUSTER_FIRMWARE_PATH,CLUSTER_FIRMWARE_NAME,CLUSTER_FIRMWARE_EXT);
		
		return f;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////MCU/////////////////////////////////////////////////////////////
	public String GetMCUFirmwareVersion(){
		int[] nVersion;
		int Version;
		String strVersion;
		nVersion = new int[4];
		
		File f = GetLastVersionProgram(MCU_FIRMWARE_PATH,MCU_FIRMWARE_NAME,MCU_FIRMWARE_EXT);
		if(f == null)
			return null;
		Version = GetVersion(MCU_FIRMWARE_PATH,MCU_FIRMWARE_NAME,MCU_FIRMWARE_EXT,f.getPath());
		
		nVersion[0] = (Version / 1000) % 10;
		nVersion[1] = (Version / 100) % 10;
		nVersion[2] = (Version / 10) % 10;
		nVersion[3] = Version % 10;
		
		strVersion = Integer.toString(nVersion[0]) + "." + Integer.toString(nVersion[1]) 
				+ "." + Integer.toString(nVersion[2]) + "." + Integer.toString(nVersion[3]);
		
		return strVersion;
	}
	public File GetMCUFirmwareUpdateFile(){
		File f = GetLastVersionProgram(MCU_FIRMWARE_PATH,MCU_FIRMWARE_NAME,MCU_FIRMWARE_EXT);
		
		return f;
	}
	public File GetBKCUFirmwareUpdateFile(){
		File f = GetLastVersionProgram(BKCU_FIRMWARE_PATH,BKCU_FIRMWARE_NAME,BKCU_FIRMWARE_EXT);
		
		return f;
	}
	public File GetRMCUFirmwareUpdateFile(){
		File f = GetLastVersionProgram(RMCU_FIRMWARE_PATH,RMCU_FIRMWARE_NAME,RMCU_FIRMWARE_EXT);
		
		return f;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	class getByteFormFile{
		int Length;
		
		public byte[] getBytesFromFile(File file)throws IOException{
			InputStream is = new FileInputStream(file);
			
			long length = file.length();
			Length = (int) length;
			if(length > Integer.MAX_VALUE){
				Log.e(TAG,"file is too largh.");
			}
			
			byte[] bytes = new byte[(int) length];
			
			int offset = 0;
			int numRead = 0;
			while(offset < bytes.length
					&& (numRead = is.read(bytes,offset,bytes.length - offset)) >= 0){
				offset += numRead;
			}
			
			if(offset < bytes.length){
				throw new IOException("Could not completely read file : " + file.getName());
			}
			
			is.close();
			return bytes;
		}
		
		public int GetLength(){
			return Length;
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////
}
