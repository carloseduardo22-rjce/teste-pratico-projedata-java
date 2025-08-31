package main;

import repository.employee.CsvEmployeeRepository;
import repository.employee.EmployeeRepository;
import services.EmployeeService;

public class Main {

	public static void main(String[] args) throws Exception {

		EmployeeRepository employeeRepository = new CsvEmployeeRepository();
		EmployeeService employeeService = new EmployeeService(employeeRepository);
		
		employeeService.deleteEmployee("João");
		
		employeeService.employees();
		
		employeeService.updateSalary();
		
		employeeService.printByFunction();
		
		employeeService.printBirthDay();
		
		employeeService.olderEmployees();
		
		employeeService.printEmployeesAlphabetically();
		
		employeeService.totalEmployeeSalaries();
		
		employeeService.minimumSalarys();
		
	}

}
