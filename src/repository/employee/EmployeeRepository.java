package repository.employee;

import java.util.*;

import models.Employee;

public interface EmployeeRepository {
	LinkedHashMap<String, Employee> loadEmployees();
}
