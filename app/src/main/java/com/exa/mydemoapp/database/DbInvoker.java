package com.exa.mydemoapp.database;

import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.exa.mydemoapp.mapper.DropDownMapper;
import com.exa.mydemoapp.mapper.UserMapper;
import com.exa.mydemoapp.model.DropdownMasterModel;
import com.exa.mydemoapp.model.UserModel;


public class DbInvoker {
    // Database fields
    private SQLiteDatabase database;
    private DBService dbHelper;

    public static final String DEFAULT_DATE_FORMAT = "yyyyMMdd";
    public static final String DEFAULT_TIME_FORMAT = "HHmm";

    private final Context context;

    public DbInvoker(Context context) {
        this.context = context;
        dbHelper = new DBService(context);
        open();
    }

    public boolean isOpen() throws SQLException {
        return dbHelper.getWritableDatabase().isOpen()
                || dbHelper.getReadableDatabase().isOpen();
    }

    public void open() throws SQLException {

        database = dbHelper.getWritableDatabase();

    }


    public void executeSQL(String sql) {
        database.execSQL(sql);
    }

    public void executeSQL(String sql, String[] params) {
        database.execSQL(sql, params);
    }

    public Cursor executeSelect(String sql, String[] params) {
        ensure();
        Cursor rs = null;
        Log.d("aapi", "Executing " + sql);
        rs = database.rawQuery(sql, params);
        // Log.i("Db Service", "Sql:" + sql + " Result " + rs.getCount());
        return rs;
    }

    public <T> Collection<T> executeSelect(String sql, String[] params,
                                           DbMapper<T> mapper) {
        open();

        Cursor rs = null;
        try {
            Log.d("aapi", "Executing " + sql);
            rs = database.rawQuery(sql, null);
            return mapper.map(rs);
        } finally {
            if (rs != null) {
                rs.close();
            }
        }

    }

    public <T> T executeSelectOne(String sql, String[] params,
                                  DbMapper<T> mapper) {
        Collection<T> result = executeSelect(sql, params, mapper);
        if (!result.isEmpty()) {
            return result.iterator().next();
        }
        return null;
    }

    private void ensure() {
        if (!database.isOpen()) {
            this.dbHelper = new DBService(context);
            open();
        }
    }

    public void close() {
        dbHelper.close();
    }

    public long executeCreate(ContentHolder holder) {
        ensure();
        long id;
        id = database.insert(holder.getTable(), null, holder.getValues());
        return id;
    }

    public int executeUpdate(ContentHolder holder) {
        return database.update(holder.getTable(), holder.getValues(),
                holder.getWhereClause(), holder.getWhereArgs());

    }

    public void executeDelete(ContentHolder holder) {
        database.delete(holder.getTable(), holder.getWhereClause(),
                holder.getWhereArgs());
    }

    private void insertUpdateBean(BasicBean bean) {
        ContentHolder holder = new ContentHolder();
        bean.dbBinding(holder);
        if (bean.getLocalId() == null || bean.getLocalId() <= 0) {
            long id = executeCreate(holder);
            bean.setLocalId((int) id);
        } else {
            String[] params = {"" + bean.getLocalId()};
            holder.setWhereClause("localId=?");
            holder.setWhereArgs(params);
            executeUpdate(holder);
            // update
        }

    }

    // USer
    public void insertUpdateUser(UserModel bean) {
        insertUpdateBean(bean);
    }

    public List<UserModel> getStudentListByClass(String className) {
        String sql = SQLs.sel_student_data + " and className=" + "'" + className + "'" + " ORDER BY localId ASC";
        String[] params = {""};
        return (List<UserModel>) executeSelect(sql, params,
                new UserMapper());
    }

    public List<UserModel> getUserList() {
        String sql = SQLs.sel_student_data + " ORDER BY localId ASC";
        String[] params = {""};
        return (List<UserModel>) executeSelect(sql, params,
                new UserMapper());
    }

    public List<UserModel> getUserListByStudent(String className) {
        String sql = SQLs.sel_student_data + " and userType='STUDENT' and className='" + className + "' ORDER BY localId ASC";
        String[] params = {""};
        return (List<UserModel>) executeSelect(sql, params,
                new UserMapper());
    }

    public UserModel getStudentById(String studentId) {
        String[] params = {""};
        String sql = SQLs.sel_student_data + " and pkeyId='" + studentId + "'";
        return executeSelectOne(sql, params,
                new UserMapper());
    }

    /*public List<IdValue> getCodeValue(int codeTypeId) {
        String sql = SQLs.sel_class_name+" and pkeyId="+codeTypeId;
        String[] params = new String[] { ""  };
        return (List<IdValue>) executeSelect(sql, params, new IdValueMapper());
    }*/


    public void deleteStudents() {
        String[] params = new String[0];
        String sql = SQLs.del_student;
        executeSQL(sql, params);
    }

    //DropDown
    public void insertUpdateDropDown(DropdownMasterModel bean) {
        insertUpdateBean(bean);
    }

    public List<DropdownMasterModel> getDropDownList() {
        String sql = SQLs.sel_dropdown_data + " ORDER BY localId ASC";
        String[] params = {""};
        return (List<DropdownMasterModel>) executeSelect(sql, params,
                new DropDownMapper());
    }

