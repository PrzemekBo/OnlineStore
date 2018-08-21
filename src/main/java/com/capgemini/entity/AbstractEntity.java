package com.capgemini.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@MappedSuperclass
public abstract class   AbstractEntity {

    @Version
    @Column(columnDefinition = "bigint DEFAULT 0")
    private long version;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false, updatable = false)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated", nullable = false)
    private Date updated;

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
