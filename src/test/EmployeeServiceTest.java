package test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

import org.junit.*;

import models.Employee;
import service.Csv;
import service.EmployeeService;

public class EmployeeServiceTest {

    private Csv csv;
    private EmployeeService employeeService;
    
    @Before
    public void setUp() {
        csv = new Csv();
        employeeService = new EmployeeService(csv);
    }
    
    @After
    public void tearDown() {
        csv = null;
        employeeService = null;
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        LinkedHashMap<String, Employee> employeeData = csv.readCsv();
        assertTrue("João deveria existir antes da exclusão", employeeData.containsKey("Joao"));
        
        employeeService.deleteEmployee("Joao");
        
        LinkedHashMap<String, Employee> updatedData = employeeService.getEmployeeData();
        assertFalse("João não deveria existir após a exclusão", updatedData.containsKey("Joao"));
    }
    
    @Test
    public void testOlderEmployee() {
        Employee oldestEmployee = employeeService.getOldestEmployee();
        int age = employeeService.getEmployeeAge(oldestEmployee);
        
        assertEquals("Funcionário mais velho deve ter 64 anos", 64, age);
        assertEquals("Nome do funcionário mais velho deve ser Caio", "Caio", oldestEmployee.getName());
    }
    
    @Test
    public void testTotalSalaryAfterDeletionAndIncrease() throws Exception {
        employeeService.deleteEmployee("Joao");
        
        employeeService.updateSalary();
        
        BigDecimal totalSalary = employeeService.totalSalary();
        
        assertEquals("O resultado da soma após exclusão do João e aumento de 10%", 
                     new BigDecimal("50906.82"), totalSalary);
    }
}