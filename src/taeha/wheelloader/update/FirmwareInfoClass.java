package taeha.wheelloader.update;

public class FirmwareInfoClass {
	protected int 		SlaveID;
	protected int 		FWID;
	protected byte[] 	FWName;
	protected byte[] 	FWModel;
	protected byte[] 	FWVersion;
	protected byte[] 	ProtoVer;
	protected byte[] 	Date;
	protected byte[] 	Time;
	protected int 		FWFileSize;
	protected int 		SectionUnitSize;
	protected int 		PacketUnitSize;
	protected int 		NumberofSection;
	protected int 		NumberofPacket;
	protected int 		AppstartAddress;
	
	protected short[]	AppSectionCRC;
	protected short		AppCRC;
	
	FirmwareInfoClass(){
	  	FWName = new byte[18];
		FWModel = new byte[20];
		FWVersion = new byte[4];
		ProtoVer = new byte[4];
		Date = new byte[3];
		Time = new byte[3];
		AppSectionCRC = new short[450];
	}
}
