package com.capgemini.listener;

import com.capgemini.entity.AbstractEntity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.sql.Timestamp;
import java.util.Date;

public class UpdateListener {

	@PrePersist
	@PreUpdate
	public void setUpdatedAt(final AbstractEntity entity) {
		Date date = new Date();
		entity.setUpdated(new Timestamp(date.getTime()));
	}

}
