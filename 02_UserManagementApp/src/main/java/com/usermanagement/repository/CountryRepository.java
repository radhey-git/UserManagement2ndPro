package com.usermanagement.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usermanagement.entity.Country;
@Repository
public interface CountryRepository extends JpaRepository<Country, Serializable>{

}
