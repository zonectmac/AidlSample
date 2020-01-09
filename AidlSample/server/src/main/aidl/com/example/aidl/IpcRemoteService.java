package com.example.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;


public class IpcRemoteService extends Service {
    private static final String TAG = "IpcRemoteService";

    private static final String PACKAGE_SAYHI = "com.example.client";

    private static RemoteCallbackList<CallBack> sCallbackList;

    @Override
    public void onCreate() {
        super.onCreate();
        sCallbackList = new RemoteCallbackList<>();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind Service success!");
        return new RemoteBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind Service success!");
        return super.onUnbind(intent);
    }


    class RemoteBinder extends IpcService.Stub {

        @Override
        public Response send(Request request) throws RemoteException {
            String msgContent = request.msgContent;
            Response response = new Response();
            //这里根据msgType处理不同的业务逻辑
            switch (request.msgType) {
                case Request.MSG_TYPE_0:
                    Log.d(TAG, msgContent);
                    response.setResult("return success");
                    //有返回的可以不用这个回调
                    dispatchResult(true, msgContent);
                    break;
                case Request.MSG_TYPE_1:

                    break;
                case Request.MSG_TYPE_2:

                    break;
                case Request.MSG_TYPE_3:

                    break;
                case Request.MSG_TYPE_4:

                    break;
                default:
                    break;
            }


            return response;
        }

        @Override
        public void register(CallBack callback) throws RemoteException {
            if (callback != null) {
                sCallbackList.register(callback);
            }
        }

        @Override
        public void unregister(CallBack callback) throws RemoteException {
            if (callback != null) {
                sCallbackList.unregister(callback);
            }
        }

        /**
         * 权限验证,包名必须是com.example.client,才能通过验证
         *
         * @param code
         * @param data
         * @param reply
         * @param flags
         * @return
         * @throws RemoteException
         */
        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String packageName = null;
            String[] packages = IpcRemoteService.this.getPackageManager().
                    getPackagesForUid(getCallingUid());
            if (packages != null && packages.length > 0) {
                packageName = packages[0];
            }
            Log.d(TAG, "onTransact: " + packageName);
            if (!PACKAGE_SAYHI.equals(packageName)) {
                return false;
            }

            return super.onTransact(code, data, reply, flags);
        }
    }

    /**
     * 分发结果
     *
     * @param result     成功还是失败
     * @param msgContent msg
     */
    private void dispatchResult(boolean result, String msgContent) {
        int length = sCallbackList.beginBroadcast();

        for (int i = 0; i < length; i++) {
            CallBack callback = sCallbackList.getBroadcastItem(i);
            try {
                if (result) {
                    callback.onSuccess(msgContent);
                } else {
                    callback.onFailed(msgContent);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        sCallbackList.finishBroadcast();

    }


}
