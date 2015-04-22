package com.teachandroid.app.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nick on 11.03.2015.
 */
public class Comment {

@SerializedName("id")
    private int id;
 //	идентификатор комментария.

    @SerializedName("from_id")
    private int from_id;  //идентификатор автора комментария.

    @SerializedName("text")
   private String text;  //текст комментария.


    @SerializedName("reply_to_user") //идентификатор пользователя или сообщества,
    private int reply_to_user; // в ответ которому оставлен текущий комментарий (если применимо).


    @SerializedName("reply_to_comment") //идентификатор комментария, в ответ на который оставлен текущий (если применимо).
    private int reply_to_comment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrom_id() {
        return from_id;
    }

    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getReply_to_user() {
        return reply_to_user;
    }

    public void setReply_to_user(int reply_to_user) {
        this.reply_to_user = reply_to_user;
    }

    public int getReply_to_comment() {
        return reply_to_comment;
    }

    public void setReply_to_comment(int reply_to_comment) {
        this.reply_to_comment = reply_to_comment;
    }
}
