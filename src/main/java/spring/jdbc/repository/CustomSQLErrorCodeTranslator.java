package spring.jdbc.repository;

import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.lang.Nullable;

public class CustomSQLErrorCodeTranslator extends SQLErrorCodeSQLExceptionTranslator{
	@Override
	protected DataAccessException customTranslate(final String task, final String sql, final SQLException sqlEx) {
		if(sqlEx.getErrorCode() == 23505)
			return new DuplicateKeyException("Custom Exception translator - Integrity contraint violation.", sqlEx);
		
		return null;
	}
}
