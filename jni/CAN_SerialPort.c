
#include "CAN_DataParsing.c"

void	Set_nRecvSendBootloaderStatusFlag_61184_250_17(JNIEnv * env, jobject this, int Data)
{

	nRecvSendBootloaderStatusFlag_61184_250_17 = Data;
}

jint	Get_nRecvSendBootloaderStatusFlag_61184_250_17(JNIEnv * env, jobject this)
{
	return nRecvSendBootloaderStatusFlag_61184_250_17;
}


void	Set_MultiPacketErrFlag(JNIEnv * env, jobject this, int Data)
{
	MultiPacketErrFlag = Data;
}

jint	Get_MultiPacketErrFlag(JNIEnv * env, jobject this)
{
	return MultiPacketErrFlag;
}

jint	Get_TargetSourceAddress(JNIEnv * env, jobject this)
{
	return TargetSourceAddress;
}

void	Set_TargetSourceAddress(JNIEnv * env, jobject this, int Data)
{
	TargetSourceAddress = Data;
}

void	Set_nLength_61184_250_66(JNIEnv * env, jobject this, int Data)
{
	nLength_61184_250_66 = Data;
}

void	Set_nLength_61184_250_81(JNIEnv * env, jobject this, int Data)
{
	nLength_61184_250_81 = Data;
}

jint	Get_nRecAckNewFWNInfoFlag_61184_250_82(JNIEnv * env, jobject this)
{
	return nRecAckNewFWNInfoFlag_61184_250_82;
}

jint	Get_nRecvFWInfoFlag_61184_250_48(JNIEnv * env, jobject this)
{
	return nRecvFWInfoFlag_61184_250_48;
}

jint	Get_nRecvRequestPacketMFlag_61184_250_83(JNIEnv * env, jobject this)
{
	return nRecvRequestPacketMFlag_61184_250_83;
}

jint	Get_nRecvFWDLCompleteFlag_61184_250_80(JNIEnv * env, jobject this)
{
	return nRecvFWDLCompleteFlag_61184_250_80;
}

jint	Get_nRecvFWUpdateCompleteFlag_61184_250_112(JNIEnv * env, jobject this)
{
	return nRecvFWUpdateCompleteFlag_61184_250_112;
}

void	Set_nRecAckNewFWNInfoFlag_61184_250_82(JNIEnv * env, jobject this, int Data)
{
	nRecAckNewFWNInfoFlag_61184_250_82 = Data;
}

void	Set_nRecvFWInfoFlag_61184_250_48(JNIEnv * env, jobject this, int Data)
{
	nRecvFWInfoFlag_61184_250_48 = Data;
}

void	Set_nRecvRequestPacketMFlag_61184_250_83(JNIEnv * env, jobject this, int Data)
{
	nRecvRequestPacketMFlag_61184_250_83 = Data;
}

void	Set_nRecvFWDLCompleteFlag_61184_250_80(JNIEnv * env, jobject this, int Data)
{
	nRecvFWDLCompleteFlag_61184_250_80 = Data;

}

void	Set_nRecvFWUpdateCompleteFlag_61184_250_112(JNIEnv * env, jobject this, int Data)
{
	nRecvFWUpdateCompleteFlag_61184_250_112 = Data;

}

// ++ 150820, cjg
void	Set_nRecvUPDFormatCompleteFlag_61184_250_85(JNIEnv * env, jobject this, int Data)
{
	nRecvUPDFormatCompleteFlag_61184_250_85 = Data;

}
jint	Get_nRecvUPDFormatCompleteFlag_61184_250_85(JNIEnv * env, jobject this)
{
	return nRecvUPDFormatCompleteFlag_61184_250_85;
}
// -- 150820, cjg

////////////////////////////////////CAN Structure//////////////////////////////////////////////////////////////////

void	Set_FWID_TX_REQUEST_FW_N_INFO_61184_250_32(JNIEnv * env, jobject this, int Data)
{
	TX_REQUEST_FW_N_INFO_61184_250_32.FWID = Data;
}
//void	Set_FirmwareInformation_SlaveID_TX_ENTER_DL_MODE_61184_250_65(JNIEnv * env, jobject this, int Data)
//{
//	TX_ENTER_DL_MODE_61184_250_65.FirmwareInformation.SlaveID= Data;
//}
//void	Set_FirmwareInformation_FWID_TX_ENTER_DL_MODE_61184_250_65(JNIEnv * env, jobject this, int Data)
//{
//	TX_ENTER_DL_MODE_61184_250_65.FirmwareInformation.FWID= Data;
//}
//void	Set_FirmwareInformation_FWName_TX_ENTER_DL_MODE_61184_250_65(JNIEnv * env, jobject this, jbyteArray Data)
//{
//	jbyte *pArr;
//	int i;
//	int size = sizeof(TX_ENTER_DL_MODE_61184_250_65.FirmwareInformation.FWName);
//
//	pArr = (*env)->GetByteArrayElements(env, Data, NULL);
//
//	for (i = 0; i < size; i++)
//		TX_ENTER_DL_MODE_61184_250_65.FirmwareInformation.FWName[i] = pArr[i];
//
//	(*env)->ReleaseByteArrayElements(env, Data, pArr, 0);
//}
//void	Set_FirmwareInformation_FWModel_TX_ENTER_DL_MODE_61184_250_65(JNIEnv * env, jobject this, jbyteArray Data)
//{
//	jbyte *pArr;
//	int i;
//	int size = sizeof(TX_ENTER_DL_MODE_61184_250_65.FirmwareInformation.FWModel);
//
//	pArr = (*env)->GetByteArrayElements(env, Data, NULL);
//
//	for (i = 0; i < size; i++)
//		TX_ENTER_DL_MODE_61184_250_65.FirmwareInformation.FWModel[i] = pArr[i];
//
//	(*env)->ReleaseByteArrayElements(env, Data, pArr, 0);
//}
//void	Set_FirmwareInformation_FWVersion_TX_ENTER_DL_MODE_61184_250_65(JNIEnv * env, jobject this, jbyteArray Data)
//{
//	jbyte *pArr;
//	int i;
//	int size = sizeof(TX_ENTER_DL_MODE_61184_250_65.FirmwareInformation.FWVersion);
//
//	pArr = (*env)->GetByteArrayElements(env, Data, NULL);
//
//	for (i = 0; i < size; i++)
//		TX_ENTER_DL_MODE_61184_250_65.FirmwareInformation.FWVersion[i] = pArr[i];
//
//	(*env)->ReleaseByteArrayElements(env, Data, pArr, 0);
//}
//void	Set_FirmwareInformation_ProtoType_TX_ENTER_DL_MODE_61184_250_65(JNIEnv * env, jobject this, jbyteArray Data)
//{
//	jbyte *pArr;
//	int i;
//	int size = sizeof(TX_ENTER_DL_MODE_61184_250_65.FirmwareInformation.ProtoType);
//
//	pArr = (*env)->GetByteArrayElements(env, Data, NULL);
//
//	for (i = 0; i < size; i++)
//		TX_ENTER_DL_MODE_61184_250_65.FirmwareInformation.ProtoType[i] = pArr[i];
//
//	(*env)->ReleaseByteArrayElements(env, Data, pArr, 0);
//}
//void	Set_FirmwareInformation_FWChangeDay_TX_ENTER_DL_MODE_61184_250_65(JNIEnv * env, jobject this, jbyteArray Data)
//{
//	jbyte *pArr;
//	int i;
//	int size = sizeof(TX_ENTER_DL_MODE_61184_250_65.FirmwareInformation.FWChangeDay);
//
//	pArr = (*env)->GetByteArrayElements(env, Data, NULL);
//
//	for (i = 0; i < size; i++)
//		TX_ENTER_DL_MODE_61184_250_65.FirmwareInformation.FWChangeDay[i] = pArr[i];
//
//	(*env)->ReleaseByteArrayElements(env, Data, pArr, 0);
//}
//void	Set_FirmwareInformation_FWChangeTime_TX_ENTER_DL_MODE_61184_250_65(JNIEnv * env, jobject this, jbyteArray Data)
//{
//	jbyte *pArr;
//	int i;
//	int size = sizeof(TX_ENTER_DL_MODE_61184_250_65.FirmwareInformation.FWChangeTime);
//
//	pArr = (*env)->GetByteArrayElements(env, Data, NULL);
//
//	for (i = 0; i < size; i++)
//		TX_ENTER_DL_MODE_61184_250_65.FirmwareInformation.FWChangeTime[i] = pArr[i];
//
//	(*env)->ReleaseByteArrayElements(env, Data, pArr, 0);
//}
//void	Set_FirmwareInformation_FWFileSize_TX_ENTER_DL_MODE_61184_250_65(JNIEnv * env, jobject this, int Data)
//{
//	TX_ENTER_DL_MODE_61184_250_65.FirmwareInformation.FWFileSize= Data;
//}
//void	Set_FirmwareInformation_SectionUnitSize_TX_ENTER_DL_MODE_61184_250_65(JNIEnv * env, jobject this, int Data)
//{
//	TX_ENTER_DL_MODE_61184_250_65.FirmwareInformation.SectionUnitSize= Data;
//}
//void	Set_FirmwareInformation_PacketUnitSize_TX_ENTER_DL_MODE_61184_250_65(JNIEnv * env, jobject this, int Data)
//{
//	TX_ENTER_DL_MODE_61184_250_65.FirmwareInformation.PacketUnitSize= Data;
//}
//void	Set_FirmwareInformation_NumberofSectioninaFile_TX_ENTER_DL_MODE_61184_250_65(JNIEnv * env, jobject this, int Data)
//{
//	TX_ENTER_DL_MODE_61184_250_65.FirmwareInformation.NumberofSectioninaFile= Data;
//}
//void	Set_FirmwareInformation_NumberofPacketinaSection_TX_ENTER_DL_MODE_61184_250_65(JNIEnv * env, jobject this, int Data)
//{
//	TX_ENTER_DL_MODE_61184_250_65.FirmwareInformation.NumberofPacketinaSection= Data;
//}
//void	Set_FirmwareInformation_AppStartAddress_TX_ENTER_DL_MODE_61184_250_65(JNIEnv * env, jobject this, int Data)
//{
//	TX_ENTER_DL_MODE_61184_250_65.FirmwareInformation.AppStartAddress= Data;
//}
//
//void	Set_FirmwareInformation_CRC_TX_ENTER_DL_MODE_61184_250_65(JNIEnv * env, jobject this, int Index, int Data)
//{
//	TX_ENTER_DL_MODE_61184_250_65.FirmwareInformationCRC.CRC[Index] = Data;
//}

