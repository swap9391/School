package com.exa.mydemoapp.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import android.database.Cursor;

import com.exa.mydemoapp.database.DbMapper;


public class DateMapper extends DbMapper<Date> {

	@Override
	protected Collection<Date> doMap(Cursor rs) {
		List<Date> lst = new ArrayList<Date>();
		while (rs.moveToNext()) {
			lst.add(getDate(rs,0));
		}
		return lst;
	}
}
