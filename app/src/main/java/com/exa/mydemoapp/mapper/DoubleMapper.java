package com.exa.mydemoapp.mapper;

import android.database.Cursor;

import com.exa.mydemoapp.database.DbMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DoubleMapper extends DbMapper<Double> {

	@Override
	protected Collection<Double> doMap(Cursor rs) {
		List<Double> lst = new ArrayList<Double>();
		while (rs.moveToNext()) {
			lst.add(rs.getDouble(0));
		}
		return lst;
	}
}
