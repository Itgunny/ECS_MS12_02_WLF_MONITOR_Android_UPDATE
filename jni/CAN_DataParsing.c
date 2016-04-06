#include "CAN_DataParsing.h"

#define NEW_CAN2

///////////////////////////// Timer////////////////////////////////////////////////////////////////
timer_t firstTimerID;
timer_t SecondTimerID;

void MultiPacketTest()
{
	unsigned char Test[1785];
	memset((unsigned char*) &Test[0], 0xFF, sizeof(Test));
	int i;
	for(i = 0; i < 255; i++)
		MakeMultiPacketData(0x1c,0xff,0x94,0x17,SA_CANUPDATE,Test,1785,i);
}

static void timer_handler( int sig, siginfo_t *si, void *uc )
{
    timer_t *tidp;
    tidp = si->si_value.sival_ptr;

    if ( *tidp == firstTimerID )
    {
    	//__android_log_print(ANDROID_LOG_INFO, "Timer","Timer1\n");
    	SendDataFromRingBuffer();
    	if(UpdateCancel == 0)
    	{
    		CheckCTSData_MCU();
    		//CheckAckData_MCU();
    	}
    	//sleep(0);
    	//timer_delete(*tidp);		// Timer Kill
    }

    else if ( *tidp == SecondTimerID )
    {
    	__android_log_print(ANDROID_LOG_INFO, "Timer","Timer2\n");
    	//timer_delete(SecondTimerID);	// Timer Kill
    }
}

static int makeTimer( char *name, timer_t *timerID, int sec, int msec )
{
    struct sigevent         te;
    struct itimerspec       its;
    struct sigaction        sa;
    int                     sigNo = SIGRTMIN;

    /* Set up signal handler. */
    sa.sa_flags = SA_SIGINFO;
    sa.sa_sigaction = timer_handler;
    sigemptyset(&sa.sa_mask);

    if (sigaction(sigNo, &sa, NULL) == -1)
    {
        printf("sigaction error\n");
        return -1;
    }

    /* Set and enable alarm */
    te.sigev_notify = SIGEV_SIGNAL;
    te.sigev_signo = sigNo;
    te.sigev_value.sival_ptr = timerID;
    timer_create(CLOCK_REALTIME, &te, timerID);

    its.it_interval.tv_sec = sec;
    its.it_interval.tv_nsec = msec * 1000000;
    its.it_value.tv_sec = sec;

    its.it_value.tv_nsec = msec * 1000000;
    timer_settime(*timerID, 0, &its, NULL);

    return 0;
}

void SetDatatoRingBuffer(unsigned char* Buf)
{


	pthread_mutex_lock(&mutex_UART1_tx);
	memcpy((unsigned char*)&UART1_TxBuff[TxRingBuffHead][0],&Buf[0],UART1_TXPACKET_SIZE);
	if(TxRingBuffHead >= RINGBUFF_SIZE - 1)
			TxRingBuffHead = 0;
	else
		TxRingBuffHead++;
	pthread_mutex_unlock(&mutex_UART1_tx);
}

void MakeCANDataSingle(unsigned char Priority, unsigned char PF, unsigned char PS, unsigned char SA, unsigned char *Data)
{
	unsigned char Buf[UART1_TXPACKET_SIZE];
	int i;
	Buf[0] = SERIAL_RX_STX;
	Buf[1] = Priority;
	Buf[2] = PF;
	Buf[3] = PS;
	Buf[4] = SA;
	for(i = 0; i < 8; i++)
		Buf[i + 5] = Data[i];
	Buf[UART1_TXPACKET_SIZE-1] = SERIAL_RX_ETX;

	SetDatatoRingBuffer(Buf);
}
void MakeCANBKCURTSData(unsigned char Priority, unsigned char PF, unsigned char PS, unsigned char SA, unsigned char *Data){
	unsigned char Buf[UART1_TXPACKET_SIZE];
	int i;
	Buf[0] = SERIAL_RX_STX;
	Buf[1] = Priority;
	Buf[2] = PF;
	Buf[3] = PS;
	Buf[4] = SA;

	Buf[5] = 16;

	for(i = 0; i < 4; i++)
		Buf[i + 6] = Data[i + 1];

	Buf[10] = 0xFE;
	Buf[11] = 0xFF;
	Buf[12] = 0;
	Buf[UART1_TXPACKET_SIZE-1] = SERIAL_RX_ETX;

	SetDatatoRingBuffer(Buf);
}
void MakeCANBKCUSendData(unsigned char Priority, unsigned char PF, unsigned char PS, unsigned char SA, unsigned char *Data){
	unsigned char Buf[UART1_TXPACKET_SIZE];
	int i;
	Buf[0] = SERIAL_RX_STX;
	Buf[1] = Priority;
	Buf[2] = PF;
	Buf[3] = PS;
	Buf[4] = SA;
	for(i = 0; i < 8; i++)
		Buf[i + 5] = Data[i];

	Buf[UART1_TXPACKET_SIZE-1] = SERIAL_RX_ETX;

	SetDatatoRingBuffer(Buf);
}
void MakeCANDataMultiBoradcast(unsigned char Priority, unsigned char PF, unsigned char PS, unsigned char DA, unsigned char SA, unsigned char *Data, unsigned int Length)
{
	unsigned char Buf[UART1_TXPACKET_SIZE];
	int PacketNum;
	int i;

	if(Length % 7 == 0)
	{
		PacketNum = Length / 7;
	}
	else
	{
		PacketNum = (Length / 7) + 1;
	}


	//////////////TP.CM_BAM///////////////////////
	Buf[0] = SERIAL_RX_STX;
	Buf[1] = Priority;
	Buf[2] = 0xEC;
	Buf[3] = DA;
	Buf[4] = SA;
	Buf[5] = 0x20;
	Buf[6] = (Length & 0x00FF);
	Buf[7] = (Length & 0xFF00) >> 8;
	Buf[8] = PacketNum;
	Buf[9] = 0xFF;
	Buf[10] = PS;
	Buf[11] = PF;
	Buf[12] = 0;
	Buf[UART1_TXPACKET_SIZE-1] = SERIAL_RX_ETX;

	SetDatatoRingBuffer(Buf);
	//////////////////////////////////////////

	////////////////TP.DT/////////////////////

	for(i = 0; i < PacketNum; i++){
		MakeMultiPacketData(Priority,PF,PS,DA,SA,&Data[i*7],Length,i);
	}


	//////////////////////////////////////////
}

void MakeMultiPacketData(unsigned char Priority, unsigned char PF, unsigned char PS, unsigned char DA, unsigned char SA, unsigned char* Data, unsigned int Length, unsigned int Index)
{
	unsigned char Buf[UART1_TXPACKET_SIZE];
	memset((unsigned char*) &Buf[0], 0xFF,sizeof(Buf));
	int i;
	Buf[0] = SERIAL_RX_STX;
	Buf[1] = Priority;
	Buf[2] = 0xEB;
	Buf[3] = DA;
	Buf[4] = SA;
	Buf[5] = Index+1;

	for(i = 0; i < 7; i++){
		if((Index * 7) + i >= Length + 1)
			break;
		else
			Buf[i + 6] = Data[i];
	}


	Buf[UART1_TXPACKET_SIZE-1] = SERIAL_RX_ETX;

	SetDatatoRingBuffer(Buf);
}
int SendDataFromRingBuffer()
{
	unsigned char Buf[UART1_TXPACKET_SIZE];
	int result = 0;
	if(TxRingBuffHead != TxRingBuffTail)
	{

		pthread_mutex_lock(&mutex_UART1_tx);
		memcpy((unsigned char*)&Buf[0],&UART1_TxBuff[TxRingBuffTail][0],UART1_TXPACKET_SIZE);

		if(TxRingBuffTail >= RINGBUFF_SIZE - 1)
			TxRingBuffTail = 0;
		else
			TxRingBuffTail++;
		result = write(fd_UART1, Buf, UART1_TXPACKET_SIZE);
		pthread_mutex_unlock(&mutex_UART1_tx);

//		__android_log_print(ANDROID_LOG_INFO, "RingBuffer","SendDataFromRingBuffer\n");

//		__android_log_print(ANDROID_LOG_INFO, "NATIVE","TX PGN[0x%x%x%x%x] Data[0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x]\n",
//				Buf[1],Buf[2],Buf[3],Buf[4],Buf[5],Buf[6],Buf[7],Buf[8],Buf[9],Buf[10],Buf[11],Buf[12]);



	}
	return result;
}

void Send_CTS(unsigned char Priority,unsigned char DA, unsigned char SA, unsigned char* Data)
{
	unsigned char Buf[UART1_TXPACKET_SIZE];
	int i;
	Buf[0] = SERIAL_RX_STX;
	Buf[1] = Priority;
	Buf[2] = 0xEC;
	Buf[3] = DA;
	Buf[4] = SA;
	Buf[5] = 0x11;		// CTS
	Buf[6] = 0xFF; //Data[3];
	Buf[7] = 1;
	Buf[8] = 0xFF;
	Buf[9] = 0xFF;
	Buf[10] = Data[5];
	Buf[11] = Data[6];
	Buf[12] = Data[7];
	Buf[UART1_TXPACKET_SIZE-1] = SERIAL_RX_ETX;

	SetDatatoRingBuffer(Buf);
}
void Send_ACK(unsigned char Priority, unsigned char DA, unsigned char SA, unsigned char* Data)
{
	unsigned char Buf[UART1_TXPACKET_SIZE];
	int i;
	Buf[0] = SERIAL_RX_STX;
	Buf[1] = Priority;
	Buf[2] = 0xEC;
	Buf[3] = DA;
	Buf[4] = SA;
	Buf[5] = 0x13;		// ACK
	Buf[6] = Data[1];
	Buf[7] = Data[2];
	Buf[8] = Data[3];
	Buf[9] = Data[4];
	Buf[10] = Data[5];
	Buf[11] = Data[6];
	Buf[12] = Data[7];
	Buf[UART1_TXPACKET_SIZE-1] = SERIAL_RX_ETX;

	SetDatatoRingBuffer(Buf);
}

void Send_RTS(unsigned char Priority, unsigned char PF, unsigned char PS, unsigned char DA, unsigned char SA, unsigned short Size)
{
	unsigned char _TotalPacketNum;
	unsigned char Buf[UART1_TXPACKET_SIZE];
	int i;

	if(Size % 7 == 0){
		_TotalPacketNum = Size / 7;
	}else{
		_TotalPacketNum = (Size / 7) + 1;
	}

	Buf[0] = SERIAL_RX_STX;
	Buf[1] = Priority;
	Buf[2] = 0xEC;
	Buf[3] = DA;
	Buf[4] = SA;
	Buf[5] = 0x10;		// RTS
	Buf[6] = Size & 0x00FF;
	Buf[7] = (Size & 0xFF00) >> 8;
	Buf[8] = _TotalPacketNum;
	Buf[9] = 0xFF;
	Buf[10] = PS;
	Buf[11] = PF;
	Buf[12] = 0x00;
	Buf[UART1_TXPACKET_SIZE-1] = SERIAL_RX_ETX;

	memcpy(&RTSCM[0],&Buf[0],sizeof(RTSCM));

	SetDatatoRingBuffer(Buf);

	nRTSFlag_MCU = 1;
}

