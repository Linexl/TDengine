/***************************************************************************
 * Copyright (c) 2019 TAOS Data, Inc. <jhtao@taosdata.com>
 *
 * This program is free software: you can use, redistribute, and/or modify
 * it under the terms of the GNU Affero General Public License, version 3
 * or later ("AGPL"), as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *****************************************************************************/
package com.taosdata.jdbc;

import com.taosdata.jdbc.utils.NullType;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;

public class TSDBResultSetRowData {

    private ArrayList<Object> data;
    private int colSize;

    public TSDBResultSetRowData(int colSize) {
        this.colSize = colSize;
        this.clear();
    }

    public void clear() {
        if (this.data != null) {
            this.data.clear();
        }
        if (this.colSize == 0) {
            return;
        }
        this.data = new ArrayList<>(colSize);
        this.data.addAll(Collections.nCopies(this.colSize, null));
    }

    public boolean wasNull(int col) {
        return data.get(col - 1) == null;
    }

    /**
     * !!! this method is invoked by JNI method and the index start from 0 in C implementations
     */
    public void setBoolean(int col, boolean value) {
        data.set(col, value);
    }

    public boolean getBoolean(int col, int nativeType) throws SQLException {
        Object obj = data.get(col - 1);

        switch (nativeType) {
            case TSDBConstants.TSDB_DATA_TYPE_BOOL:
                return (Boolean) obj;
            case TSDBConstants.TSDB_DATA_TYPE_TINYINT:
                return ((Byte) obj) == 1 ? Boolean.TRUE : Boolean.FALSE;
            case TSDBConstants.TSDB_DATA_TYPE_SMALLINT:
                return ((Short) obj) == 1 ? Boolean.TRUE : Boolean.FALSE;
            case TSDBConstants.TSDB_DATA_TYPE_INT:
                return ((Integer) obj) == 1 ? Boolean.TRUE : Boolean.FALSE;
            case TSDBConstants.TSDB_DATA_TYPE_BIGINT:
                return ((Long) obj) == 1L ? Boolean.TRUE : Boolean.FALSE;
            case TSDBConstants.TSDB_DATA_TYPE_BINARY:
            case TSDBConstants.TSDB_DATA_TYPE_NCHAR: {
                return obj.toString().contains("1");
            }
            default:
                return false;
        }
    }

    /**
     * !!! this method is invoked by JNI method and the index start from 0 in C implementations
     */
    public void setByte(int col, byte value) {
        data.set(col, value);
    }

    /**
     * !!! this method is invoked by JNI method and the index start from 0 in C implementations
     */
    public void setShort(int col, short value) {
        data.set(col, value);
    }

    /**
     * !!! this method is invoked by JNI method and the index start from 0 in C implementations
     */
    public void setInt(int col, int value) {
        data.set(col, value);
    }

    public int getInt(int col, int nativeType) throws SQLException {
        Object obj = data.get(col - 1);
        if (obj == null)
            return NullType.getIntNull();

        switch (nativeType) {
            case TSDBConstants.TSDB_DATA_TYPE_BOOL:
                return Boolean.TRUE.equals(obj) ? 1 : 0;
            case TSDBConstants.TSDB_DATA_TYPE_TINYINT:
                return (Byte) obj;
            case TSDBConstants.TSDB_DATA_TYPE_SMALLINT:
                return (Short) obj;
            case TSDBConstants.TSDB_DATA_TYPE_INT:
                return (Integer) obj;
            case TSDBConstants.TSDB_DATA_TYPE_BIGINT:
            case TSDBConstants.TSDB_DATA_TYPE_TIMESTAMP:
                return ((Long) obj).intValue();
            case TSDBConstants.TSDB_DATA_TYPE_NCHAR:
            case TSDBConstants.TSDB_DATA_TYPE_BINARY:
                return Integer.parseInt((String) obj);
            case TSDBConstants.TSDB_DATA_TYPE_UTINYINT: {
                Byte value = (byte) obj;
                if (value < 0)
                    throw TSDBError.createSQLException(TSDBErrorNumbers.ERROR_NUMERIC_VALUE_OUT_OF_RANGE);
                return value;
            }
            case TSDBConstants.TSDB_DATA_TYPE_USMALLINT: {
                short value = (short) obj;
                if (value < 0)
                    throw TSDBError.createSQLException(TSDBErrorNumbers.ERROR_NUMERIC_VALUE_OUT_OF_RANGE);
                return value;
            }
            case TSDBConstants.TSDB_DATA_TYPE_UINT: {
                int value = (int) obj;
                if (value < 0)
                    throw TSDBError.createSQLException(TSDBErrorNumbers.ERROR_NUMERIC_VALUE_OUT_OF_RANGE);
                return value;
            }
            case TSDBConstants.TSDB_DATA_TYPE_UBIGINT: {
                long value = (long) obj;
                if (value < 0)
                    throw TSDBError.createSQLException(TSDBErrorNumbers.ERROR_NUMERIC_VALUE_OUT_OF_RANGE);
                return Long.valueOf(value).intValue();
            }
            case TSDBConstants.TSDB_DATA_TYPE_FLOAT:
                return ((Float) obj).intValue();
            case TSDBConstants.TSDB_DATA_TYPE_DOUBLE:
                return ((Double) obj).intValue();
            default:
                return 0;
        }
    }

