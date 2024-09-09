package com.manya.studentManagementSystem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.manya.studentManagementSystem.data.StudentEntity;
import com.manya.studentManagementSystem.model.StudentRequestModel;
import com.manya.studentManagementSystem.model.StudentResponseModel;
import com.manya.studentManagementSystem.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private ModelMapper modelMapper;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public StudentResponseModel saveStudent(StudentRequestModel studentRequestModel) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		StudentEntity studentEntity = modelMapper.map(studentRequestModel, StudentEntity.class);
		studentEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(studentRequestModel.getPassword()));
		//studentEntity.setEncryptedPassword(studentRequestModel.getPassword());
		studentRepository.save(studentEntity);
		StudentResponseModel studentResponseModel = modelMapper.map(studentEntity, StudentResponseModel.class);
		return studentResponseModel;
	}

	@Override
	public List<StudentResponseModel> getAllStudents() {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Iterable<StudentEntity> studentList = studentRepository.findAll();
		List<StudentResponseModel> studentsReponseModels = new ArrayList<>();
		for (StudentEntity studentEntity : studentList) {
			StudentResponseModel studentResponseModel = modelMapper.map(studentEntity, StudentResponseModel.class);
			studentsReponseModels.add(studentResponseModel);
		}
		return studentsReponseModels;
	}

	@Override
	public StudentResponseModel updateStudent(StudentRequestModel studentRequestModel, Long id) {
		StudentEntity studentEntity = studentRepository.findById(id).orElseThrow();
		studentEntity.setAddress(studentRequestModel.getAddress());
		studentEntity.setName(studentRequestModel.getName());
		studentEntity.setPincode(studentRequestModel.getPincode());
		studentEntity.setContactNumber(studentRequestModel.getContactNumber());
		studentEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(studentRequestModel.getPassword()));
		//studentEntity.setEncryptedPassword(studentRequestModel.getPassword());
		studentRepository.save(studentEntity);
		StudentResponseModel studentResponseModel = modelMapper.map(studentEntity, StudentResponseModel.class);
		return studentResponseModel;
	}

	@Override
	public void deleteStudent(Long id) {
		Optional<StudentEntity> optStudent = studentRepository.findById(id);
		StudentEntity studentEntity = optStudent.get();
		studentRepository.delete(studentEntity);
	}

	@Override
	public StudentResponseModel getStudentbyStudentId(Long id) {
		StudentEntity studentEntity = studentRepository.findById(id).orElseThrow();
		StudentResponseModel studentResponseModel = modelMapper.map(studentEntity, StudentResponseModel.class);
		return studentResponseModel;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		StudentEntity student = studentRepository.findByContactNumber(Long.valueOf(username));
		if(student==null) {
			throw new UsernameNotFoundException(username);
		}
		return new User(String.valueOf(student.getContactNumber()), student.getEncryptedPassword(), true, true, true, true,
				new ArrayList<>());
	}

}
