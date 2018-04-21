package com.zzd.ipareainfo.mybatis;

import com.zzd.ipareainfo.bean.IpAreaInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface IpAreaInfoMapper {

	void addIpAreaInfo(IpAreaInfo ipAreaInfo);

	void updateIpAreaInfo(IpAreaInfo ipAreaInfo);

	IpAreaInfo getIpAreaInfoByIp(String ip);

	List<IpAreaInfo> getIpAreaInfoByIps(List<String> list);

	List<String> getBhByIps(List<String> list);

	List<String> getUnHandleIp();
}
