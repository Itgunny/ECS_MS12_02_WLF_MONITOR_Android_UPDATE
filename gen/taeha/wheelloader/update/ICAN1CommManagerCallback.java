/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\WL_F\\Wheel_Loader_F_Series_Update\\src\\taeha\\wheelloader\\update\\ICAN1CommManagerCallback.aidl
 */
package taeha.wheelloader.update;
public interface ICAN1CommManagerCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements taeha.wheelloader.update.ICAN1CommManagerCallback
{
private static final java.lang.String DESCRIPTOR = "taeha.wheelloader.update.ICAN1CommManagerCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an taeha.wheelloader.update.ICAN1CommManagerCallback interface,
 * generating a proxy if needed.
 */
public static taeha.wheelloader.update.ICAN1CommManagerCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof taeha.wheelloader.update.ICAN1CommManagerCallback))) {
return ((taeha.wheelloader.update.ICAN1CommManagerCallback)iin);
}
return new taeha.wheelloader.update.ICAN1CommManagerCallback.Stub.Proxy(obj);
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
case TRANSACTION_CallbackFunc:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.CallbackFunc(_arg0);
return true;
}
case TRANSACTION_KeyButtonCallBack:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.KeyButtonCallBack(_arg0);
return true;
}
case TRANSACTION_UpdateResponseCallBack:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.UpdateResponseCallBack(_arg0);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements taeha.wheelloader.update.ICAN1CommManagerCallback
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
@Override public void CallbackFunc(int Data) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(Data);
mRemote.transact(Stub.TRANSACTION_CallbackFunc, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void KeyButtonCallBack(int Data) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(Data);
mRemote.transact(Stub.TRANSACTION_KeyButtonCallBack, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void UpdateResponseCallBack(int Data) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(Data);
mRemote.transact(Stub.TRANSACTION_UpdateResponseCallBack, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_CallbackFunc = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_KeyButtonCallBack = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_UpdateResponseCallBack = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public void CallbackFunc(int Data) throws android.os.RemoteException;
public void KeyButtonCallBack(int Data) throws android.os.RemoteException;
public void UpdateResponseCallBack(int Data) throws android.os.RemoteException;
}
