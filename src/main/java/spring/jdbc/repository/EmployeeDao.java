package spring.jdbc.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import spring.jdbc.mapper.EmployeeRowMapper;
import spring.jdbc.model.Employee;

@Repository
public class EmployeeDao {
	
	private JdbcTemplate jdbcTemplate;
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	private SimpleJdbcInsert simpleJdbcInsert;
	
	@Autowired
	public void setDataSource(final DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		final CustomSQLErrorCodeTranslator customSQLErrorCodeTranslator = new CustomSQLErrorCodeTranslator();
		jdbcTemplate.setExceptionTranslator(customSQLErrorCodeTranslator);
		
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("EMPLOYEE");
	}
	
	/*** JdbcTemplate ***/
	
	public int getCountOfEmployees() {
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM EMPLOYEE", Integer.class);
	}
	
	public List<Employee> getAllEmployees(){
		return jdbcTemplate.query("SELECT * FROM EMPLOYEE", new EmployeeRowMapper());
	}
	
	public int addEmployeeUsingUpdateMethod(final int id) {
		return jdbcTemplate.update("INSERT INTO EMPLOYEE VALUES (?,?,?,?)",id , "Momo", "Titi","France");
	}
	
	public void addEmployeeUsingExcuteMethod() {
		jdbcTemplate.execute("INSERT INTO EMPLOYEE VALUES(7,'Toto','Tata','Ecosse')");
	}
	
	/*** JdbcTemplate: Mapping a single result row to a result object (Java) via a RowMapper. ***/
	
	public Employee getEmployee(final int id) {
		return jdbcTemplate.queryForObject("SELECT * FROM EMPLOYEE WHERE ID = ?", new EmployeeRowMapper(), new Object[] {id});
	}
	
	/*** BatchUpdate: using JdbcTemplate ***/
	
	public int[] batchUpdateUsingJdbcTemplate(final List<Employee> employees) {
		return jdbcTemplate.batchUpdate("INSERT INTO EMPLOYEE VALUES(?,?,?,?)",new BatchPreparedStatementSetter (){
			@Override
			public void setValues(final PreparedStatement ps, final int i) throws SQLException{
				ps.setInt(1, employees.get(i).getId());
				ps.setString(2, employees.get(i).getFirstName());
				ps.setString(3, employees.get(i).getLastName());
				ps.setString(4, employees.get(i).getAddress());
			}
			
			@Override
			public int getBatchSize() {
				return 3;
			}
		});
	}
	
	/*** BatchUpdate: using NamedParameterJdbcTemplate ***/
	
	public int [] batchUpdateUsingNamedParameterJdbcTemplate(final List<Employee> employees) {
		final SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(employees.toArray());
		final int[] updateCounts = namedParameterJdbcTemplate.batchUpdate("INSERT INTO EMPLOYEE VALUES (:id, :firstname, :lastname, :address)", batch);
	
		return updateCounts;
	}
	
	/*** SimpleJdbcInsert ***/
	
	public int addEmployeeUsingSimpleJdbcInsert(final Employee emp) {
		final Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ID", emp.getId());
		parameters.put("FIRST_NAME", emp.getFirstName());
		parameters.put("LAST_NAME", emp.getLastName());
		parameters.put("ADDRESS", emp.getAddress());
		
		return simpleJdbcInsert.execute(parameters);
	}
	
	/*** NamedParameterJdbcTemplate ***/
	
	public String getEmployeeUsingMapSqlParameterSource() {
		final SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", 1);
		return namedParameterJdbcTemplate.queryForObject("SELECT FIRST_NAME FROM EMPLOYEE WHERE ID = :id", namedParameters, String.class);
	}
	
	public int getEmployeeUsingBeanPropertySqlParmeterSource() {
		final Employee employee = new Employee();
		employee.setFirstName("Jean");
		
		final String SELECT_BY_ID = "SELECT COUNT(*) FROM EMPLOYEE WHERE FIRST_NAME = :firstName";
		
		final SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(employee);
		
		return namedParameterJdbcTemplate.queryForObject(SELECT_BY_ID, namedParameters, Integer.class);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