void Send_RTSData(unsigned char* Data, unsigned int Length,unsigned char DA)
{

	int i;
	int PacketNum;

	if(Length % 7 == 0)
		PacketNum = Length / 7;
	else
		PacketNum = (Length / 7) + 1;

	for(i = 0; i < PacketNum; i++)
		MakeMultiPacketData(0x1C,0xEF,0x00,DA,SA_CANUPDATE,&Data[i*7],Length,i);
}

void CheckCTSData_MCU()
{
    static int CTSCount = 0;
	if(nCTSFlag_MCU == 1)
	{
		//__android_log_print(ANDROID_LOG_INFO, "CheckCTSData_MCU", "nCTSFlag_MCU:UpdateCancel=%d\n", UpdateCancel);
		Send_RTSData(&RTSData[0],nRTSDataLength,TargetSourceAddress);
		nCTSFlag_MCU = 0;
		nRTSFlag_MCU = 0;
		CTSCount = 0;
		nDTFlag_MCU = 1;
	}
	if(nRTSFlag_MCU == 1)
	{
		//__android_log_print(ANDROID_LOG_INFO, "CheckCTSData_MCU", "nRTSFlag_MCU:UpdateCancel=%d\n", UpdateCancel);
		if(nCTSFlag_MCU == 0)
		{
			CTSCount++;
		}
		if(CTSCount > 250)
		{
			CTSCount = 0;
			RTSSendCount++;
			SetDatatoRingBuffer(RTSCM);
		}
		if(RTSSendCount >= MULTIPACKET_RETRY_NUM)
		{
			CTSCount = 0;
			RTSSendCount = 0;
			nCTSFlag_MCU = 0;
			nRTSFlag_MCU = 0;
			nDTFlag_MCU = 0;
			nAckFlag_MCU = 0;
			MultiPacketErrFlag = 1;
		}

	}
}
void CheckAckData_MCU()
{
    static int AckCount = 0;
    if(nAckFlag_MCU == 1)
	{
		nDTFlag_MCU = 0;
		nAckFlag_MCU = 0;
		RTSSendCount = 0;
		AckCount = 0;
	}

	if(nDTFlag_MCU == 1)
	{
		if(nAckFlag_MCU == 0)
		{
			AckCount++;
		}
		if(AckCount > 1000)
		{
			AckCount = 0;
			RTSSendCount++;
			SetDatatoRingBuffer(RTSCM);
		}
		if(RTSSendCount >= MULTIPACKET_RETRY_NUM)
		{
			AckCount = 0;
			RTSSendCount = 0;
			nCTSFlag_MCU = 0;
			nRTSFlag_MCU = 0;
			nDTFlag_MCU = 0;
			nAckFlag_MCU = 0;
			MultiPacketErrFlag = 1;
		}
	}
}

