/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: G:\\Project\\Wheel Loader F Monitor\\SRC\\Wheel_Loader_F_Series_Update\\src\\taeha\\wheelloader\\update\\ICAN1CommManager.aidl
 */
package taeha.wheelloader.update;
public interface ICAN1CommManager extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements taeha.wheelloader.update.ICAN1CommManager
{
private static final java.lang.String DESCRIPTOR = "taeha.wheelloader.update.ICAN1CommManager";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an taeha.wheelloader.update.ICAN1CommManager interface,
 * generating a proxy if needed.
 */
public static taeha.wheelloader.update.ICAN1CommManager asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof taeha.wheelloader.update.ICAN1CommManager))) {
return ((taeha.wheelloader.update.ICAN1CommManager)iin);
}
return new taeha.wheelloader.update.ICAN1CommManager.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_OpenComport:
{
data.enforceInterface(DESCRIPTOR);
this.OpenComport();
reply.writeNoException();
return true;
}
case TRANSACTION_CloseComport:
{
data.enforceInterface(DESCRIPTOR);
this.CloseComport();
reply.writeNoException();
return true;
}
case TRANSACTION_TxCANToMCU:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.TxCANToMCU(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_TxUpdate:
{
data.enforceInterface(DESCRIPTOR);
byte[] _arg0;
_arg0 = data.createByteArray();
int _arg1;
_arg1 = data.readInt();
int _result = this.TxUpdate(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_UART3_UpdatePacketComm:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
byte[] _arg3;
_arg3 = data.createByteArray();
int _result = this.UART3_UpdatePacketComm(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_Callback_KeyButton:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.Callback_KeyButton(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_Callback_UpdateResponse:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.Callback_UpdateResponse(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements taeha.wheelloader.update.ICAN1CommManager
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void OpenComport() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_OpenComport, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void CloseComport() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_CloseComport, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int TxCANToMCU(int PS) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(PS);
mRemote.transact(Stub.TRANSACTION_TxCANToMCU, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//int TxCMDToMCU(int CMD, int DAT);

@Override public int TxUpdate(byte[] Data, int size) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeByteArray(Data);
_data.writeInt(size);
mRemote.transact(Stub.TRANSACTION_TxUpdate, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int UART3_UpdatePacketComm(int Index, int CRC, int EOTFlag, byte[] arr) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(Index);
_data.writeInt(CRC);
_data.writeInt(EOTFlag);
_data.writeByteArray(arr);
mRemote.transact(Stub.TRANSACTION_UART3_UpdatePacketComm, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
////////////////////////////////////////////////////////////////////
// CALLBACK METHOD
///////////////////////////////////////////////////////////////////

@Override public void Callback_KeyButton(int Data) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(Data);
mRemote.transact(Stub.TRANSACTION_Callback_KeyButton, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void Callback_UpdateResponse(int Data) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(Data);
mRemote.transact(Stub.TRANSACTION_Callback_UpdateResponse, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_OpenComport = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_CloseComport = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_TxCANToMCU = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_TxUpdate = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_UART3_UpdatePacketComm = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_Callback_KeyButton = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_Callback_UpdateResponse = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
}
public void OpenComport() throws android.os.RemoteException;
public void CloseComport() throws android.os.RemoteException;
public int TxCANToMCU(int PS) throws android.os.RemoteException;
//int TxCMDToMCU(int CMD, int DAT);

public int TxUpdate(byte[] Data, int size) throws android.os.RemoteException;
public int UART3_UpdatePacketComm(int Index, int CRC, int EOTFlag, byte[] arr) throws android.os.RemoteException;
////////////////////////////////////////////////////////////////////
// CALLBACK METHOD
///////////////////////////////////////////////////////////////////

public void Callback_KeyButton(int Data) throws android.os.RemoteException;
public void Callback_UpdateResponse(int Data) throws android.os.RemoteException;
}
