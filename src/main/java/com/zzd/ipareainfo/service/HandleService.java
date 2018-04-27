package com.zzd.ipareainfo.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.reflect.TypeToken;
import com.zzd.ipareainfo.bean.IpAreaInfo;
import com.zzd.ipareainfo.bean.TBResult;
import com.zzd.ipareainfo.bean.TBResultData;
import com.zzd.ipareainfo.builder.Builder;
import com.zzd.ipareainfo.client.GSClient;
import com.zzd.ipareainfo.service.DatabaseService;
import com.zzd.ipareainfo.util.Constant;
import com.zzd.ipareainfo.util.UtilTool;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HandleService {

	private static final Logger logger = LoggerFactory.getLogger(HandleService.class);
	@Autowired
	private DatabaseService databaseService;
	@Autowired
	private Builder builder;

	/**
	 * 更新IP库，将没有查的部分查询淘宝
	 */
	@SuppressWarnings({ "static-access", "rawtypes", "unchecked", "serial" })
	public void handleData() {
		List<String> ipList = databaseService.getUnHandleIp();
		logger.info("需要查询的ip段数量有：" + ipList.size());
		JSONObject json = new JSONObject();
		for (String ipString : ipList) {
			List<IpAreaInfo> infolist = new ArrayList();
			GSClient client = GSClient.getInstance();
			String url = Constant.iptaobaourl + Constant.AppendIP(ipString);
			String result;
			try {
				result = client.getMethod(url);
				logger.info("返回结果：" + result);
				TBResult tbresult = json.parseObject(result, new TypeToken<TBResult>() {
				}.getType());
				if (tbresult.getCode() == 0) {
					TBResultData tbResultData = json.parseObject(json.toJSONString(tbresult.getData()),
							new TypeToken<TBResultData>() {
							}.getType());
					IpAreaInfo areaInfo = builder.build(tbResultData, ipString);
					infolist.add(areaInfo);
				} else {
					logger.info("返回了错误代码：" + result);
				}
				logger.info("已经查询到的ip数量有：" + infolist.size());
				databaseService.updateIpAreaInfo(infolist);
			} catch (Exception e) {
				logger.error("返回了错误代码：", e);
			}
		}
	}

	public void makingIps(String ipstart, String ipend) {
		logger.info("需要创建的IP有：" + ipstart + "-" + ipend);
		String[] start = StringUtils.split(ipstart, ".");
		String[] end = StringUtils.split(ipend, ".");
		int[] startArr = new int[4];
		int[] endarr = new int[4];
		
		for (int i = 0; i < 4; i++) {
			startArr[i] = Integer.parseInt(start[i]);
			endarr[i] = Integer.parseInt(end[i]);
		}
		int firstStart = startArr[0];
		int firstend = endarr[0];
		for (int i = firstStart; i <= firstend; i++) {
			int secondStart = 0;
			int secondend = 255;
			if (startArr[0] == endarr[0]) {
				secondStart = startArr[1];
				secondend = endarr[1];
			}
			for (int j = secondStart; j <= secondend; j++) {
				int thirdStart = 0;
				int thirdend = 255;
				if (startArr[1] == endarr[1]) {
					thirdStart = startArr[2];
					thirdend = endarr[2];
				}
				for(int k=thirdStart;k<thirdend;k++){
					String ip=i+"."+j+"."+k+"."+1;
					IpAreaInfo ipAreaInfo = databaseService.getIpAreaInfoByIp(ip);
					if(ipAreaInfo==null){
						IpAreaInfo newIpAreaInfo=UtilTool.getNewIpAreaInfo(ip);
						IpAreaInfo ipinfo=databaseService.getIpAreaInfoByIp(ip);
						if(ipinfo==null){
							databaseService.saveIpAreaInfo(newIpAreaInfo);
						}
					}
				}
			}
		}
	}
}
