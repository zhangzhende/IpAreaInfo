package com.zzd.ipareainfo.bean;

import java.util.List;

public class Result {
private int status;
private String msg;
private List<ServiceIpInfo> dataList;

public Result(int status, String msg, List<ServiceIpInfo> dataList) {
	this.status = status;
	this.msg = msg;
	this.dataList = dataList;
}



public int getStatus() {
	return status;
}
public void setStatus(int status) {
	this.status = status;
}
public String getMsg() {
	return msg;
}
public void setMsg(String msg) {
	this.msg = msg;
}

public List<ServiceIpInfo> getDataList() {
	return dataList;
}
public void setDataList(List<ServiceIpInfo> dataList) {
	this.dataList = dataList;
}
}
