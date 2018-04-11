package com.exa.mydemoapp.mapper;

import android.database.Cursor;

import com.exa.mydemoapp.database.DbMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LoginMapper extends DbMapper<LoginDataModel> {

    @Override
    protected Collection<LoginDataModel> doMap(Cursor rs) {
        List<LoginDataModel> lst = new ArrayList<>();
        while (rs.moveToNext()) {
            /*public static final String sel_student_data = "Select localId, _id,registrationDate,schoolName,className,divisionId," +
                    "registrationId,studentName,studentAddress,studentUserName,studentPassword,userType,studentBloodGrp," +
                    "gender,totalFees,installmentType,installment1,installment2,installment3,rollNumber,contactNumber," +
                    "dateInsvestment2,dateInsvestment3,subscribed,visiblity" +
                    " From student_data Where 1=1";*/
            LoginDataModel bean = new LoginDataModel();
            bean.setLocalId(getInt(rs, 0));
            bean.setId(getInt(rs, 1));
            bean.setUserName(getString(rs, 2));
            bean.setPassword(getString(rs, 3));
            lst.add(bean);
        }
        return lst;
    }
}
