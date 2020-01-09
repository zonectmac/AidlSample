package com.example.aidl;


import android.os.Parcel;
import android.os.Parcelable;

public class Response implements Parcelable {
    /**
     * 结果json串
     */
    private String result;
    /**
     * 是否成功
     */
    private boolean isSuccess;

    public Response() {
    }

    public Response(String result, boolean isSuccess) {
        this.result = result;
        this.isSuccess = isSuccess;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    protected Response(Parcel in) {
        result = in.readString();
        isSuccess = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(result);
        dest.writeByte((byte) (isSuccess ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Response> CREATOR = new Creator<Response>() {
        @Override
        public Response createFromParcel(Parcel in) {
            return new Response(in);
        }

        @Override
        public Response[] newArray(int size) {
            return new Response[size];
        }
    };

    @Override
    public String toString() {
        return "Response{" +
                "result='" + result + '\'' +
                ", isSuccess=" + isSuccess +
                '}';
    }
}
