// Request.aidl
package com.example.aidl;

// Declare any non-default types here with import statements
import com.example.aidl.Request;
import com.example.aidl.Response;
import com.example.aidl.CallBack;

interface IpcService{
  Response send(in Request request);
  void register(CallBack callback);
  void unregister(CallBack callback);
}
