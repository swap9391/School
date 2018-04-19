package com.exa.mydemoapp.mapper;

import android.database.Cursor;

import com.exa.mydemoapp.database.DbMapper;
import com.exa.mydemoapp.model.DropdownMasterModel;
import com.exa.mydemoapp.model.DropdownMasterModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DropDownMapper extends DbMapper<DropdownMasterModel> {

    @Override
    protected Collection<DropdownMasterModel> doMap(Cursor rs) {
        List<DropdownMasterModel> lst = new ArrayList<>();
        while (rs.moveToNext()) {
            /*  public static final String sel_dropdown_data = "Select localId," +
            " pkeyId," +
            "dropdownType," +
            "serverValue," +
            "dropdownValue" +
            " From dropdown_data Where 1=1";*/
            DropdownMasterModel bean = new DropdownMasterModel();
            bean.setLocalId(getInt(rs, 0));
            bean.setPkeyId(getString(rs, 1));
            bean.setDropdownType(getString(rs, 2));
            bean.setServerValue(getString(rs, 3));
            bean.setDropdownValue(getString(rs, 4));

            lst.add(bean);
        }
        return lst;
    }
}