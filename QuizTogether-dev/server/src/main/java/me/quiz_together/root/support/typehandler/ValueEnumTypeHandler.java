package me.quiz_together.root.support.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import me.quiz_together.root.support.EnumUtils;
import me.quiz_together.root.support.enumeration.ValueEnum;

@MappedTypes(ValueEnum.class)
@MappedJdbcTypes(JdbcType.NUMERIC)
public class ValueEnumTypeHandler<T extends Enum<T> & ValueEnum> extends BaseTypeHandler<T> {
	protected Class<T> type;

	public ValueEnumTypeHandler(Class<T> clazz) {
		this.type = clazz;
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType)
            throws SQLException {
		ps.setInt(i, parameter.getValue());
	}

	@Override
	public T getNullableResult(ResultSet rs, String columnName) throws SQLException {

		return EnumUtils.getValueEnum(type, rs.getInt(columnName));
	}

	@Override
	public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {

		int value = rs.getInt(columnIndex);

		return EnumUtils.getValueEnum(type, value);
	}

	@Override
	public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {

		int value = cs.getInt(columnIndex);

		return EnumUtils.getValueEnum(type, value);
	}
}
