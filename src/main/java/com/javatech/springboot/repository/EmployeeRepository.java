package com.javatech.springboot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.javatech.springboot.model.Employee;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, Long>{

}
