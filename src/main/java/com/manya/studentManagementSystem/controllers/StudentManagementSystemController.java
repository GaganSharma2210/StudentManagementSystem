package com.manya.studentManagementSystem.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manya.studentManagementSystem.model.StudentRequestModel;
import com.manya.studentManagementSystem.model.StudentResponseModel;
import com.manya.studentManagementSystem.service.StudentService;

@RestController
@RequestMapping("/students")
public class StudentManagementSystemController {

	@Autowired
	private StudentService studentService;

	@Autowired
	private Environment envi;

	@GetMapping("/status/check")
	public String getStatus() {
		return "Working on port number " + envi.getProperty("local.server.port");
	}
	
	@GetMapping("")
	public ResponseEntity<List<StudentResponseModel>> getAllStudents() {
		return ResponseEntity.status(HttpStatus.OK).body(studentService.getAllStudents());
	}

	@GetMapping(value = "/{studentId}")
	public ResponseEntity<StudentResponseModel> getAllStudents(@PathVariable Long studentId) {
		StudentResponseModel student = studentService.getStudentbyStudentId(studentId);
		return ResponseEntity.status(HttpStatus.OK).body(student);
	}

	@PostMapping(value = "")
	public ResponseEntity<StudentResponseModel> createStudent(@RequestBody StudentRequestModel studentRequestModel) {

		StudentResponseModel createdStudent = studentService.saveStudent(studentRequestModel);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);

	}
	
	@PutMapping(value = "/{studentId}")
	public ResponseEntity<StudentResponseModel> updateStudent(@PathVariable Long studentId , @RequestBody StudentRequestModel studentRequestModel) {

		StudentResponseModel updatedStudent = studentService.updateStudent(studentRequestModel, studentId);
		return ResponseEntity.status(HttpStatus.CREATED).body(updatedStudent);

	}
	
	@DeleteMapping(value = "/{studentId}")
	public ResponseEntity<String> deleteStudent(@PathVariable Long studentId) {
		studentService.deleteStudent(studentId);
		return ResponseEntity.status(HttpStatus.OK).body("Record Deleted Successfully...");
	}
	

}
