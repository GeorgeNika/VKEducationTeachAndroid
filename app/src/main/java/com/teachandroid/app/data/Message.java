package com.teachandroid.app.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Parcelable{

    @SerializedName("id")
    private long id ;

    @SerializedName("user_id")
    private long userId;

    @SerializedName("from_id")
    private long fromId;

    @SerializedName("date")
    private long date;

    @SerializedName("read_state")
    private int readState;

    @SerializedName("out")
    private int out;

    @SerializedName("title")
    private String title;

    @SerializedName("body")
    private String body;

//    geo;
    @SerializedName("attachments")
    ArrayList<Attachment> attachments;

//    @SerializedName("fwd_messages")
//    private List<Message> fwdMessage;

    @SerializedName("emoji")
    private int emoji;

    @SerializedName("important")
    private int important;

    @SerializedName("deleted")
    private int deleted;

    @SerializedName("chat_id")
    private long chatId;

    @SerializedName("chat_active")
    private ArrayList<Long> chatActive;

    @SerializedName("push_settings")
    private String pushSettings;

    @SerializedName("users_count")
    private int usersCount;

    @SerializedName("admin_id")
    private long adminId;

    @SerializedName("action_text")
    private String actionText;

    @SerializedName("photo_50")
    private String photo50;

    @SerializedName("photo_100")
    private String photo100;

    @SerializedName("photo_200")
    private String photo200;


    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getUserIdString() {
        return ""+userId;
    }

    public long getFromId() {
        return fromId;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getPhoto50() {
        return photo50;
    }

    public String getPhoto100() {
        return photo100;
    }

    public String getPhoto200() {
        return photo200;
    }

    public Long getChatId(){
        return chatId;
    }

    public ArrayList<Long> getChatActive() {
        return chatActive;
    }

    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {

        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(userId);
        dest.writeLong(fromId);
        dest.writeLong(date);
        dest.writeInt(readState);
        dest.writeInt(out);
        dest.writeString(title);
        dest.writeString(body);
        dest.writeList(attachments);
        dest.writeInt(emoji);
        dest.writeInt(important);
        dest.writeInt(deleted);
        dest.writeLong(chatId);
        dest.writeList(chatActive);
        dest.writeInt(usersCount);
        dest.writeLong(adminId);
        dest.writeString(actionText);
        dest.writeString(photo50);
        dest.writeString(photo100);
        dest.writeString(photo200);
    }

    public Message(Parcel parcel) {
        this.id = parcel.readLong();
        this.userId = parcel.readLong();
        this.fromId = parcel.readLong();
        this.date = parcel.readLong();
        this.readState = parcel.readInt();
        this.out = parcel.readInt();
        this.title = parcel.readString();
        this.body = parcel.readString();
        this.attachments = new ArrayList<Attachment>();
        parcel.readList(this.attachments,getClass().getClassLoader());
        this.emoji = parcel.readInt();
        this.important = parcel.readInt();
        this.deleted = parcel.readInt();
        this.chatId = parcel.readLong();
        this.chatActive = new ArrayList<Long>();
        parcel.readList(this.chatActive,getClass().getClassLoader());
        this.usersCount = parcel.readInt();
        this.adminId = parcel.readLong();
        this.actionText = parcel.readString();
        this.photo50 = parcel.readString();
        this.photo100 = parcel.readString();
        this.photo200 = parcel.readString();
    }
}
