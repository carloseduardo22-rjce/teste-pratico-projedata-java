package service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

import model.Employee;

public class Csv {

	private BufferedReader br = null;
    private String line = "";
	
    public LinkedHashMap<String, Employee> readCsv() {
    	try {
    		LinkedHashMap<String, Employee> employeeData = new LinkedHashMap<>();
            br = new BufferedReader(new FileReader("src/file/dadosfuncionarios.csv"));
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		return null;
    }
    
}
