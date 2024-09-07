package com.manya.studentManagementSystem.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.manya.studentManagementSystem.data.StudentEntity;

@Repository
public interface StudentRepository extends CrudRepository<StudentEntity, Long> {
	
	public StudentEntity findByContactNumber(Long contactNumber);
	
}
