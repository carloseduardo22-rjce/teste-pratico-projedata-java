package services;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import models.Employee;
import repository.employee.EmployeeRepository;
import util.FormatterUtil;

public class EmployeeService {
	
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
	
	public void deleteEmployee(String name) throws Exception {
		LinkedHashMap<String, Employee> employeeData = employeeRepository.loadEmployees();
		if(employeeData.remove("Joao") == null) {
			throw new Exception("Funcionário joão não encontrado.");
		} 
		System.out.print("Funcionário: " + name + " excluído com sucesso.");
		System.out.println("");
	}
	
	
	public void employees() {
		LinkedHashMap<String, Employee> employeeData = employeeRepository.loadEmployees();
		employeeData.remove("Joao");
		System.out.println("");
		System.out.println("Todos os funcionários: ");

		for (Entry<String, Employee> entry : employeeData.entrySet()) {
	        Employee employee = entry.getValue();
	        
	        String dateFormatted = FormatterUtil.formatDate(employee.getDateOfBirth());
	        
	        String formattedSalary = FormatterUtil.formatCurrency(employee.getSalary());
	        
	        System.out.println("Nome do Funcionário: " + employee.getName() 
	        					+ " / Data de nascimento do funcionário: " + dateFormatted
	        					+ " / Salário do funcionário: " + formattedSalary
	        					+ " / Função do funcionário: " + employee.getRole()); 
	    }
	}
	
	public LinkedHashMap<String, Employee> getEmployeeData () {
		LinkedHashMap<String, Employee> employeeData = employeeRepository.loadEmployees();
		employeeData.remove("Joao");
		return employeeData;
	}
	
	public void updateSalary() {
		LinkedHashMap<String, Employee> employeeData = employeeRepository.loadEmployees();
		employeeData.remove("Joao");
		for (Entry<String, Employee> entry : employeeData.entrySet()) {
            Employee employee = entry.getValue();
            BigDecimal salary = increaseSalary(employee.getSalary());
            employee.setSalary(salary);
		}
		
		System.out.println("");
		System.out.println("Salário dos funcionários atualizados com 10%: ");
		for (Entry<String, Employee> entry : employeeData.entrySet()) {
            Employee employee = entry.getValue();

            String dateFormatted = FormatterUtil.formatDate(employee.getDateOfBirth());
            
            String formattedSalary = FormatterUtil.formatCurrency(employee.getSalary());
            
            System.out.println("Nome do Funcionário: " + employee.getName() 
            					+ " / Data de nascimento do funcionário: " + dateFormatted 
            					+ " / Salário do funcionário: " + formattedSalary
            					+ " / Função do funcionário: " + employee.getRole()); 
        }
	}
	
	private static BigDecimal increaseSalary(BigDecimal salary) {
        BigDecimal increaseFactor = new BigDecimal("1.10");
        BigDecimal newSalary = salary.multiply(increaseFactor);
        return newSalary.setScale(2, RoundingMode.HALF_UP);
    }
	

	private Map<String, List<Employee>> groupEmployeesByFunction() {
		LinkedHashMap<String, Employee> employeeData = employeeRepository.loadEmployees();
		employeeData.remove("Joao");
		Map<String, List<Employee>> employeesByFunction = new HashMap<>();

		for (Entry<String, Employee> entry : employeeData.entrySet()) {
		    Employee employee = entry.getValue();
		    String role = employee.getRole();

		    List<Employee> employees = employeesByFunction.getOrDefault(role, new ArrayList<>());

		    employees.add(employee);

		    employeesByFunction.put(role, employees);
		}
		    
		return employeesByFunction;
	}
	
	
	public void printByFunction() {
		Map<String, List<Employee>> employeesByFunction = groupEmployeesByFunction();
		
		System.out.println("");
		System.out.println("Funcionários e suas funções: ");
		for (Map.Entry<String, List<Employee>> entry : employeesByFunction.entrySet()) {
	    	System.out.println(); 
	    	System.out.println("Função: " + entry.getKey());
	        for (Employee e : entry.getValue()) {
	            System.out.println("   - Nome: " + e.getName());
	        }
	        System.out.println(); 
	    }
	}
	
	public void printBirthDay() {
		LinkedHashMap<String, Employee> employeeData = employeeRepository.loadEmployees();
		employeeData.remove("Joao");
		System.out.println("Aniversariantes no mês 10 e 12:");
		for (Entry<String, Employee> entry : employeeData.entrySet()) {
            Employee employee = entry.getValue();
            LocalDate birthDay = employee.getDateOfBirth();
            
            String dateFormatted = FormatterUtil.formatDate(birthDay);
            
            if (birthDay.getMonthValue() == 10 || birthDay.getMonthValue() == 12) {
            	System.out.println("Nome: " + employee.getName() + " / Data de nascimento: " + dateFormatted);
            }
        }
		System.out.println("");
	}

	public Employee getOldestEmployee() {
		LinkedHashMap<String, Employee> employeeData = employeeRepository.loadEmployees();
		employeeData.remove("Joao");
    	return employeeData.values().stream()
            						.min(Comparator.comparing(Employee::getDateOfBirth))
            						.orElseThrow();
	}

	public int getEmployeeAge(Employee employee) {
    	return Period.between(employee.getDateOfBirth(), LocalDate.now()).getYears();
	}
	
	public void olderEmployees() {
		Employee oldestEmployee = getOldestEmployee();
    	int age = getEmployeeAge(oldestEmployee);
		
		System.out.println("----Funcionário mais velho----");
		System.out.println("Nome: " + oldestEmployee.getName() + " / Idade: " + age);
		System.out.println("");
		System.out.println("----Funcionários por ordem alfabética----");
	}
	
	public void printEmployeesAlphabetically() {
		LinkedHashMap<String, Employee> employeeData = employeeRepository.loadEmployees();
		List<Employee> employees = employeeData.values().stream().sorted(Comparator.comparing(Employee::getName)).toList();
		for (Employee employee : employees) {
			System.out.println("Nome: " + employee.getName());
		}
	}
	
	public BigDecimal totalSalary () throws Exception {
		LinkedHashMap<String, Employee> employeeData = employeeRepository.loadEmployees();
		employeeData.remove("Joao");
		
		for (Entry<String, Employee> entry : employeeData.entrySet()) {
            Employee employee = entry.getValue();
            BigDecimal salary = increaseSalary(employee.getSalary());
            employee.setSalary(salary);
		}
		
		return employeeData.values().stream()
		        .map(Employee::getSalary) 
		        .reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	public void totalEmployeeSalaries() throws Exception {
		System.out.println("");
		
        String formattedTotalSalary = FormatterUtil.formatCurrency(totalSalary());

	    System.out.println("Soma total dos salários: " + formattedTotalSalary);
	}
	
	public void minimumSalarys() {
		LinkedHashMap<String, Employee> employeeData = employeeRepository.loadEmployees();
		employeeData.remove("Joao");
		for (Entry<String, Employee> entry : employeeData.entrySet()) {
            Employee employee = entry.getValue();
            BigDecimal salary = increaseSalary(employee.getSalary());
            employee.setSalary(salary);
		}
		BigDecimal minimumSalary = new BigDecimal("1212.00");

	    System.out.println("");
	    for (Entry<String, Employee> entry : employeeData.entrySet()) {
	        Employee employee = entry.getValue();
	        BigDecimal result = employee.getSalary().divide(minimumSalary, 2, RoundingMode.HALF_UP);
	        System.out.println("Nome do funcionário: " + employee.getName() + 
	                           " | Quantidade aproximada de salários mínimos: " + result);
	    }
	}
	
}