    /**
     * !!! this method is invoked by JNI method and the index start from 0 in C implementations
     */
    public void setLong(int col, long value) {
        data.set(col, value);
    }

    public long getLong(int col, int nativeType) throws SQLException {
        Object obj = data.get(col - 1);
        if (obj == null) {
            return NullType.getBigIntNull();
        }

        switch (nativeType) {
            case TSDBConstants.TSDB_DATA_TYPE_BOOL:
                return Boolean.TRUE.equals(obj) ? 1 : 0;
            case TSDBConstants.TSDB_DATA_TYPE_TINYINT:
                return (Byte) obj;
            case TSDBConstants.TSDB_DATA_TYPE_SMALLINT:
                return (Short) obj;
            case TSDBConstants.TSDB_DATA_TYPE_INT:
                return (Integer) obj;
            case TSDBConstants.TSDB_DATA_TYPE_BIGINT:
            case TSDBConstants.TSDB_DATA_TYPE_TIMESTAMP:
                return (Long) obj;
            case TSDBConstants.TSDB_DATA_TYPE_NCHAR:
            case TSDBConstants.TSDB_DATA_TYPE_BINARY:
                return Long.parseLong((String) obj);
            case TSDBConstants.TSDB_DATA_TYPE_UTINYINT: {
                Byte value = (byte) obj;
                if (value < 0)
                    throw TSDBError.createSQLException(TSDBErrorNumbers.ERROR_NUMERIC_VALUE_OUT_OF_RANGE);
                return value;
            }
            case TSDBConstants.TSDB_DATA_TYPE_USMALLINT: {
                short value = (short) obj;
                if (value < 0)
                    throw TSDBError.createSQLException(TSDBErrorNumbers.ERROR_NUMERIC_VALUE_OUT_OF_RANGE);
                return value;
            }
            case TSDBConstants.TSDB_DATA_TYPE_UINT: {
                int value = (int) obj;
                if (value < 0)
                    throw TSDBError.createSQLException(TSDBErrorNumbers.ERROR_NUMERIC_VALUE_OUT_OF_RANGE);
                return value;
            }
            case TSDBConstants.TSDB_DATA_TYPE_UBIGINT: {
                long value = (long) obj;
                if (value < 0)
                    throw TSDBError.createSQLException(TSDBErrorNumbers.ERROR_NUMERIC_VALUE_OUT_OF_RANGE);
                return value;
            }
            case TSDBConstants.TSDB_DATA_TYPE_FLOAT:
                return ((Float) obj).longValue();
            case TSDBConstants.TSDB_DATA_TYPE_DOUBLE:
                return ((Double) obj).longValue();
            default:
                return 0;
        }
    }

    /**
     * !!! this method is invoked by JNI method and the index start from 0 in C implementations
     */
    public void setFloat(int col, float value) {
        data.set(col, value);
    }

    public float getFloat(int col, int nativeType) {
        Object obj = data.get(col - 1);
        if (obj == null)
            return NullType.getFloatNull();

        switch (nativeType) {
            case TSDBConstants.TSDB_DATA_TYPE_BOOL:
                return Boolean.TRUE.equals(obj) ? 1 : 0;
            case TSDBConstants.TSDB_DATA_TYPE_FLOAT:
                return (Float) obj;
            case TSDBConstants.TSDB_DATA_TYPE_DOUBLE:
                return ((Double) obj).floatValue();
            case TSDBConstants.TSDB_DATA_TYPE_TINYINT:
                return (Byte) obj;
            case TSDBConstants.TSDB_DATA_TYPE_SMALLINT:
                return (Short) obj;
            case TSDBConstants.TSDB_DATA_TYPE_INT:
                return (Integer) obj;
            case TSDBConstants.TSDB_DATA_TYPE_TIMESTAMP:
            case TSDBConstants.TSDB_DATA_TYPE_BIGINT:
                return (Long) obj;
            default:
                return NullType.getFloatNull();
        }
    }

