package com.zzd.ipareainfo.mybatis;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.zzd.ipareainfo.bean.IpAreaInfo;

@Mapper
public interface IpAreaInfoMapper {

	void addIpAreaInfo(IpAreaInfo ipAreaInfo);

	void updateIpAreaInfo(IpAreaInfo ipAreaInfo);

	IpAreaInfo getIpAreaInfoByIp(String ip);

	List<IpAreaInfo> getIpAreaInfoByIps(List<String> list);

	List<String> getBhByIps(List<String> list);

	List<String> getUnHandleIp();

	List<IpAreaInfo> getHandledIp(@Param("limit") Long limit, @Param("offset") Long offset,
			@Param("start") Date start, @Param("end") Date end);
	Integer getHandledIpCount(@Param("start") Date start, @Param("end") Date end);
}