void	Set_FirmwareInformation_SlaveID_TX_SEND_NEW_FW_N_INFO_61184_250_66(JNIEnv * env, jobject this, int Data)
{
	TX_SEND_NEW_FW_N_INFO_61184_250_66.FirmwareInformation.SlaveID= Data;
}
void	Set_FirmwareInformation_FWID_TX_SEND_NEW_FW_N_INFO_61184_250_66(JNIEnv * env, jobject this, int Data)
{
	TX_SEND_NEW_FW_N_INFO_61184_250_66.FirmwareInformation.FWID= Data;
}
void	Set_FirmwareInformation_FWName_TX_SEND_NEW_FW_N_INFO_61184_250_66(JNIEnv * env, jobject this, jbyteArray Data)
{
	jbyte *pArr;
	int i;
	int size = sizeof(TX_SEND_NEW_FW_N_INFO_61184_250_66.FirmwareInformation.FWName);

	pArr = (*env)->GetByteArrayElements(env, Data, NULL);

	for (i = 0; i < size; i++)
		TX_SEND_NEW_FW_N_INFO_61184_250_66.FirmwareInformation.FWName[i] = pArr[i];

	(*env)->ReleaseByteArrayElements(env, Data, pArr, 0);
}
void	Set_FirmwareInformation_FWModel_TX_SEND_NEW_FW_N_INFO_61184_250_66(JNIEnv * env, jobject this, jbyteArray Data)
{
	jbyte *pArr;
	int i;
	int size = sizeof(TX_SEND_NEW_FW_N_INFO_61184_250_66.FirmwareInformation.FWModel);

	pArr = (*env)->GetByteArrayElements(env, Data, NULL);

	for (i = 0; i < size; i++)
		TX_SEND_NEW_FW_N_INFO_61184_250_66.FirmwareInformation.FWModel[i] = pArr[i];

	(*env)->ReleaseByteArrayElements(env, Data, pArr, 0);
}
void	Set_FirmwareInformation_FWVersion_TX_SEND_NEW_FW_N_INFO_61184_250_66(JNIEnv * env, jobject this, jbyteArray Data)
{
	jbyte *pArr;
	int i;
	int size = sizeof(TX_SEND_NEW_FW_N_INFO_61184_250_66.FirmwareInformation.FWVersion);

	pArr = (*env)->GetByteArrayElements(env, Data, NULL);

	for (i = 0; i < size; i++)
		TX_SEND_NEW_FW_N_INFO_61184_250_66.FirmwareInformation.FWVersion[i] = pArr[i];

	(*env)->ReleaseByteArrayElements(env, Data, pArr, 0);
}
void	Set_FirmwareInformation_ProtoType_TX_SEND_NEW_FW_N_INFO_61184_250_66(JNIEnv * env, jobject this, jbyteArray Data)
{
	jbyte *pArr;
	int i;
	int size = sizeof(TX_SEND_NEW_FW_N_INFO_61184_250_66.FirmwareInformation.ProtoType);

	pArr = (*env)->GetByteArrayElements(env, Data, NULL);

	for (i = 0; i < size; i++)
		TX_SEND_NEW_FW_N_INFO_61184_250_66.FirmwareInformation.ProtoType[i] = pArr[i];

	(*env)->ReleaseByteArrayElements(env, Data, pArr, 0);
}
void	Set_FirmwareInformation_FWChangeDay_TX_SEND_NEW_FW_N_INFO_61184_250_66(JNIEnv * env, jobject this, jbyteArray Data)
{
	jbyte *pArr;
	int i;
	int size = sizeof(TX_SEND_NEW_FW_N_INFO_61184_250_66.FirmwareInformation.FWChangeDay);

	pArr = (*env)->GetByteArrayElements(env, Data, NULL);

	for (i = 0; i < size; i++)
		TX_SEND_NEW_FW_N_INFO_61184_250_66.FirmwareInformation.FWChangeDay[i] = pArr[i];

	(*env)->ReleaseByteArrayElements(env, Data, pArr, 0);
}
void	Set_FirmwareInformation_FWChangeTime_TX_SEND_NEW_FW_N_INFO_61184_250_66(JNIEnv * env, jobject this, jbyteArray Data)
{
	jbyte *pArr;
	int i;
	int size = sizeof(TX_SEND_NEW_FW_N_INFO_61184_250_66.FirmwareInformation.FWChangeTime);

	pArr = (*env)->GetByteArrayElements(env, Data, NULL);

	for (i = 0; i < size; i++)
		TX_SEND_NEW_FW_N_INFO_61184_250_66.FirmwareInformation.FWChangeTime[i] = pArr[i];

	(*env)->ReleaseByteArrayElements(env, Data, pArr, 0);
}
void	Set_FirmwareInformation_FWFileSize_TX_SEND_NEW_FW_N_INFO_61184_250_66(JNIEnv * env, jobject this, int Data)
{
	TX_SEND_NEW_FW_N_INFO_61184_250_66.FirmwareInformation.FWFileSize= Data;
}
void	Set_FirmwareInformation_SectionUnitSize_TX_SEND_NEW_FW_N_INFO_61184_250_66(JNIEnv * env, jobject this, int Data)
{
	TX_SEND_NEW_FW_N_INFO_61184_250_66.FirmwareInformation.SectionUnitSize= Data;
}
void	Set_FirmwareInformation_PacketUnitSize_TX_SEND_NEW_FW_N_INFO_61184_250_66(JNIEnv * env, jobject this, int Data)
{
	TX_SEND_NEW_FW_N_INFO_61184_250_66.FirmwareInformation.PacketUnitSize= Data;
}
void	Set_FirmwareInformation_NumberofSectioninaFile_TX_SEND_NEW_FW_N_INFO_61184_250_66(JNIEnv * env, jobject this, int Data)
{
	TX_SEND_NEW_FW_N_INFO_61184_250_66.FirmwareInformation.NumberofSectioninaFile= Data;
}
void	Set_FirmwareInformation_NumberofPacketinaSection_TX_SEND_NEW_FW_N_INFO_61184_250_66(JNIEnv * env, jobject this, int Data)
{
	TX_SEND_NEW_FW_N_INFO_61184_250_66.FirmwareInformation.NumberofPacketinaSection= Data;
}
void	Set_FirmwareInformation_AppStartAddress_TX_SEND_NEW_FW_N_INFO_61184_250_66(JNIEnv * env, jobject this, int Data)
{
	TX_SEND_NEW_FW_N_INFO_61184_250_66.FirmwareInformation.AppStartAddress= Data;
}
void	Set_FirmwareInformation_CRC_TX_SEND_NEW_FW_N_INFO_61184_250_66(JNIEnv * env, jobject this,int Index, int Data)
{
	TX_SEND_NEW_FW_N_INFO_61184_250_66.FirmwareInformationCRC.CRC[Index] = Data;
}
void	Set_SectionNumber_TX_SEND_PACKET_M_61184_250_67(JNIEnv * env, jobject this, int Data)
{
	TX_SEND_PACKET_M_61184_250_67.SectionNumber= Data;
}
void	Set_PacketNumber_TX_SEND_PACKET_M_61184_250_67(JNIEnv * env, jobject this, int Data)
{
	TX_SEND_PACKET_M_61184_250_67.PacketNumber= Data;
}
void	Set_PacketLength_TX_SEND_PACKET_M_61184_250_67(JNIEnv * env, jobject this, int Data)
{
	TX_SEND_PACKET_M_61184_250_67.PacketLength= Data;
}
void	Set_DistributionFileData_TX_SEND_PACKET_M_61184_250_67(JNIEnv * env, jobject this, jbyteArray Data)
{
	jbyte *pArr;
	int i;
	int size = 0;

	if(TX_SEND_PACKET_M_61184_250_67.PacketLength > sizeof(TX_SEND_PACKET_M_61184_250_67.DistributionFileData))
		size = sizeof(TX_SEND_PACKET_M_61184_250_67.DistributionFileData);
	else
		size = TX_SEND_PACKET_M_61184_250_67.PacketLength;
	pArr = (*env)->GetByteArrayElements(env, Data, NULL);

	for (i = 0; i < size; i++){
		TX_SEND_PACKET_M_61184_250_67.DistributionFileData[i] = pArr[i];
	}

	(*env)->ReleaseByteArrayElements(env, Data, pArr, 0);
}
void	Set_PacketCRC_TX_SEND_PACKET_M_61184_250_67(JNIEnv * env, jobject this, int Data)
{
	TX_SEND_PACKET_M_61184_250_67.PacketCRC= Data;
}


