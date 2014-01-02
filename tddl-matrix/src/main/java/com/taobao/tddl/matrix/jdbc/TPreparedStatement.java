package com.taobao.tddl.matrix.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.taobao.tddl.common.jdbc.ParameterContext;
import com.taobao.tddl.common.jdbc.ParameterMethod;
import com.taobao.tddl.common.model.SqlType;
import com.taobao.tddl.executor.common.ExecutionContext;
import com.taobao.tddl.matrix.jdbc.utils.PreParser;

import com.taobao.tddl.common.utils.logger.Logger;
import com.taobao.tddl.common.utils.logger.LoggerFactory;

/**
 * @author mengshi.sunmengshi 2013-11-22 下午3:26:18
 * @since 5.1.0
 */
public class TPreparedStatement extends TStatement implements PreparedStatement {

    private static final Logger              log               = LoggerFactory.getLogger(TPreparedStatement.class);

    // 参数列表到参数上下文的映射 如 1:name 2：'2011-11-11'
    protected Map<Integer, ParameterContext> parameterSettings = new HashMap<Integer, ParameterContext>();

    public TPreparedStatement(TDataSource ds, TConnection conn, String sql, ExecutionContext ec){
        super(ds, conn, sql, ec);
    }

    public ResultSet executeQuery() throws SQLException {
        checkClosed();
        ensureResultSetIsEmpty();
        this.currentResultSet = this.conn.executeSQL(sql, parameterSettings, this, extraCmd, this.executionContext);
        return currentResultSet;
    }

    public int executeUpdate() throws SQLException {

        checkClosed();
        ensureResultSetIsEmpty();
        this.currentResultSet = this.conn.executeSQL(sql, parameterSettings, this, extraCmd, this.executionContext);
        int affectRows = ((TResultSet) this.currentResultSet).getAffectRows();
        this.currentResultSet.close();
        return affectRows;
    }

    // jdbc规范: 返回true表示executeQuery，false表示executeUpdate
    public boolean execute() throws SQLException {
        if (log.isDebugEnabled()) {
            log.debug("invoke execute, sql = " + sql);
        }

        SqlType sqlType = PreParser.getSqlType(sql);
        if (sqlType == SqlType.SELECT || sqlType == SqlType.SELECT_FOR_UPDATE
        /**
         * show 不支持吗 || sqlType == SqlType.SHOW
         **/
        ) {
            executeQuery();
            return true;
        } else if (sqlType == SqlType.INSERT || sqlType == SqlType.UPDATE || sqlType == SqlType.DELETE
                   || sqlType == SqlType.REPLACE || sqlType == SqlType.TRUNCATE || sqlType == SqlType.CREATE
                   || sqlType == SqlType.DROP || sqlType == SqlType.LOAD || sqlType == SqlType.MERGE) {
            super.updateCount = executeUpdate();
            return false;
        } else {
            throw new SQLException("only select, insert, update, delete,truncate,create,drop,load,merge sql is supported");
        }
    }

    public void setShort(int parameterIndex, short x) throws SQLException {
        parameterSettings.put(parameterIndex - 1, new ParameterContext(ParameterMethod.setShort, new Object[] {
                parameterIndex - 1, x }));

    }

    // 数据库从0开始，jdbc规范从1开始，jdbc到数据库要减一
    public void setInt(int parameterIndex, int x) throws SQLException {
        parameterSettings.put(parameterIndex - 1, new ParameterContext(ParameterMethod.setInt, new Object[] {
                parameterIndex - 1, x }));
    }

