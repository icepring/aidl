/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\AndroidS\\AIDL\\app\\src\\main\\aidl\\com\\tym\\aidl\\IBookManagerInterface.aidl
 */
package com.tym.aidl;
// Declare any non-default types here with import statements

public interface IBookManagerInterface extends android.os.IInterface{
	
	/** Local-side IPC implementation stub class. */
	public static abstract class Stub extends android.os.Binder implements com.tym.aidl.IBookManagerInterface{
	
		/**binder 的唯一标识*/
		private static final java.lang.String DESCRIPTOR = "com.tym.aidl.IBookManagerInterface";
		
		/** Construct the stub at attach it to the interface. */
		public Stub(){
			this.attachInterface(this, DESCRIPTOR);
		}
		
		/**
		 * Cast an IBinder object into an com.tym.aidl.IBookManagerInterface interface,
		 * generating a proxy if needed.
		 */
		public static com.tym.aidl.IBookManagerInterface asInterface(android.os.IBinder obj){
			if ((obj==null)) {
				return null;
			}
			android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
			if (((iin!=null)&&(iin instanceof com.tym.aidl.IBookManagerInterface))) {
				return ((com.tym.aidl.IBookManagerInterface)iin);
			}
			return new com.tym.aidl.IBookManagerInterface.Stub.Proxy(obj);
		}
		
		@Override 
		public android.os.IBinder asBinder(){
			return this;
		}
		
		/**
		* 该方法运行在服务端Binder线程池中
		*
		*/
		@Override 
		public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException{
			
			switch (code){
				case INTERFACE_TRANSACTION:{
					reply.writeString(DESCRIPTOR);
					return true;
				}
				case TRANSACTION_basicTypes:{
					data.enforceInterface(DESCRIPTOR);
					int _arg0;
					_arg0 = data.readInt();
					long _arg1;
					_arg1 = data.readLong();
					boolean _arg2;
					_arg2 = (0!=data.readInt());
					float _arg3;
					_arg3 = data.readFloat();
					double _arg4;
					_arg4 = data.readDouble();
					java.lang.String _arg5;
					_arg5 = data.readString();
					this.basicTypes(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
					reply.writeNoException();
					return true;
				}
				case TRANSACTION_getBooks:{
					data.enforceInterface(DESCRIPTOR);
					java.util.List<com.tym.aidl.Book> _result = this.getBooks();
					reply.writeNoException();
					reply.writeTypedList(_result);
					return true;
				}
				case TRANSACTION_addBook:{
					data.enforceInterface(DESCRIPTOR);
					com.tym.aidl.Book _arg0;
					if ((0!=data.readInt())) {
						_arg0 = com.tym.aidl.Book.CREATOR.createFromParcel(data);
					}
					else {
						_arg0 = null;
					}
					this.addBook(_arg0);
					reply.writeNoException();
					return true;
				}
			}
			return super.onTransact(code, data, reply, flags);
		}
		
		/**
		* 该方法运行在客户端
		*
		*/
		private static class Proxy implements com.tym.aidl.IBookManagerInterface{
			
			private android.os.IBinder mRemote;
			
			Proxy(android.os.IBinder remote){
				mRemote = remote;
			}
			
			@Override 
			public android.os.IBinder asBinder(){
				return mRemote;
			}
			
			public java.lang.String getInterfaceDescriptor(){
			return DESCRIPTOR;
			}
		
			/**
			 * Demonstrates some basic types that you can use as parameters
			 * and return values in AIDL.
			 */
			@Override 
			public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, java.lang.String aString) throws android.os.RemoteException{
				android.os.Parcel _data = android.os.Parcel.obtain();
				android.os.Parcel _reply = android.os.Parcel.obtain();
				try {
				_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeInt(anInt);
					_data.writeLong(aLong);
					_data.writeInt(((aBoolean)?(1):(0)));
					_data.writeFloat(aFloat);
					_data.writeDouble(aDouble);
					_data.writeString(aString);
					mRemote.transact(Stub.TRANSACTION_basicTypes, _data, _reply, 0);
					_reply.readException();
				}
				finally {
					_reply.recycle();
					_data.recycle();
				}
			}
			
			@Override 
			public java.util.List<com.tym.aidl.Book> getBooks() throws android.os.RemoteException{
				android.os.Parcel _data = android.os.Parcel.obtain();
				android.os.Parcel _reply = android.os.Parcel.obtain();
				java.util.List<com.tym.aidl.Book> _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(Stub.TRANSACTION_getBooks, _data, _reply, 0);
					_reply.readException();
					_result = _reply.createTypedArrayList(com.tym.aidl.Book.CREATOR);
				}
				finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}
			
			@Override 
			public void addBook(com.tym.aidl.Book book) throws android.os.RemoteException{
				android.os.Parcel _data = android.os.Parcel.obtain();
				android.os.Parcel _reply = android.os.Parcel.obtain();
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					if ((book!=null)) {
						_data.writeInt(1);
						book.writeToParcel(_data, 0);
					}
					else {
						_data.writeInt(0);
					}
					mRemote.transact(Stub.TRANSACTION_addBook, _data, _reply, 0);
					_reply.readException();
				}
				finally {
					_reply.recycle();
					_data.recycle();
				}
			}
	}

	static final int TRANSACTION_basicTypes = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
	static final int TRANSACTION_getBooks = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
	static final int TRANSACTION_addBook = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);

}

	/**
	 * Demonstrates some basic types that you can use as parameters
	 * and return values in AIDL.
	 */
	public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, java.lang.String aString) throws android.os.RemoteException;
	public java.util.List<com.tym.aidl.Book> getBooks() throws android.os.RemoteException;
	public void addBook(com.tym.aidl.Book book) throws android.os.RemoteException;
}
