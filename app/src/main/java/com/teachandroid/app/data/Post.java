package com.teachandroid.app.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Post implements Parcelable{

    @SerializedName("id")
    Long id;

    @SerializedName("owner_id")
    Long ownerId;

    @SerializedName("from_id")
    Long fromId;

    @SerializedName("date")
    Long date;

    @SerializedName("text")
    String text;

//    @SerializedName("reply_owner_id")
//    Integer replyOwnerId;
//
//    @SerializedName("reply_post_id")
//    Integer replyPostId;
//
//    @SerializedName("friends_only")
//    Integer friendsOnly;

    // comments
    // likes
    // reposts

    @SerializedName("attachments")
    ArrayList<Attachment> attachments;

    // geo
    // signer_id
    // copy_history
    // can_pin
    // is_pinned

    public Long getId() {
        return id;
    }

    public ArrayList<Attachment> getAttachments() {
        return attachments;
    }

    public String getFirstAttachmentType(){
        String result="";
        if (attachments!=null && attachments.size()>0){
            Attachment firstAttachment = attachments.get(0);
            if (firstAttachment!=null) {
                result = firstAttachment.getType();
            }
        }
        return result;
    }
    public Attachment getFirstAttachment(){
        Attachment result = new Attachment();
        if (attachments!=null && attachments.size()>0){
            result = attachments.get(0);
        }
        return result;
    }

    public String getText() {
        return text;
    }

    public Long getDate() {
        return date;
    }

    public Long getFromId() {
        return fromId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(ownerId);
        dest.writeLong(fromId);
        dest.writeLong(date);
        dest.writeString(text);
        dest.writeList(attachments);
    }
    public Post (Parcel parcel){
        this.id = parcel.readLong();
        this.ownerId = parcel.readLong();
        this.fromId = parcel.readLong();
        this.date = parcel.readLong();
        this.text = parcel.readString();
        this.attachments = new ArrayList<Attachment>();
        parcel.readList(this.attachments,getClass().getClassLoader());
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {

        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
