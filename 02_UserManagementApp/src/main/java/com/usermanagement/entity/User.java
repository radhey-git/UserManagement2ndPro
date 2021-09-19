package com.usermanagement.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Data
@Table(name = "USER_ACCOUNTS")
public class User {

	@Id
	@Column(name = "USER_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer userId;
	
	@Column(name = "ACC_STATUS")
	private String accStatus;
	
	@Column(name = "CITY_ID")
	private Integer cityId;
	
	@Column(name = "COUNTRY_ID")
	private Integer countryId;
	
	@CreationTimestamp()
	@Column(name = "CREATED_DATE",updatable = false)
	private LocalDate createdDate;
	
	@Column(name = "DOB")
	private LocalDate dob;
	
	@Column(name = "FIRST_NAME")
	private String firstName;
	
	@Column(name = "GENDER")
	private String gender;
	
	@Column(name = "STATE_ID")
	private Integer stateId;
	
	@UpdateTimestamp()
	@Column(name = "UPDATED_DATE",insertable = false)
	private LocalDate updatedDate;
	
	@Column(name = "USER_EMAIL")
	private String userEmail;
	
	@Column(name = "LAST_NAME")
	private String lastName;
	
	@Column(name = "USER_PWD")
	private String userPwd;
	
	@Column(name = "USER_MOBILE")
	private String userMobile;
}