    public List<DropdownMasterModel> getDropDownByType(String type) {
        String[] params = {""};
        String sql = SQLs.sel_dropdown_data + " AND dropdownType='" + type + "'";
        return (List<DropdownMasterModel>) executeSelect(sql, params,
                new DropDownMapper());
    }

    public List<DropdownMasterModel> getDropDownExceptValue(String type, String value) {
        String[] params = {""};
        String sql = SQLs.sel_dropdown_data + " AND dropdownType='" + type + "' AND dropdownValue != '" + value + "'";
        return (List<DropdownMasterModel>) executeSelect(sql, params,
                new DropDownMapper());
    }

    public void deleteDropDown() {
        String[] params = new String[0];
        String sql = SQLs.del_drop_down;
        executeSQL(sql, params);
    }

    /*
    public List<DivisionName> getDivisionNameByClass(int classId) {
        String sql = SQLs.sel_division_name+" and classpkeyId="+classId+" ORDER BY division_name ASC";
        String[] params = { "" };
        return (List<DivisionName>) executeSelect(sql, params,
                new DivisionNameMapper());
    }


    public DivisionName getDivisionNameById(int id) {
        String[] params = { "" };
        String sql = SQLs.sel_division_name+" and pkeyId="+id;
        return  executeSelectOne(sql, params,
                new DivisionNameMapper());
    }
*/

/*
    public void deleteDivisionById(int Id) {
        String[] params = new String[0];
        String sql = SQLs.del_division_name+" WHERE pkeyId="+Id;
        executeSQL(sql, params);

    }
*/


/*
    public void insertUpdateUser(UserBean bean,Context context,Bitmap bmp) {
        insertUpdateBean(bean);
        bean.setLocalPath("/sdcard/CoolSchool/"+ bean.getImageFolder().trim()+"/school_image"+ bean.getId() +".jpg");
        insertUpdateBean(bean);
        ImageUtills imageUtills = new ImageUtills();
        String filename=bean.getLocalPath().substring(bean.getLocalPath().lastIndexOf("/")+1);
        imageUtills.saveToInternalStorage(bmp,context,"Profile",filename);

    }

    public List<UserBean> getUserName() {
        String sql = SQLs.sel_user_data+" ORDER BY name ASC";
        String[] params = { "" };
        return (List<UserBean>) executeSelect(sql, params,
                new UserMapper());
    }

    public UserBean getUserByName(String name) {
        String[] params = { "" };
        String sql = SQLs.sel_user_data+" and name = "+name;
        return  executeSelectOne(sql, params,
                new UserMapper());
    }

    public UserBean getUserById(int id) {
        String[] params = { "" };
        String sql = SQLs.sel_user_data+" and pkeyId="+id+" and user_type='TEACHER'";
        return  executeSelectOne(sql, params,
                new UserMapper());
    }



    public void deleteUserById(int Id) {
        String[] params = new String[0];
        String sql = SQLs.del_user_data+" WHERE pkeyId="+Id;
        executeSQL(sql, params);

    }


    public List<AttendanceBean> getAttendance() {
        String sql = SQLs.sel_attendance;
        String[] params = { "" };
        return (List<AttendanceBean>) executeSelect(sql, params,
                new AttendanceMapper());
    }

    public AttendanceBean getAttendanceById(int id) {
        String[] params = { "" };
        String sql = SQLs.sel_attendance+" and pkeyId="+id;
        return  executeSelectOne(sql, params,
                new AttendanceMapper());
    }
    public AttendanceBean getAttendanceByUserId(int userId,String date) {
        String[] params = { "" };
        String sql = SQLs.sel_attendance+" and userpkeyId="+userId+" and attendance_date = "+date;
        return  executeSelectOne(sql, params,
                new AttendanceMapper());
    }

    public List<AttendanceBean> getAttendanceByUser(int userId) {
        String[] params = { "" };
        String sql = SQLs.sel_attendance+" and userpkeyId="+userId;
        return (List<AttendanceBean>) executeSelect(sql, params,
                new AttendanceMapper());
    }
    public List<ImageFileBean> getImageFileByEvent(String event) {
        String[] params = { "" };
        String sql = SQLs.sel_image_file + " AND event_name = " + "'"+event+"'";
        return (List<ImageFileBean>) executeSelect(sql, params,
                new ImageFileMapper());
    }

    public List<ImageFileBean> getImageFile() {
        String[] params = { "" };
        String sql = SQLs.sel_image_file + " GROUP BY event_name";
        return (List<ImageFileBean>) executeSelect(sql, params,
                new ImageFileMapper());
    }

    public ImageFileBean getImageFileById( int id) {
        String[] params = { "" };
        String sql = SQLs.sel_image_file + " and pkeyId="+id;
        return (ImageFileBean) executeSelectOne(sql, params,
                new ImageFileMapper());
    }


    public void deleteIamgeById(int Id) {
        String[] params = new String[0];
        String sql = SQLs.del_image_file+" WHERE pkeyId="+Id;
        executeSQL(sql, params);

    }


    public void deleteAttendanceById(int Id) {
        String[] params = new String[0];
        String sql = SQLs.del_attendance+" WHERE pkeyId="+Id;
        executeSQL(sql, params);

    }

    public void deleteAttendanceByUser(int Id,String date) {
        String[] params = new String[0];
        String sql = SQLs.del_attendance+" WHERE userpkeyId="+Id+" and attendance_date = "+date;
        executeSQL(sql, params);

    }
*/


}
