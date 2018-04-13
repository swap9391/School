package com.exa.mydemoapp.database;


import com.exa.mydemoapp.Common.CommonUtils;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class BasicBean {
    private Map<String, Object> data;
    private String pkeyId;
    private Integer localId;
    protected String createdBy;
    protected String updatedBy;
    protected long createdAt;
    protected long updatedAt;
    protected boolean isDeleted;
    protected String sessionKey;

    private boolean sync = true;


    public void addData(String key, Object value) {
        if (data == null) {
            data = new HashMap<String, Object>();
        }
        data.put(key, value);
    }

    public Object getData(String key) {
        if (data != null) {
            return data.get(key);
        }
        return null;
    }

    protected Map<String, Object> getData() {
        return this.data;
    }

    public String getPkeyId() {
        return pkeyId;
    }

    public void setPkeyId(String pkeyId) {
        this.pkeyId = pkeyId;
    }

    public Integer getLocalId() {
        return localId;
    }

    public void setLocalId(Integer localId) {
        this.localId = localId;
    }

    public void dbBinding(ContentHolder holder) {

    }

    protected String formatDate(Date d) {
        return d != null ? CommonUtils.format(d) : null;
    }

    protected String formatTime(Date d) {
        return d != null ? CommonUtils.formatTime(d) : null;
    }

    public void reset() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof BasicBean) {
            BasicBean b = (BasicBean) o;
            if (getPkeyId() == null || b.getPkeyId() == null) {
                return false;
            }
            return getPkeyId().equals(b.getPkeyId());
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return getPkeyId() != null ? getPkeyId().hashCode() : -1;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }
}
