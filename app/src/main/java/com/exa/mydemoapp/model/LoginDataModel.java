package com.exa.mydemoapp.model;

import com.exa.mydemoapp.database.BasicBean;
import com.exa.mydemoapp.database.ContentHolder;

import java.io.Serializable;

/**
 * Created by midt-078 on 20/3/18.
 */

public class LoginDataModel extends BasicBean implements Serializable {

    private String userName;
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void dbBinding(ContentHolder holder) {
        super.dbBinding(holder);
        holder.setTable("login_data");
        holder.getValues().put("_id",getId());
        holder.getValues().put("username",getUserName());
        holder.getValues().put("password",getPassword());
    }
}
