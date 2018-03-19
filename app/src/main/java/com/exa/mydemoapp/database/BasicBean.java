package com.exa.mydemoapp.database;


import com.exa.mydemoapp.Common.CommonUtils;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class BasicBean {
	private Map<String, Object> data;
	@SerializedName("_id")
	private Integer id;
	private Integer localId;

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

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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
		if(this == o){
			return true;
		}
		if(o instanceof BasicBean){
			BasicBean b = (BasicBean) o;
			if(getId() ==null || b.getId() ==null){
				return false;
			}
			return getId().equals(b.getId());
		}
		return super.equals(o);
	}
	
	@Override
	public int hashCode() {
		return getId() != null ?  getId().hashCode():-1;
	}

	public boolean isSync() {
		return sync;
	}

	public void setSync(boolean sync) {
		this.sync = sync;
	}


}
