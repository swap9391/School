package com.exa.mydemoapp.mapper;

import android.database.Cursor;

import com.exa.mydemoapp.Common.CommonUtils;
import com.exa.mydemoapp.database.DbMapper;
import com.exa.mydemoapp.model.StudentModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StudentMapper extends DbMapper<StudentModel> {

    @Override
    protected Collection<StudentModel> doMap(Cursor rs) {
        List<StudentModel> lst = new ArrayList<>();
        while (rs.moveToNext()) {
            /* public static final String sel_student_data = "Select localId, _id,registrationDate,schoolName,className,divisionId," +
            "registrationId,studentName,studentAddress,studentUserName,studentPassword,userType,studentBloodGrp," +
            "gender,totalFees,installmentType,installment1,installment2,installment3,rollNumber,contactNumber," +
            "dateInsvestment2,dateInsvestment3,subscribed,visiblity" +
            " From student_data Where 1=1";*/
            StudentModel bean = new StudentModel();
            bean.setLocalId(getInt(rs, 0));
            bean.setId(getInt(rs, 1));
            bean.setDateStamp(getString(rs, 2));
            bean.setSchoolName(getString(rs, 3));
            bean.setClassName(getString(rs, 4));
            bean.setDivision(getString(rs, 5));
            bean.setRegistrationId(getString(rs, 6));
            bean.setStudentName(getString(rs, 7));
            bean.setStudentAddress(getString(rs, 8));
            bean.setStudentUserName(getString(rs, 9));
            bean.setStudentPassword(getString(rs, 10));
            bean.setUserType(getString(rs, 11));
            bean.setStudentBloodGrp(getString(rs, 12));
            bean.setGender(getString(rs, 13));
            bean.setTotalFees(CommonUtils.asInt(getString(rs, 14), 0));
            bean.setInstallmentType(getString(rs, 15));
            bean.setInstallment1(getString(rs, 16));
            bean.setInstallment2(getString(rs, 17));
            bean.setInstallment3(getString(rs, 18));
            bean.setRollNumber(getString(rs, 19));
            bean.setContactNumber(getString(rs, 20));
            bean.setDateInsvestment2(getString(rs, 21));
            bean.setDateInsvestment3(getString(rs, 22));
            bean.setSubscribed(getString(rs, 23));
            bean.setVisiblity(getString(rs, 24));
            lst.add(bean);
        }
        return lst;
    }
}