//void	Set_FirmwareInformation_SlaveID_TX_QUIT_DL_MODE_61184_250_81(JNIEnv * env, jobject this, int Data)
//{
//	TX_QUIT_DL_MODE_61184_250_81.FirmwareInformation.SlaveID= Data;
//}
//void	Set_FirmwareInformation_FWID_TX_QUIT_DL_MODE_61184_250_81(JNIEnv * env, jobject this, int Data)
//{
//	TX_QUIT_DL_MODE_61184_250_81.FirmwareInformation.FWID= Data;
//}
//void	Set_FirmwareInformation_FWName_TX_QUIT_DL_MODE_61184_250_81(JNIEnv * env, jobject this, jbyteArray Data)
//{
//	jbyte *pArr;
//	int i;
//	int size = sizeof(TX_QUIT_DL_MODE_61184_250_81.FirmwareInformation.FWName);
//
//	pArr = (*env)->GetByteArrayElements(env, Data, NULL);
//
//	for (i = 0; i < size; i++)
//		TX_QUIT_DL_MODE_61184_250_81.FirmwareInformation.FWName[i] = pArr[i];
//
//	(*env)->ReleaseByteArrayElements(env, Data, pArr, 0);
//}
//void	Set_FirmwareInformation_FWModel_TX_QUIT_DL_MODE_61184_250_81(JNIEnv * env, jobject this, jbyteArray Data)
//{
//	jbyte *pArr;
//	int i;
//	int size = sizeof(TX_QUIT_DL_MODE_61184_250_81.FirmwareInformation.FWModel);
//
//	pArr = (*env)->GetByteArrayElements(env, Data, NULL);
//
//	for (i = 0; i < size; i++)
//		TX_QUIT_DL_MODE_61184_250_81.FirmwareInformation.FWModel[i] = pArr[i];
//
//	(*env)->ReleaseByteArrayElements(env, Data, pArr, 0);
//}
//void	Set_FirmwareInformation_FWVersion_TX_QUIT_DL_MODE_61184_250_81(JNIEnv * env, jobject this, jbyteArray Data)
//{
//	jbyte *pArr;
//	int i;
//	int size = sizeof(TX_QUIT_DL_MODE_61184_250_81.FirmwareInformation.FWVersion);
//
//	pArr = (*env)->GetByteArrayElements(env, Data, NULL);
//
//	for (i = 0; i < size; i++)
//		TX_QUIT_DL_MODE_61184_250_81.FirmwareInformation.FWVersion[i] = pArr[i];
//
//	(*env)->ReleaseByteArrayElements(env, Data, pArr, 0);
//}
//void	Set_FirmwareInformation_ProtoType_TX_QUIT_DL_MODE_61184_250_81(JNIEnv * env, jobject this, jbyteArray Data)
//{
//	jbyte *pArr;
//	int i;
//	int size = sizeof(TX_QUIT_DL_MODE_61184_250_81.FirmwareInformation.ProtoType);
//
//	pArr = (*env)->GetByteArrayElements(env, Data, NULL);
//
//	for (i = 0; i < size; i++)
//		TX_QUIT_DL_MODE_61184_250_81.FirmwareInformation.ProtoType[i] = pArr[i];
//
//	(*env)->ReleaseByteArrayElements(env, Data, pArr, 0);
//}
//void	Set_FirmwareInformation_FWChangeDay_TX_QUIT_DL_MODE_61184_250_81(JNIEnv * env, jobject this, jbyteArray Data)
//{
//	jbyte *pArr;
//	int i;
//	int size = sizeof(TX_QUIT_DL_MODE_61184_250_81.FirmwareInformation.FWChangeDay);
//
//	pArr = (*env)->GetByteArrayElements(env, Data, NULL);
//
//	for (i = 0; i < size; i++)
//		TX_QUIT_DL_MODE_61184_250_81.FirmwareInformation.FWChangeDay[i] = pArr[i];
//
//	(*env)->ReleaseByteArrayElements(env, Data, pArr, 0);
//}
//void	Set_FirmwareInformation_FWChangeTime_TX_QUIT_DL_MODE_61184_250_81(JNIEnv * env, jobject this, jbyteArray Data)
//{
//	jbyte *pArr;
//	int i;
//	int size = sizeof(TX_QUIT_DL_MODE_61184_250_81.FirmwareInformation.FWChangeTime);
//
//	pArr = (*env)->GetByteArrayElements(env, Data, NULL);
//
//	for (i = 0; i < size; i++)
//		TX_QUIT_DL_MODE_61184_250_81.FirmwareInformation.FWChangeTime[i] = pArr[i];
//
//	(*env)->ReleaseByteArrayElements(env, Data, pArr, 0);
//}
//void	Set_FirmwareInformation_FWFileSize_TX_QUIT_DL_MODE_61184_250_81(JNIEnv * env, jobject this, int Data)
//{
//	TX_QUIT_DL_MODE_61184_250_81.FirmwareInformation.FWFileSize= Data;
//}
//void	Set_FirmwareInformation_SectionUnitSize_TX_QUIT_DL_MODE_61184_250_81(JNIEnv * env, jobject this, int Data)
//{
//	TX_QUIT_DL_MODE_61184_250_81.FirmwareInformation.SectionUnitSize= Data;
//}
//void	Set_FirmwareInformation_PacketUnitSize_TX_QUIT_DL_MODE_61184_250_81(JNIEnv * env, jobject this, int Data)
//{
//	TX_QUIT_DL_MODE_61184_250_81.FirmwareInformation.PacketUnitSize= Data;
//}
//void	Set_FirmwareInformation_NumberofSectioninaFile_TX_QUIT_DL_MODE_61184_250_81(JNIEnv * env, jobject this, int Data)
//{
//	TX_QUIT_DL_MODE_61184_250_81.FirmwareInformation.NumberofSectioninaFile= Data;
//}
//void	Set_FirmwareInformation_NumberofPacketinaSection_TX_QUIT_DL_MODE_61184_250_81(JNIEnv * env, jobject this, int Data)
//{
//	TX_QUIT_DL_MODE_61184_250_81.FirmwareInformation.NumberofPacketinaSection= Data;
//}
//void	Set_FirmwareInformation_AppStartAddress_TX_QUIT_DL_MODE_61184_250_81(JNIEnv * env, jobject this, int Data)
//{
//	TX_QUIT_DL_MODE_61184_250_81.FirmwareInformation.AppStartAddress= Data;
//}
//void	Set_FirmwareInformation_CRC_TX_QUIT_DL_MODE_61184_250_81(JNIEnv * env, jobject this, int Index, int Data)
//{
//	TX_QUIT_DL_MODE_61184_250_81.FirmwareInformationCRC.CRC[Index] = Data;
//}




////////////////////////////////////////////////////////////////////////////////////////////////////////////
jint	Get_ResultCPUCRC_RX_SEND_BOOTLOADER_STATUS_61184_250_17(JNIEnv * env, jobject this)
{
	return RX_SEND_BOOTLOADER_STATUS_61184_250_17.ResultCPUCRC;
}

