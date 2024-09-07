package com.manya.studentManagementSystem.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.manya.studentManagementSystem.model.StudentRequestModel;
import com.manya.studentManagementSystem.model.StudentResponseModel;

public interface StudentService extends UserDetailsService{

	public StudentResponseModel saveStudent(StudentRequestModel studentRequestModel);

	public List<StudentResponseModel> getAllStudents();

	public StudentResponseModel updateStudent(StudentRequestModel studentRequestModel, Long id);

	public void deleteStudent(Long id);
	
	public StudentResponseModel getStudentbyStudentId(Long id);

}
