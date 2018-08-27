package com.capgemini.listener;


import com.capgemini.entity.AbstractEntity;

import javax.persistence.PrePersist;
import java.sql.Timestamp;
import java.util.Date;

public class CreateListener {

	@PrePersist
	public void setCreatedAt(final AbstractEntity entity) {
		Date date = new Date();
		entity.setCreated(new Timestamp(date.getTime()));
	}

}