void InitNewProtoclValuable() {
	UpdateCancel = 0;
	SendEngineAutoShutdown = 0;

	memset((unsigned char*) &RX_RES_RTC, 0xFF, sizeof(RX_RES_RTC));
	memset((unsigned char*) &RX_RES_Version, 0xFF, sizeof(RX_RES_Version));
	memset((unsigned char*) &RX_RES_SMK, 0xFF, sizeof(RX_RES_SMK));
	memset((unsigned char*) &TX_CMD_Lamp, 0xFF, sizeof(TX_CMD_Lamp));


	memset((unsigned char*) &TX_CMD_Lamp, 0xFF, sizeof(TX_CMD_Lamp));
	memset((unsigned char*) &TX_CMD_Lamp, 0xFF, sizeof(TX_CMD_Lamp));
	memset((unsigned char*) &TX_CMD_Lamp, 0xFF, sizeof(TX_CMD_Lamp));
	memset((unsigned char*) &TX_CMD_Lamp, 0xFF, sizeof(TX_CMD_Lamp));
	memset((unsigned char*) &TX_CMD_Lamp, 0xFF, sizeof(TX_CMD_Lamp));
	memset((unsigned char*) &TX_CMD_Lamp, 0xFF, sizeof(TX_CMD_Lamp));
	memset((unsigned char*) &TX_CMD_Lamp, 0xFF, sizeof(TX_CMD_Lamp));
	memset((unsigned char*) &TX_CMD_Lamp, 0xFF, sizeof(TX_CMD_Lamp));
	memset((unsigned char*) &TX_CMD_Lamp, 0xFF, sizeof(TX_CMD_Lamp));
	memset((unsigned char*) &TX_CMD_Lamp, 0xFF, sizeof(TX_CMD_Lamp));
	memset((unsigned char*) &TX_CMD_Lamp, 0xFF, sizeof(TX_CMD_Lamp));
	memset((unsigned char*) &TX_CMD_Lamp, 0xFF, sizeof(TX_CMD_Lamp));
	memset((unsigned char*) &TX_CMD_Lamp, 0xFF, sizeof(TX_CMD_Lamp));
	memset((unsigned char*) &TX_CMD_Lamp, 0xFF, sizeof(TX_CMD_Lamp));


	memset((unsigned char*) &TX_SEND_BOOTLOADER_STATUS_61184_250_17, 0xFF, sizeof(TX_SEND_BOOTLOADER_STATUS_61184_250_17));
	memset((unsigned char*) &TX_REQUEST_SLAVE_INFO_61184_250_33, 0xFF, sizeof(TX_REQUEST_SLAVE_INFO_61184_250_33));
	memset((unsigned char*) &TX_SEND_SLAVE_INFO_61184_250_49, 0xFF, sizeof(TX_SEND_SLAVE_INFO_61184_250_49));
	memset((unsigned char*) &TX_REQUEST_FW_N_INFO_61184_250_32, 0xFF, sizeof(TX_REQUEST_FW_N_INFO_61184_250_32));
	memset((unsigned char*) &TX_SEND_FW_N_INFO_61184_250_48, 0xFF, sizeof(TX_SEND_FW_N_INFO_61184_250_48));
	memset((unsigned char*) &TX_ENTER_DL_MODE_61184_250_65, 0xFF, sizeof(TX_ENTER_DL_MODE_61184_250_65));
	memset((unsigned char*) &TX_SEND_NEW_FW_N_INFO_61184_250_66, 0xFF, sizeof(TX_SEND_NEW_FW_N_INFO_61184_250_66));
	memset((unsigned char*) &TX_ACK_NEW_FW_N_INFOR_61184_250_82, 0xFF, sizeof(TX_ACK_NEW_FW_N_INFOR_61184_250_82));
	memset((unsigned char*) &TX_APP_N_DL_START_61184_250_64, 0xFF, sizeof(TX_APP_N_DL_START_61184_250_64));
	memset((unsigned char*) &TX_REQUEST_PACKET_M_61184_250_83, 0xFF, sizeof(TX_REQUEST_PACKET_M_61184_250_83));
	memset((unsigned char*) &TX_SEND_PACKET_M_61184_250_67, 0xFF, sizeof(TX_SEND_PACKET_M_61184_250_67));
	memset((unsigned char*) &TX_FW_N_DL_COMPLETE_61184_250_80, 0xFF, sizeof(TX_FW_N_DL_COMPLETE_61184_250_80));
	memset((unsigned char*) &TX_QUIT_DL_MODE_61184_250_81, 0xFF, sizeof(TX_QUIT_DL_MODE_61184_250_81));
	memset((unsigned char*) &TX_FW_UPDATE_START_61184_250_96, 0xFF, sizeof(TX_FW_UPDATE_START_61184_250_96));
	memset((unsigned char*) &TX_FW_UPDATE_STATUS_61184_250_113, 0xFF, sizeof(TX_FW_UPDATE_STATUS_61184_250_113));
	memset((unsigned char*) &TX_FW_UPDATE_COMPLETE_61184_250_112, 0xFF, sizeof(TX_FW_UPDATE_COMPLETE_61184_250_112));
	memset((unsigned char*) &TX_UPD_UPDATE_START_61184_250_69, 0xFF, sizeof(TX_UPD_UPDATE_START_61184_250_69));
	memset((unsigned char*) &TX_UPD_UPDATE_STATUS_61184_250_84, 0xFF, sizeof(TX_UPD_UPDATE_STATUS_61184_250_84));
	memset((unsigned char*) &TX_UPD_UPDATE_COMPLETE_61184_250_85, 0xFF, sizeof(TX_UPD_UPDATE_COMPLETE_61184_250_85));
	memset((unsigned char*) &TX_APP_N_DL_CANCEL_61184_250_70, 0xFF, sizeof(TX_APP_N_DL_CANCEL_61184_250_70));
	memset((unsigned char*) &TX_BKCU_RTS_DOWNLOAD_MODE_65280_250_52, 0xFF, sizeof(TX_BKCU_RTS_DOWNLOAD_MODE_65280_250_52));
	memset((unsigned char*) &TX_BKCU_SEND_PACKET_65024_250_52, 0xFF, sizeof(TX_BKCU_SEND_PACKET_65024_250_52));

	memset((unsigned char*) &TX_SEND_BOOTLOADER_STATUS_61184_250_17, 0xFF, sizeof(TX_SEND_BOOTLOADER_STATUS_61184_250_17));
	memset((unsigned char*) &RX_REQUEST_SLAVE_INFO_61184_250_33, 0xFF, sizeof(RX_REQUEST_SLAVE_INFO_61184_250_33));
	memset((unsigned char*) &RX_SEND_SLAVE_INFO_61184_250_49, 0xFF, sizeof(RX_SEND_SLAVE_INFO_61184_250_49));
	memset((unsigned char*) &RX_REQUEST_FW_N_INFO_61184_250_32, 0xFF, sizeof(RX_REQUEST_FW_N_INFO_61184_250_32));
	memset((unsigned char*) &RX_SEND_FW_N_INFO_61184_250_48, 0xFF, sizeof(RX_SEND_FW_N_INFO_61184_250_48));
	memset((unsigned char*) &RX_ENTER_DL_MODE_61184_250_65, 0xFF, sizeof(RX_ENTER_DL_MODE_61184_250_65));
	memset((unsigned char*) &RX_SEND_NEW_FW_N_INFO_61184_250_66, 0xFF, sizeof(RX_SEND_NEW_FW_N_INFO_61184_250_66));
	memset((unsigned char*) &RX_ACK_NEW_FW_N_INFOR_61184_250_82, 0xFF, sizeof(RX_ACK_NEW_FW_N_INFOR_61184_250_82));
	memset((unsigned char*) &RX_APP_N_DL_START_61184_250_64, 0xFF, sizeof(RX_APP_N_DL_START_61184_250_64));
	memset((unsigned char*) &RX_REQUEST_PACKET_M_61184_250_83, 0xFF, sizeof(RX_REQUEST_PACKET_M_61184_250_83));
	memset((unsigned char*) &RX_SEND_PACKET_M_61184_250_67, 0xFF, sizeof(RX_SEND_PACKET_M_61184_250_67));
	memset((unsigned char*) &RX_FW_N_DL_COMPLETE_61184_250_80, 0xFF, sizeof(RX_FW_N_DL_COMPLETE_61184_250_80));
	memset((unsigned char*) &RX_QUIT_DL_MODE_61184_250_81, 0xFF, sizeof(RX_QUIT_DL_MODE_61184_250_81));
	memset((unsigned char*) &RX_FW_UPDATE_START_61184_250_96, 0xFF, sizeof(RX_FW_UPDATE_START_61184_250_96));
	memset((unsigned char*) &RX_FW_UPDATE_STATUS_61184_250_113, 0xFF, sizeof(RX_FW_UPDATE_STATUS_61184_250_113));
	memset((unsigned char*) &RX_FW_UPDATE_COMPLETE_61184_250_112, 0xFF, sizeof(RX_FW_UPDATE_COMPLETE_61184_250_112));
	memset((unsigned char*) &RX_UPD_UPDATE_START_61184_250_69, 0xFF, sizeof(RX_UPD_UPDATE_START_61184_250_69));
	memset((unsigned char*) &RX_UPD_UPDATE_STATUS_61184_250_84, 0xFF, sizeof(RX_UPD_UPDATE_STATUS_61184_250_84));
	memset((unsigned char*) &RX_UPD_UPDATE_COMPLETE_61184_250_85, 0xFF, sizeof(RX_UPD_UPDATE_COMPLETE_61184_250_85));
	memset((unsigned char*) &RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52, 0xFF, sizeof(RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52));
	memset((unsigned char*) &RX_BKCU_DOWNLOAD_COMPLETE_65280_250_52, 0xFF, sizeof(RX_BKCU_DOWNLOAD_COMPLETE_65280_250_52));

	memset((unsigned char*) &TX_ENGINE_SHUTDOWN_MODE_SETTING_61184_121, 0xFF, sizeof(TX_ENGINE_SHUTDOWN_MODE_SETTING_61184_121));
	memset((unsigned char*) &RX_ENGINE_SHUTDOWN_MODE_STATUS_61184_122, 0xFF, sizeof(RX_ENGINE_SHUTDOWN_MODE_STATUS_61184_122));


	TX_SEND_BOOTLOADER_STATUS_61184_250_17.MessageType = 0XFE;			//SM
	TX_REQUEST_SLAVE_INFO_61184_250_33.MessageType = 0xFE;		//MS
	TX_SEND_SLAVE_INFO_61184_250_49.MessageType = 0xFE;					//SM
	TX_REQUEST_FW_N_INFO_61184_250_32.MessageType = 0xFE;		//MS
	TX_SEND_FW_N_INFO_61184_250_48.MessageType = 0xFE;					//SM
	TX_ENTER_DL_MODE_61184_250_65.MessageType = 0xFE;			//MS
	TX_SEND_NEW_FW_N_INFO_61184_250_66.MessageType = 0xFE;		//MS
	TX_ACK_NEW_FW_N_INFOR_61184_250_82.MessageType = 0xFE;				//SM
	TX_APP_N_DL_START_61184_250_64.MessageType = 0xFE;			//MS
	TX_REQUEST_PACKET_M_61184_250_83.MessageType = 0xFE;				//SM
	TX_SEND_PACKET_M_61184_250_67.MessageType = 0xFE;			//MS
	TX_FW_N_DL_COMPLETE_61184_250_80.MessageType = 0xFE;				//SM
	TX_QUIT_DL_MODE_61184_250_81.MessageType = 0xFE;			//MS
	TX_FW_UPDATE_START_61184_250_96.MessageType = 0xFE;			//MS
	TX_FW_UPDATE_STATUS_61184_250_113.MessageType = 0xFE;				//SM
	TX_FW_UPDATE_COMPLETE_61184_250_112.MessageType = 0xFE;				//SM
	TX_UPD_UPDATE_START_61184_250_69.MessageType = 0xFE;
	TX_UPD_UPDATE_STATUS_61184_250_84.MessageType = 0xFE;
	TX_UPD_UPDATE_COMPLETE_61184_250_85.MessageType = 0xFE;
	TX_APP_N_DL_CANCEL_61184_250_70.MessageType = 0xFE;
	TX_ENGINE_SHUTDOWN_MODE_SETTING_61184_121.MessageType = 121;


	TX_SEND_BOOTLOADER_STATUS_61184_250_17.Command = 17;		//SM
	TX_REQUEST_SLAVE_INFO_61184_250_33.Command = 33;		//MS
	TX_SEND_SLAVE_INFO_61184_250_49.Command = 49;				//SM
	TX_REQUEST_FW_N_INFO_61184_250_32.Command = 32;			//MS
	TX_SEND_FW_N_INFO_61184_250_48.Command = 48;				//SM
	TX_ENTER_DL_MODE_61184_250_65.Command = 65;				//MS
	TX_SEND_NEW_FW_N_INFO_61184_250_66.Command = 66;		//MS
	TX_ACK_NEW_FW_N_INFOR_61184_250_82.Command = 82;			//SM
	TX_APP_N_DL_START_61184_250_64.Command = 64;			//MS
	TX_REQUEST_PACKET_M_61184_250_83.Command = 83;				//SM
	TX_SEND_PACKET_M_61184_250_67.Command = 67;				//MS
	TX_FW_N_DL_COMPLETE_61184_250_80.Command = 80;				//SM
	TX_QUIT_DL_MODE_61184_250_81.Command = 81;				//MS
	TX_FW_UPDATE_START_61184_250_96.Command = 96;			//MS
	TX_FW_UPDATE_STATUS_61184_250_113.Command = 113;			//SM
	TX_FW_UPDATE_COMPLETE_61184_250_112.Command = 112;			//SM
	TX_UPD_UPDATE_START_61184_250_69.Command = 69;
	TX_UPD_UPDATE_STATUS_61184_250_84.Command = 84;
	TX_UPD_UPDATE_COMPLETE_61184_250_85.Command = 85;
	TX_APP_N_DL_CANCEL_61184_250_70.Command = 70;

	nPF = 0;
	nPS = 0;
	nTotalPacketNum = 0;
	nMultiPacketMessageType = 0;
	nEHCUPF	=0;
	nEHCUPS	=0;
	nEHCUTotalPacketNum	=0;
	nEHCU06PF	=0;
	nEHCU06PS	=0;
	nEHCU06TotalPacketNum	=0;
	nRMCUPF	=0;
	nRMCUPS	=0;
	nRMCUTotalPacketNum	=0;
	nRMCUMultiPacketMessageType = 0;
	nClusterPF	=0;
	nClusterPS	=0;
	nClusterTotalPacketNum	=0;
	nECMPF	=0;
	nECMPS	=0;
	nECMTotalPacketNum	=0;
	nTCUPF	=0;
	nTCUPS	=0;
	nTCUTotalPacketNum	=0;
	nCIDPF	=0;
	nCIDPS	=0;
	nCIDTotalPacketNum	=0;

	TxRingBuffHead = 0;
	TxRingBuffTail = 0;
	RxRingBuffHead = 0;
	RxRingBuffTail = 0;

	nRecAckNewFWNInfoFlag_61184_250_82 = 0;
	nRecvFWInfoFlag_61184_250_48 = 0;
	nRecvRequestPacketMFlag_61184_250_83 = 0;
	nRecvFWDLCompleteFlag_61184_250_80 = 0;
	nRecvFWUpdateCompleteFlag_61184_250_112 = 0;
	nRecvSendBootloaderStatusFlag_61184_250_17 = 0;
	nRecvUPDFormatCompleteFlag_61184_250_85 = 0;

	nLength_61184_250_66 = FIRMWAREINFO_SIZE;
	nLength_61184_250_81 = FIRMWAREINFO_SIZE;

	CANRecvIndex = 0;

	CheckBKCUComm = 0;
	CheckRMCUComm = 0;
}

void CheckAutoShutDown(){
	int EngineAutoShutdownRemainingTime = RX_ENGINE_SHUTDOWN_MODE_STATUS_61184_122.RemainingTimeforAutomaticEngineShutdown;
	int EngineAutoShutdownMode = RX_ENGINE_SHUTDOWN_MODE_STATUS_61184_122.AutomaticEngineShutdown_363;

	if(EngineAutoShutdownMode == 1){
		__android_log_print(ANDROID_LOG_INFO, "NATIVE","CheckAutoShutDown!! Mode[%d] Time[%d]", EngineAutoShutdownMode, EngineAutoShutdownRemainingTime);
		if(SendEngineAutoShutdown++ % 5 == 0)
		{
			TX_ENGINE_SHUTDOWN_MODE_SETTING_61184_121.AutomaticEngineShutdownTypeControlByte = RX_ENGINE_SHUTDOWN_MODE_STATUS_61184_122.AutomaticEngineShutdownType;
			TX_ENGINE_SHUTDOWN_MODE_SETTING_61184_121.AutomaticEngineShutdown_363 = 0;	// DATA_STATE_AUTOSHUTDOWN_OFF
			TX_ENGINE_SHUTDOWN_MODE_SETTING_61184_121.EngineShutdownCotrolByte = 1;		// STATE_COTROL_AUTO_CANCEL
			MakeCANDataSingle(0x18,0xEF,SA_MCU,SA_MONITOR,(unsigned char*)&TX_ENGINE_SHUTDOWN_MODE_SETTING_61184_121);
			TX_ENGINE_SHUTDOWN_MODE_SETTING_61184_121.EngineShutdownCotrolByte = 15;
		}
	}else{
		SendEngineAutoShutdown = 0;
	}
}

