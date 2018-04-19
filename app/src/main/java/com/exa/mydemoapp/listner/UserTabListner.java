package com.exa.mydemoapp.listner;

import com.exa.mydemoapp.model.UserModel;

import java.util.List;

public interface UserTabListner {
    public void pageChanged(List<UserModel> listUsers);
}
