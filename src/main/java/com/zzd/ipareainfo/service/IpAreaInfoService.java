package com.zzd.ipareainfo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzd.ipareainfo.bean.IpAreaInfo;
import com.zzd.ipareainfo.mybatis.IpAreaInfoMapper;
import com.zzd.ipareainfo.util.Constant;

@Service
public class IpAreaInfoService {
	@Autowired
	private IpAreaInfoMapper ipAreaInfoMapper;

	public void addIpAreaInfo(IpAreaInfo ipAreaInfo) {
		ipAreaInfoMapper.addIpAreaInfo(ipAreaInfo);
	}

	public void updateIpAreaInfo(IpAreaInfo ipAreaInfo) {
		ipAreaInfoMapper.updateIpAreaInfo(ipAreaInfo);
	}

	public IpAreaInfo getIpAreaInfoByIp(String ip) {
		return ipAreaInfoMapper.getIpAreaInfoByIp(Constant.getIPHead(ip));
	}

	public List<IpAreaInfo> getIpAreaInfoByIps(List<String> list) {
		return ipAreaInfoMapper.getIpAreaInfoByIps(list);
	}

	public List<String> getBhByIps(List<String> list) {
		return ipAreaInfoMapper.getBhByIps(list);
	}

	public List<String> getUnHandleIp() {
		return ipAreaInfoMapper.getUnHandleIp();
	}

	public List<IpAreaInfo> getHandledIp(Long limit, Long offset, Date start, Date end) {
		return ipAreaInfoMapper.getHandledIp(limit, offset, start, end);
	}

	public Integer getHandledIpCount(Date start, Date end) {
		return ipAreaInfoMapper.getHandledIpCount(start, end);
	}

}
