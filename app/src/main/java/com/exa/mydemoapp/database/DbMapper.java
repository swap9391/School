package com.exa.mydemoapp.database;

import java.util.Collection;
import java.util.Date;

import android.database.Cursor;

import com.exa.mydemoapp.Common.CommonUtils;

public abstract class DbMapper<T> {

	public Collection<T> map(Cursor rs) {
		return doMap(rs);
	}

	protected abstract Collection<T> doMap(Cursor rs);

	public static Date getDate(Cursor rs, int index) {
		String str = rs.getString(index);
		return CommonUtils.toDate(str);
	}

	public static Date getTime(Cursor rs, int index) {
		String str = rs.getString(index);
		return CommonUtils.toTime(str);
	}

	public static Integer getInt(Cursor rs, int columnIndex) {
		return getInt(rs, columnIndex, null);
	}

	public static boolean getBoolean(Cursor rs, int columnIndex) {
		return getInt(rs, columnIndex, 0) == 1;
	}

	public static Integer getInt(Cursor rs, int columnIndex,
			Integer defaultValue) {
		String value = rs.getString(columnIndex);
		return CommonUtils.asInt(value, defaultValue);
	}

	public static Double getDouble(Cursor rs, int columnIndex,
			Double defaultValue) {
		String value = rs.getString(columnIndex);
		return CommonUtils.asDouble(value, defaultValue);
	}

	public static String getString(Cursor rs, int columnIndex) {
		String value = rs.getString(columnIndex);
		if (!CommonUtils.isEmpty(value)) {
			return value;
		}
		return null;
	}


	public static Float getFloat(Cursor rs, int columnIndex) {
		String value = rs.getString(columnIndex);
		if (!CommonUtils.isEmpty(value)) {
			try {
				return Float.parseFloat(value);
			} catch (Exception ex) {
				return null;
			}
		}
		return null;
	}
}
