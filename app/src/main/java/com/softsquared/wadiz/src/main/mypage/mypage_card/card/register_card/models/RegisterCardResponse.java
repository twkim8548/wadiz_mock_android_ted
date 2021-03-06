package com.softsquared.wadiz.src.main.mypage.mypage_card.card.register_card.models;

import com.google.gson.annotations.SerializedName;

public class RegisterCardResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("isSuccess")
    private boolean isSuccess;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean getIsSuccess() {
        return isSuccess;
    }
}