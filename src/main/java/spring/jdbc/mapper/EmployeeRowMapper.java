package spring.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import spring.jdbc.model.Employee;

public class EmployeeRowMapper implements RowMapper<Employee>{
	
	public Employee mapRow(final ResultSet rs, final int rowNum) throws SQLException{
		final Employee employee = new Employee();
		
		employee.setId(rs.getInt("ID"));
		employee.setFirstName(rs.getString("FIRST_NAME"));
		employee.setLastName(rs.getString("LAST_NAME"));
		employee.setAddress(rs.getString("ADDRESS"));

		return employee;
	}
}
