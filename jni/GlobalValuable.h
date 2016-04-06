
static const char *TAG = "serial_port";
//#define DEBUG_READ

JavaVM *glpVM = NULL;
int fd_UART1;
int fd_UART3;

pthread_t p_thread[5];
int thr_id[5];

jclass jObject;


#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

////////////////////Define//////////////////////////
#define		RINGBUFF_SIZE				4096


#define		QBUFF_SIZE					17
#define 	UART1_RXPACKET_SIZE 		17
#define		UART1_PARSING_SIZE			32
#define 	UART3_RXPACKET_SIZE 		11
#define		UART3_PARSING_SIZE			1
#define		UART1_TXPACKET_SIZE			14
#define		UART3_TXPACKET_SIZE			11
#define		UART3_UPDATEPACKET_SIZE		1030			// STX(1byte) + Index(2byte) + Data(1024byte) + CRC(2byte) + ETX/EOT(1byte)

#define		CMD_RECV_SIZE				17
#define		MAX_RECV_SIZE				(1024*2)			//	2048
#define		DAT_RECV_SIZE				17


#define		SERIAL_RX_STX				0x02
#define		SERIAL_RX_ID				0xF5
#define		SERIAL_RX_DATA_LEN			0x08
#define		SERIAL_RX_ETX				0x03
#define		SERIAL_RX_EOT				0x04
#define		SERIAL_RX_ACK				0x06
#define		SERIAL_RX_NAK				0x15


#define		SERIAL_RX_SAVE_DATA1		0xE1
#define		SERIAL_RX_SAVE_DATA2		0xE2
#define		SERIAL_RX_SAVE_DATA3		0xE3



#define		KeyCMD			0x00
#define		LCDCMD			0x01
#define		BuzCMD			0x02
#define		RTCCMD			0x03
#define		CAMCMD			0x04
#define		VersionCMD		0x05
#define		DOWNCMD			0x06
#define		SMKCMD			0x07
#define		LampCMD			0x08
#define		StartCANCMD		0x09
#define		CANUPDATECMD	0x0A


#define		KeyRES			0x80
#define		LCDRES			0x81
#define		BuzRES			0x82
#define		RTCRES			0x83
#define		CAMRES			0x84
#define		VersionRES		0x85
#define		DOWNRES			0x86
#define		SMKRES			0x87
#define		LampRES			0x88
#define		StartCANRES		0x89
#define 	CANUPDATERES	0x0A

#define 	BUZZERDAT_ON          		0x11    	//	Buzz On  Data
#define 	BUZZERDAT_OFF          		0x10    	//	Buzz Off Data

#define 	SA_MCU						0x47
#define		SA_MONITOR					0x28
#define		SA_CLUSTER					0x17
#define		SA_RMCU						0x4A
#define		SA_EHCU						0xE4
#define		SA_TCU						0x03
#define		SA_ECM						0x00
#define		SA_CID						0xF4
#define		SA_CANUPDATE				0xFA
#define		SA_BKCU						0x34
////////////////////////////////////////////////////
#define		ERR_BUF_SIZE				400
#define		MULTI_BUF_SIZE				400

#define		FIRMWAREINFO_SIZE			72
#define 	TIMER1_INTERVAL				5

#define		MULTIPACKET_RETRY_NUM		4
//////////////////Variable//////////////////////////
unsigned char		UART1_TxBuff[RINGBUFF_SIZE][UART1_TXPACKET_SIZE];
unsigned int		TxRingBuffHead;
unsigned int		TxRingBuffTail;

unsigned char		UART1_RxBuff[RINGBUFF_SIZE][UART1_TXPACKET_SIZE];
unsigned int		RxRingBuffHead;
unsigned int		RxRingBuffTail;

pthread_mutex_t mutex_UART1_tx = PTHREAD_MUTEX_INITIALIZER;
jboolean				MCU_RTSFlag;
jboolean				MCU_CTSFlag;


