package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Employee extends Person {

	private BigDecimal salary;
	private String role;
	
	public Employee(String name, LocalDate dateOfBirth, BigDecimal salary, String role) {
		super(name, dateOfBirth);
		this.salary = salary;
		this.role = role;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public String getRole() {
		return role;
	}
	
	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}
	
}
