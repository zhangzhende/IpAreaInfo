package com.zzd.ipareainfo.service;

import com.zzd.ipareainfo.bean.IpAreaInfo;
import com.zzd.ipareainfo.mybatis.IpAreaInfoMapper;
import com.zzd.ipareainfo.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



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

}
