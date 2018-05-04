package com.zzd.ipareainfo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.zzd.ipareainfo.bean.IpAreaInfo;

public class UtilTool {
	/**
	 * 判断是否是有效的ip地址
	 * 
	 * @param ipAddress
	 * @return
	 */
	public static boolean isboolIp(String ipAddress) {
		String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
		Pattern pattern = Pattern.compile(ip);
		Matcher matcher = pattern.matcher(ipAddress);
		return matcher.matches();
	}

	public static IpAreaInfo getNewIpAreaInfo(String ip) {
		IpAreaInfo ipAreaInfo = new IpAreaInfo();
		ipAreaInfo.setBh(UUID.randomUUID().toString().replaceAll("-", ""));
		ipAreaInfo.setIp(Constant.getIPHead(ip));
		ipAreaInfo.setCreatetime(new Date());
		ipAreaInfo.setDelmark(Constant.DELMARK);
		return ipAreaInfo;
	}

	public static String parseIpToId(String ip) {
		StringBuilder stringBuilder = new StringBuilder();
		String[] start = StringUtils.split(ip, ".");
		for (String string : start) {
			stringBuilder.append(getStringIp(string));
		}
		return stringBuilder.toString();
	}

	public static String getStringIp(String i) {
		StringBuilder stringBuilder = new StringBuilder();
		Integer j = Integer.parseInt(i);
		stringBuilder.append(j / 100 + "");
		j = j % 100;
		stringBuilder.append(j / 10 + "");
		j = j % 10;
		stringBuilder.append(j + "");
		return stringBuilder.toString();
	}

	public static Date parseStrToDate(String str) throws ParseException {
		if (StringUtils.isNotBlank(str)) {
			SimpleDateFormat myFmt = new SimpleDateFormat("yyyyMMdd");
			return myFmt.parse(str);
		} else {
			return null;
		}
	}
}
