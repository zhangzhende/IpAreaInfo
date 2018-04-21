package com.zzd.ipareainfo.service;

import com.zzd.ipareainfo.bean.IpAreaInfo;
import com.zzd.ipareainfo.bean.ServiceGover;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class DatabaseService {
	private static final Logger logger = LoggerFactory
			.getLogger(DatabaseService.class);
	@Autowired
	private IpAreaInfoService ipAreaInfoService;
	@Autowired
	private ServiceGoverService serviceGoverService;

	public void saveIpAreaInfos(List<IpAreaInfo> list) {
		for (IpAreaInfo ipAreaInfo : list) {
			logger.info("插入新的IP地址：" + ipAreaInfo.getIp());
			ipAreaInfoService.addIpAreaInfo(ipAreaInfo);
		}
	}

	public void saveIpAreaInfo(IpAreaInfo ipAreaInfo) {
		logger.info("插入新的IP地址：" + ipAreaInfo.getIp());
		ipAreaInfoService.addIpAreaInfo(ipAreaInfo);
	}

	public void updateIpAreaInfo(List<IpAreaInfo> list) {
		for (IpAreaInfo ipAreaInfo : list) {
			logger.info("更新IP地址：" + ipAreaInfo.getIp());
			ipAreaInfoService.updateIpAreaInfo(ipAreaInfo);
		}
	}

	public void updateIpAreaInfo(IpAreaInfo ipAreaInfo) {
		logger.info("更新IP地址：" + ipAreaInfo.getIp());
		ipAreaInfoService.updateIpAreaInfo(ipAreaInfo);
	}

	public IpAreaInfo getIpAreaInfoByIp(String ip) {
		return ipAreaInfoService.getIpAreaInfoByIp(ip);
	}

	public List<IpAreaInfo> getIpAreaInfoByIps(List<String> list) {
		return ipAreaInfoService.getIpAreaInfoByIps(list);
	}
	public List<String> getBhByIps(List<String> list) {
		return ipAreaInfoService.getBhByIps(list);
	}
	public List<String> getUnHandleIp() {
		return ipAreaInfoService.getUnHandleIp();
	}

	public ServiceGover getServiceGoverByTicket(String ticket) {
		return serviceGoverService.getServiceGoverByTicket(ticket);
	}

}
