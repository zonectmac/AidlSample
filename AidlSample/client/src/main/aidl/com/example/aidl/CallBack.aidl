// Request.aidl
package com.example.aidl;

// Declare any non-default types here with import statements

interface CallBack{
    void onSuccess(String result);

    void onFailed(String errorMsg);
}
