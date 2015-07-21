
#define NEW_CAN2

//////////////// CMD Structure////////////////////
typedef struct
{
	unsigned char 	Year;

	unsigned char 	Month;

	unsigned char 	Date;

	unsigned char	Day;

	unsigned char	Hour;

	unsigned char	Min;

	unsigned char 	Sec;

	unsigned char	DM;

}__attribute__((packed))  RES_RTC;

typedef struct
{
	unsigned char 	SWVersionLow:4;
	unsigned char	SWVersionHigh:4;

	unsigned char	SWVersionSubLow:4;
	unsigned char	SWVersionSubHigh:4;

	unsigned char	HWVersionHigh;

	unsigned char	HWVersionMid;

	unsigned char	HWVersionLow;

	unsigned char	DM[3];

}__attribute__((packed))  RES_VERSION;

typedef struct
{
	unsigned char	Auth;

	unsigned char	MSG;

	unsigned char	Count;

	unsigned char	DM[5];

}__attribute__((packed))  RES_SMK;
typedef struct
{
	unsigned char	MainLight1:2;
	unsigned char	MainLight2:2;
	unsigned char	WorkLight1:2;
	unsigned char	WorkLight2:2;

	unsigned char	AutoGrease1:2;
	unsigned char	AutoGrease2:2;
	unsigned char	QuickCoupler1:2;
	unsigned char	QuickCoupler2:2;

	unsigned char	RideControl1:2;
	unsigned char	RideControl2:2;
	unsigned char	WorkLoad1:2;
	unsigned char	WorkLoad2:2;

	unsigned char	BeaconLamp:2;
	unsigned char	RearWiper:2;
	unsigned char	MirrorHeat:2;
	unsigned char	AutoPosition1:2;

	unsigned char	AutoPosition2:2;
	unsigned char	FineModulation:2;
	unsigned char	FN:2;
	unsigned char	Illumination:2;


	unsigned char	DM[3];

}__attribute__((packed))  CMD_LAMP;

typedef struct
{
	unsigned char	UpdateResponse;

	unsigned char	DM[7];


}__attribute__((packed))  RES_DOWN;

//////////////////////////////////////////////////

////////////CAN Structure/////////////////////////

typedef struct
{
	unsigned char		RX_STX;
	unsigned char		RX_ID;
	unsigned char		RX_LEN;

	unsigned char		RX_SOURCEADDR;
	unsigned char		RX_PS;
	unsigned char		RX_PF;
	unsigned char		RX_PRIORITY;

	unsigned char		RX_DATA[8];
	//unsigned int		RX_RESERVED;
	unsigned char		RX_SAVE_DATA;
	unsigned char		RX_ETX;
}__attribute__((packed))  CAN_RX_PACKET;

/////////// CAN PGN Structure//////////////////////
typedef struct
{
	unsigned short		SlaveID;

	unsigned char		SlaveName[18];

	unsigned char		SerialNumber[20];

	unsigned char		HWVersion[4];

	unsigned char		ProductDate[3];

	unsigned char		DeliveryDate[3];

	unsigned char		NumberofAPP;

	unsigned short		App1StartAddressOffset;
	unsigned short		App1Size;

	unsigned short		App2StartAddressOffset;
	unsigned short		App2Size;

	unsigned short		App3StartAddressOffset;
	unsigned short		App3Size;

	unsigned short		App4StartAddressOffset;
	unsigned short		App4Size;

	unsigned short		App5StartAddressOffset;
	unsigned short		App5Size;

	unsigned short		App6StartAddressOffset;
	unsigned short		App6Size;

	unsigned short		App7StartAddressOffset;
	unsigned short		App7Size;

	unsigned short		App8StartAddressOffset;
	unsigned short		App8Size;

	unsigned short		App9StartAddressOffset;
	unsigned short		App9Size;

	unsigned short		App10StartAddressOffset;
	unsigned short		App10Size;



}__attribute__((packed))  SLAVE_INFORMATION;

typedef struct
{
	unsigned short		SlaveID;

	unsigned short		FWID;

	unsigned char		FWName[18];

	unsigned char		FWModel[20];

	unsigned char		FWVersion[4];

	unsigned char		ProtoType[4];

	unsigned char		FWChangeDay[3];

	unsigned char		FWChangeTime[3];

	unsigned int		FWFileSize;

	unsigned short		SectionUnitSize;

	unsigned short		PacketUnitSize;

	unsigned short		NumberofSectioninaFile;

	unsigned short		NumberofPacketinaSection;

	unsigned int		AppStartAddress;


}__attribute__((packed))  FIRMWARE_INFORMATION;

