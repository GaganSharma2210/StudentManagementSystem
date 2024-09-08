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

import com.manya.studentManagementSystem.model.LoginRequestModel;
import com.manya.studentManagementSystem.model.StudentRequestModel;
import com.manya.studentManagementSystem.model.StudentResponseModel;
import com.manya.studentManagementSystem.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/students")
@SecurityRequirement(name = "bearer")
public class StudentManagementSystemController {

	@Autowired
	private StudentService studentService;

	@Autowired
	private Environment envi;

	@Operation(tags = "Get Status", description = "Get Status", responses = {
			@ApiResponse(description = "Success", responseCode = "200")

	})
	@GetMapping("/status/check")
	public String getStatus() {
		return "Working on port number " + envi.getProperty("local.server.port");
	}
	
	@Operation(tags = "Student Login", description = "Login using correct credentials", responses = {
			@ApiResponse(description = "Success", responseCode = "200")
	})
	@PostMapping("/login")
	public void login(@RequestBody LoginRequestModel loginRequestModel) {
		
	}

	@Operation(tags = "Get All Students", description = "Get All Students", responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Please input correct token", responseCode = "403")

	})
	@GetMapping("")
	public ResponseEntity<List<StudentResponseModel>> getAllStudents() {
		return ResponseEntity.status(HttpStatus.OK).body(studentService.getAllStudents());
	}

	@Operation(tags = "Get Student By Id", description = "Get Student By Id", responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Please input correct token", responseCode = "403")

	})
	@GetMapping(value = "/{studentId}")
	public ResponseEntity<StudentResponseModel> getStudent(@PathVariable Long studentId) {
		StudentResponseModel student = studentService.getStudentbyStudentId(studentId);
		return ResponseEntity.status(HttpStatus.OK).body(student);
	}

	@Operation(tags = "Create New Student", description = "Create New Student", responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Please input correct token", responseCode = "403")

	})
	@PostMapping(value = "")
	public ResponseEntity<StudentResponseModel> createStudent(@RequestBody StudentRequestModel studentRequestModel) {

		StudentResponseModel createdStudent = studentService.saveStudent(studentRequestModel);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);

	}

	@Operation(tags = "Update Existing Student", description = "Update Existing Student", responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Please input correct token", responseCode = "403")

	})
	@PutMapping(value = "/{studentId}")
	public ResponseEntity<StudentResponseModel> updateStudent(@PathVariable Long studentId,
			@RequestBody StudentRequestModel studentRequestModel) {

		StudentResponseModel updatedStudent = studentService.updateStudent(studentRequestModel, studentId);
		return ResponseEntity.status(HttpStatus.CREATED).body(updatedStudent);

	}

	@Operation(tags = "Delete Student by Id", description = "Delete Student by Id", responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Please input correct token", responseCode = "403")

	})
	@DeleteMapping(value = "/{studentId}")
	public ResponseEntity<String> deleteStudent(@PathVariable Long studentId) {
		studentService.deleteStudent(studentId);
		return ResponseEntity.status(HttpStatus.OK).body("Record Deleted Successfully...");
	}

}
