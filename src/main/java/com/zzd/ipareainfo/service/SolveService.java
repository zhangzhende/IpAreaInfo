package com.zzd.ipareainfo.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.reflect.TypeToken;
import com.zzd.ipareainfo.bean.*;
import com.zzd.ipareainfo.builder.Builder;
import com.zzd.ipareainfo.client.GSClient;
import com.zzd.ipareainfo.util.Constant;
import com.zzd.ipareainfo.util.UtilTool;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class SolveService {

	private static final Logger logger = LoggerFactory
			.getLogger(SolveService.class);

	@Autowired
	private Builder builder;
	@Autowired
	private DatabaseService databaseService;

	/**
	 * 
	 * 查询的权限认证，判断是否有权限，包括编号的权限以及ip限制
	 * 
	 * @param servicebh
	 * @param ticket
	 * @param requestip
	 * @return
	 */
	public boolean check(String servicebh, String ticket, String requestip) {
		logger.info("权限检验：servicebh=" + servicebh + ";ticket=" + ticket
				+ ";requestip=" + requestip);
		if (StringUtils.isNotBlank(servicebh) && StringUtils.isNotBlank(ticket)) {
			ServiceGover serviceGover = databaseService
					.getServiceGoverByTicket(ticket);
			if (serviceGover != null) {
				if (!serviceGover.getServicebh().equalsIgnoreCase(servicebh)) {
					logger.info("校验失败，不是有权限的请求");
					return false;
				} else if (serviceGover.getIsiplimit() != 0
						&& StringUtils.isNotBlank(requestip)
						&& serviceGover.getLimitips().contains(requestip)) {
					logger.info("校验失败，IP限制开启，请求IP不在白名单内，请求IP为：" + requestip);
					return false;
				} else {
					logger.info("校验成功");
					return true;
				}
			} else {
				logger.info("无效ticket" + ticket);
				return false;
			}
		} else {
			logger.info("校验失败，权限参数为空");
			return false;
		}
	}

	/**
	 * 单个ip查询，根据IP获取信息，但是如果库里面没有，直接查淘宝后返回并保存到数据库
	 * 
	 * @param ip
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access", "serial" })
	public List<ServiceIpInfo> getServiceIpInfoByIp(String ip) {
		List<ServiceIpInfo> list = new ArrayList();
		if (StringUtils.isNotBlank(ip)) {
			IpAreaInfo ipAreaInfo = databaseService.getIpAreaInfoByIp(ip);
			if (ipAreaInfo != null) {
				logger.info("ip数据库中查询成功");
				ServiceIpInfo serviceIpInfo = builder.parseIpAreaToService(
						ipAreaInfo, ip);
				list.add(serviceIpInfo);
			} else {
				logger.info("ip数据库中没有，请求淘宝库。。。。");
				JSONObject json = new JSONObject();
				GSClient client = GSClient.getInstance();
				String url = Constant.iptaobaourl + ip;
				String result;
				try {
					result = client.getMethod(url);
					logger.info("返回结果：" + result);
					TBResult tbresult = json.parseObject(result,
							new TypeToken<TBResult>() {
							}.getType());
					if (tbresult.getCode() == 0) {
						TBResultData tbResultData = json.parseObject(
								json.toJSONString(tbresult.getData()),
								new TypeToken<TBResultData>() {
								}.getType());
						IpAreaInfo areaInfo = builder.buildNow(tbResultData,
								Constant.getIPHead(ip));
						databaseService.saveIpAreaInfo(areaInfo);
						ServiceIpInfo serviceIpInfo = builder
								.parseIpAreaToService(areaInfo, ip);
						list.add(serviceIpInfo);
					}
				} catch (Exception e) {
					logger.error("接口返回失败!", e);
				}
			}
		}
		return list;
	}

	/**
	 * 批量ip查询，对于没有查到的有效ip，先把ip信息存库，然后等待定时任务去更新信息
	 * 
	 * @param ipList
	 * @param list
	 * @param notInList
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getServiceIpInfoByIps(List<String> ipList,
			List<ServiceIpInfo> list, List<String> notInList) {
		List<String> IPslist = new ArrayList();
		List<IpAreaInfo> addIPList = new ArrayList();
		for (String ip : ipList) {
			if (StringUtils.isNotBlank(ip) && UtilTool.isboolIp(ip)) {
				if (!IPslist.contains(Constant.getIPHead(ip))) {
					IPslist.add(Constant.getIPHead(ip));
				}
			} else {
				notInList.add(ip);//不合格IP
			}
		}
		List<IpAreaInfo> ipAreaInfoList = databaseService
				.getIpAreaInfoByIps(IPslist);
//		if (ipAreaInfoList.size() > 0) {
			Map<String, IpAreaInfo> map = new HashMap();
			for (IpAreaInfo ipAreaInfo : ipAreaInfoList) {
				map.put(ipAreaInfo.getIp(), ipAreaInfo);
			}
			List<String> hasInsert=new ArrayList<>();//已经插入的ip段要过滤掉
			for (String ip : ipList) {
				if (!notInList.contains(ip)) {
					if (map.get(Constant.getIPHead(ip)) != null  ) {// 查询为空表示为数据库中没有,并且此ip段没有被插入过
						IpAreaInfo ipAreaInfo = map.get(Constant.getIPHead(ip));
						if (ipAreaInfo.getUpdatetime() != null) {// 更新时间为空表示为数据库中已有，但是信息还没有更新的
							ServiceIpInfo serviceIpInfo = builder
									.parseIpAreaToService(ipAreaInfo, ip);
							list.add(serviceIpInfo);
						} else {
							notInList.add(ip);
						}
					} else {
						if(!hasInsert.contains(Constant.getIPHead(ip))){
							IpAreaInfo ipAreaInfo = UtilTool.getNewIpAreaInfo(ip);
							addIPList.add(ipAreaInfo);
							hasInsert.add(Constant.getIPHead(ip));
						}
						notInList.add(ip);
						
					}
				}
			}
//		} else {
//			logger.info("数据库中未查到结果");
//			notInList.addAll(ipList);
//		}
		databaseService.saveIpAreaInfos(addIPList);
	}

	/**
	 * 提前查询，将数据库中没有的ip保存到数据库，等定时任务更新信息，在查询的时候的可减少查不到的ip的数量
	 * 
	 * @param ipList
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void PrepareIps(List<String> ipList) {
		List<String> notInList = new ArrayList();
		List<String> IPslist = new ArrayList();
		List<IpAreaInfo> addIPList = new ArrayList();
		for (String ip : ipList) {
			if (StringUtils.isNotBlank(ip) && UtilTool.isboolIp(ip)) {
				if (!IPslist.contains(Constant.getIPHead(ip))) {
					IPslist.add(Constant.getIPHead(ip));
				}
			} else {
				notInList.add(ip);
			}
		}
		List<String> resultList = databaseService.getBhByIps(IPslist);//返回的是ip段的集合
		Map<String, String> map = new HashMap();
		for (String ip : resultList) {
			map.put(ip, ip);
		}
		List<String> hasInsert=new ArrayList<>();//已经插入的ip段要过滤掉
		for (String ip : ipList) {
			if (!notInList.contains(ip)) {
				if (map.get(Constant.getIPHead(ip)) == null && !hasInsert.contains(Constant.getIPHead(ip))) {// 查询为空表示为数据库中没有,并且本批次未算入该ip段
					IpAreaInfo ipAreaInfo = UtilTool.getNewIpAreaInfo(ip);
					addIPList.add(ipAreaInfo);
					hasInsert.add(Constant.getIPHead(ip));
					notInList.add(ip);
				}
			}
		}
		databaseService.saveIpAreaInfos(addIPList);
	}


}
