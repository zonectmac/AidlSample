package com.example.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class Request implements Parcelable {
    /**
     * 纯文本消息
     */
    public static final String MSG_TYPE_0 = "0";

    /**
     * 跳转到网页的消息
     */
    public static final String MSG_TYPE_1 = "1";

    /**
     * 跳转到原生界面的消息
     */
    public static final String MSG_TYPE_2 = "2";

    /**
     * 升级提示消息
     */
    public static final String MSG_TYPE_3 = "3";

    /**
     * 下载安装补丁包消息
     */
    public static final String MSG_TYPE_4 = "4";
    /**
     * 设置新的tags
     */
    public static final String MSG_TYPE_5 = "5";
    /**
     * 日志上传
     */
    public static final String MSG_TYPE_6 = "6";
    /**
     * 推送升级
     */
    public static final String MSG_TYPE_7 = "7";

    /**
     * 自定义消息类型
     */
    public String msgType;

    /**
     * 消息内容
     */
    public String msgContent;


    /**
     * 是否重启
     */
    public boolean isReboot;

    protected Request(Parcel in) {
        msgType = in.readString();
        msgContent = in.readString();
        isReboot = in.readByte() != 0;
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(msgType);
        dest.writeString(msgContent);
        dest.writeByte((byte) (isReboot ? 1 : 0));
    }

}