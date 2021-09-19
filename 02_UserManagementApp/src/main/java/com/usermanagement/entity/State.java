package com.usermanagement.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "STATE_MASTER")
public class State {

	@Id
	@Column(name = "STATE_ID")
	private Integer stateId;
	@Column(name = "COUNTRY_ID")
	private Integer countryId;
	@Column(name = "STATE_NAME")
	private String stateName;
}