typedef struct
{
	unsigned short		CRC[451];		// AppSectionCRC(NumberofSection) + AppCRC

}__attribute__((packed))  FIRMWARE_INFORMATION_CRC;

typedef struct
{
	unsigned char		MessageType;

	unsigned short		Command;

	unsigned char		Reserved0;

	unsigned char		ResultCPUCRC;

	unsigned char		DM[3];
}__attribute__((packed))  SEND_BOOTLOADER_STATUS_61184_250_17;

typedef struct
{
	unsigned char		MessageType;

	unsigned short		Command;

	unsigned char		DM[5];


}__attribute__((packed))  REQUEST_SLAVE_INFO_61184_250_33;

typedef struct
{
	unsigned char		MessageType;

	unsigned short		Command;

	unsigned short		Reserved;

	SLAVE_INFORMATION	SlaveInformation;
}__attribute__((packed))  SEND_SLAVE_INFO_61184_250_49;

typedef struct
{
	unsigned char		MessageType;

	unsigned short		Command;

	unsigned short		FWID;
}__attribute__((packed))  REQUEST_FW_N_INFO_61184_250_32;
typedef struct
{
	unsigned char		MessageType;

	unsigned short		Command;

	unsigned short		Reserved;

	FIRMWARE_INFORMATION	FirmwareInformation;
}__attribute__((packed)) SEND_FW_N_INFO_61184_250_48;
typedef struct
{
	unsigned char		MessageType;

	unsigned short		Command;

	unsigned short		Reserved;

	unsigned char		DM[3];

//	FIRMWARE_INFORMATION	FirmwareInformation;
//
//	FIRMWARE_INFORMATION_CRC FirmwareInformationCRC;
}__attribute__((packed))  ENTER_DL_MODE_61184_250_65;
typedef struct
{
	unsigned char		MessageType;

	unsigned short		Command;

	unsigned short		Reserved;

	FIRMWARE_INFORMATION	FirmwareInformation;

	FIRMWARE_INFORMATION_CRC FirmwareInformationCRC;
}__attribute__((packed))  SEND_NEW_FW_N_INFO_61184_250_66;
typedef struct
{
	unsigned char		MessageType;

	unsigned short		Command;


}__attribute__((packed))  ACK_NEW_FW_N_INFOR_61184_250_82;
typedef struct
{
	unsigned char		MessageType;

	unsigned short		Command;

	unsigned short		Reserved;

	unsigned char		DM[3];

}__attribute__((packed))  APP_N_DL_START_61184_250_64;
typedef struct
{
	unsigned char		MessageType;

	unsigned short		Command;

	unsigned short		FWID;

	unsigned short		SectionNumber;

	unsigned short		PacketNumber;
}__attribute__((packed))  REQUEST_PACKET_M_61184_250_83;
typedef struct
{
	unsigned char		MessageType;

	unsigned short		Command;

	unsigned short		SectionNumber;

	unsigned short		PacketNumber;

	unsigned short		PacketLength;

	unsigned char		DistributionFileData[1024];		// Variable(128 x n)bytes, Default n = 2

	unsigned short		PacketCRC;

}__attribute__((packed))  SEND_PACKET_M_61184_250_67;
typedef struct
{
	unsigned char		MessageType;

	unsigned short		Command;

	unsigned char		Reserved;

	unsigned char		ResultFlashCRC;				// 0x41 : CRC_OK, 0x42 : CRC_ERROR, 0x42 : No_F/W

	unsigned char		DM[3];
}__attribute__((packed))  FW_N_DL_COMPLETE_61184_250_80;
typedef struct
{
	unsigned char		MessageType;

	unsigned short		Command;

	unsigned short		Reserved;

//	FIRMWARE_INFORMATION	FirmwareInformation;
//
//	FIRMWARE_INFORMATION_CRC FirmwareInformationCRC;
}__attribute__((packed))  QUIT_DL_MODE_61184_250_81;
typedef struct
{
	unsigned char		MessageType;

	unsigned short		Command;

	unsigned char		DM[5];
}__attribute__((packed))  FW_UPDATE_START_61184_250_96;

typedef struct
{
	unsigned char		MessageType;

	unsigned short		Command;

	unsigned char		Reserved0;

	unsigned char		Status;

	unsigned char		Progress_ResultFlashCRC;
}__attribute__((packed))  FW_UPDATE_STATUS_61184_250_113;
typedef struct
{
	unsigned char		MessageType;

	unsigned short		Command;

	unsigned char		Reserved;

	unsigned char		ResultFlashCRC;				// 0x41 : CRC_OK, 0x42 : CRC_ERROR, 0x42 : No_F/W

	FIRMWARE_INFORMATION	FirmwareInformation;
}__attribute__((packed))  FW_UPDATE_COMPLETE_61184_250_112;