void UART1_SeperateData_NEWCAN2(int Priority, int PF, int PS, int SourceAddress, unsigned char* Data)
{
	if(SourceAddress == SA_BKCU){
		CheckBKCUComm = 1;
	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "PGN[0x%x] Data 0x%2x 0x%2x 0x%2x ", Priority, PF, PS, SourceAddress);
	}
	else if(SourceAddress == SA_RMCU)
		CheckRMCUComm = 1;

	if(SourceAddress == TargetSourceAddress){
		UART1_SeperateData_Default(Priority,PF,PS,Data);
	}
	else if(SourceAddress == SA_MCU)
		CheckAutoDefault(Priority,PF,PS,Data);

	unsigned int PGN;
	PGN = ((Data[6] & 0xFF) << 24) + ((Data[5] & 0xFF) << 16) + ((Data[4] & 0xFF) << 8) + (Data[3] & 0xFF);

}
void CheckAutoDefault(int Priority, int PF, int PS, unsigned char* Data)
{
	unsigned short Command;


	CAN_RX_PACKET*		CANPacket;

	CANPacket = (CAN_RX_PACKET*) Data;

	Command = Data[8] + (Data[9] << 8);

	switch (PF) {
		case 239:	// 0xEF00 61184

			switch (Data[7]) {	// Message Type

				case 122	:
							memcpy((unsigned char*)&RX_ENGINE_SHUTDOWN_MODE_STATUS_61184_122,&Data[7],8);
							CheckAutoShutDown();
							break	;
			}

			break;
	}
}

void UART1_SeperateData_Default(int Priority, int PF, int PS, unsigned char* Data)
{
	//__android_log_print(ANDROID_LOG_INFO, "NATIVE","UART1_SeperateData_Default PF[%02X] PS[%02X]", PF, PS);
	unsigned short Command;


	CAN_RX_PACKET*		CANPacket;

	CANPacket = (CAN_RX_PACKET*) Data;



	Command = Data[8] + (Data[9] << 8);

	switch (PF) {
		case 239:	// 0xEF00 61184

			switch (Data[7]) {	// Message Type
				case 122:
					memcpy((unsigned char*)&RX_ENGINE_SHUTDOWN_MODE_STATUS_61184_122,&Data[7],8);
					CheckAutoShutDown();
					break;
				case 0xFE:	// 0xFE
					switch (Command) {	// Command
					case 17:	// 0x11
						memcpy((unsigned char*) &RX_SEND_BOOTLOADER_STATUS_61184_250_17, &Data[7], 8);
						nRecvSendBootloaderStatusFlag_61184_250_17 = 1;
						break;
					case 49:	// 0x31
						// MultiPacket
						break;
					case 48:	// 0x30
						memcpy((unsigned char*) &RX_SEND_FW_N_INFO_61184_250_48, &Data[7], 8);
						break;
					case 82:	// 0x52
						memcpy((unsigned char*) &RX_ACK_NEW_FW_N_INFOR_61184_250_82, &Data[7], 8);
						nRecAckNewFWNInfoFlag_61184_250_82 = 1;
						break;
					case 83:	// 0x53
						memcpy((unsigned char*) &RX_REQUEST_PACKET_M_61184_250_83, &Data[7], 8);
						nRecvRequestPacketMFlag_61184_250_83 = 1;
						break;
					case 80:	// 0x50
						memcpy((unsigned char*) &RX_FW_N_DL_COMPLETE_61184_250_80, &Data[7], 8);
						nRecvFWDLCompleteFlag_61184_250_80 = 1;
						__android_log_print(ANDROID_LOG_INFO, "NATIVE","DL Complete");
						break;
					case 113:	// 0x71
						memcpy((unsigned char*) &RX_FW_UPDATE_STATUS_61184_250_113, &Data[7], 8);
						break;
					case 112:	// 0x70
						memcpy((unsigned char*) &RX_FW_UPDATE_COMPLETE_61184_250_112, &Data[7], 8);
						nRecvFWUpdateCompleteFlag_61184_250_112 = 1;
						break;
					case 84:    //0x54
						memcpy((unsigned char*) &RX_UPD_UPDATE_STATUS_61184_250_84, &Data[7], 8);
						break;
					case 85:   //0x55
						memcpy((unsigned char*) &RX_UPD_UPDATE_COMPLETE_61184_250_85, &Data[7], 8);
						nRecvUPDFormatCompleteFlag_61184_250_85 = 1;
						__android_log_print(ANDROID_LOG_INFO, "NATIVE","UPD Format Complete");
						break;
					default:
						break;
					}
				break;
			}

			break;
		case 255:	// 0xFF00
		default:
			switch (PS) {
			case 250:
				if(Data[7] == 17){
					__android_log_print(ANDROID_LOG_INFO, "NATIVE","BKCU CTS");
					memcpy((unsigned char*) &RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52, &Data[7], 8);
				}
				else if(Data[7] == 19){
					__android_log_print(ANDROID_LOG_INFO, "NATIVE","BKCU COMPLETED");
					memcpy((unsigned char*) &RX_BKCU_DOWNLOAD_COMPLETE_65280_250_52, &Data[7], 8);
				}
				break;
			default:
				break;
			}

			break;
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		case 236:	// 0xEC Multi Packet TP.CM_BAM
			__android_log_print(ANDROID_LOG_INFO, "NATIVE","Multi TP.CM_BAM");
			if(CANPacket->RX_PS == 0xFF || CANPacket->RX_PS == SA_CANUPDATE){
				if(Data[7] == 0x20){				// Control Byte (Normal)
					nPF = Data[13];
					nPS = Data[12];
					nTotalPacketNum = Data[10];
				}else if(Data[7] == 0x10){			// Control Byte (RTS)
					Send_CTS(0x1C,TargetSourceAddress,SA_CANUPDATE,&CANPacket->RX_DATA[0]);
					memcpy(&MultiPacket_ACK_MCU[0],(unsigned char*) &CANPacket->RX_DATA[0], 8);
					bAckFlag_MCU = 1;
					nPF = Data[13];
					nPS = Data[12];
					nTotalPacketNum = Data[10];
					__android_log_print(ANDROID_LOG_INFO, "NATIVE","Recv RTS!!!!");
					__android_log_print(ANDROID_LOG_INFO, "NATIVE","Send CTS!!!!");
				}else if(Data[7] == 0x11){			// Control Byte (CTS)
					nCTSFlag_MCU = 1;
					__android_log_print(ANDROID_LOG_INFO, "NATIVE","Recv CTS!!!!");
				}else if(Data[7] == 0x13){
					nAckFlag_MCU = 1;
					__android_log_print(ANDROID_LOG_INFO, "NATIVE","Recv ACK!!!!");
				}
			}


			break;
		case 235:	// 0xEB	Multi Packet TP.DT
			if(nPF != 0 || nPS != 0)
			{
				UART1_SeperateData_Default_Multi(Priority,nPF,nPS,Data);
			}
			break;
	}
}

void UART1_SeperateData_Default_Multi(int Priority, int PF, int PS, unsigned char* Data)
{
	if(bAckFlag_MCU == 1)
	{
		if (Data[7] == nTotalPacketNum)
		{
			bAckFlag_MCU = 0;
			Send_ACK(0x1C,TargetSourceAddress,SA_CANUPDATE,MultiPacket_ACK_MCU);
			__android_log_print(ANDROID_LOG_INFO, "NATIVE", "Send Ack!!!", PS);
		}
	}

	switch (PF) {
		case 239:
			memcpy((unsigned char*) &gRecvMulti[(Data[7] - 1) * 7],&Data[8], 7);
			if(Data[7] == 1)
			{
				nMultiPacketMessageType = Data[8];
				nMultiPacketCommand = Data[9]  + (Data[10] << 8);

			}
			switch (nMultiPacketMessageType) {	// Message Type
				case 0xFE:
					switch(nMultiPacketCommand){
					case 49:	// 0x31
						if (Data[7] == nTotalPacketNum) {
							memcpy((unsigned char*) &RX_SEND_SLAVE_INFO_61184_250_49, &gRecvMulti,sizeof(RX_SEND_SLAVE_INFO_61184_250_49));
							nPF = nPS = nTotalPacketNum = nMultiPacketMessageType = nMultiPacketCommand = 0;
						}
						break;
					case 48:	// 0x30
						if (Data[7] == nTotalPacketNum) {
							memcpy((unsigned char*) &RX_SEND_FW_N_INFO_61184_250_48, &gRecvMulti,sizeof(RX_SEND_FW_N_INFO_61184_250_48));
							nPF = nPS = nTotalPacketNum = nMultiPacketMessageType = nMultiPacketCommand = 0;
							nRecvFWInfoFlag_61184_250_48 = 1;
						}
						break;
					case 82:	// 0x52
						// Single
						break;
					case 83:	// 0x53
						if (Data[7] == nTotalPacketNum) {
							memcpy((unsigned char*) &RX_REQUEST_PACKET_M_61184_250_83, &gRecvMulti,sizeof(RX_REQUEST_PACKET_M_61184_250_83));
							nPF = nPS = nTotalPacketNum = nMultiPacketMessageType = nMultiPacketCommand = 0;
							nRecvRequestPacketMFlag_61184_250_83 = 1;
							__android_log_print(ANDROID_LOG_INFO, "NATIVE", "SectionNumber[0x%x]\n",RX_REQUEST_PACKET_M_61184_250_83.SectionNumber);
						}
						break;
					case 80:	// 0x50
						// Single
						break;
					case 112:	// 0x70
						if (Data[7] == nTotalPacketNum) {
							memcpy((unsigned char*) &RX_FW_UPDATE_COMPLETE_61184_250_112, &gRecvMulti,sizeof(RX_FW_UPDATE_COMPLETE_61184_250_112));
							nPF = nPS = nTotalPacketNum = nMultiPacketMessageType = nMultiPacketCommand = 0;
							nRecvFWUpdateCompleteFlag_61184_250_112 = 1;
						}
						break;
					default:
						nPF = nPS = nTotalPacketNum = nMultiPacketMessageType = nMultiPacketCommand = 0;
						break;
					}
					break;
				default:
					nPF = nPS = nTotalPacketNum = nMultiPacketMessageType = nMultiPacketCommand = 0;
				break;
			}
			break;

		case 255:
		default:
			switch (PS) {

			///////////////////////////////////////////////////////////////////////////////////////////////////
			default:
				nPF = nPS = nTotalPacketNum = nMultiPacketMessageType = 0;
					break;
			}
			break;
	}


}

jint UART1_Tx(JNIEnv *env, jobject this, jint PF, jint PS, jint Flag) {
	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "UART1_Tx [%d]\n", PS);
	int result = 0;



	return result;
}