jint	Get_SlaveInformation_SlaveID_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	return RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.SlaveID;
}
jint	Get_SlaveInformation_SlaveName_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	return RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.SlaveName;
}
jbyteArray	Get_SlaveInformation_SerialNumber_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	jbyteArray Data = (*env)->NewByteArray(env, sizeof(RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.SerialNumber));

	(*env)->SetByteArrayRegion(env, Data, 0, sizeof(RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.SerialNumber),RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.SerialNumber);

	return Data;
}
jbyteArray	Get_SlaveInformation_HWVersion_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	jbyteArray Data = (*env)->NewByteArray(env, sizeof(RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.HWVersion));

	(*env)->SetByteArrayRegion(env, Data, 0, sizeof(RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.HWVersion),RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.HWVersion);

	return Data;
}
jbyteArray	Get_SlaveInformation_ProductDate_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	jbyteArray Data = (*env)->NewByteArray(env, sizeof(RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.ProductDate));

	(*env)->SetByteArrayRegion(env, Data, 0, sizeof(RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.ProductDate),RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.ProductDate);

	return Data;
}
jbyteArray	Get_SlaveInformation_DeliveryDate_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	jbyteArray Data = (*env)->NewByteArray(env, sizeof(RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.DeliveryDate));

	(*env)->SetByteArrayRegion(env, Data, 0, sizeof(RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.DeliveryDate),RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.DeliveryDate);

	return Data;
}
jint	Get_SlaveInformation_NumberofAPP_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	return RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.NumberofAPP;
}
jint	Get_SlaveInformation_App1StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	return RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.App1StartAddressOffset;
}
jint	Get_SlaveInformation_App1Size_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	return RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.App1Size;
}
jint	Get_SlaveInformation_App2StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	return RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.App2StartAddressOffset;
}
jint	Get_SlaveInformation_App2Size_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	return RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.App2Size;
}
jint	Get_SlaveInformation_App3StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	return RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.App3StartAddressOffset;
}
jint	Get_SlaveInformation_App3Size_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	return RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.App3Size;
}
jint	Get_SlaveInformation_App4StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	return RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.App4StartAddressOffset;
}
jint	Get_SlaveInformation_App4Size_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	return RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.App4Size;
}
jint	Get_SlaveInformation_App5StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	return RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.App5StartAddressOffset;
}
jint	Get_SlaveInformation_App5Size_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	return RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.App5Size;
}
jint	Get_SlaveInformation_App6StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	return RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.App6StartAddressOffset;
}
jint	Get_SlaveInformation_App6Size_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	return RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.App6Size;
}
jint	Get_SlaveInformation_App7StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	return RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.App7StartAddressOffset;
}
jint	Get_SlaveInformation_App7Size_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	return RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.App7Size;
}
jint	Get_SlaveInformation_App8StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	return RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.App8StartAddressOffset;
}
jint	Get_SlaveInformation_App8Size_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	return RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.App8Size;
}
jint	Get_SlaveInformation_App9StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	return RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.App9StartAddressOffset;
}
jint	Get_SlaveInformation_App9Size_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	return RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.App9Size;
}
jint	Get_SlaveInformation_App10StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	return RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.App10StartAddressOffset;
}
jint	Get_SlaveInformation_App10Size_RX_SEND_SLAVE_INFO_61184_250_49(JNIEnv * env, jobject this)
{
	return RX_SEND_SLAVE_INFO_61184_250_49.SlaveInformation.App10Size;
}
jint	Get_FirmwareInformation_SlaveID_RX_SEND_FW_N_INFO_61184_250_48(JNIEnv * env, jobject this)
{
	return RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.SlaveID;
}
jint	Get_FirmwareInformation_FWID_RX_SEND_FW_N_INFO_61184_250_48(JNIEnv * env, jobject this)
{
	return RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.FWID;
}
jbyteArray	Get_FirmwareInformation_FWName_RX_SEND_FW_N_INFO_61184_250_48(JNIEnv * env, jobject this)
{
	jbyteArray Data = (*env)->NewByteArray(env, sizeof(RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.FWName));

	(*env)->SetByteArrayRegion(env, Data, 0, sizeof(RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.FWName),RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.FWName);

	return Data;
}
jbyteArray	Get_FirmwareInformation_FWModel_RX_SEND_FW_N_INFO_61184_250_48(JNIEnv * env, jobject this)
{
	jbyteArray Data = (*env)->NewByteArray(env, sizeof(RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.FWModel));

	(*env)->SetByteArrayRegion(env, Data, 0, sizeof(RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.FWModel),RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.FWModel);

	return Data;
}
jbyteArray	Get_FirmwareInformation_FWVersion_RX_SEND_FW_N_INFO_61184_250_48(JNIEnv * env, jobject this)
{
	jbyteArray Data = (*env)->NewByteArray(env, sizeof(RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.FWVersion));

	(*env)->SetByteArrayRegion(env, Data, 0, sizeof(RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.FWVersion),RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.FWVersion);

	return Data;
}
jbyteArray	Get_FirmwareInformation_ProtoType_RX_SEND_FW_N_INFO_61184_250_48(JNIEnv * env, jobject this)
{
	jbyteArray Data = (*env)->NewByteArray(env, sizeof(RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.ProtoType));

	(*env)->SetByteArrayRegion(env, Data, 0, sizeof(RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.ProtoType),RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.ProtoType);

	return Data;
}
jbyteArray	Get_FirmwareInformation_FWChangeDay_RX_SEND_FW_N_INFO_61184_250_48(JNIEnv * env, jobject this)
{
	jbyteArray Data = (*env)->NewByteArray(env, sizeof(RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.FWChangeDay));

	(*env)->SetByteArrayRegion(env, Data, 0, sizeof(RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.FWChangeDay),RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.FWChangeDay);

	return Data;
}
jbyteArray	Get_FirmwareInformation_FWChangeTime_RX_SEND_FW_N_INFO_61184_250_48(JNIEnv * env, jobject this)
{
	jbyteArray Data = (*env)->NewByteArray(env, sizeof(RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.FWChangeTime));

	(*env)->SetByteArrayRegion(env, Data, 0, sizeof(RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.FWChangeTime),RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.FWChangeTime);

	return Data;
}
jint	Get_FirmwareInformation_FWFileSize_RX_SEND_FW_N_INFO_61184_250_48(JNIEnv * env, jobject this)
{
	return RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.FWFileSize;
}
jint	Get_FirmwareInformation_SectionUnitSize_RX_SEND_FW_N_INFO_61184_250_48(JNIEnv * env, jobject this)
{
	return RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.SectionUnitSize;
}
jint	Get_FirmwareInformation_PacketUnitSize_RX_SEND_FW_N_INFO_61184_250_48(JNIEnv * env, jobject this)
{
	return RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.PacketUnitSize;
}
jint	Get_FirmwareInformation_NumberofSectioninaFile_RX_SEND_FW_N_INFO_61184_250_48(JNIEnv * env, jobject this)
{
	return RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.NumberofSectioninaFile;
}
jint	Get_FirmwareInformation_NumberofPacketinaSection_RX_SEND_FW_N_INFO_61184_250_48(JNIEnv * env, jobject this)
{
	return RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.NumberofPacketinaSection;
}
jint	Get_FirmwareInformation_AppStartAddress_RX_SEND_FW_N_INFO_61184_250_48(JNIEnv * env, jobject this)
{
	return RX_SEND_FW_N_INFO_61184_250_48.FirmwareInformation.AppStartAddress;
}
jint	Get_FWID_RX_REQUEST_PACKET_M_61184_250_83(JNIEnv * env, jobject this)
{
	return RX_REQUEST_PACKET_M_61184_250_83.FWID;
}
jint	Get_SectionNumber_RX_REQUEST_PACKET_M_61184_250_83(JNIEnv * env, jobject this)
{
	return RX_REQUEST_PACKET_M_61184_250_83.SectionNumber;
}
jint	Get_PacketNumber_RX_REQUEST_PACKET_M_61184_250_83(JNIEnv * env, jobject this)
{
	return RX_REQUEST_PACKET_M_61184_250_83.PacketNumber;
}
jint	Get_ResultFlashCRC_RX_FW_N_DL_COMPLETE_61184_250_80(JNIEnv * env, jobject this)
{
	return RX_FW_N_DL_COMPLETE_61184_250_80.ResultFlashCRC;
}
jint	Get_Status_RX_FW_UPDATE_STATUS_61184_250_113(JNIEnv * env, jobject this)
{
	return RX_FW_UPDATE_STATUS_61184_250_113.Status;
}
jint	Get_Progress_ResultFlashCRC_RX_FW_UPDATE_STATUS_61184_250_113(JNIEnv * env, jobject this)
{
	return RX_FW_UPDATE_STATUS_61184_250_113.Progress_ResultFlashCRC;
}
jint	Get_ResultFlashCRC_RX_FW_UPDATE_COMPLETE_61184_250_112(JNIEnv * env, jobject this)
{
	return RX_FW_UPDATE_COMPLETE_61184_250_112.ResultFlashCRC;
}
jint	Get_FirmwareInformation_SlaveID_RX_FW_UPDATE_COMPLETE_61184_250_112(JNIEnv * env, jobject this)
{
	return RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.SlaveID;
}
jint	Get_FirmwareInformation_FWID_RX_FW_UPDATE_COMPLETE_61184_250_112(JNIEnv * env, jobject this)
{
	return RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.FWID;
}
jbyteArray	Get_FirmwareInformation_FWName_RX_FW_UPDATE_COMPLETE_61184_250_112(JNIEnv * env, jobject this)
{
	jbyteArray Data = (*env)->NewByteArray(env, sizeof(RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.FWName));

	(*env)->SetByteArrayRegion(env, Data, 0, sizeof(RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.FWName),RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.FWName);

	return Data;
}
jbyteArray	Get_FirmwareInformation_FWModel_RX_FW_UPDATE_COMPLETE_61184_250_112(JNIEnv * env, jobject this)
{
	jbyteArray Data = (*env)->NewByteArray(env, sizeof(RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.FWModel));

	(*env)->SetByteArrayRegion(env, Data, 0, sizeof(RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.FWModel),RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.FWModel);

	return Data;
}
jbyteArray	Get_FirmwareInformation_FWVersion_RX_FW_UPDATE_COMPLETE_61184_250_112(JNIEnv * env, jobject this)
{
	jbyteArray Data = (*env)->NewByteArray(env, sizeof(RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.FWVersion));

	(*env)->SetByteArrayRegion(env, Data, 0, sizeof(RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.FWVersion),RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.FWVersion);

	return Data;
}
jbyteArray	Get_FirmwareInformation_ProtoType_RX_FW_UPDATE_COMPLETE_61184_250_112(JNIEnv * env, jobject this)
{
	jbyteArray Data = (*env)->NewByteArray(env, sizeof(RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.ProtoType));

	(*env)->SetByteArrayRegion(env, Data, 0, sizeof(RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.ProtoType),RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.ProtoType);

	return Data;
}
jbyteArray	Get_FirmwareInformation_FWChangeDay_RX_FW_UPDATE_COMPLETE_61184_250_112(JNIEnv * env, jobject this)
{
	jbyteArray Data = (*env)->NewByteArray(env, sizeof(RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.FWChangeDay));

	(*env)->SetByteArrayRegion(env, Data, 0, sizeof(RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.FWChangeDay),RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.FWChangeDay);

	return Data;
}
jbyteArray	Get_FirmwareInformation_FWChangeTime_RX_FW_UPDATE_COMPLETE_61184_250_112(JNIEnv * env, jobject this)
{
	jbyteArray Data = (*env)->NewByteArray(env, sizeof(RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.FWChangeTime));

	(*env)->SetByteArrayRegion(env, Data, 0, sizeof(RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.FWChangeTime),RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.FWChangeTime);

	return Data;
}
jint	Get_FirmwareInformation_FWFileSize_RX_FW_UPDATE_COMPLETE_61184_250_112(JNIEnv * env, jobject this)
{
	return RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.FWFileSize;
}
jint	Get_FirmwareInformation_SectionUnitSize_RX_FW_UPDATE_COMPLETE_61184_250_112(JNIEnv * env, jobject this)
{
	return RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.SectionUnitSize;
}
jint	Get_FirmwareInformation_PacketUnitSize_RX_FW_UPDATE_COMPLETE_61184_250_112(JNIEnv * env, jobject this)
{
	return RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.PacketUnitSize;
}
jint	Get_FirmwareInformation_NumberofSectioninaFile_RX_FW_UPDATE_COMPLETE_61184_250_112(JNIEnv * env, jobject this)
{
	return RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.NumberofSectioninaFile;
}
jint	Get_FirmwareInformation_NumberofPacketinaSection_RX_FW_UPDATE_COMPLETE_61184_250_112(JNIEnv * env, jobject this)
{
	return RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.NumberofPacketinaSection;
}
jint	Get_FirmwareInformation_AppStartAddress_RX_FW_UPDATE_COMPLETE_61184_250_112(JNIEnv * env, jobject this)
{
	return RX_FW_UPDATE_COMPLETE_61184_250_112.FirmwareInformation.AppStartAddress;
}

