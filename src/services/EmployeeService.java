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

import exception.EmployeeNotFoundException;
import models.Employee;
import repository.employee.EmployeeRepository;
import util.FormatterUtil;

public class EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    private LinkedHashMap<String, Employee> employeeData;
    private boolean joaoDeleted = false;
    private boolean salaryUpdated = false; 

    
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeData = employeeRepository.loadEmployees();
    }
    
    
    public boolean deleteEmployee(String name) {
        try {
            if (employeeData.remove(name) == null) {
                throw new EmployeeNotFoundException("Funcionário com nome: " + name + " não existe.");
            }
            
            if ("Joao".equals(name)) {
                joaoDeleted = true;
            }
            
            System.out.println("Funcionário com nome: " + name + " excluído com sucesso.");
            return true;
            
        } catch (EmployeeNotFoundException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
    
    
    private LinkedHashMap<String, Employee> getEmployeeData() {
        if (!joaoDeleted && employeeData.containsKey("Joao")) {
            try {
                deleteEmployee("Joao");
            } catch (Exception e) {
                System.err.println("Aviso: Não foi possível remover João automaticamente.");
            }
        }
        return employeeData;
    }
    
    
    public void employees() {
        LinkedHashMap<String, Employee> data = getEmployeeData();
        System.out.println("");
        System.out.println("Todos os funcionários: ");

        for (Entry<String, Employee> entry : data.entrySet()) {
            Employee employee = entry.getValue();
            
            System.out.println("Nome do Funcionário: " + employee.getName() 
                            + " / Data de nascimento do funcionário: " + FormatterUtil.formatDate(employee.getDateOfBirth())
                            + " / Salário do funcionário: " + FormatterUtil.formatCurrency(employee.getSalary())
                            + " / Função do funcionário: " + employee.getRole()); 
        }
    }
    
    
    public void updateSalary() {
        LinkedHashMap<String, Employee> data = getEmployeeData();
        
        if (!salaryUpdated) {
            for (Entry<String, Employee> entry : data.entrySet()) {
                Employee employee = entry.getValue();
                BigDecimal salary = increaseSalary(employee.getSalary());
                employee.setSalary(salary);
            }
            salaryUpdated = true; 
        }
        
        System.out.println("");
        System.out.println("Salário dos funcionários atualizados com 10%: ");
        for (Entry<String, Employee> entry : data.entrySet()) {
            Employee employee = entry.getValue();
            
            System.out.println("Nome do Funcionário: " + employee.getName() 
                            + " / Data de nascimento do funcionário: " + FormatterUtil.formatDate(employee.getDateOfBirth()) 
                            + " / Salário do funcionário: " + FormatterUtil.formatCurrency(employee.getSalary())
                            + " / Função do funcionário: " + employee.getRole()); 
        }
    }
    
    
    private static BigDecimal increaseSalary(BigDecimal salary) {
        BigDecimal increaseFactor = new BigDecimal("1.10");
        BigDecimal newSalary = salary.multiply(increaseFactor);
        return newSalary.setScale(2, RoundingMode.HALF_UP);
    }

    
    private Map<String, List<Employee>> groupEmployeesByFunction() {
        LinkedHashMap<String, Employee> data = getEmployeeData();
        Map<String, List<Employee>> employeesByFunction = new HashMap<>();

        for (Entry<String, Employee> entry : data.entrySet()) {
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
        LinkedHashMap<String, Employee> data = getEmployeeData();
        System.out.println("Aniversariantes no mês 10 e 12:");
        for (Entry<String, Employee> entry : data.entrySet()) {
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
        LinkedHashMap<String, Employee> data = getEmployeeData();
        return data.values().stream()
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
        LinkedHashMap<String, Employee> data = getEmployeeData();
        List<Employee> employees = data.values().stream()
                                     .sorted(Comparator.comparing(Employee::getName))
                                     .toList();
        for (Employee employee : employees) {
            System.out.println("Nome: " + employee.getName());
        }
    }
    
    
    public BigDecimal totalSalary() {
        LinkedHashMap<String, Employee> data = getEmployeeData();
        
        return data.values().stream()
                .map(Employee::getSalary) 
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    
    public void totalEmployeeSalaries() {
        System.out.println("");
        System.out.println("Soma total dos salários: " + FormatterUtil.formatCurrency(totalSalary()));
    }
    
    
    public void minimumSalarys() {
        LinkedHashMap<String, Employee> data = getEmployeeData();
        
        BigDecimal minimumSalary = new BigDecimal("1212.00");

        System.out.println("");
        for (Entry<String, Employee> entry : data.entrySet()) {
            Employee employee = entry.getValue();
            BigDecimal result = employee.getSalary().divide(minimumSalary, 2, RoundingMode.HALF_UP);
            System.out.println("Nome do funcionário: " + employee.getName() + 
                               " | Quantidade aproximada de salários mínimos: " + result);
        }
    }
}