package repository.employee;

import java.io.*;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

import models.Employee;

public class CsvEmployeeRepository implements EmployeeRepository {

	private final String path = "src/file/employeesdata.csv";
	private BufferedReader br = null;
    private String line = "";
	
	@Override
	public LinkedHashMap<String, Employee> loadEmployees() {
		try {
    		LinkedHashMap<String, Employee> employeeData = new LinkedHashMap<>();
            br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) {
            	String[] dataPerson = line.split(";");
            	String name = dataPerson[0];
            	
            	String dataString = dataPerson[1]; 
            	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            	LocalDate dateOfBirth = LocalDate.parse(dataString, formatter);
            	
            	BigDecimal salary = new BigDecimal(dataPerson[2]);
            	
            	String role = dataPerson[3];
            	
            	Employee employee = new Employee(name, dateOfBirth, salary, role);
            
            	employeeData.put(name, employee);
            }
            return employeeData;
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo não encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.err.println("Erro ao fechar o BufferedReader: " + e.getMessage());
                }
            }
        }
		return null;
	}

}
