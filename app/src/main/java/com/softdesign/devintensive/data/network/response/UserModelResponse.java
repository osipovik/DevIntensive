package com.softdesign.devintensive.data.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OsIpoFF on 05.11.16.
 */

public class UserModelResponse {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public class Contacts {

        @SerializedName("vk")
        @Expose
        private String vk;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("updated")
        @Expose
        private String updated;

    }

    public class Data {

        @SerializedName("user")
        @Expose
        private User user;
        @SerializedName("token")
        @Expose
        private String token;

        public String getToken() {
            return token;
        }

        public User getUser() {
            return user;
        }
    }

    public class ProfileValues {

        @SerializedName("homeTask")
        @Expose
        private int homeTask;
        @SerializedName("projects")
        @Expose
        private int projects;
        @SerializedName("linesCode")
        @Expose
        private int linesCode;
        @SerializedName("likesBy")
        @Expose
        private List<Object> likesBy = new ArrayList<Object>();
        @SerializedName("rait")
        @Expose
        private int rait;
        @SerializedName("updated")
        @Expose
        private String updated;
        @SerializedName("rating")
        @Expose
        private int rating;

        public int getProjects() {
            return projects;
        }

        public int getLinesCode() {
            return linesCode;
        }

        public int getRaiting() {
            return rait;
        }
    }

    public class PublicInfo {

        @SerializedName("bio")
        @Expose
        private String bio;
        @SerializedName("avatar")
        @Expose
        private String avatar;
        @SerializedName("photo")
        @Expose
        private String photo;
        @SerializedName("updated")
        @Expose
        private String updated;

    }

    public class Repo {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("git")
        @Expose
        private String git;
        @SerializedName("title")
        @Expose
        private String title;

    }

    public class Repositories {

        @SerializedName("repo")
        @Expose
        private List<Repo> repo = new ArrayList<Repo>();
        @SerializedName("updated")
        @Expose
        private String updated;

    }

    public class User {

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
        private Repositories repositories;
        @SerializedName("contacts")
        @Expose
        private Contacts contacts;
        @SerializedName("profileValues")
        @Expose
        private ProfileValues profileValues;
        @SerializedName("publicInfo")
        @Expose
        private PublicInfo publicInfo;
        @SerializedName("specialization")
        @Expose
        private String specialization;
        @SerializedName("role")
        @Expose
        private String role;
        @SerializedName("updated")
        @Expose
        private String updated;

        public String getId() {
            return id;
        }

        public ProfileValues getProfileValues() {
            return profileValues;
        }
    }
}
