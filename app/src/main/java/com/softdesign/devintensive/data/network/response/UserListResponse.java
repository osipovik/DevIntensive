package com.softdesign.devintensive.data.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OsIpoFF on 06.11.16.
 */

public class UserListResponse {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("data")
    @Expose
    private List<UserData> data = new ArrayList<UserData>();

    public List<UserData> getData() {
        return data;
    }

    public class UserData {
        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("second_name")
        @Expose
        private String secondName;
        @SerializedName("__v")
        @Expose
        private int v;
        @SerializedName("repositories")
        @Expose
        private UserModelResponse.Repositories repositories;
        @SerializedName("profileValues")
        @Expose
        private UserModelResponse.ProfileValues profileValues;
        @SerializedName("publicInfo")
        @Expose
        private UserModelResponse.PublicInfo publicInfo;
        @SerializedName("specialization")
        @Expose
        private String specialization;
        @SerializedName("updated")
        @Expose
        private String updated;

        public UserModelResponse.Repositories getRepositories() {
            return repositories;
        }

        public UserModelResponse.ProfileValues getProfileValues() {
            return profileValues;
        }

        public UserModelResponse.PublicInfo getPublicInfo() {
            return publicInfo;
        }

        public String getFullName() {
            return firstName + " " + secondName;
        }
    }
}
