package main;

import service.Csv;
import service.EmployeeService;

public class Main {

	public static void main(String[] args) throws Exception {

		Csv csv = new Csv ();
		EmployeeService employeeService = new EmployeeService(csv);
		
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
