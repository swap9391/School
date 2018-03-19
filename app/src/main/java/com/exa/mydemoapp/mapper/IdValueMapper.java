package com.exa.mydemoapp.mapper;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.database.Cursor;

/*
public class IdValueMapper extends DbMapper<IdValue> {

	@Override
	protected Collection<IdValue> doMap(Cursor rs) {
		List<IdValue> lst = new ArrayList<IdValue>();
		while (rs.moveToNext()) {
			if(rs.getInt(0) >0){
				IdValue cv = new IdValue(rs.getInt(0),rs.getString(1));
				lst.add(cv);
			}
		}
		return lst;
	}
}*/