jint	Get_Status_RX_UPD_UPDATE_STATUS_61184_250_84(JNIEnv * env, jobject this)
{
	return RX_UPD_UPDATE_STATUS_61184_250_84.Status;
}
jint	Get_Progress_RX_UPD_UPDATE_STATUS_61184_250_84(JNIEnv * env, jobject this)
{
	return RX_UPD_UPDATE_STATUS_61184_250_84.Progress_Update_Status;
}

void	Set_Status_RX_UPD_UPDATE_STATUS_61184_250_84(JNIEnv * env, jobject this, int Data)
{
	RX_UPD_UPDATE_STATUS_61184_250_84.Status = Data;
}

void	Set_Progress_RX_UPD_UPDATE_STATUS_61184_250_84(JNIEnv * env, jobject this, int Data)
{
	RX_UPD_UPDATE_STATUS_61184_250_84.Progress_Update_Status = Data;
}

void	Set_Status_RX_FW_UPDATE_STATUS_61184_250_113(JNIEnv * env, jobject this, int Data)
{
	RX_FW_UPDATE_STATUS_61184_250_113.Status = Data;
}
void	Set_Progress_ResultFlashCRC_RX_FW_UPDATE_STATUS_61184_250_113(JNIEnv * env, jobject this, int Data)
{
	RX_FW_UPDATE_STATUS_61184_250_113.Progress_ResultFlashCRC = Data;
}
void	Set_ResultFlashCRC_RX_FW_N_DL_COMPLETE_61184_250_80(JNIEnv * env, jobject this, int Data)
{
	RX_FW_N_DL_COMPLETE_61184_250_80.ResultFlashCRC = Data;
}

jint Get_CheckBKCUComm(JNIEnv * env, jobject this) {
	return CheckBKCUComm;
}
jint Get_CheckRMCUComm(JNIEnv * env, jobject this) {
	return CheckRMCUComm;
}
//BKCU

jint Get_SentPacketNumber_RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52(JNIEnv * env, jobject this) {
	return RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52.SentPacketNumber;
}
jint Get_NextStartPacketNumber_RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52(JNIEnv * env, jobject this) {
	return RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52.NextStartPacketNumber;
}
void	Set_SentPacketNumber_RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52(JNIEnv * env, jobject this, int Data)
{
	RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52.SentPacketNumber= Data;
}
void	Set_NextStartPacketNumber_RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52(JNIEnv * env, jobject this, int Data)
{
	RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52.NextStartPacketNumber= Data;
}


jint Get_ControlByte_RX_BKCU_DOWNLOAD_COMPLETE_65280_250_52(JNIEnv * env, jobject this) {
	return RX_BKCU_DOWNLOAD_COMPLETE_65280_250_52.ControlByte;
}
jint Get_TotalReceiveByte_RX_BKCU_DOWNLOAD_COMPLETE_65280_250_52(JNIEnv * env, jobject this) {
	return RX_BKCU_DOWNLOAD_COMPLETE_65280_250_52.TotalReceiveByte;
}
jint Get_TotalReceivePacket_RX_BKCU_DOWNLOAD_COMPLETE_65280_250_52(JNIEnv * env, jobject this) {
	return RX_BKCU_DOWNLOAD_COMPLETE_65280_250_52.TotalReceivePacket;
}

void	Set_TotalSendByte_TX_BKCU_RTS_DOWNLOAD_MODE_65280_250_52(JNIEnv * env, jobject this, int Data)
{
	TX_BKCU_RTS_DOWNLOAD_MODE_65280_250_52.TotalSendByte= Data;
}
void	Set_TotalSendPacket_TX_BKCU_RTS_DOWNLOAD_MODE_65280_250_52(JNIEnv * env, jobject this, int Data)
{
	TX_BKCU_RTS_DOWNLOAD_MODE_65280_250_52.TotalSendPacket= Data;
}

void	Set_SequenceNumber_TX_BKCU_SEND_PACKET_65024_250_52(JNIEnv * env, jobject this, int Data)
{
	TX_BKCU_SEND_PACKET_65024_250_52.SequenceNumber= Data;
}

void	Set_Data_TX_BKCU_SEND_PACKET_65024_250_52(JNIEnv * env, jobject this, jbyteArray Data)
{
	jbyte *pArr;
	int i;
	int size = sizeof(TX_BKCU_SEND_PACKET_65024_250_52.Data);
	pArr = (*env)->GetByteArrayElements(env, Data, NULL);
	for (i = 0; i < size; i++)
		TX_BKCU_SEND_PACKET_65024_250_52.Data[i] = pArr[i];
	(*env)->ReleaseByteArrayElements(env, Data, pArr, 0);
}

//BKCU