CAN_RX_PACKET*		UART1_Rx_Data;
unsigned char		UART1_ReadBuff[2048];
unsigned char 		UART1_RxTmpOne[MAX_RECV_SIZE][UART1_RXPACKET_SIZE];
unsigned char 		UART1_RxTmpTwo[MAX_RECV_SIZE][UART1_RXPACKET_SIZE];
unsigned char		cUART1_RxData[UART1_RXPACKET_SIZE];
unsigned char		MultiPacket_ACK_MCU[8];
unsigned char		MultiPacket_ACK_RMCU[8];
int					UART1ReadFlag = 0;
unsigned char 		bReadRunningFlag_UART1;
unsigned char		bParsingRunningFlag_UART1 = 0;
unsigned char		bWriteRunningFlag_UART1 = 0;
int					dwUART1ReadCnt = 0;
int					UART1_RxInx = 0;
unsigned char		bParsingFlag_UART1 = 0;
unsigned char		bAckFlag_MCU = 0;
unsigned char		bAckFlag_RMCU = 0;
unsigned char		nCTSFlag_MCU = 0;
unsigned char		nCTSFlag_RMCU = 0;
unsigned char		nRTSFlag_MCU = 0;
unsigned char		nAckFlag_MCU = 0;
unsigned char		nDTFlag_MCU = 0;

unsigned int 		RTSSendCount = 0;
unsigned int		MultiPacketErrFlag = 0;

unsigned char		UpdateCancel = 0;

unsigned int		nRTSDataLength = 0;
unsigned char		RTSData[4096];
unsigned char 		RTSCM[UART1_TXPACKET_SIZE];
pthread_mutex_t mutex_UART1 = PTHREAD_MUTEX_INITIALIZER;




CAN_RX_PACKET*		UART3_Rx_Data;
unsigned char		UART3_ReadBuff[2048];
unsigned char 		UART3_RxTmpOne[MAX_RECV_SIZE][UART3_RXPACKET_SIZE];
unsigned char 		UART3_RxTmpTwo[MAX_RECV_SIZE][UART3_RXPACKET_SIZE];
unsigned char		cUART3_RxData[UART1_RXPACKET_SIZE];
int					UART3ReadFlag = 0;
unsigned char 		bReadRunningFlag_UART3;
unsigned char		bParsingRunningFlag_UART3 = 0;
int					dwUART3ReadCnt = 0;
int					UART3_RxInx = 0;
unsigned char		bParsingFlag_UART3 = 0;

unsigned short		nPGN;
unsigned char		nTotalPacketNum;
unsigned short		nEHCUPGN;
unsigned short		nEHCUPGN06;
unsigned char		nEHCUTotalPacketNum_E;
unsigned char		nEHCUTotalPacketNum;
unsigned char		nEHCUTotalPacketNum06;
unsigned char		nEHCUTotalError;
unsigned char		ehcu_s_or_m;
unsigned char 		RecvScuError;
unsigned short		nRMCUPGN;
unsigned char		nRMCUTotalPacketNum;
unsigned short		nClusterPGN;
unsigned char 		nClusterTotalPacketNum;
unsigned short		nCIDPGN;
unsigned char 		nCIDTotalPacketNum;
unsigned short		nECMPGN;
unsigned char 		nECMTotalPacketNum;
unsigned short		nTCUPGN;
unsigned char 		nTCUTotalPacketNum;

unsigned char		gRecvMulti[MULTI_BUF_SIZE];
unsigned char		gRecvMulti_EHCU[MULTI_BUF_SIZE];
unsigned char		gRecvMulti_EHCU_06[MULTI_BUF_SIZE];
unsigned char		gRecvMulti_RMCU[MULTI_BUF_SIZE];
unsigned char		gRecvMulti_Cluster[MULTI_BUF_SIZE];
unsigned char		gRecvMulti_ECM[MULTI_BUF_SIZE];
unsigned char		gRecvMulti_TCU[MULTI_BUF_SIZE];
unsigned char		gRecvMulti_CID[MULTI_BUF_SIZE];

pthread_mutex_t mutex_UART3 = PTHREAD_MUTEX_INITIALIZER;

////////////////////////////////////////////////////

/////////////////////// CAN Rx Data/////////////////




////////////////////////////////////////////////////

