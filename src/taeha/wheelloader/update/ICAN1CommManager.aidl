package taeha.wheelloader.update;

interface ICAN1CommManager
{
	void OpenComport();
	void CloseComport();

	int TxCANToMCU(int PS) ;
	int native_system_updates_Native();
	
	//int TxCMDToMCU(int CMD, int DAT);
	
	int TxUpdate(in byte[] Data, int size);
	int UART3_UpdatePacketComm(int Index, int CRC, int EOTFlag,in byte[] arr);
	////////////////////////////////////////////////////////////////////
	// CALLBACK METHOD
	///////////////////////////////////////////////////////////////////
	void Callback_KeyButton(int Data);
	void Callback_UpdateResponse(int Data);
}