static JNINativeMethod methods[] = {
		{ "Open_UART1", "(Ljava/lang/String;II)Ljava/io/FileDescriptor;",(void*) _Open_UART1 },
		{ "Close_UART1", "()V",(void*) _Close_UART1 },
		{ "Write_UART1", "([BI)I",(void*) _Write_UART1 },
		{ "UART1_TxComm", "(I)I",(void*) _UART1_TxComm },

		{ "Open_UART3", "(Ljava/lang/String;II)Ljava/io/FileDescriptor;",(void*) _Open_UART3 },
		{ "Close_UART3", "()V",(void*) _Close_UART3 },
		{ "Write_UART3", "([BI)I",(void*) _Write_UART3 },
		{ "UART3_TxComm", "(IIIIIIIII)I",(void*) _UART3_TxComm },
		{"UART3_UpdatePacketComm", "(III[B)I", (void*)_UART3_UpdatePacketComm },


		{"Set_nRecvSendBootloaderStatusFlag_61184_250_17", "(I)V", (void*)Set_nRecvSendBootloaderStatusFlag_61184_250_17},
		{"Get_nRecvSendBootloaderStatusFlag_61184_250_17", "()I", (void*)*Get_nRecvSendBootloaderStatusFlag_61184_250_17},
		{"Set_MultiPacketErrFlag", "(I)V", (void*)Set_MultiPacketErrFlag},
		{"Get_MultiPacketErrFlag", "()I", (void*)*Get_MultiPacketErrFlag},
		{"Get_TargetSourceAddress", "()I", (void*)*Get_TargetSourceAddress},
		{"Set_TargetSourceAddress", "(I)V", (void*)Set_TargetSourceAddress},
		{"Set_nLength_61184_250_66", "(I)V", (void*)Set_nLength_61184_250_66},
		{"Set_nLength_61184_250_81", "(I)V", (void*)Set_nLength_61184_250_81},
		{"Get_nRecAckNewFWNInfoFlag_61184_250_82", "()I", (void*)*Get_nRecAckNewFWNInfoFlag_61184_250_82},
		{"Get_nRecvFWInfoFlag_61184_250_48", "()I", (void*)*Get_nRecvFWInfoFlag_61184_250_48},
		{"Get_nRecvRequestPacketMFlag_61184_250_83", "()I", (void*)*Get_nRecvRequestPacketMFlag_61184_250_83},
		{"Get_nRecvFWDLCompleteFlag_61184_250_80", "()I", (void*)*Get_nRecvFWDLCompleteFlag_61184_250_80},
		{"Get_nRecvFWUpdateCompleteFlag_61184_250_112", "()I", (void*)*Get_nRecvFWUpdateCompleteFlag_61184_250_112},
		{"Set_nRecAckNewFWNInfoFlag_61184_250_82", "(I)V", (void*)Set_nRecAckNewFWNInfoFlag_61184_250_82},
		{"Set_nRecvFWInfoFlag_61184_250_48", "(I)V", (void*)Set_nRecvFWInfoFlag_61184_250_48},
		{"Set_nRecvRequestPacketMFlag_61184_250_83", "(I)V", (void*)Set_nRecvRequestPacketMFlag_61184_250_83},
		{"Set_nRecvFWDLCompleteFlag_61184_250_80", "(I)V", (void*)Set_nRecvFWDLCompleteFlag_61184_250_80},
		{"Set_nRecvFWUpdateCompleteFlag_61184_250_112", "(I)V", (void*)Set_nRecvFWUpdateCompleteFlag_61184_250_112},
		{"Set_nRecvUPDFormatCompleteFlag_61184_250_85", "(I)V", (void*)Set_nRecvUPDFormatCompleteFlag_61184_250_85},
		{"Set_Status_RX_UPD_UPDATE_STATUS_61184_250_84", "(I)V", (void*)Set_Status_RX_UPD_UPDATE_STATUS_61184_250_84},
		{"Set_Progress_RX_UPD_UPDATE_STATUS_61184_250_84", "(I)V", (void*)Set_Progress_RX_UPD_UPDATE_STATUS_61184_250_84},
		{"Set_Status_RX_FW_UPDATE_STATUS_61184_250_113", "(I)V", (void*)Set_Status_RX_FW_UPDATE_STATUS_61184_250_113},
		{"Set_Progress_ResultFlashCRC_RX_FW_UPDATE_STATUS_61184_250_113", "(I)V", (void*)Set_Progress_ResultFlashCRC_RX_FW_UPDATE_STATUS_61184_250_113},
		{"Set_ResultFlashCRC_RX_FW_N_DL_COMPLETE_61184_250_80", "(I)V", (void*)Set_ResultFlashCRC_RX_FW_N_DL_COMPLETE_61184_250_80},

		///////////////////////////////////////Set///////////////////////////////////////////////
		{"Set_FWID_TX_REQUEST_FW_N_INFO_61184_250_32", "(I)V", (void*)Set_FWID_TX_REQUEST_FW_N_INFO_61184_250_32},
//		{"Set_FirmwareInformation_SlaveID_TX_ENTER_DL_MODE_61184_250_65", "(I)V", (void*)Set_FirmwareInformation_SlaveID_TX_ENTER_DL_MODE_61184_250_65},
//		{"Set_FirmwareInformation_FWID_TX_ENTER_DL_MODE_61184_250_65", "(I)V", (void*)Set_FirmwareInformation_FWID_TX_ENTER_DL_MODE_61184_250_65},
//		{"Set_FirmwareInformation_FWName_TX_ENTER_DL_MODE_61184_250_65", "([B)V", (void*)Set_FirmwareInformation_FWName_TX_ENTER_DL_MODE_61184_250_65},
//		{"Set_FirmwareInformation_FWModel_TX_ENTER_DL_MODE_61184_250_65", "([B)V", (void*)Set_FirmwareInformation_FWModel_TX_ENTER_DL_MODE_61184_250_65},
//		{"Set_FirmwareInformation_FWVersion_TX_ENTER_DL_MODE_61184_250_65", "([B)V", (void*)Set_FirmwareInformation_FWVersion_TX_ENTER_DL_MODE_61184_250_65},
//		{"Set_FirmwareInformation_ProtoType_TX_ENTER_DL_MODE_61184_250_65", "([B)V", (void*)Set_FirmwareInformation_ProtoType_TX_ENTER_DL_MODE_61184_250_65},
//		{"Set_FirmwareInformation_FWChangeDay_TX_ENTER_DL_MODE_61184_250_65", "([B)V", (void*)Set_FirmwareInformation_FWChangeDay_TX_ENTER_DL_MODE_61184_250_65},
//		{"Set_FirmwareInformation_FWChangeTime_TX_ENTER_DL_MODE_61184_250_65", "([B)V", (void*)Set_FirmwareInformation_FWChangeTime_TX_ENTER_DL_MODE_61184_250_65},
//		{"Set_FirmwareInformation_FWFileSize_TX_ENTER_DL_MODE_61184_250_65", "(I)V", (void*)Set_FirmwareInformation_FWFileSize_TX_ENTER_DL_MODE_61184_250_65},
//		{"Set_FirmwareInformation_SectionUnitSize_TX_ENTER_DL_MODE_61184_250_65", "(I)V", (void*)Set_FirmwareInformation_SectionUnitSize_TX_ENTER_DL_MODE_61184_250_65},
//		{"Set_FirmwareInformation_PacketUnitSize_TX_ENTER_DL_MODE_61184_250_65", "(I)V", (void*)Set_FirmwareInformation_PacketUnitSize_TX_ENTER_DL_MODE_61184_250_65},
//		{"Set_FirmwareInformation_NumberofSectioninaFile_TX_ENTER_DL_MODE_61184_250_65", "(I)V", (void*)Set_FirmwareInformation_NumberofSectioninaFile_TX_ENTER_DL_MODE_61184_250_65},
//		{"Set_FirmwareInformation_NumberofPacketinaSection_TX_ENTER_DL_MODE_61184_250_65", "(I)V", (void*)Set_FirmwareInformation_NumberofPacketinaSection_TX_ENTER_DL_MODE_61184_250_65},
//		{"Set_FirmwareInformation_AppStartAddress_TX_ENTER_DL_MODE_61184_250_65", "(I)V", (void*)Set_FirmwareInformation_AppStartAddress_TX_ENTER_DL_MODE_61184_250_65},
//		{"Set_FirmwareInformation_CRC_TX_ENTER_DL_MODE_61184_250_65", "(II)V", (void*)Set_FirmwareInformation_CRC_TX_ENTER_DL_MODE_61184_250_65},
		{"Set_FirmwareInformation_SlaveID_TX_SEND_NEW_FW_N_INFO_61184_250_66", "(I)V", (void*)Set_FirmwareInformation_SlaveID_TX_SEND_NEW_FW_N_INFO_61184_250_66},
		{"Set_FirmwareInformation_FWID_TX_SEND_NEW_FW_N_INFO_61184_250_66", "(I)V", (void*)Set_FirmwareInformation_FWID_TX_SEND_NEW_FW_N_INFO_61184_250_66},
		{"Set_FirmwareInformation_FWName_TX_SEND_NEW_FW_N_INFO_61184_250_66", "([B)V", (void*)Set_FirmwareInformation_FWName_TX_SEND_NEW_FW_N_INFO_61184_250_66},
		{"Set_FirmwareInformation_FWModel_TX_SEND_NEW_FW_N_INFO_61184_250_66", "([B)V", (void*)Set_FirmwareInformation_FWModel_TX_SEND_NEW_FW_N_INFO_61184_250_66},
		{"Set_FirmwareInformation_FWVersion_TX_SEND_NEW_FW_N_INFO_61184_250_66", "([B)V", (void*)Set_FirmwareInformation_FWVersion_TX_SEND_NEW_FW_N_INFO_61184_250_66},
		{"Set_FirmwareInformation_ProtoType_TX_SEND_NEW_FW_N_INFO_61184_250_66", "([B)V", (void*)Set_FirmwareInformation_ProtoType_TX_SEND_NEW_FW_N_INFO_61184_250_66},
		{"Set_FirmwareInformation_FWChangeDay_TX_SEND_NEW_FW_N_INFO_61184_250_66", "([B)V", (void*)Set_FirmwareInformation_FWChangeDay_TX_SEND_NEW_FW_N_INFO_61184_250_66},
		{"Set_FirmwareInformation_FWChangeTime_TX_SEND_NEW_FW_N_INFO_61184_250_66", "([B)V", (void*)Set_FirmwareInformation_FWChangeTime_TX_SEND_NEW_FW_N_INFO_61184_250_66},
		{"Set_FirmwareInformation_FWFileSize_TX_SEND_NEW_FW_N_INFO_61184_250_66", "(I)V", (void*)Set_FirmwareInformation_FWFileSize_TX_SEND_NEW_FW_N_INFO_61184_250_66},
		{"Set_FirmwareInformation_SectionUnitSize_TX_SEND_NEW_FW_N_INFO_61184_250_66", "(I)V", (void*)Set_FirmwareInformation_SectionUnitSize_TX_SEND_NEW_FW_N_INFO_61184_250_66},
		{"Set_FirmwareInformation_PacketUnitSize_TX_SEND_NEW_FW_N_INFO_61184_250_66", "(I)V", (void*)Set_FirmwareInformation_PacketUnitSize_TX_SEND_NEW_FW_N_INFO_61184_250_66},
		{"Set_FirmwareInformation_NumberofSectioninaFile_TX_SEND_NEW_FW_N_INFO_61184_250_66", "(I)V", (void*)Set_FirmwareInformation_NumberofSectioninaFile_TX_SEND_NEW_FW_N_INFO_61184_250_66},
		{"Set_FirmwareInformation_NumberofPacketinaSection_TX_SEND_NEW_FW_N_INFO_61184_250_66", "(I)V", (void*)Set_FirmwareInformation_NumberofPacketinaSection_TX_SEND_NEW_FW_N_INFO_61184_250_66},
		{"Set_FirmwareInformation_AppStartAddress_TX_SEND_NEW_FW_N_INFO_61184_250_66", "(I)V", (void*)Set_FirmwareInformation_AppStartAddress_TX_SEND_NEW_FW_N_INFO_61184_250_66},
		{"Set_FirmwareInformation_CRC_TX_SEND_NEW_FW_N_INFO_61184_250_66", "(II)V", (void*)Set_FirmwareInformation_CRC_TX_SEND_NEW_FW_N_INFO_61184_250_66},
		{"Set_SectionNumber_TX_SEND_PACKET_M_61184_250_67", "(I)V", (void*)Set_SectionNumber_TX_SEND_PACKET_M_61184_250_67},
		{"Set_PacketNumber_TX_SEND_PACKET_M_61184_250_67", "(I)V", (void*)Set_PacketNumber_TX_SEND_PACKET_M_61184_250_67},
		{"Set_PacketLength_TX_SEND_PACKET_M_61184_250_67", "(I)V", (void*)Set_PacketLength_TX_SEND_PACKET_M_61184_250_67},
		{"Set_DistributionFileData_TX_SEND_PACKET_M_61184_250_67", "([B)V", (void*)Set_DistributionFileData_TX_SEND_PACKET_M_61184_250_67},
		{"Set_PacketCRC_TX_SEND_PACKET_M_61184_250_67", "(I)V", (void*)Set_PacketCRC_TX_SEND_PACKET_M_61184_250_67},
//		{"Set_FirmwareInformation_SlaveID_TX_QUIT_DL_MODE_61184_250_81", "(I)V", (void*)Set_FirmwareInformation_SlaveID_TX_QUIT_DL_MODE_61184_250_81},
//		{"Set_FirmwareInformation_FWID_TX_QUIT_DL_MODE_61184_250_81", "(I)V", (void*)Set_FirmwareInformation_FWID_TX_QUIT_DL_MODE_61184_250_81},
//		{"Set_FirmwareInformation_FWName_TX_QUIT_DL_MODE_61184_250_81", "([B)V", (void*)Set_FirmwareInformation_FWName_TX_QUIT_DL_MODE_61184_250_81},
//		{"Set_FirmwareInformation_FWModel_TX_QUIT_DL_MODE_61184_250_81", "([B)V", (void*)Set_FirmwareInformation_FWModel_TX_QUIT_DL_MODE_61184_250_81},
//		{"Set_FirmwareInformation_FWVersion_TX_QUIT_DL_MODE_61184_250_81", "([B)V", (void*)Set_FirmwareInformation_FWVersion_TX_QUIT_DL_MODE_61184_250_81},
//		{"Set_FirmwareInformation_ProtoType_TX_QUIT_DL_MODE_61184_250_81", "([B)V", (void*)Set_FirmwareInformation_ProtoType_TX_QUIT_DL_MODE_61184_250_81},
//		{"Set_FirmwareInformation_FWChangeDay_TX_QUIT_DL_MODE_61184_250_81", "([B)V", (void*)Set_FirmwareInformation_FWChangeDay_TX_QUIT_DL_MODE_61184_250_81},
//		{"Set_FirmwareInformation_FWChangeTime_TX_QUIT_DL_MODE_61184_250_81", "([B)V", (void*)Set_FirmwareInformation_FWChangeTime_TX_QUIT_DL_MODE_61184_250_81},
//		{"Set_FirmwareInformation_FWFileSize_TX_QUIT_DL_MODE_61184_250_81", "(I)V", (void*)Set_FirmwareInformation_FWFileSize_TX_QUIT_DL_MODE_61184_250_81},
//		{"Set_FirmwareInformation_SectionUnitSize_TX_QUIT_DL_MODE_61184_250_81", "(I)V", (void*)Set_FirmwareInformation_SectionUnitSize_TX_QUIT_DL_MODE_61184_250_81},
//		{"Set_FirmwareInformation_PacketUnitSize_TX_QUIT_DL_MODE_61184_250_81", "(I)V", (void*)Set_FirmwareInformation_PacketUnitSize_TX_QUIT_DL_MODE_61184_250_81},
//		{"Set_FirmwareInformation_NumberofSectioninaFile_TX_QUIT_DL_MODE_61184_250_81", "(I)V", (void*)Set_FirmwareInformation_NumberofSectioninaFile_TX_QUIT_DL_MODE_61184_250_81},
//		{"Set_FirmwareInformation_NumberofPacketinaSection_TX_QUIT_DL_MODE_61184_250_81", "(I)V", (void*)Set_FirmwareInformation_NumberofPacketinaSection_TX_QUIT_DL_MODE_61184_250_81},
//		{"Set_FirmwareInformation_AppStartAddress_TX_QUIT_DL_MODE_61184_250_81", "(I)V", (void*)Set_FirmwareInformation_AppStartAddress_TX_QUIT_DL_MODE_61184_250_81},
//		{"Set_FirmwareInformation_CRC_TX_QUIT_DL_MODE_61184_250_81", "(II)V", (void*)Set_FirmwareInformation_CRC_TX_QUIT_DL_MODE_61184_250_81},



		///////////////////////////////////////Get///////////////////////////////////////////////

		{"Get_ResultCPUCRC_RX_SEND_BOOTLOADER_STATUS_61184_250_17", "()I", (void*)*Get_ResultCPUCRC_RX_SEND_BOOTLOADER_STATUS_61184_250_17},
		{"Get_SlaveInformation_SlaveID_RX_SEND_SLAVE_INFO_61184_250_49", "()I", (void*)*Get_SlaveInformation_SlaveID_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_SlaveName_RX_SEND_SLAVE_INFO_61184_250_49", "()I", (void*)*Get_SlaveInformation_SlaveName_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_SerialNumber_RX_SEND_SLAVE_INFO_61184_250_49", "()[B", (void*)*Get_SlaveInformation_SerialNumber_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_HWVersion_RX_SEND_SLAVE_INFO_61184_250_49", "()[B", (void*)*Get_SlaveInformation_HWVersion_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_ProductDate_RX_SEND_SLAVE_INFO_61184_250_49", "()[B", (void*)*Get_SlaveInformation_ProductDate_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_DeliveryDate_RX_SEND_SLAVE_INFO_61184_250_49", "()[B", (void*)*Get_SlaveInformation_DeliveryDate_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_NumberofAPP_RX_SEND_SLAVE_INFO_61184_250_49", "()I", (void*)*Get_SlaveInformation_NumberofAPP_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_App1StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49", "()I", (void*)*Get_SlaveInformation_App1StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_App1Size_RX_SEND_SLAVE_INFO_61184_250_49", "()I", (void*)*Get_SlaveInformation_App1Size_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_App2StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49", "()I", (void*)*Get_SlaveInformation_App2StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_App2Size_RX_SEND_SLAVE_INFO_61184_250_49", "()I", (void*)*Get_SlaveInformation_App2Size_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_App3StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49", "()I", (void*)*Get_SlaveInformation_App3StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_App3Size_RX_SEND_SLAVE_INFO_61184_250_49", "()I", (void*)*Get_SlaveInformation_App3Size_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_App4StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49", "()I", (void*)*Get_SlaveInformation_App4StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_App4Size_RX_SEND_SLAVE_INFO_61184_250_49", "()I", (void*)*Get_SlaveInformation_App4Size_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_App5StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49", "()I", (void*)*Get_SlaveInformation_App5StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_App5Size_RX_SEND_SLAVE_INFO_61184_250_49", "()I", (void*)*Get_SlaveInformation_App5Size_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_App6StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49", "()I", (void*)*Get_SlaveInformation_App6StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_App6Size_RX_SEND_SLAVE_INFO_61184_250_49", "()I", (void*)*Get_SlaveInformation_App6Size_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_App7StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49", "()I", (void*)*Get_SlaveInformation_App7StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_App7Size_RX_SEND_SLAVE_INFO_61184_250_49", "()I", (void*)*Get_SlaveInformation_App7Size_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_App8StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49", "()I", (void*)*Get_SlaveInformation_App8StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_App8Size_RX_SEND_SLAVE_INFO_61184_250_49", "()I", (void*)*Get_SlaveInformation_App8Size_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_App9StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49", "()I", (void*)*Get_SlaveInformation_App9StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_App9Size_RX_SEND_SLAVE_INFO_61184_250_49", "()I", (void*)*Get_SlaveInformation_App9Size_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_App10StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49", "()I", (void*)*Get_SlaveInformation_App10StartAddressOffset_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_SlaveInformation_App10Size_RX_SEND_SLAVE_INFO_61184_250_49", "()I", (void*)*Get_SlaveInformation_App10Size_RX_SEND_SLAVE_INFO_61184_250_49},
		{"Get_FirmwareInformation_SlaveID_RX_SEND_FW_N_INFO_61184_250_48", "()I", (void*)*Get_FirmwareInformation_SlaveID_RX_SEND_FW_N_INFO_61184_250_48},
		{"Get_FirmwareInformation_FWID_RX_SEND_FW_N_INFO_61184_250_48", "()I", (void*)*Get_FirmwareInformation_FWID_RX_SEND_FW_N_INFO_61184_250_48},
		{"Get_FirmwareInformation_FWName_RX_SEND_FW_N_INFO_61184_250_48", "()[B", (void*)*Get_FirmwareInformation_FWName_RX_SEND_FW_N_INFO_61184_250_48},
		{"Get_FirmwareInformation_FWModel_RX_SEND_FW_N_INFO_61184_250_48", "()[B", (void*)*Get_FirmwareInformation_FWModel_RX_SEND_FW_N_INFO_61184_250_48},
		{"Get_FirmwareInformation_FWVersion_RX_SEND_FW_N_INFO_61184_250_48", "()[B", (void*)*Get_FirmwareInformation_FWVersion_RX_SEND_FW_N_INFO_61184_250_48},
		{"Get_FirmwareInformation_ProtoType_RX_SEND_FW_N_INFO_61184_250_48", "()[B", (void*)*Get_FirmwareInformation_ProtoType_RX_SEND_FW_N_INFO_61184_250_48},
		{"Get_FirmwareInformation_FWChangeDay_RX_SEND_FW_N_INFO_61184_250_48", "()[B", (void*)*Get_FirmwareInformation_FWChangeDay_RX_SEND_FW_N_INFO_61184_250_48},
		{"Get_FirmwareInformation_FWChangeTime_RX_SEND_FW_N_INFO_61184_250_48", "()[B", (void*)*Get_FirmwareInformation_FWChangeTime_RX_SEND_FW_N_INFO_61184_250_48},
		{"Get_FirmwareInformation_FWFileSize_RX_SEND_FW_N_INFO_61184_250_48", "()I", (void*)*Get_FirmwareInformation_FWFileSize_RX_SEND_FW_N_INFO_61184_250_48},
		{"Get_FirmwareInformation_SectionUnitSize_RX_SEND_FW_N_INFO_61184_250_48", "()I", (void*)*Get_FirmwareInformation_SectionUnitSize_RX_SEND_FW_N_INFO_61184_250_48},
		{"Get_FirmwareInformation_PacketUnitSize_RX_SEND_FW_N_INFO_61184_250_48", "()I", (void*)*Get_FirmwareInformation_PacketUnitSize_RX_SEND_FW_N_INFO_61184_250_48},
		{"Get_FirmwareInformation_NumberofSectioninaFile_RX_SEND_FW_N_INFO_61184_250_48", "()I", (void*)*Get_FirmwareInformation_NumberofSectioninaFile_RX_SEND_FW_N_INFO_61184_250_48},
		{"Get_FirmwareInformation_NumberofPacketinaSection_RX_SEND_FW_N_INFO_61184_250_48", "()I", (void*)*Get_FirmwareInformation_NumberofPacketinaSection_RX_SEND_FW_N_INFO_61184_250_48},
		{"Get_FirmwareInformation_AppStartAddress_RX_SEND_FW_N_INFO_61184_250_48", "()I", (void*)*Get_FirmwareInformation_AppStartAddress_RX_SEND_FW_N_INFO_61184_250_48},
		{"Get_FWID_RX_REQUEST_PACKET_M_61184_250_83", "()I", (void*)*Get_FWID_RX_REQUEST_PACKET_M_61184_250_83},
		{"Get_SectionNumber_RX_REQUEST_PACKET_M_61184_250_83", "()I", (void*)*Get_SectionNumber_RX_REQUEST_PACKET_M_61184_250_83},
		{"Get_PacketNumber_RX_REQUEST_PACKET_M_61184_250_83", "()I", (void*)*Get_PacketNumber_RX_REQUEST_PACKET_M_61184_250_83},
		{"Get_ResultFlashCRC_RX_FW_N_DL_COMPLETE_61184_250_80", "()I", (void*)*Get_ResultFlashCRC_RX_FW_N_DL_COMPLETE_61184_250_80},
		{"Get_Status_RX_FW_UPDATE_STATUS_61184_250_113", "()I", (void*)*Get_Status_RX_FW_UPDATE_STATUS_61184_250_113},
		{"Get_Progress_ResultFlashCRC_RX_FW_UPDATE_STATUS_61184_250_113", "()I", (void*)*Get_Progress_ResultFlashCRC_RX_FW_UPDATE_STATUS_61184_250_113},
		{"Get_ResultFlashCRC_RX_FW_UPDATE_COMPLETE_61184_250_112", "()I", (void*)*Get_ResultFlashCRC_RX_FW_UPDATE_COMPLETE_61184_250_112},
		{"Get_FirmwareInformation_SlaveID_RX_FW_UPDATE_COMPLETE_61184_250_112", "()I", (void*)*Get_FirmwareInformation_SlaveID_RX_FW_UPDATE_COMPLETE_61184_250_112},
		{"Get_FirmwareInformation_FWID_RX_FW_UPDATE_COMPLETE_61184_250_112", "()I", (void*)*Get_FirmwareInformation_FWID_RX_FW_UPDATE_COMPLETE_61184_250_112},
		{"Get_FirmwareInformation_FWName_RX_FW_UPDATE_COMPLETE_61184_250_112", "()[B", (void*)*Get_FirmwareInformation_FWName_RX_FW_UPDATE_COMPLETE_61184_250_112},
		{"Get_FirmwareInformation_FWModel_RX_FW_UPDATE_COMPLETE_61184_250_112", "()[B", (void*)*Get_FirmwareInformation_FWModel_RX_FW_UPDATE_COMPLETE_61184_250_112},
		{"Get_FirmwareInformation_FWVersion_RX_FW_UPDATE_COMPLETE_61184_250_112", "()[B", (void*)*Get_FirmwareInformation_FWVersion_RX_FW_UPDATE_COMPLETE_61184_250_112},
		{"Get_FirmwareInformation_ProtoType_RX_FW_UPDATE_COMPLETE_61184_250_112", "()[B", (void*)*Get_FirmwareInformation_ProtoType_RX_FW_UPDATE_COMPLETE_61184_250_112},
		{"Get_FirmwareInformation_FWChangeDay_RX_FW_UPDATE_COMPLETE_61184_250_112", "()[B", (void*)*Get_FirmwareInformation_FWChangeDay_RX_FW_UPDATE_COMPLETE_61184_250_112},
		{"Get_FirmwareInformation_FWChangeTime_RX_FW_UPDATE_COMPLETE_61184_250_112", "()[B", (void*)*Get_FirmwareInformation_FWChangeTime_RX_FW_UPDATE_COMPLETE_61184_250_112},
		{"Get_FirmwareInformation_FWFileSize_RX_FW_UPDATE_COMPLETE_61184_250_112", "()I", (void*)*Get_FirmwareInformation_FWFileSize_RX_FW_UPDATE_COMPLETE_61184_250_112},
		{"Get_FirmwareInformation_SectionUnitSize_RX_FW_UPDATE_COMPLETE_61184_250_112", "()I", (void*)*Get_FirmwareInformation_SectionUnitSize_RX_FW_UPDATE_COMPLETE_61184_250_112},
		{"Get_FirmwareInformation_PacketUnitSize_RX_FW_UPDATE_COMPLETE_61184_250_112", "()I", (void*)*Get_FirmwareInformation_PacketUnitSize_RX_FW_UPDATE_COMPLETE_61184_250_112},
		{"Get_FirmwareInformation_NumberofSectioninaFile_RX_FW_UPDATE_COMPLETE_61184_250_112", "()I", (void*)*Get_FirmwareInformation_NumberofSectioninaFile_RX_FW_UPDATE_COMPLETE_61184_250_112},
		{"Get_FirmwareInformation_NumberofPacketinaSection_RX_FW_UPDATE_COMPLETE_61184_250_112", "()I", (void*)*Get_FirmwareInformation_NumberofPacketinaSection_RX_FW_UPDATE_COMPLETE_61184_250_112},
		{"Get_FirmwareInformation_AppStartAddress_RX_FW_UPDATE_COMPLETE_61184_250_112", "()I", (void*)*Get_FirmwareInformation_AppStartAddress_RX_FW_UPDATE_COMPLETE_61184_250_112},
		{"Get_Status_RX_UPD_UPDATE_STATUS_61184_250_84", "()I", (void*)*Get_Status_RX_UPD_UPDATE_STATUS_61184_250_84},
		{"Get_Progress_RX_UPD_UPDATE_STATUS_61184_250_84", "()I", (void*)*Get_Progress_RX_UPD_UPDATE_STATUS_61184_250_84},
		{"Get_nRecvUPDFormatCompleteFlag_61184_250_85", "()I", (void*)*Get_nRecvUPDFormatCompleteFlag_61184_250_85},
		//BKCU
		{"Get_SentPacketNumber_RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52", "()I", (void*)*Get_SentPacketNumber_RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52},
		{"Get_NextStartPacketNumber_RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52", "()I", (void*)*Get_NextStartPacketNumber_RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52},
		{"Set_SentPacketNumber_RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52", "(I)V", (void*)Set_SentPacketNumber_RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52},
		{"Set_NextStartPacketNumber_RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52", "(I)V", (void*)Set_NextStartPacketNumber_RX_BKCU_CTS_DOWNLOAD_MODE_65280_250_52},

		{"Get_ControlByte_RX_BKCU_DOWNLOAD_COMPLETE_65280_250_52", "()I",(void*) Get_ControlByte_RX_BKCU_DOWNLOAD_COMPLETE_65280_250_52 },
		{"Get_TotalReceiveByte_RX_BKCU_DOWNLOAD_COMPLETE_65280_250_52", "()I", (void*)*Get_TotalReceiveByte_RX_BKCU_DOWNLOAD_COMPLETE_65280_250_52},
		{"Get_TotalReceivePacket_RX_BKCU_DOWNLOAD_COMPLETE_65280_250_52", "()I", (void*)*Get_TotalReceivePacket_RX_BKCU_DOWNLOAD_COMPLETE_65280_250_52},



		{"Set_TotalSendByte_TX_BKCU_RTS_DOWNLOAD_MODE_65280_250_52", "(I)V", (void*)Set_TotalSendByte_TX_BKCU_RTS_DOWNLOAD_MODE_65280_250_52},
		{"Set_TotalSendPacket_TX_BKCU_RTS_DOWNLOAD_MODE_65280_250_52", "(I)V", (void*)Set_TotalSendPacket_TX_BKCU_RTS_DOWNLOAD_MODE_65280_250_52},

		{"Set_SequenceNumber_TX_BKCU_SEND_PACKET_65024_250_52", "(I)V", (void*)Set_SequenceNumber_TX_BKCU_SEND_PACKET_65024_250_52},
		{"Set_Data_TX_BKCU_SEND_PACKET_65024_250_52", "([B)V", (void*)Set_Data_TX_BKCU_SEND_PACKET_65024_250_52},
		//BKCU
		{ "Get_CheckBKCUComm", "()I",(void*) Get_CheckBKCUComm },
		{ "Get_CheckRMCUComm", "()I",(void*) Get_CheckRMCUComm },
};


jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
	__android_log_print(ANDROID_LOG_INFO, "NATIVE", "JNI_OnLoad.\n");
    jint result = -1;
    JNIEnv* env = NULL;
    jclass cls;

    if ((*vm)->GetEnv(vm, (void**)&env, JNI_VERSION_1_4) != JNI_OK) {
        __android_log_print(ANDROID_LOG_INFO, "NATIVE", "GetEnv failed.\n");
        goto error;
    }
    cls = (*env)->FindClass(env, "taeha/wheelloader/update/CommService");
    if (cls == NULL) {
        __android_log_print(ANDROID_LOG_INFO, "NATIVE", "Native registration unable to find class(AVMJni)");
        goto error;
    }

    if ((*env)->RegisterNatives(env, cls, methods, sizeof(methods)/sizeof(methods[0])) < 0) {
        __android_log_print(ANDROID_LOG_INFO, "NATIVE", "RegisterNatives failed !!!\n");
        goto error;
    }
    __android_log_print(ANDROID_LOG_INFO, "NATIVE", "JNI_OnLoad !!!\n");
    result = JNI_VERSION_1_4;
    glpVM = vm;
    jObject = (jclass)(*env)->NewGlobalRef(env, cls);


error:
    return result;
}


/////////////////////////////////////////////////////////////////////////////////////////////////////////////

