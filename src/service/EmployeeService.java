package service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import model.Employee;

public class EmployeeService {

	private Csv csv;
    private LinkedHashMap<String, Employee> employeeData;

    public EmployeeService(Csv csv) throws Exception {
        this.csv = csv;
        this.employeeData = this.csv.readCsv();
    }
	
	public void deleteEmployee(String name) throws Exception {
		if(employeeData.remove("Joao") == null) {
			throw new Exception("Funcionário joão não encontrado.");
		} 
		System.out.print("Funcionário: " + name + " excluído com sucesso.");
		System.out.println("");
	}
	
	
	public void employees() {
		try {
			System.out.println("");
			System.out.println("Todos os funcionários: ");
			for (Entry<String, Employee> entry : employeeData.entrySet()) {
		        Employee employee = entry.getValue();

		        String formattedSalary = formatValue(employee.getSalary());
		        
		        System.out.println("Nome do Funcionário: " + employee.getName() 
		        					+ " / Data de nascimento do funcionário: " + employee.getDateOfBirth() 
		        					+ " / Salário do funcionário: " + formattedSalary
		        					+ " / Função do funcionário: " + employee.getRole()); 
		    }
		} catch (IllegalStateException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}
	
	public void updateSalary() throws Exception {
		for (Entry<String, Employee> entry : employeeData.entrySet()) {
            Employee employee = entry.getValue();
            BigDecimal salary = increaseSalary(employee.getSalary());
            employee.setSalary(salary);
		}
		
		System.out.println("");
		System.out.println("Salário dos funcionários atualizados com 10%: ");
		for (Entry<String, Employee> entry : employeeData.entrySet()) {
            Employee employee = entry.getValue();

            String formattedSalary = formatValue(employee.getSalary());
            
            System.out.println("Nome do Funcionário: " + employee.getName() 
            					+ " / Data de nascimento do funcionário: " + employee.getDateOfBirth() 
            					+ " / Salário do funcionário: " + formattedSalary
            					+ " / Função do funcionário: " + employee.getRole()); 
        }
	}
	
	private static BigDecimal increaseSalary(BigDecimal salary) throws Exception {
        BigDecimal increaseFactor = new BigDecimal("1.10");
        BigDecimal newSalary = salary.multiply(increaseFactor);
        return newSalary.setScale(2, RoundingMode.HALF_UP);
    }
	

	private Map<String, List<Employee>> groupEmployeesByFunction() throws Exception {
	
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
	
	
	public void printByFunction() throws Exception {
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
		try {
			System.out.println("Aniversariantes no mês 10 e 12:");
			for (Entry<String, Employee> entry : employeeData.entrySet()) {
	            Employee employee = entry.getValue();
	            LocalDate birthDay = employee.getDateOfBirth();
	            if (birthDay.getMonthValue() == 10 || birthDay.getMonthValue() == 12) {
	            	System.out.println("Nome: " + employee.getName() + " / Data de nascimento: " + employee.getDateOfBirth());
	            }
	        }
			System.out.println("");
		} catch (IllegalStateException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}
	
	public void olderEmployees() throws Exception {
		Employee employee = employeeData.values().stream()
			        .min(Comparator.comparing(Employee::getDateOfBirth))
			        .orElseThrow();

		int age = Period.between(employee.getDateOfBirth(), LocalDate.now()).getYears();
			
		System.out.println("----Funcionário mais velho----");
		System.out.println("Nome: " + employee.getName() + " / Idade: " + age);
		System.out.println("");
		System.out.println("----Funcionários por ordem alfabética----");
	}
	
	public void printEmployeesAlphabetically() {
		List<Employee> employees = employeeData.values().stream().sorted(Comparator.comparing(Employee::getName)).toList();
		for (Employee employee : employees) {
			System.out.println("Nome: " + employee.getName());
		}
	}
	
	public void totalEmployeeSalaries() {
		System.out.println("");
	    BigDecimal totalSalary = employeeData.values().stream()
	        .map(Employee::getSalary) 
	        .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        String formattedTotalSalary = formatValue(totalSalary);

	    System.out.println("Soma total dos salários: " + formattedTotalSalary);
	}
	
	private static String formatValue (BigDecimal value) {
		NumberFormat nf = NumberFormat.getInstance(new Locale("pt", "BR"));
        nf.setMinimumFractionDigits(2); 
        nf.setMaximumFractionDigits(2); 

        return nf.format(value);
	}
	
	public void minimumSalarys() throws Exception {
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
