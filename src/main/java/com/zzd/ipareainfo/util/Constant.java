package com.zzd.ipareainfo.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "constant")
public class Constant {
	public static String servicename;
	public static Boolean isUpdating;
	/**
	 * 定时任务的开关
	 */
	public static String switchupdatetask;
	public static  String iptaobaourl;
	public static final int DELMARK=0;
	
	public static final String ES_INDEX="ipinfos";
	public static final String ES_TYPE="ipv4";
	public static final int BULKSIZE=10000;
	public static String getIPHead(String IP){
		String IPHead ="";
		if(StringUtils.isNotBlank(IP) && IP.contains(".")){
			int index =StringUtils.lastIndexOf(IP, ".");
			IPHead=StringUtils.substring(IP, 0, index);
		}
		return IPHead;
	}
	public static String AppendIP(String IPHead){
		if(StringUtils.isNotBlank(IPHead)){
			IPHead=IPHead+".1";
		}
		return IPHead;
	}
	/** 返回状态枚举 */
	public static enum ResultStatusCode {
		OK(1, "成功"), INNER_ERROR(2, "失败,接口异常"), RIGHT_ERROR(2, "失败,没有权限的请求"), SIZE_ERROR(
				2, "失败，请求数量超过上限"), NOTHINGTOSEARCH_ERROR(2, "失败,没有需要查询的数据"),IP_ERROR(2, "失败,ip格式不正确");

		private int code;
		private String message;

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		private ResultStatusCode(int code, String message) {
			this.code = code;
			this.message = message;
		}
	}
	public static void setServicename(String servicename) {
		Constant.servicename = servicename;
	}
	public  void setSwitchupdatetask(String switchupdatetask) {
		Constant.switchupdatetask = switchupdatetask;
	}
	
	public  void setIptaobaourl(String iptaobaourl) {
		Constant.iptaobaourl = iptaobaourl;
	}
}
