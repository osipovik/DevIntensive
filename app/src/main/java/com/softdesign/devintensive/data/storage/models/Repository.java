package com.softdesign.devintensive.data.storage.models;

import com.softdesign.devintensive.data.network.response.UserModelResponse;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by OsIpoFF on 07.11.16.
 */

@Entity(active = true, nameInDb = "REPOSITORIES")
public class Repository {

    @Id
    private Long id;

    @NotNull
    @Unique
    private String remoteId;

    private String repositoryName;

    private String userRemoteId;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 332345895)
    private transient RepositoryDao myDao;

    @Generated(hash = 1976272162)
    public Repository(Long id, @NotNull String remoteId, String repositoryName,
            String userRemoteId) {
        this.id = id;
        this.remoteId = remoteId;
        this.repositoryName = repositoryName;
        this.userRemoteId = userRemoteId;
    }

    @Generated(hash = 984204935)
    public Repository() {
    }

    public Repository(UserModelResponse.Repo repositoryRes, String userId) {
        this.repositoryName = repositoryRes.getGit();
        this.userRemoteId = userId;
        this.remoteId = repositoryRes.getId();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRemoteId() {
        return this.remoteId;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public String getRepositoryName() {
        return this.repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getUserRemoteId() {
        return this.userRemoteId;
    }

    public void setUserRemoteId(String userRemoteId) {
        this.userRemoteId = userRemoteId;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 636002579)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRepositoryDao() : null;
    }
}