    /**
     * !!! this method is invoked by JNI method and the index start from 0 in C implementations
     */
    public void setDouble(int col, double value) {
        data.set(col, value);
    }

    public double getDouble(int col, int nativeType) {
        Object obj = data.get(col - 1);
        if (obj == null)
            return NullType.getDoubleNull();

        switch (nativeType) {
            case TSDBConstants.TSDB_DATA_TYPE_BOOL:
                return Boolean.TRUE.equals(obj) ? 1 : 0;
            case TSDBConstants.TSDB_DATA_TYPE_FLOAT:
                return (Float) obj;
            case TSDBConstants.TSDB_DATA_TYPE_DOUBLE:
                return (Double) obj;
            case TSDBConstants.TSDB_DATA_TYPE_TINYINT:
                return (Byte) obj;
            case TSDBConstants.TSDB_DATA_TYPE_SMALLINT:
                return (Short) obj;
            case TSDBConstants.TSDB_DATA_TYPE_INT:
                return (Integer) obj;
            case TSDBConstants.TSDB_DATA_TYPE_TIMESTAMP:
            case TSDBConstants.TSDB_DATA_TYPE_BIGINT:
                return (Long) obj;
            default:
                return NullType.getDoubleNull();
        }
    }

    /**
     * !!! this method is invoked by JNI method and the index start from 0 in C implementations
     */
    public void setString(int col, String value) {
        data.set(col, value);
    }

    /**
     * !!! this method is invoked by JNI method and the index start from 0 in C implementations
     */
    public void setByteArray(int col, byte[] value) {
        data.set(col, value);
    }

    public String getString(int col, int nativeType) {
        Object obj = data.get(col - 1);
        if (obj == null)
            return null;

        switch (nativeType) {
            case TSDBConstants.TSDB_DATA_TYPE_UTINYINT: {
                Byte value = new Byte(String.valueOf(obj));
                if (value >= 0)
                    return value.toString();
                return Integer.toString(value & 0xff);
            }
            case TSDBConstants.TSDB_DATA_TYPE_USMALLINT: {
                Short value = new Short(String.valueOf(obj));
                if (value >= 0)
                    return value.toString();
                return Integer.toString(value & 0xffff);
            }
            case TSDBConstants.TSDB_DATA_TYPE_UINT: {
                Integer value = new Integer(String.valueOf(obj));
                if (value >= 0)
                    return value.toString();
                return Long.toString(value & 0xffffffffl);
            }
            case TSDBConstants.TSDB_DATA_TYPE_UBIGINT: {
                Long value = new Long(String.valueOf(obj));
                if (value >= 0)
                    return value.toString();
                long lowValue = value & 0x7fffffffffffffffL;
                return BigDecimal.valueOf(lowValue).add(BigDecimal.valueOf(Long.MAX_VALUE)).add(BigDecimal.valueOf(1)).toString();
            }
            case TSDBConstants.TSDB_DATA_TYPE_BINARY:
            case TSDBConstants.TSDB_DATA_TYPE_NCHAR:
                return (String) obj;
            default:
                return String.valueOf(obj);
        }
    }

    /**
     * !!! this method is invoked by JNI method and the index start from 0 in C implementations
     */
    public void setTimestamp(int col, long ts) {
        //TODO: this implementation contains logical error
        // when precision is us the (long ts) is 16 digital number
        // when precision is ms, the (long ts) is 13 digital number
        // we need a JNI function like this:
        //      public void setTimestamp(int col, long epochSecond, long nanoAdjustment)
        if (ts < 1_0000_0000_0000_0L) {
            data.set(col, new Timestamp(ts));
        } else {
            long epochSec = ts / 1000_000l;
            long nanoAdjustment = ts % 1000_000l * 1000l;
            Timestamp timestamp = Timestamp.from(Instant.ofEpochSecond(epochSec, nanoAdjustment));
            data.set(col, timestamp);
        }
    }

    public Timestamp getTimestamp(int col, int nativeType) {
        Object obj = data.get(col - 1);
        if (obj == null)
            return null;
        switch (nativeType) {
            case TSDBConstants.TSDB_DATA_TYPE_BIGINT:
                return new Timestamp((Long) obj);
            default:
                return (Timestamp) obj;
        }
    }

    public Object get(int col) {
        return data.get(col - 1);
    }

}