void InitUART1Valuable() {
	UART1_Rx_Data = (CAN_RX_PACKET*) &cUART1_RxData;

	bParsingFlag_UART3 = 0;
	bParsingFlag_UART1 = 0;


	ehcu_s_or_m = 0;


	InitNewProtoclValuable();

}
void ThreadParsing_UART1(void *data) {
	unsigned char Buf[UART1_RXPACKET_SIZE];
	int i = 0;

	InitUART1Valuable();
	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "ThreadParsing UART1\n");
	while (bParsingRunningFlag_UART1)
	{
//		if(RxRingBuffHead != RxRingBuffTail)
//		{
//			pthread_mutex_lock(&mutex_UART1);
//			memcpy((unsigned char*)&Buf[0],&UART1_RxBuff[RxRingBuffTail][0],UART1_RXPACKET_SIZE);
//			if(RxRingBuffTail >= RINGBUFF_SIZE - 1)
//				RxRingBuffTail = 0;
//			else
//				RxRingBuffTail++;
//			pthread_mutex_unlock(&mutex_UART1);
//			//__android_log_print(ANDROID_LOG_INFO, "NATIVE", "ThreadParsing_UART1 RxRingBuffHead[%d] RxRingBuffTail[%d]\n",RxRingBuffHead,RxRingBuffTail);
//			UART1_DataParsing(Buf);
//		}
		sleep(0); // 다른 Thread 들의 점유를 위해 사용
	}
}


void UART1_DataParsing(unsigned char* Data) {
	CAN_RX_PACKET*		CANPacket;

	CANPacket = (CAN_RX_PACKET*) Data;

	if (CANPacket->RX_STX == SERIAL_RX_STX && CANPacket->RX_ID == SERIAL_RX_ID)
	{
		if (CANPacket->RX_LEN == SERIAL_RX_DATA_LEN &&  CANPacket->RX_ETX == SERIAL_RX_ETX)
		{

			UART1_SeperateData_NEWCAN2((CANPacket->RX_PRIORITY) >> 2, CANPacket->RX_PF, CANPacket->RX_PS, CANPacket->RX_SOURCEADDR,Data);

		}
		else
		{
			__android_log_print(ANDROID_LOG_INFO, "NATIVE","UART1 Data Length[0x%x], ETX Fault[0x%x]\n",CANPacket->RX_LEN,CANPacket->RX_ETX == SERIAL_RX_ETX);
		}
	}
	else
	{
		__android_log_print(ANDROID_LOG_INFO, "NATIVE","UART1 Data STX[0x%x], RX_ID Fault[0x%x]\n",CANPacket->RX_STX, CANPacket->RX_ID == SERIAL_RX_ID);
	}



}
void ThreadParsing_UART3(void *data) {
	unsigned char UART3_DataCurr[UART3_PARSING_SIZE][UART3_RXPACKET_SIZE];

	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "ThreadParsing UART3\n");
	while (bParsingRunningFlag_UART3) {
		if (bParsingFlag_UART3 == 1) {
			pthread_mutex_lock(&mutex_UART3);
			memcpy(&UART3_DataCurr, &UART3_RxTmpTwo,UART3_PARSING_SIZE * UART3_RXPACKET_SIZE);
			pthread_mutex_unlock(&mutex_UART3);
			UART3_DataParsing(UART3_DataCurr);
			bParsingFlag_UART3 = 0;
		}
		sleep(0); // 다른 Thread 들의 점유를 위해 사용

	}
}


void UART3_DataParsing(unsigned char (*pBuff)[UART3_RXPACKET_SIZE]) {
	unsigned char PF, PS, SourceAddr, priority;
//	int nPF,nPS,nSourceAddr,npriority;
	int i = 0;
	int j = 0;

	for (i = 0; i < UART3_PARSING_SIZE; i++) {
		memcpy(&cUART3_RxData[0], &(pBuff[i][0]), UART3_RXPACKET_SIZE);
		if (cUART3_RxData[0] == SERIAL_RX_STX && cUART3_RxData[UART3_RXPACKET_SIZE-1] == SERIAL_RX_ETX)
		{
		//	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "ETX");
			UART3_SeparateData(cUART3_RxData[1]);

		}
		else if(cUART3_RxData[UART3_RXPACKET_SIZE-1] == SERIAL_RX_ACK){
		//	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "ACK");
			UART3_AckCheck(cUART3_RxData[UART3_RXPACKET_SIZE-1]);
		}
		else if(cUART3_RxData[UART3_RXPACKET_SIZE-1] == SERIAL_RX_NAK){
		//	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "NAK");
			UART3_AckCheck(cUART3_RxData[UART3_RXPACKET_SIZE-1]);
		}
		else
		{
			__android_log_print(ANDROID_LOG_INFO, "NATIVE", "ELSE");
		}

	}
}

void UART3_SeparateData(unsigned char RES_Kind) {

	switch (RES_Kind) {
	case KeyRES:
		KeyButtonCallback(cUART3_RxData[2] + (cUART3_RxData[3] << 8) + (cUART3_RxData[4] << 16) + (cUART3_RxData[5] << 24));
		break;
	case LCDRES:

		break;
	case BuzRES:

		break;
	case RTCRES:
		memcpy((unsigned char*) &RX_RES_RTC, &cUART3_RxData[2], 8);
		break;
	case CAMRES:

		break;
	case VersionRES:
		memcpy((unsigned char*) &RX_RES_Version, &cUART3_RxData[2], 8);
		break;
	case DOWNRES:
		UpdateResponseCallback(cUART3_RxData[2]);
		break;
	case SMKRES:

		break;
	case LampRES:

		break;
	case StartCANRES:

		break;
	}


}
void UART3_AckCheck(unsigned char Tail)
{
	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "Tail[0x%x]\n",Tail);
	switch(Tail)
	{
		case SERIAL_RX_ACK:
		case SERIAL_RX_NAK:
			UpdateResponseCallback(cUART3_RxData[UART3_RXPACKET_SIZE-1]);
			break;
	}
}


/////////////////////////////////////////////////
void *Thread_Read_UART1(void *data)
{
	int dwRead = 0;
	int i = 0;
	unsigned char UART1_SingleBuff;

	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "Thread_Read1\n");
	if (!glpVM)
	{
		__android_log_print(ANDROID_LOG_INFO, "NATIVE", "error (!glpVM)");
		return;
	}

	JNIEnv *env = NULL;
	(*glpVM)->AttachCurrentThread(glpVM, &env, NULL);
	if (env == NULL || jObject == NULL)
	{
		(*glpVM)->DetachCurrentThread(glpVM);
		__android_log_print(ANDROID_LOG_INFO, "NATIVE",
			"error (env == NULL || AVM_JM.jObject == NULL)");
		return;
	}

	while (bReadRunningFlag_UART1)
	{
		dwRead = 0;
		//	통신 시작 후 1byte씩 읽고, 정상 데이터를 수신한 다음부터는 원래 싸이즈로 받는다.
		if (UART1ReadFlag == 0)
		{
			dwRead = read(fd_UART1, &UART1_SingleBuff, 1);
//			__android_log_print(ANDROID_LOG_INFO, "NATIVE", "Thread_Read_UART1 Read OK [0x%x]\n",UART1_SingleBuff);
		}
		else
		{
			dwRead = read(fd_UART1, UART1_ReadBuff, UART1_RXPACKET_SIZE);

//			__android_log_print(ANDROID_LOG_INFO, "NATIVE", "UART1_ReadBuff [0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x]\n",
//							UART1_ReadBuff[0],UART1_ReadBuff[1],UART1_ReadBuff[2],UART1_ReadBuff[3],UART1_ReadBuff[4],UART1_ReadBuff[5],UART1_ReadBuff[6],UART1_ReadBuff[7],UART1_ReadBuff[8],UART1_ReadBuff[9],
//							UART1_ReadBuff[10],UART1_ReadBuff[11],UART1_ReadBuff[12],UART1_ReadBuff[13],UART1_ReadBuff[14],UART1_ReadBuff[15],UART1_ReadBuff[16]);
//			__android_log_print(ANDROID_LOG_INFO, "NATIVE","RX PGN[0x%x%x%x%x] Data[0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x]\n",
//					UART1_ReadBuff[6],UART1_ReadBuff[5],UART1_ReadBuff[4],UART1_ReadBuff[3],UART1_ReadBuff[7],UART1_ReadBuff[8],UART1_ReadBuff[9]
//					,UART1_ReadBuff[10],UART1_ReadBuff[11],UART1_ReadBuff[12],UART1_ReadBuff[13],UART1_ReadBuff[14]);
		}

		//	CAN PACKET 구조가 아니면 들어온 것을 모두 버린다.
		if (UART1ReadFlag == 1)
		{
			if (UART1_ReadBuff[0] != SERIAL_RX_STX || UART1_ReadBuff[UART1_RXPACKET_SIZE - 1] != SERIAL_RX_ETX)
			{

				tcflush(fd_UART1, TCIOFLUSH);
				UART1ReadFlag = 0;
				//				__android_log_print(ANDROID_LOG_INFO, "UART1_ReadBuff","UART1_Read Error\n");
				__android_log_print(ANDROID_LOG_INFO, "NATIVE", "UART1_ReadBuff Error[%d][%d][%d][%d][%d][%d][%d][%d][%d][%d][%d][%d][%d][%d][%d][%d][%d]\n",
					UART1_ReadBuff[0],UART1_ReadBuff[1],UART1_ReadBuff[2],UART1_ReadBuff[3],UART1_ReadBuff[4],UART1_ReadBuff[5],UART1_ReadBuff[6],UART1_ReadBuff[7],UART1_ReadBuff[8],UART1_ReadBuff[9],
					UART1_ReadBuff[10],UART1_ReadBuff[11],UART1_ReadBuff[12],UART1_ReadBuff[13],UART1_ReadBuff[14],UART1_ReadBuff[15],UART1_ReadBuff[16]);
			}
			else
			{
				UART1_DataParsing(UART1_ReadBuff);
				//CANRecvIndex++;
				//__android_log_print(ANDROID_LOG_INFO, "NATIVE", "CANRecvIndex[%d]\n",CANRecvIndex);
			}
		}
		else if (UART1ReadFlag == 0)
		{

			switch (dwUART1ReadCnt) {
				case 0:
					if (UART1_SingleBuff == SERIAL_RX_STX)
					{
						UART1_ReadBuff[dwUART1ReadCnt++] = UART1_SingleBuff;
					}
					else
					{
						dwUART1ReadCnt = 0;
					}
					break;
				case UART1_RXPACKET_SIZE - 1:
					if (UART1_SingleBuff == SERIAL_RX_ETX)
					{
						UART1_ReadBuff[dwUART1ReadCnt] = UART1_SingleBuff;
						dwUART1ReadCnt = 0;
						UART1ReadFlag = 1;
						UART1_DataParsing(UART1_ReadBuff);
						//CANRecvIndex++;
//						__android_log_print(ANDROID_LOG_INFO, "NATIVE", "UART1_ReadBuff [0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x][0x%x]\n",
//													UART1_ReadBuff[0],UART1_ReadBuff[1],UART1_ReadBuff[2],UART1_ReadBuff[3],UART1_ReadBuff[4],UART1_ReadBuff[5],UART1_ReadBuff[6],UART1_ReadBuff[7],UART1_ReadBuff[8],UART1_ReadBuff[9],
//													UART1_ReadBuff[10],UART1_ReadBuff[11],UART1_ReadBuff[12],UART1_ReadBuff[13],UART1_ReadBuff[14],UART1_ReadBuff[15],UART1_ReadBuff[16]);
//						__android_log_print(ANDROID_LOG_INFO, "NATIVE", "CANRecvIndex[%d]\n",CANRecvIndex);
					}else
					{
						dwUART1ReadCnt = 0;
						UART1ReadFlag = 0;
					}
					break;

				default:
					UART1_ReadBuff[dwUART1ReadCnt++] = UART1_SingleBuff;
					break;
			}

		}
		sleep(0); // 다른 Thread 들의 점유를 위해 사용
	}
	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "Thread_Read1 Finish\n");
	(*glpVM)->DetachCurrentThread(glpVM);
}

