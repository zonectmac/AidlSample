package com.example.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.aidl.CallBack;
import com.example.aidl.IpcService;
import com.example.aidl.Request;
import com.example.aidl.Response;

import java.util.List;

public class ClientConnectHelper {
    private static final String TAG = "ClientConnectHelper";
    private static ClientConnectHelper mInstance;
    private IpcService ipcService;
    private boolean isBind = false;
    private Context mContext;
    /**
     * 服务的action
     */
    private static final String BIND_SERVICE_ACTION = "android.intent.action.AIDL_SAMPLE_SERVICE";

    private ClientConnectHelper() {

    }

    public synchronized static ClientConnectHelper getmInstance() {
        if (mInstance == null) {
            mInstance = new ClientConnectHelper();

        }
        return mInstance;
    }

    public void init(Context context) {
        mContext = context;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ipcService = IpcService.Stub.asInterface(service);
            Log.d(TAG, "onServiceConnected");
            if (ipcService == null) {
                Log.i(TAG, "mStudentService == null");
                return;
            }
            try {
                //设置死亡代理
                ipcService.asBinder().linkToDeath(mDeathRecipient, 0);

                if (mCallback != null) {
                    Log.i(TAG, "mCallback != null");
                    ipcService.register(mCallback);
                } else {
                    Log.i(TAG, "mCallback == null");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "client onServiceDisconnected");
            ipcService = null;
            // 重新绑定服务
            bindService();
        }

    };

    CallBack mCallback = new CallBack.Stub() {

        @Override
        public void onSuccess(String result) throws RemoteException {
            Log.i(TAG, "onSuccess:" + result);
        }

        @Override
        public void onFailed(String errorMsg) throws RemoteException {
            Log.i(TAG, "onFailed:" + errorMsg);
        }
    };

    /**
     * 服务端和客户端断开连接, binder死亡,重新绑定
     */
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {

        @Override
        public void binderDied() {
            if (ipcService == null) {
                return;
            }
            //解除死亡代理
            ipcService.asBinder().unlinkToDeath(mDeathRecipient, 0);
            ipcService = null;
            //重新绑定服务
            bindService();
            Log.i(TAG, "binderDied, bindService again");
        }
    };

    /**
     * 绑定服务
     */
    public void bindService() {
        Intent serviceIntent = new Intent();
        serviceIntent.setAction(BIND_SERVICE_ACTION);
        final Intent mIntent = new Intent(achieveExplicitFromImplicitIntent(mContext, serviceIntent));
        isBind = mContext.bindService(mIntent, mConnection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "bindService");
    }

    public Intent achieveExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        Log.d(TAG, "packageName = " + packageName);
        Log.d(TAG, "className = " + className);
        ComponentName component = new ComponentName(packageName, className);

        Intent explicitIntent = new Intent(implicitIntent);

        explicitIntent.setComponent(component);
        return explicitIntent;
    }

    /**
     * 解绑服务
     */
    public void unBindSevice() {
        if (isBind) {
            mContext.unbindService(mConnection);
            isBind = false;
            ipcService = null;
            Log.d(TAG, "unbindService");
        }
    }

    /**
     * 发送请求
     *
     * @param request 请求类
     * @return response
     */
    public Response sendRequest(Request request) {
        Response response = null;
        if (ipcService != null) {
            try {
                response = ipcService.send(request);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return response;
    }
}