    public void setLong(int parameterIndex, long x) throws SQLException {
        parameterSettings.put(parameterIndex - 1, new ParameterContext(ParameterMethod.setLong, new Object[] {
                parameterIndex - 1, x }));

    }

    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        parameterSettings.put(parameterIndex - 1, new ParameterContext(ParameterMethod.setBoolean, new Object[] {
                parameterIndex - 1, x }));
    }

    public void setString(int parameterIndex, String x) throws SQLException {

        parameterSettings.put(parameterIndex - 1, new ParameterContext(ParameterMethod.setString, new Object[] {
                parameterIndex - 1, x }));
    }

    public void setFloat(int parameterIndex, float x) throws SQLException {
        parameterSettings.put(parameterIndex - 1, new ParameterContext(ParameterMethod.setFloat, new Object[] {
                parameterIndex - 1, x }));

    }

    public void setDouble(int parameterIndex, double x) throws SQLException {
        parameterSettings.put(parameterIndex - 1, new ParameterContext(ParameterMethod.setDouble, new Object[] {
                parameterIndex - 1, x }));

    }

    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        parameterSettings.put(parameterIndex - 1, new ParameterContext(ParameterMethod.setBytes, new Object[] {
                parameterIndex - 1, x }));

    }

    // 这里ustore底层将date按long存储
    public void setDate(int parameterIndex, Date x) throws SQLException {
        parameterSettings.put(parameterIndex - 1, new ParameterContext(ParameterMethod.setDate1, new Object[] {
                parameterIndex - 1, x }));
    }

    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        throw new SQLException("not support this methord!");

    }

    public void clearParameters() throws SQLException {
        this.parameterSettings.clear();
    }

    public void setNull(int parameterIndex, Object o) throws SQLException {
        parameterSettings.put(parameterIndex - 1, new ParameterContext(ParameterMethod.setNull1, new Object[] {
                parameterIndex - 1, null }));
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        parameterSettings.put(parameterIndex - 1, new ParameterContext(ParameterMethod.setNull1, new Object[] {
                parameterIndex - 1, null }));
    }

    public void setByte(int parameterIndex, byte x) throws SQLException {
        parameterSettings.put(parameterIndex - 1, new ParameterContext(ParameterMethod.setByte, new Object[] {
                parameterIndex - 1, x }));
    }

    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        parameterSettings.put(parameterIndex - 1, new ParameterContext(ParameterMethod.setBigDecimal, new Object[] {
                parameterIndex - 1, x }));
    }

    public void setTime(int parameterIndex, Time x) throws SQLException {
        parameterSettings.put(parameterIndex - 1, new ParameterContext(ParameterMethod.setTime1, new Object[] {
                parameterIndex - 1, x }));
    }

    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        parameterSettings.put(parameterIndex - 1, new ParameterContext(ParameterMethod.setTimestamp1, new Object[] {
                parameterIndex - 1, x }));

    }

    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        parameterSettings.put(parameterIndex - 1, new ParameterContext(ParameterMethod.setObject3, new Object[] {
                parameterIndex - 1, x, targetSqlType }));
    }

    public void setObject(int parameterIndex, Object x) throws SQLException {
        // /*
        // * 对于int long之类的类型，使用反射去调用其专属的setter，其他的加if判断，暂不支持
        // */
        // if (x instanceof java.util.Date)
        // x = new Date(((java.util.Date) x).getTime());
        parameterSettings.put(parameterIndex - 1, new ParameterContext(ParameterMethod.setObject1, new Object[] {
                parameterIndex - 1, x }));
        // if (x == null) {
        // setNull(parameterIndex, x);
        // } else if (x instanceof Integer) {
        // setInt(parameterIndex, (Integer) x);
        // } else if (x instanceof Long) {
        // setLong(parameterIndex, (Long) x);
        // } else if (x instanceof String) {
        // setString(parameterIndex, (String) x);
        // } else if (x instanceof Boolean) {
        // setBoolean(parameterIndex, (Boolean) x);
        // } else if (x instanceof Float) {
        // setFloat(parameterIndex, (Float) x);
        // } else if (x instanceof Double) {
        // setDouble(parameterIndex, (Double) x);
        // } else if (x instanceof byte[]) {
        // setBytes(parameterIndex, (byte[]) x);
        // } else if (x instanceof Date) {
        // setDate(parameterIndex, (Date) x);
        // } else if (x instanceof java.util.Date) {
        // setDate(parameterIndex,new Date(((java.util.Date)x).getTime()));
        // }else
        // {
        // throw new
        // UnsupportedOperationException("the type of "+x+" is not supported by prepare statement , index is "+parameterIndex);
        // }
    }

    public void addBatch() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();

    }

    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();

    }

    public void setRef(int parameterIndex, Ref x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        parameterSettings.put(parameterIndex - 1, new ParameterContext(ParameterMethod.setBlob, new Object[] {
                parameterIndex - 1, x }));
    }

    public void setClob(int parameterIndex, Clob x) throws SQLException {
        parameterSettings.put(parameterIndex - 1, new ParameterContext(ParameterMethod.setClob, new Object[] {
                parameterIndex - 1, x }));

    }

    public void setArray(int parameterIndex, Array x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    /**
     * 不支持
     */
    public ResultSetMetaData getMetaData() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
        // return null;
    }

    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        if (null == cal) {
            parameterSettings.put(parameterIndex - 1, new ParameterContext(ParameterMethod.setTime1, new Object[] {
                    parameterIndex - 1, x }));
        } else {
            parameterSettings.put(parameterIndex - 1, new ParameterContext(ParameterMethod.setTime2, new Object[] {
                    parameterIndex - 1, x }));
        }
    }

    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        parameterSettings.put(parameterIndex - 1, new ParameterContext(ParameterMethod.setTimestamp2, new Object[] {
                parameterIndex - 1, x }));

    }

    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        parameterSettings.put(parameterIndex - 1, new ParameterContext(ParameterMethod.setNull2, new Object[] {
                parameterIndex - 1, sqlType, typeName }));

    }

    public void setURL(int parameterIndex, URL x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();

    }

    /**
     * 用map实现的parameterSettings，未实现ParameterMetaData
     */
    public ParameterMetaData getParameterMetaData() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
        // return null;
    }

    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();

    }

    /**
     * 不支持
     */
    public void setNString(int parameterIndex, String value) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();

    }

    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();

    }

    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();

    }

    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

}