void *Thread_Read_UART3(void *data) {
	int dwRead = 0;
	int i = 0;

	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "Thread_Read3\n");
	if (!glpVM) {
		__android_log_print(ANDROID_LOG_INFO, "NATIVE", "error (!glpVM)");
		return;
	}

	JNIEnv *env = NULL;
	(*glpVM)->AttachCurrentThread(glpVM, &env, NULL);
	if (env == NULL || jObject == NULL) {
		(*glpVM)->DetachCurrentThread(glpVM);
		__android_log_print(ANDROID_LOG_INFO, "NATIVE",
				"error (env == NULL || AVM_JM.jObject == NULL)");
		return;
	}

	while (bReadRunningFlag_UART3) {
		dwRead = 0;
		//	통신 시작 후 1byte씩 읽고, 정상 데이터를 수신한 다음부터는 원래 싸이즈로 받는다.
		if (UART3ReadFlag == 0 || UART3ReadFlag == 1) {
			dwRead = read(fd_UART3, UART3_ReadBuff, 1);
//			__android_log_print(ANDROID_LOG_INFO, "UART3_ReadBuff", "dwRead[%d] UART3_ReadBuff [%d]\n",dwRead,UART3_ReadBuff[0]);
		}

		else {
			dwRead = read(fd_UART3, UART3_ReadBuff, UART3_RXPACKET_SIZE);
//			__android_log_print(ANDROID_LOG_INFO, "UART3_ReadBuff", "dwRead[%d] UART3_ReadBuff [%d][%d][%d][%d][%d][%d][%d][%d][%d][%d][%d]\n",dwRead,UART3_ReadBuff[0],UART3_ReadBuff[1]
//												  ,UART3_ReadBuff[2],UART3_ReadBuff[3],UART3_ReadBuff[4],UART3_ReadBuff[5],UART3_ReadBuff[6],UART3_ReadBuff[7]
//												  ,UART3_ReadBuff[8],UART3_ReadBuff[9],UART3_ReadBuff[10]);
		}

		//	CAN PACKET 구조가 아니면 들어온 것을 모두 버린다.
		if (UART3ReadFlag == 2) {
			if (UART3_ReadBuff[0] != SERIAL_RX_STX || (UART3_ReadBuff[UART3_RXPACKET_SIZE - 1] != SERIAL_RX_ETX &&  UART3_ReadBuff[UART3_RXPACKET_SIZE - 1] != SERIAL_RX_ACK && UART3_ReadBuff[UART3_RXPACKET_SIZE - 1] != SERIAL_RX_NAK)) {

				tcflush(fd_UART3, TCIOFLUSH);
				UART3ReadFlag = 0;

				__android_log_print(ANDROID_LOG_INFO, "UART3_ReadBuff", "dwRead[%d] UART3_ReadBuff error [%d][%d][%d][%d][%d][%d][%d][%d][%d][%d][%d]\n",dwRead,UART3_ReadBuff[0],UART3_ReadBuff[1]
																  ,UART3_ReadBuff[2],UART3_ReadBuff[3],UART3_ReadBuff[4],UART3_ReadBuff[5],UART3_ReadBuff[6],UART3_ReadBuff[7]
																  ,UART3_ReadBuff[8],UART3_ReadBuff[9],UART3_ReadBuff[10]);
			} else {

				memcpy(&(UART3_RxTmpOne[UART3_RxInx++][0]), &UART3_ReadBuff[0],
						UART3_RXPACKET_SIZE);

				if (UART3_RxInx >= UART3_PARSING_SIZE) {
					UART3_RxInx = 0;
					pthread_mutex_lock(&mutex_UART3);
					memcpy(&(UART3_RxTmpTwo), &(UART3_RxTmpOne),
							UART3_PARSING_SIZE * UART3_RXPACKET_SIZE);
					pthread_mutex_unlock(&mutex_UART3);
					bParsingFlag_UART3 = 1;
				}
			}
		} else if (UART3ReadFlag == 0) {
			if (UART3_ReadBuff[0] == SERIAL_RX_STX) {
				UART3ReadFlag = 1;
				dwUART3ReadCnt = 1;
			} else {
				dwUART3ReadCnt = 0;
			}
		} else if (UART3ReadFlag == 1) {
			if (dwUART3ReadCnt == (UART3_RXPACKET_SIZE - 1) && UART3_ReadBuff[0] == SERIAL_RX_ETX)
			{
				UART3ReadFlag = 2;
			}
		else
		{
			if (dwUART3ReadCnt == (UART3_RXPACKET_SIZE - 1))
			{
				UART3ReadFlag = 0;
				dwUART3ReadCnt = 0;
			}
			else
			{
				dwUART3ReadCnt++;
			}
		}
	}
		sleep(0); // 다른 Thread 들의 점유를 위해 사용
	}
	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "Thread_Read3 Finish\n");
	(*glpVM)->DetachCurrentThread(glpVM);
}

speed_t getBaudrate(jint baudrate) {
	switch (baudrate) {
	case 0:
		return B0;
	case 50:
		return B50;
	case 75:
		return B75;
	case 110:
		return B110;
	case 134:
		return B134;
	case 150:
		return B150;
	case 200:
		return B200;
	case 300:
		return B300;
	case 600:
		return B600;
	case 1200:
		return B1200;
	case 1800:
		return B1800;
	case 2400:
		return B2400;
	case 4800:
		return B4800;
	case 9600:
		return B9600;
	case 19200:
		return B19200;
	case 38400:
		return B38400;
	case 57600:
		return B57600;
	case 115200:
		return B115200;
	case 230400:
		return B230400;
	case 460800:
		return B460800;
	case 500000:
		return B500000;
	case 576000:
		return B576000;
	case 921600:
		return B921600;
	case 1000000:
		return B1000000;
	case 1152000:
		return B1152000;
	case 1500000:
		return B1500000;
	case 2000000:
		return B2000000;
	case 2500000:
		return B2500000;
	case 3000000:
		return B3000000;
	case 3500000:
		return B3500000;
	case 4000000:
		return B4000000;
	default:
		return -1;
	}
}

jobject _Open_UART1(JNIEnv *env, jclass this, jstring path, jint baudrate,jint flags) {
	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "Oepn UART1.\n");

	speed_t speed;
	jobject mFileDescriptor;

	/* Check arguments */
	{
		speed = getBaudrate(baudrate);
		if (speed == -1) {
			/* TODO: throw an exception */
			LOGE("Invalid baudrate");
			return NULL;
		}
	}

	/* Opening device */
	{
		jboolean iscopy;
		const char *path_utf = (*env)->GetStringUTFChars(env, path, &iscopy);
		LOGD("Opening serial port %s with flags 0x%x", path_utf,
				O_RDWR | flags);
		fd_UART1 = open(path_utf, O_RDWR | flags);
		LOGD("open() fd_UART1 = %d", fd_UART1);
		(*env)->ReleaseStringUTFChars(env, path, path_utf);
		if (fd_UART1 == -1) {
			/* Throw an exception */
			LOGE("Cannot open port UART1");
			/* TODO: throw an exception */
			return NULL;
		}
	}

	/* Configure device */
	{
		struct termios cfg;
		LOGD("Configuring serial port UART1");
		if (tcgetattr(fd_UART1, &cfg)) {
			LOGE("tcgetattr() failed");
			close(fd_UART1);
			/* TODO: throw an exception */
			return NULL;
		}

		cfmakeraw(&cfg);
		cfsetispeed(&cfg, speed);
		cfsetospeed(&cfg, speed);

		cfg.c_cc[VTIME] = 0;
		cfg.c_cc[VMIN] = UART1_RXPACKET_SIZE;

		if (tcsetattr(fd_UART1, TCSANOW, &cfg)) {
			LOGE("tcsetattr() failed");
			close(fd_UART1);
			/* TODO: throw an exception */
			return NULL;
		}
	}

	/* Create a corresponding file descriptor */
	{
		jclass cFileDescriptor = (*env)->FindClass(env,
				"java/io/FileDescriptor");
		jmethodID iFileDescriptor = (*env)->GetMethodID(env, cFileDescriptor,
				"<init>", "()V");
		jfieldID descriptorID = (*env)->GetFieldID(env, cFileDescriptor,
				"descriptor", "I");
		mFileDescriptor = (*env)->NewObject(env, cFileDescriptor,
				iFileDescriptor);
		(*env)->SetIntField(env, mFileDescriptor, descriptorID,
				(jint) fd_UART1);
	}

	int arg;
	bReadRunningFlag_UART1 = 1;
	thr_id[0] = pthread_create(&p_thread[0], NULL, Thread_Read_UART1,
			(void *) &arg);
	if (thr_id[0] < 0) {
		__android_log_print(ANDROID_LOG_INFO, "NATIVE","Create read Thread_Read_UART1 fail.\n");
		return -1;
	} else {
		__android_log_print(ANDROID_LOG_INFO, "NATIVE","Create read Thread_Read_UART1 success id[%d].\n", p_thread[0]);
	}

	int arg2;
	bParsingRunningFlag_UART1 = 1;
	thr_id[1] = pthread_create(&p_thread[1], NULL, ThreadParsing_UART1,
			(void *) &arg2);
	if (thr_id[1] < 0) {
		__android_log_print(ANDROID_LOG_INFO, "NATIVE","Create parsing ThreadParsing_UART1 fail.\n");
		return -1;
	} else {
		__android_log_print(ANDROID_LOG_INFO, "NATIVE","Create parsing ThreadParsing_UART1 success id[%d].\n",p_thread[1]);
	}



	makeTimer("First Timer", &firstTimerID, 0, TIMER1_INTERVAL);
	//makeTimer("Second Timer", &SecondTimerID, 0, 20);

	return mFileDescriptor;

}

