package com.exa.mydemoapp.model;

import com.exa.mydemoapp.database.BasicBean;
import com.exa.mydemoapp.database.ContentHolder;

import java.io.Serializable;

/**
 * Created by midt-078 on 11/4/18.
 */

public class DropdownMasterModel extends BasicBean implements Serializable {
    private String dropdownType;
    private String dropdownValue;
    private String serverValue;

    public String getDropdownType() {
        return dropdownType;
    }

    public void setDropdownType(String dropdownType) {
        this.dropdownType = dropdownType;
    }

    public String getDropdownValue() {
        return dropdownValue;
    }

    public void setDropdownValue(String dropdownValue) {
        this.dropdownValue = dropdownValue;
    }

    public String getServerValue() {
        return serverValue;
    }

    public void setServerValue(String serverValue) {
        this.serverValue = serverValue;
    }

    @Override
    public String toString() {
        return dropdownValue;
    }

    @Override
    public void dbBinding(ContentHolder holder) {
        super.dbBinding(holder);
        holder.setTable("dropdown_data");
        holder.getValues().put("pkeyId", getPkeyId());
        holder.getValues().put("dropdownType", getDropdownType());
        holder.getValues().put("serverValue", getServerValue());
        holder.getValues().put("dropdownValue", getDropdownValue());
    }
}
