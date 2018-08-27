package com.capgemini.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;


@Data
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", updatable = false)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated")
    private Date updated;

    @Version
    private int version;


    public AbstractEntity() {
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(final Timestamp created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(final Timestamp updated) {
        this.updated = updated;
    }

}
