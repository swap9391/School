package com.exa.mydemoapp.mapper;

import android.database.Cursor;

import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.database.DbMapper;
import com.exa.mydemoapp.model.UserModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserMapper extends DbMapper<UserModel> {

    @Override
    protected Collection<UserModel> doMap(Cursor rs) {
        List<UserModel> lst = new ArrayList<>();
        while (rs.moveToNext()) {
            /*   public static final String sel_student_data = "Select localId," +
            " pkeyId," +
            "username," +
            "password," +
            "userType," +
            "firstName," +
            "middleName," +
            "lastName," +
            "profilePicUrl," +
            "email," +
            "contactNumber," +
            "busRoute" +
            " From user_data Where 1=1";*/
            UserModel bean = new UserModel();
            bean.setLocalId(getInt(rs, 0));
            bean.setPkeyId(getString(rs, 1));
            bean.setUsername(getString(rs, 2));
            bean.setPassword(getString(rs, 3));
            bean.setUserType(getString(rs, 4));
            bean.setFirstName(getString(rs, 5));
            bean.setMiddleName(getString(rs, 6));
            bean.setLastName(getString(rs, 7));
            bean.setProfilePicUrl(getString(rs, 8));
            bean.setEmail(getString(rs, 9));
            bean.setContactNumber(getString(rs, 10));
            bean.setBusRoute(getString(rs, 11));
            bean.setSessionKey(getString(rs, 12));
            lst.add(bean);
        }
        return lst;
    }
}
