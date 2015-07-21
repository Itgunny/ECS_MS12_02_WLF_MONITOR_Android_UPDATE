package taeha.wheelloader.update;

oneway interface ICAN1CommManagerCallback
{
	void CallbackFunc(int Data);
	void KeyButtonCallBack(int Data);
	void UpdateResponseCallBack(int Data);
}