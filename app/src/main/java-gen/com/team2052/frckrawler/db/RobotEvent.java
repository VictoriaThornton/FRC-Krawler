package com.team2052.frckrawler.db;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "ROBOT_EVENT".
 */
@Entity(active = true)
public class RobotEvent implements java.io.Serializable {

    @Id(autoincrement = true)
    @Unique
    private Long id;
    private long robot_id;
    private long event_id;
    private String data;

    /**
     * Used to resolve relations
     */
    @Generated
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated
    private transient RobotEventDao myDao;

    @ToOne(joinProperty = "robot_id")
    private Robot robot;

    @Generated
    private transient Long robot__resolvedKey;

    @ToOne(joinProperty = "event_id")
    private Event event;

    @Generated
    private transient Long event__resolvedKey;

    @Generated
    public RobotEvent() {
    }

    public RobotEvent(Long id) {
        this.id = id;
    }

    @Generated
    public RobotEvent(Long id, long robot_id, long event_id, String data) {
        this.id = id;
        this.robot_id = robot_id;
        this.event_id = event_id;
        this.data = data;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRobotEventDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getRobot_id() {
        return robot_id;
    }

    public void setRobot_id(long robot_id) {
        this.robot_id = robot_id;
    }

    public long getEvent_id() {
        return event_id;
    }

    public void setEvent_id(long event_id) {
        this.event_id = event_id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    /** To-one relationship, resolved on first access. */
    @Generated
    public Robot getRobot() {
        long __key = this.robot_id;
        if (robot__resolvedKey == null || !robot__resolvedKey.equals(__key)) {
            __throwIfDetached();
            RobotDao targetDao = daoSession.getRobotDao();
            Robot robotNew = targetDao.load(__key);
            synchronized (this) {
                robot = robotNew;
                robot__resolvedKey = __key;
            }
        }
        return robot;
    }

    @Generated
    public void setRobot(Robot robot) {
        if (robot == null) {
            throw new DaoException("To-one property 'robot_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.robot = robot;
            robot_id = robot.getId();
            robot__resolvedKey = robot_id;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated
    public Event getEvent() {
        long __key = this.event_id;
        if (event__resolvedKey == null || !event__resolvedKey.equals(__key)) {
            __throwIfDetached();
            EventDao targetDao = daoSession.getEventDao();
            Event eventNew = targetDao.load(__key);
            synchronized (this) {
                event = eventNew;
                event__resolvedKey = __key;
            }
        }
        return event;
    }

    @Generated
    public void setEvent(Event event) {
        if (event == null) {
            throw new DaoException("To-one property 'event_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.event = event;
            event_id = event.getId();
            event__resolvedKey = event_id;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
    */
    @Generated
    public void delete() {
        __throwIfDetached();
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
    */
    @Generated
    public void update() {
        __throwIfDetached();
        myDao.update(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
    */
    @Generated
    public void refresh() {
        __throwIfDetached();
        myDao.refresh(this);
    }

    @Generated
    private void __throwIfDetached() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
    }

}