/////////////////////////////////////////////////////
unsigned char		nPF;
unsigned char		nPS;
unsigned char		nTotalPacketNum;
unsigned char		nMultiPacketMessageType;
unsigned int		nMultiPacketCommand;

unsigned char		nEHCUPF;
unsigned char		nEHCUPS;
unsigned char		nEHCUTotalPacketNum;
unsigned char		nEHCU06PF;
unsigned char		nEHCU06PS;
unsigned char		nEHCU06TotalPacketNum;

unsigned char		nRMCUPF;
unsigned char		nRMCUPS;
unsigned char		nRMCUTotalPacketNum;
unsigned char		nRMCUMultiPacketMessageType;


unsigned char		nClusterPF;
unsigned char		nClusterPS;
unsigned char		nClusterTotalPacketNum;

unsigned char		nECMPF;
unsigned char		nECMPS;
unsigned char		nECMTotalPacketNum;

unsigned char		nTCUPF;
unsigned char		nTCUPS;
unsigned char		nTCUTotalPacketNum;

unsigned char		nCIDPF;
unsigned char		nCIDPS;
unsigned char		nCIDTotalPacketNum;

unsigned int		nRecvSeedFlag;
unsigned int		nRecvPasswordResultFlag;
unsigned int		nRecvPasswordChangeResultFlag;
unsigned int		nRecvSensorCalibrationStatusFlag;
unsigned int		nRecvResDownFlag;

unsigned char		CheckBKCUComm;
unsigned char		CheckRMCUComm;

/////////////CAN Update///////////////
unsigned char		TargetSourceAddress;

unsigned int		nRecAckNewFWNInfoFlag_61184_250_82;
unsigned int		nRecvFWInfoFlag_61184_250_48;
unsigned int		nRecvRequestPacketMFlag_61184_250_83;
unsigned int		nRecvFWDLCompleteFlag_61184_250_80;
unsigned int		nRecvFWUpdateCompleteFlag_61184_250_112;
unsigned int		nRecvSendBootloaderStatusFlag_61184_250_17;
unsigned int		nRecvUPDFormatCompleteFlag_61184_250_85;
unsigned int		nLength_61184_250_66;
unsigned int		nLength_61184_250_81;

unsigned long		CANRecvIndex;
/////////////////////////////////////



/////////////////////// CMD Data/////////////////
RES_RTC							RX_RES_RTC;
RES_VERSION						RX_RES_Version;
RES_SMK							RX_RES_SMK;
CMD_LAMP						TX_CMD_Lamp;
RES_DOWN						RX_RES_DOWN;
////////////////////////////////////////////////////

/////////////////// CAN Data//////////////////////
SEND_BOOTLOADER_STATUS_61184_250_17			TX_SEND_BOOTLOADER_STATUS_61184_250_17;
REQUEST_SLAVE_INFO_61184_250_33				TX_REQUEST_SLAVE_INFO_61184_250_33;
SEND_SLAVE_INFO_61184_250_49				TX_SEND_SLAVE_INFO_61184_250_49;
REQUEST_FW_N_INFO_61184_250_32				TX_REQUEST_FW_N_INFO_61184_250_32;
SEND_FW_N_INFO_61184_250_48					TX_SEND_FW_N_INFO_61184_250_48;
ENTER_DL_MODE_61184_250_65					TX_ENTER_DL_MODE_61184_250_65;
SEND_NEW_FW_N_INFO_61184_250_66				TX_SEND_NEW_FW_N_INFO_61184_250_66;
ACK_NEW_FW_N_INFOR_61184_250_82				TX_ACK_NEW_FW_N_INFOR_61184_250_82;
APP_N_DL_START_61184_250_64					TX_APP_N_DL_START_61184_250_64;
REQUEST_PACKET_M_61184_250_83				TX_REQUEST_PACKET_M_61184_250_83;
SEND_PACKET_M_61184_250_67					TX_SEND_PACKET_M_61184_250_67;
FW_N_DL_COMPLETE_61184_250_80				TX_FW_N_DL_COMPLETE_61184_250_80;
QUIT_DL_MODE_61184_250_81					TX_QUIT_DL_MODE_61184_250_81;
FW_UPDATE_START_61184_250_96				TX_FW_UPDATE_START_61184_250_96;
FW_UPDATE_STATUS_61184_250_113				TX_FW_UPDATE_STATUS_61184_250_113;
FW_UPDATE_COMPLETE_61184_250_112			TX_FW_UPDATE_COMPLETE_61184_250_112;
UPD_UPDATE_START_61184_250_69				TX_UPD_UPDATE_START_61184_250_69;
UPD_UPDATE_STATUS_61184_250_84				TX_UPD_UPDATE_STATUS_61184_250_84;
UPD_UPDATE_COMPLETE_61184_250_85			TX_UPD_UPDATE_COMPLETE_61184_250_85;
APP_N_DL_CANCEL_61184_250_70				TX_APP_N_DL_CANCEL_61184_250_70;
BKCU_RTS_DOWNLOAD_MODE_65280_250_52			TX_BKCU_RTS_DOWNLOAD_MODE_65280_250_52;
BKCU_SEND_PACKET_65024_250_52				TX_BKCU_SEND_PACKET_65024_250_52;


