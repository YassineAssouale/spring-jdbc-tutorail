package spring.jdbc;


import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import spring.jdbc.config.AppConfig;
import spring.jdbc.model.Employee;
import spring.jdbc.repository.EmployeeDao;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class}, loader = AnnotationConfigContextLoader.class)
public class EmployeeDAOTest {

	@Autowired
	EmployeeDao employeeDao;
	
	@Test
	void testGetCountOfEmployees() {
		Assertions.assertEquals(8, employeeDao.getCountOfEmployees());
	}
	
	@Test 
	void testGetAllEmployeesQueryMethod(){
		Assertions.assertEquals(4, employeeDao.getAllEmployees().size());
	}
	
	@Test
	void testAddEmployeeUsingUpdateMethod() {
		Assertions.assertEquals(1, employeeDao.addEmployeeUsingUpdateMethod(5));
	}
	
	@Test
	void testAddEmployeeUsingSimpleJdbcInsert() {
		Employee employee = new Employee();
		employee.setId(6);
		employee.setFirstName("Test 6");
		employee.setLastName("Test Simple JDBC Insert");
		employee.setAddress("Test-6");
		
		Assertions.assertEquals(1, employeeDao.addEmployeeUsingSimpleJdbcInsert(employee));
	}
	
	@Test
	void testGetEmployee() {
		Assertions.assertEquals(2, employeeDao.getEmployee(2).getId());
	}
	
	@Test
	void testAddEmployeeUsingExecuteMethod() {
		employeeDao.addEmployeeUsingExcuteMethod();
		Assertions.assertEquals("Toto", employeeDao.getEmployee(7).getFirstName());
	}
	
	@Test
	void testGetEmployeeUsingMapSqlParameterSource() {
		Assertions.assertEquals("Jean", employeeDao.getEmployeeUsingMapSqlParameterSource());
	}
	
	@Test
	void testGetEmployeeUsingBeanPropertySqlParmeterSource() {
		Assertions.assertEquals(1, employeeDao.getEmployeeUsingBeanPropertySqlParmeterSource());
	}
	
	@Test
	void testbatchUpdateUsingJdbcTemplate() {

		final List<Employee> employees = new ArrayList<Employee>();
		final Employee emp1 = new Employee();
		emp1.setId(10);
		emp1.setFirstName("Test FN 1");
		emp1.setLastName("Test LN 1");
		emp1.setAddress("test Adresse 1");

		final Employee emp2 = new Employee();
		emp2.setId(20);
		emp2.setFirstName("Test FN 2");
		emp2.setLastName("Test LN 2");
		emp2.setAddress("test Adresse 2");

		final Employee emp3 = new Employee();
		emp3.setId(30);
		emp3.setFirstName("Test FN 3");
		emp3.setLastName("Test LN 3");
		emp3.setAddress("test Adresse 3");

		employees.add(emp1);
		employees.add(emp2);
		employees.add(emp3);

		employeeDao.batchUpdateUsingJdbcTemplate(employees);

		Assertions.assertNotNull(employeeDao.getEmployee(10));
		Assertions.assertNotNull(employeeDao.getEmployee(20));
		Assertions.assertNotNull(employeeDao.getEmployee(30));
	}

	@Test
	void batchUpdateUsingNamesParameterJdbcTemplate() {

		final List<Employee> employees = new ArrayList<Employee>();
		final Employee emp1 = new Employee();
		emp1.setId(40);
		emp1.setFirstName("Test FN 1");
		emp1.setLastName("Test LN 1");
		emp1.setAddress("test Adresse 1");

		final Employee emp2 = new Employee();
		emp2.setId(50);
		emp2.setFirstName("Test FN 2");
		emp2.setLastName("Test LN 2");
		emp2.setAddress("test Adresse 2");

		final Employee emp3 = new Employee();
		emp3.setId(60);
		emp3.setFirstName("Test FN 3");
		emp3.setLastName("Test LN 3");
		emp3.setAddress("test Adresse 3");

		employees.add(emp1);
		employees.add(emp2);
		employees.add(emp3);

		employeeDao.batchUpdateUsingJdbcTemplate(employees);

		Assertions.assertNotNull(employeeDao.getEmployee(40));
		Assertions.assertNotNull(employeeDao.getEmployee(50));
		Assertions.assertNotNull(employeeDao.getEmployee(60));
	}
	
	@Test
	void testCustomExceptionTranslator() {
		employeeDao.addEmployeeUsingUpdateMethod(8);
		try {
			employeeDao.addEmployeeUsingUpdateMethod(8);
		} catch (final DuplicateKeyException e) {
			Assertions.assertTrue(e.getMessage().contains("Custom Exception translator - Integrity contraint violation."));
		}
	}

}
