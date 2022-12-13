package com.javatech.springboot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javatech.springboot.exception.ResourceNotFoundException;
import com.javatech.springboot.model.Employee;
import com.javatech.springboot.repository.EmployeeRepository;
import com.javatech.springboot.service.SequenceGeneratorService;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;
	
	@GetMapping("/employees")
	public List<Employee> getAllEmployees(){
		return employeeRepository.findAll();
	}
	
	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById( @PathVariable long id){
		Employee emp = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("record not found for this id :" + id));
		return ResponseEntity.ok().body(emp);
	}
	
	@PostMapping("/employees")
	public Employee createEmployee(@Valid @RequestBody Employee employee) {
		employee.setId(sequenceGeneratorService.generateSequence(Employee.SEQUENCE_NAME));
		return employeeRepository.save(employee);
	}
	
	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updateEmployee( @PathVariable long id,
			@Valid @RequestBody Employee employee)throws ResourceNotFoundException{
		
		Employee emp = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("record not found for this id :" + id));
		emp.setFirstName(employee.getFirstName());
		emp.setLastName(employee.getLastName());
		emp.setEmailId(employee.getEmailId());
		final Employee updatedEmp =  employeeRepository.save(emp);
		
		return ResponseEntity.ok().body(updatedEmp);
	}
	
	@DeleteMapping("/employees/{id}")
	public Map<String,Boolean> deleteEmployee( @PathVariable(value = "id") long employeeId)throws ResourceNotFoundException{
		
		Employee emp = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("record not found for this id :" + employeeId));
		
		 employeeRepository.delete(emp);
		 Map<String,Boolean> response = new HashMap<String, Boolean>();
		 response.put("deleted", Boolean.TRUE);
		 return response;
	}
	
}