SEND_BOOTLOADER_STATUS_61184_250_17			RX_SEND_BOOTLOADER_STATUS_61184_250_17;
REQUEST_SLAVE_INFO_61184_250_33				RX_REQUEST_SLAVE_INFO_61184_250_33;
SEND_SLAVE_INFO_61184_250_49				RX_SEND_SLAVE_INFO_61184_250_49;
REQUEST_FW_N_INFO_61184_250_32				RX_REQUEST_FW_N_INFO_61184_250_32;
SEND_FW_N_INFO_61184_250_48					RX_SEND_FW_N_INFO_61184_250_48;
ENTER_DL_MODE_61184_250_65					RX_ENTER_DL_MODE_61184_250_65;
SEND_NEW_FW_N_INFO_61184_250_66				RX_SEND_NEW_FW_N_INFO_61184_250_66;
ACK_NEW_FW_N_INFOR_61184_250_82				RX_ACK_NEW_FW_N_INFOR_61184_250_82;
APP_N_DL_START_61184_250_64					RX_APP_N_DL_START_61184_250_64;
REQUEST_PACKET_M_61184_250_83				RX_REQUEST_PACKET_M_61184_250_83;
SEND_PACKET_M_61184_250_67					RX_SEND_PACKET_M_61184_250_67;
FW_N_DL_COMPLETE_61184_250_80				RX_FW_N_DL_COMPLETE_61184_250_80;
QUIT_DL_MODE_61184_250_81					RX_QUIT_DL_MODE_61184_250_81;
FW_UPDATE_START_61184_250_96				RX_FW_UPDATE_START_61184_250_96;
FW_UPDATE_STATUS_61184_250_113				RX_FW_UPDATE_STATUS_61184_250_113;
FW_UPDATE_COMPLETE_61184_250_112			RX_FW_UPDATE_COMPLETE_61184_250_112;
UPD_UPDATE_START_61184_250_69				RX_UPD_UPDATE_START_61184_250_69;
UPD_UPDATE_STATUS_61184_250_84				RX_UPD_UPDATE_STATUS_61184_250_84;
UPD_UPDATE_COMPLETE_61184_250_85			RX_UPD_UPDATE_COMPLETE_61184_250_85;
APP_N_DL_CANCEL_61184_250_70				RX_APP_N_DL_CANCEL_61184_250_70;
BKCU_CTS_DOWNLOAD_MODE_65280_250_52			RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52;
BKCU_DOWNLOAD_COMPLETE_65280_250_52			RX_BKCU_DOWNLOAD_COMPLETE_65280_250_52;


unsigned int SendEngineAutoShutdown;
ENGINE_SHUTDOWN_MODE_SETTING_61184_121								TX_ENGINE_SHUTDOWN_MODE_SETTING_61184_121;
ENGINE_SHUTDOWN_MODE_STATUS_61184_122								RX_ENGINE_SHUTDOWN_MODE_STATUS_61184_122;

//////////////////////////////////////////////////


