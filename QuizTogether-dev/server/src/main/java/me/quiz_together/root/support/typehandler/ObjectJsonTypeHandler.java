package me.quiz_together.root.support.typehandler;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectJsonTypeHandler<T extends Object> extends BaseTypeHandler<T> {

    private Class<T> type;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
    }


    public ObjectJsonTypeHandler(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, T t, JdbcType jdbcType) throws SQLException {
        try {
            if (jdbcType == null) {
                preparedStatement.setString(i, toJson(t));
            } else {
                preparedStatement.setObject(i, toJson(t), jdbcType.TYPE_CODE);
            }
        } catch (IOException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }

    @Override
    public T getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return valueOf(resultSet.getBytes(s));
    }

    @Override
    public T getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return valueOf(resultSet.getBytes(i));
    }

    @Override
    public T getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return valueOf(callableStatement.getBytes(i));
    }

    @SuppressWarnings("rawtypes")
    protected String toJson(T parameter) throws JsonGenerationException, JsonMappingException, IOException {
        if (parameter == null)
            return null;
        if (parameter instanceof Collection<?>) {
            if (CollectionUtils.isEmpty((Collection)parameter)) {
                return null;
            }
        }
        return OBJECT_MAPPER.writeValueAsString(parameter);
    }

    private T valueOf(byte[] data) throws SQLException {
        if (StringUtils.isEmpty(data))
            return null;
        try {
            return OBJECT_MAPPER.readValue(data, type);
        } catch (IOException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }
}