jobject _Open_UART3(JNIEnv *env, jclass this, jstring path, jint baudrate,
		jint flags) {
	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "Oepn UART3.\n");

	speed_t speed;
	jobject mFileDescriptor;

	/* Check arguments */
	{
		speed = getBaudrate(baudrate);
		if (speed == -1) {
			/* TODO: throw an exception */
			LOGE("Invalid baudrate");
			return NULL;
		}
	}

	/* Opening device */
	{
		jboolean iscopy;
		const char *path_utf = (*env)->GetStringUTFChars(env, path, &iscopy);
		LOGD("Opening serial port %s with flags 0x%x", path_utf,O_RDWR | flags);
		fd_UART3 = open(path_utf, O_RDWR | flags);
		LOGD("open() fd_UART3 = %d", fd_UART3);
		(*env)->ReleaseStringUTFChars(env, path, path_utf);
		if (fd_UART3 == -1) {
			/* Throw an exception */
			LOGE("Cannot open port UART3");
			/* TODO: throw an exception */
			return NULL;
		}
	}

	/* Configure device */
	{
		struct termios cfg;
		LOGD("Configuring serial port UART3");
		if (tcgetattr(fd_UART3, &cfg)) {
			LOGE("tcgetattr() failed");
			close(fd_UART3);
			/* TODO: throw an exception */
			return NULL;
		}

		cfmakeraw(&cfg);
		cfsetispeed(&cfg, speed);
		cfsetospeed(&cfg, speed);

		cfg.c_cc[VTIME] = 0;
		cfg.c_cc[VMIN] = UART3_RXPACKET_SIZE;

		if (tcsetattr(fd_UART3, TCSANOW, &cfg)) {
			LOGE("tcsetattr() failed");
			close(fd_UART3);
			/* TODO: throw an exception */
			return NULL;
		}
	}

	/* Create a corresponding file descriptor */
	{
		jclass cFileDescriptor = (*env)->FindClass(env,
				"java/io/FileDescriptor");
		jmethodID iFileDescriptor = (*env)->GetMethodID(env, cFileDescriptor,
				"<init>", "()V");
		jfieldID descriptorID = (*env)->GetFieldID(env, cFileDescriptor,
				"descriptor", "I");
		mFileDescriptor = (*env)->NewObject(env, cFileDescriptor,
				iFileDescriptor);
		(*env)->SetIntField(env, mFileDescriptor, descriptorID,
				(jint) fd_UART3);
	}

	int arg;
	bReadRunningFlag_UART3 = 1;
	thr_id[2] = pthread_create(&p_thread[2], NULL, Thread_Read_UART3,
			(void *) &arg);
	if (thr_id[2] < 0) {
		__android_log_print(ANDROID_LOG_INFO, "NATIVE",
				"Create read Thread_Read_UART3 fail.\n");
		return -1;
	} else {
		__android_log_print(ANDROID_LOG_INFO, "NATIVE",
				"Create read Thread_Read_UART3 success id[%d].\n", p_thread[2]);
	}

	int arg2;
	bParsingRunningFlag_UART3 = 1;
	thr_id[3] = pthread_create(&p_thread[3], NULL, ThreadParsing_UART3,
			(void *) &arg2);
	if (thr_id[3] < 0) {
		__android_log_print(ANDROID_LOG_INFO, "NATIVE",
				"Create parsing ThreadParsing_UART3 fail.\n");
		return -1;
	} else {
		__android_log_print(ANDROID_LOG_INFO, "NATIVE",
				"Create parsing ThreadParsing_UART3 success id[%d].\n",
				p_thread[3]);
	}

	return mFileDescriptor;

}

void _Close_UART1(JNIEnv *env, jobject this) {
	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "Close UART1.\n");
	jclass SerialPortClass = (*env)->GetObjectClass(env, this);
	jclass FileDescriptorClass = (*env)->FindClass(env,
			"java/io/FileDescriptor");

	jfieldID mFdID = (*env)->GetFieldID(env, SerialPortClass, "mFdUART1",
			"Ljava/io/FileDescriptor;");
	jfieldID descriptorID = (*env)->GetFieldID(env, FileDescriptorClass,
			"descriptor", "I");

	jobject mFd = (*env)->GetObjectField(env, this, mFdID);
	jint descriptor = (*env)->GetIntField(env, mFd, descriptorID);

	LOGD("close(fd = %d)", descriptor);
	close(descriptor);
	bReadRunningFlag_UART1 = 0;
	bParsingRunningFlag_UART1 = 0;

	int status = pthread_kill(p_thread[0], 0);

	if (status == 0) {
		__android_log_print(ANDROID_LOG_INFO, "NATIVE",
				"Thread ID[%d] kill sucess\n", p_thread[0]);
	}
	//else if ( status == ESRCH ) {
	//	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "Thread ID[%d] not exist..\n", p_thread[0]);
	//}
	//else if ( status == EINVAL ) {
	//	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "Thread ID[%d] is yet alive\n", p_thread[0]);
	//}
	else {
		__android_log_print(ANDROID_LOG_INFO, "NATIVE",
				"Thread ID[%d] kill fail\n", p_thread[0]);
	}

	status = pthread_kill(p_thread[1], 0);

	if (status == 0) {
		__android_log_print(ANDROID_LOG_INFO, "NATIVE",
				"Thread ID[%d] kill sucess\n", p_thread[1]);
	}
	//else if ( status == ESRCH ) {
	//	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "Thread ID[%d] not exist..\n", p_thread[1]);
	//}
	//else if ( status == EINVAL ) {
	//	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "Thread ID[%d] is yet alive\n", p_thread[1]);
	//}
	else {
		__android_log_print(ANDROID_LOG_INFO, "NATIVE",
				"Thread ID[%d] kill fail\n", p_thread[1]);
	}

	timer_delete(&firstTimerID);
}

void _Close_UART3(JNIEnv *env, jobject this) {
	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "Close UART3.\n");
	jclass SerialPortClass = (*env)->GetObjectClass(env, this);
	jclass FileDescriptorClass = (*env)->FindClass(env,
			"java/io/FileDescriptor");

	jfieldID mFdID = (*env)->GetFieldID(env, SerialPortClass, "mFdUART3",
			"Ljava/io/FileDescriptor;");
	jfieldID descriptorID = (*env)->GetFieldID(env, FileDescriptorClass,
			"descriptor", "I");

	jobject mFd = (*env)->GetObjectField(env, this, mFdID);
	jint descriptor = (*env)->GetIntField(env, mFd, descriptorID);

	LOGD("close(fd = %d)", descriptor);
	close(descriptor);
	bReadRunningFlag_UART3 = 0;
	bParsingRunningFlag_UART3 = 0;

	int status = pthread_kill(p_thread[0], 0);

	if (status == 0) {
		__android_log_print(ANDROID_LOG_INFO, "NATIVE",
				"Thread ID[%d] kill sucess\n", p_thread[2]);
	}
	//else if ( status == ESRCH ) {
	//	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "Thread ID[%d] not exist..\n", p_thread[2]);
	//}
	//else if ( status == EINVAL ) {
	//	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "Thread ID[%d] is yet alive\n", p_thread[2]);
	//}
	else {
		__android_log_print(ANDROID_LOG_INFO, "NATIVE",
				"Thread ID[%d] kill fail\n", p_thread[2]);
	}

	status = pthread_kill(p_thread[1], 0);

	if (status == 0) {
		__android_log_print(ANDROID_LOG_INFO, "NATIVE",
				"Thread ID[%d] kill sucess\n", p_thread[3]);
	}
	//else if ( status == ESRCH ) {
	//	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "Thread ID[%d] not exist..\n", p_thread[3]);
	//}
	//else if ( status == EINVAL ) {
	//	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "Thread ID[%d] is yet alive\n", p_thread[3]);
	//}
	else {
		__android_log_print(ANDROID_LOG_INFO, "NATIVE",
				"Thread ID[%d] kill fail\n", p_thread[3]);
	}
}

jint _Write_UART1(JNIEnv *env, jobject this, jbyteArray arr, jint size) {
	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "Write.\n");
	jbyte *carr;
	int result = 0;

	carr = (*env)->GetByteArrayElements(env, arr, NULL);

	result = write(fd_UART1, carr, size);

#ifdef DEBUG_WRITE
	int i = 0;
	for(i = 0; i < size; i++)
	{
		__android_log_print(ANDROID_LOG_INFO, "NATIVE_WRITE UART1", "carr[%d].\n",carr[i]);
	}
	__android_log_print(ANDROID_LOG_INFO, "NATIVE_WRITE", "size[%d].\n",size);
	__android_log_print(ANDROID_LOG_INFO, "NATIVE_WRITE", "result[%d].\n",result);
#endif

	(*env)->ReleaseByteArrayElements(env, arr, carr, 0);

	return result;
}

jint _Write_UART3(JNIEnv *env, jobject this, jbyteArray arr, jint size) {
	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "Write UART3.\n");
	jbyte *carr;
	int result = 0;

	carr = (*env)->GetByteArrayElements(env, arr, NULL);

	result = write(fd_UART3, carr, size);

#ifdef DEBUG_WRITE
	int i = 0;
	for(i = 0; i < size; i++)
	{
		__android_log_print(ANDROID_LOG_INFO, "NATIVE_WRITE", "carr[%d].\n",carr[i]);
	}
	__android_log_print(ANDROID_LOG_INFO, "NATIVE_WRITE", "size[%d].\n",size);
	__android_log_print(ANDROID_LOG_INFO, "NATIVE_WRITE", "result[%d].\n",result);
#endif

	(*env)->ReleaseByteArrayElements(env, arr, carr, 0);

	return result;
}

