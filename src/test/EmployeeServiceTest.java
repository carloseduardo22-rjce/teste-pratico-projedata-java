package test;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.*;

import models.Employee;
import repository.employee.CsvEmployeeRepository;
import services.EmployeeService;

public class EmployeeServiceTest {
    private EmployeeService employeeService;
    
    @Before
    public void setUp() {
    	CsvEmployeeRepository csvEmployeeRepository = new CsvEmployeeRepository();
        employeeService = new EmployeeService(csvEmployeeRepository);
    }
    
    @After
    public void tearDown() {
        employeeService = null;
    }

    @Test
    public void testDeleteEmployee() {
        assertTrue("João deveria existir antes da exclusão", employeeService.deleteEmployee("Joao"));
    }
    
    @Test
    public void testOlderEmployee() {
        Employee oldestEmployee = employeeService.getOldestEmployee();
        int age = employeeService.getEmployeeAge(oldestEmployee);
        
        assertEquals("Funcionário mais velho deve ter 64 anos", 64, age);
        assertEquals("Nome do funcionário mais velho deve ser Caio", "Caio", oldestEmployee.getName());
    }
    
    @Test
    public void testTotalSalaryAfterDeletionAndIncrease() {
        employeeService.deleteEmployee("Joao");
        
        employeeService.updateSalary();
        
        BigDecimal totalSalary = employeeService.totalSalary();
        
        assertEquals("O resultado da soma após exclusão do João e aumento de 10%", 
                     new BigDecimal("50906.82"), totalSalary);
    }
}