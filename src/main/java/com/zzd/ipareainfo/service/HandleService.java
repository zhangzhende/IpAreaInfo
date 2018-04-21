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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;




@Service
public class HandleService {
	
	private static final Logger logger = LoggerFactory
			.getLogger(HandleService.class);
	@Autowired
	private DatabaseService databaseService;
	@Autowired
	private Builder builder;
	
	/**
	 * 更新IP库，将没有查的部分查询淘宝
	 */
	@SuppressWarnings({ "static-access", "rawtypes", "unchecked", "serial" })
	public void handleData(){
		List<String> ipList=databaseService.getUnHandleIp();
		logger.info("需要查询的ip段数量有："+ipList.size());
		JSONObject json=new JSONObject();
		for (String ipString : ipList) {
			List<IpAreaInfo> infolist=new ArrayList();
			GSClient client = GSClient.getInstance();
			String url= Constant.iptaobaourl+Constant.AppendIP(ipString);
			String result;
			try {
				result = client.getMethod(url);
				logger.info("返回结果："+result);
				TBResult tbresult=json.parseObject(result,new TypeToken<TBResult>() {}.getType());
				if(tbresult.getCode()==0){
					TBResultData tbResultData=json.parseObject(json.toJSONString(tbresult.getData()),new TypeToken<TBResultData>() {}.getType());
					IpAreaInfo areaInfo=builder.build(tbResultData,ipString);
					infolist.add(areaInfo);
				}else{
					logger.info("返回了错误代码："+result);
				}
				logger.info("已经查询到的ip数量有："+infolist.size());
				databaseService.updateIpAreaInfo(infolist);
			} catch (Exception e) {
				logger.error("返回了错误代码：",e);
			}
		}
		
	}
}
