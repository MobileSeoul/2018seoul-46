package me.quiz_together.root.support.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(Long.class)
@MappedJdbcTypes({ JdbcType.TIMESTAMP, JdbcType.DATE})
public class DateLongTypeHandler extends BaseTypeHandler<Long> {
        public void setNonNullParameter(PreparedStatement ps, int i, Long parameter, JdbcType jdbcType) throws SQLException {
            ps.setTimestamp(i, new Timestamp(parameter));
        }

        @Override
        public Long getNullableResult(ResultSet rs, String columnName) throws SQLException {
            Timestamp sqlTimestamp = rs.getTimestamp(columnName);
            return sqlTimestamp != null? sqlTimestamp.getTime():null;
        }

        @Override
        public Long getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
            Timestamp sqlTimestamp = rs.getTimestamp(columnIndex);
            return sqlTimestamp != null? sqlTimestamp.getTime():null;
        }

        @Override
        public Long getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
            Timestamp sqlTimestamp = cs.getTimestamp(columnIndex);
            return sqlTimestamp != null? sqlTimestamp.getTime():null;
        }
}