jint _UART1_TxComm(JNIEnv *env, jobject this, jint PS) {
	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "UART1_Tx [%d]\n", PS);
	int result = 0;

	switch (PS) {
			case 33:	//0x21
				MakeCANDataSingle(0x18,0xEF,TargetSourceAddress,SA_CANUPDATE,(unsigned char*)&TX_REQUEST_SLAVE_INFO_61184_250_33);
				break;
			case 32:	//0x20
				MakeCANDataSingle(0x18,0xEF,TargetSourceAddress,SA_CANUPDATE,(unsigned char*)&TX_REQUEST_FW_N_INFO_61184_250_32);
				break;
			case 65:	// 0x41
//				nRTSDataLength = sizeof(TX_ENTER_DL_MODE_61184_250_65);
//				memcpy(&RTSData[0], (unsigned char*) &TX_ENTER_DL_MODE_61184_250_65, sizeof(TX_ENTER_DL_MODE_61184_250_65));
//				Send_RTS(0x1C,0xEF,0x00,TargetSourceAddress,SA_CANUPDATE,nRTSDataLength);
				MakeCANDataSingle(0x18,0xEF,TargetSourceAddress,SA_CANUPDATE,(unsigned char*)&TX_ENTER_DL_MODE_61184_250_65);
				UpdateCancel = 0;	// Update 시작이므로 0으로 초기화
				break;
			case 66:	// 0x42
				nRTSDataLength = nLength_61184_250_66;
				memcpy(&RTSData[0], (unsigned char*) &TX_SEND_NEW_FW_N_INFO_61184_250_66, sizeof(TX_SEND_NEW_FW_N_INFO_61184_250_66));
				Send_RTS(0x1C,0xEF,0x00,TargetSourceAddress,SA_CANUPDATE,nRTSDataLength);
				break;
			case 64:	// 0x40
				MakeCANDataSingle(0x18,0xEF,TargetSourceAddress,SA_CANUPDATE,(unsigned char*)&TX_APP_N_DL_START_61184_250_64);
				break;
			case 67:	// 0x43
				if(TX_SEND_PACKET_M_61184_250_67.PacketLength == 1024)
				{
					nRTSDataLength = sizeof(TX_SEND_PACKET_M_61184_250_67);
					memcpy(&RTSData[0], (unsigned char*) &TX_SEND_PACKET_M_61184_250_67, sizeof(TX_SEND_PACKET_M_61184_250_67));
					Send_RTS(0x1C,0xEF,0x00,TargetSourceAddress,SA_CANUPDATE,nRTSDataLength);
				}
				else if(TX_SEND_PACKET_M_61184_250_67.PacketLength != 1024)
				{
					nRTSDataLength = sizeof(TX_SEND_PACKET_M_61184_250_67) - (1024-TX_SEND_PACKET_M_61184_250_67.PacketLength);
					TX_SEND_PACKET_M_61184_250_67.DistributionFileData[TX_SEND_PACKET_M_61184_250_67.PacketLength] = TX_SEND_PACKET_M_61184_250_67.PacketCRC % 256;
					TX_SEND_PACKET_M_61184_250_67.DistributionFileData[TX_SEND_PACKET_M_61184_250_67.PacketLength+1] = TX_SEND_PACKET_M_61184_250_67.PacketCRC / 256;
					memcpy(&RTSData[0], (unsigned char*) &TX_SEND_PACKET_M_61184_250_67, sizeof(TX_SEND_PACKET_M_61184_250_67));
					Send_RTS(0x1C,0xEF,0x00,TargetSourceAddress,SA_CANUPDATE,nRTSDataLength);
				}
				break;

			case 81:	// 0x51
//				nRTSDataLength = nLength_61184_250_81;
//				memcpy(&RTSData[0], (unsigned char*) &TX_QUIT_DL_MODE_61184_250_81, sizeof(TX_QUIT_DL_MODE_61184_250_81));
//				Send_RTS(0x1C,0xEF,0x00,TargetSourceAddress,SA_CANUPDATE,nRTSDataLength);
				MakeCANDataSingle(0x18,0xEF,TargetSourceAddress,SA_CANUPDATE,(unsigned char*)&TX_QUIT_DL_MODE_61184_250_81);
				break;
			case 69:  // 0x45
				MakeCANDataSingle(0x18,0xEF,TargetSourceAddress,SA_CANUPDATE,(unsigned char*)&TX_UPD_UPDATE_START_61184_250_69);
				break;
			case 70:  // 0x46
				MakeCANDataSingle(0x18,0xEF,TargetSourceAddress,SA_CANUPDATE,(unsigned char*)&TX_APP_N_DL_CANCEL_61184_250_70);
				UpdateCancel = 1;	// Update 취소되었으므로 CheckCTS,ACK문 삭제
				break;
			case 96:	// 0x60
				MakeCANDataSingle(0x18,0xEF,TargetSourceAddress,SA_CANUPDATE,(unsigned char*)&TX_FW_UPDATE_START_61184_250_96);
				break;
			case 121:
				MakeCANDataSingle(0x18,0xEF,SA_MCU,SA_MONITOR,(unsigned char*)&TX_ENGINE_SHUTDOWN_MODE_SETTING_61184_121);
				break;
			case 52: //0x34
				MakeCANBKCURTSData(0x18,0xFF,TargetSourceAddress,SA_CANUPDATE,(unsigned char*)&TX_BKCU_RTS_DOWNLOAD_MODE_65280_250_52);
				break;
			case 53: //0x35
				MakeCANBKCUSendData(0x18,0xFE,TargetSourceAddress,SA_CANUPDATE,(unsigned char*)&TX_BKCU_SEND_PACKET_65024_250_52);
				break;

		}
	return result;
}

jint _UART3_TxComm(JNIEnv *env, jobject this, jint CMD, jint DAT1, jint DAT2,
		jint DAT3, jint DAT4, jint DAT5, jint DAT6, jint DAT7, jint DAT8) {
	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "Write UART3.\n");
	int result = 0;

	jbyte tx_buf[UART3_TXPACKET_SIZE];

	tx_buf[0] = 0x02;	// STX
	tx_buf[1] = CMD;
	tx_buf[2] = DAT1;
	tx_buf[3] = DAT2;
	tx_buf[4] = DAT3;
	tx_buf[5] = DAT4;
	tx_buf[6] = DAT5;
	tx_buf[7] = DAT6;
	tx_buf[8] = DAT7;
	tx_buf[9] = DAT8;
	tx_buf[UART3_TXPACKET_SIZE-1] = 0x03;	// ETX


	result = write(fd_UART3, tx_buf, UART3_TXPACKET_SIZE);

	return result;
}

jint _UART3_UpdatePacketComm(JNIEnv *env, jobject this, jint Index, jint CRC, jint EOTFlag, jbyteArray arr)
{
	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "Send Update Packet.\n");
	int result = 0;
	jbyte *carr;
	int i;

	carr = (*env)->GetByteArrayElements(env, arr, NULL);

	jbyte tx_buf[UART3_UPDATEPACKET_SIZE];

	tx_buf[0] = SERIAL_RX_STX;
	tx_buf[1] = (Index & 0x00FF);		// Index Low
	tx_buf[2] = (Index & 0xFF00) >> 8;	// Index High

	for(i = 0; i < 1024; i++){			// Data(1024byte)
		tx_buf[i+3] = carr[i];
	}

	tx_buf[1027] = (CRC & 0x00FF);		// CRC Low
	tx_buf[1028] = (CRC & 0xFF00) >> 8;	// CRC High
	if(EOTFlag == 1)
		tx_buf[1029] = SERIAL_RX_EOT;
	else
		tx_buf[1029] = SERIAL_RX_ETX;


	result = write(fd_UART3,tx_buf,UART3_TXPACKET_SIZE);

	return result;
}


void KeyButtonCallback(unsigned int KeyData) {
	jmethodID funcKeyCallBack;

	if (!glpVM) {
		__android_log_print(ANDROID_LOG_INFO, "NATIVE", "error (!glpVM)");
		return;
	}

	JNIEnv *env = NULL;
	(*glpVM)->AttachCurrentThread(glpVM, &env, NULL);
	if (env == NULL || jObject == NULL) {
		(*glpVM)->DetachCurrentThread(glpVM);
		__android_log_print(ANDROID_LOG_INFO, "NATIVE",
				"error (env == NULL || AVM_JM.jObject == NULL)");
		return;
	}

	funcKeyCallBack = (*env)->GetStaticMethodID(env, jObject,
			"KeyButtonCallBack", "(I)V");

	if (funcKeyCallBack == 0) {
		__android_log_print(ANDROID_LOG_INFO, "NATIVE",
				"Can't fine the function.");
		(*env)->DeleteGlobalRef(env, jObject);
	}

	else {
		(*env)->CallStaticVoidMethod(env, jObject, funcKeyCallBack, KeyData);
	}

	(*glpVM)->DetachCurrentThread(glpVM);
}

void CIDCallback() {
	jmethodID CIDCallBack;

	if (!glpVM) {
		__android_log_print(ANDROID_LOG_INFO, "NATIVE", "error (!glpVM)");
		return;
	}

	JNIEnv *env = NULL;
	(*glpVM)->AttachCurrentThread(glpVM, &env, NULL);
	if (env == NULL || jObject == NULL) {
		(*glpVM)->DetachCurrentThread(glpVM);
		__android_log_print(ANDROID_LOG_INFO, "NATIVE",
				"error (env == NULL || AVM_JM.jObject == NULL)");
		return;
	}

	CIDCallBack = (*env)->GetStaticMethodID(env, jObject, "CIDCallBack", "()V");

	if (CIDCallBack == 0) {
		__android_log_print(ANDROID_LOG_INFO, "NATIVE",
				"Can't fine the function.");
		(*env)->DeleteGlobalRef(env, jObject);
	}

	else {
		(*env)->CallStaticVoidMethod(env, jObject, CIDCallBack);
	}

	(*glpVM)->DetachCurrentThread(glpVM);
}

void ASCallback() {
	jmethodID ASCallback;

	if (!glpVM) {
		__android_log_print(ANDROID_LOG_INFO, "NATIVE", "error (!glpVM)");
		return;
	}

	JNIEnv *env = NULL;
	(*glpVM)->AttachCurrentThread(glpVM, &env, NULL);
	if (env == NULL || jObject == NULL) {
		(*glpVM)->DetachCurrentThread(glpVM);
		__android_log_print(ANDROID_LOG_INFO, "NATIVE",
				"error (env == NULL || AVM_JM.jObject == NULL)");
		return;
	}

	ASCallback = (*env)->GetStaticMethodID(env, jObject, "ASCallback", "()V");

	if (ASCallback == 0) {
		__android_log_print(ANDROID_LOG_INFO, "NATIVE",
				"Can't fine the function.");
		(*env)->DeleteGlobalRef(env, jObject);
	}

	else {
		(*env)->CallStaticVoidMethod(env, jObject, ASCallback);
	}

	(*glpVM)->DetachCurrentThread(glpVM);
}


void UpdateResponseCallback(unsigned char ResponseData)
{
	jmethodID funcUpdateResponseCallBack;
	int Temp;
	Temp = (ResponseData & 0xFF);
	if(!glpVM)
	{
		__android_log_print(ANDROID_LOG_INFO, "NATIVE", "error (!glpVM)");
		return;
	}

	JNIEnv *env = NULL;
	(*glpVM)->AttachCurrentThread(glpVM, &env, NULL);
	if(env == NULL || jObject == NULL){
			(*glpVM)->DetachCurrentThread(glpVM);
			__android_log_print(ANDROID_LOG_INFO, "NATIVE", "error (env == NULL || AVM_JM.jObject == NULL)");
			return;
	}


	funcUpdateResponseCallBack = (*env)->GetStaticMethodID(env, jObject,"UpdateResponseCallBack", "(I)V");

	if (funcUpdateResponseCallBack == 0 )
	{
	   __android_log_print(ANDROID_LOG_INFO, "NATIVE", "Can't fine the function.");
	   (*env)->DeleteGlobalRef(env, jObject);
	}

	else
	{
	   (*env)->CallStaticVoidMethod(env, jObject, funcUpdateResponseCallBack,Temp);
	}

	(*glpVM)->DetachCurrentThread(glpVM);
}


