package com.zzd.ipareainfo.builder;

import com.zzd.ipareainfo.bean.IpAreaInfo;
import com.zzd.ipareainfo.bean.Province;
import com.zzd.ipareainfo.bean.ServiceIpInfo;
import com.zzd.ipareainfo.bean.TBResultData;
import com.zzd.ipareainfo.service.ProvinceService;
import com.zzd.ipareainfo.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;



@Service
public class Builder {
	private static final Logger logger = LoggerFactory.getLogger(Builder.class);

	@Autowired
	private ProvinceService provinceService;

	/**
	 * 更新IP信息，数据库中已有ip但没有更新的
	 * 
	 * @param data
	 * @param ip
	 * @return
	 */
	public IpAreaInfo build(TBResultData data, String ip) {
		IpAreaInfo ipAreaInfo = new IpAreaInfo();
		
		String regionid = parseString(data.getRegionid());
		Province province = provinceService.getProvinceByRegionid(regionid);
		if (province != null) {
			// 对于国内省份，需要特殊处理，1.省份有些有省字有些没有省字，现统一用映射表里面的
			ipAreaInfo.setRegion(province.getRegion());
			// 对于港澳台地区，国家不为中国，咱给他矫正过来，统一为中国
			ipAreaInfo.setCountry(province.getCountry());
			ipAreaInfo.setCountryid(province.getCountryid());
		} else {
			ipAreaInfo.setRegion(parseString(data.getRegion()));
			ipAreaInfo.setCountry(parseString(data.getCountry()));
			ipAreaInfo.setCountryid(parseString(data.getCountryid()));
		}
		ipAreaInfo.setBh(UUID.randomUUID().toString().replaceAll("-", ""));
		ipAreaInfo.setIp(ip);
		ipAreaInfo.setArea(parseString(data.getArea()));
		ipAreaInfo.setAreaid(parseString(data.getAreaid()));
		ipAreaInfo.setRegionid(parseString(data.getRegionid()));
		ipAreaInfo.setCity(parseString(data.getCity()));
		ipAreaInfo.setCityid(parseString(data.getCityid()));
		ipAreaInfo.setCounty(parseString(data.getCounty()));
		ipAreaInfo.setCountyid(parseString(data.getCountyid()));
		ipAreaInfo.setIsp(parseString(data.getIsp()));
		ipAreaInfo.setIspid(parseString(data.getIspid()));
		ipAreaInfo.setDelmark(Constant.DELMARK);
		ipAreaInfo.setUpdatetime(new Date());
		return ipAreaInfo;
	}

	/**
	 * 像单个查询这样，新增的ip同时查询插入
	 * 
	 * @param data
	 * @param ip
	 * @return
	 */
	public IpAreaInfo buildNow(TBResultData data, String ip) {
		IpAreaInfo ipAreaInfo = new IpAreaInfo();

		String regionid = parseString(data.getRegionid());
		Province province = provinceService.getProvinceByRegionid(regionid);
		if (province != null) {
			// 对于国内省份，需要特殊处理，1.省份有些有省字有些没有省字，现统一用映射表里面的
			ipAreaInfo.setRegion(province.getRegion());
			// 对于港澳台地区，国家不为中国，咱给他矫正过来，统一为中国
			ipAreaInfo.setCountry(province.getCountry());
			ipAreaInfo.setCountryid(province.getCountryid());
		} else {
			ipAreaInfo.setRegion(parseString(data.getRegion()));
			ipAreaInfo.setCountry(parseString(data.getCountry()));
			ipAreaInfo.setCountryid(parseString(data.getCountryid()));
		}
		ipAreaInfo.setBh(UUID.randomUUID().toString().replaceAll("-", ""));
		ipAreaInfo.setIp(ip);
		ipAreaInfo.setArea(parseString(data.getArea()));
		ipAreaInfo.setAreaid(parseString(data.getAreaid()));
		ipAreaInfo.setRegionid(parseString(data.getRegionid()));
		ipAreaInfo.setCity(parseString(data.getCity()));
		ipAreaInfo.setCityid(parseString(data.getCityid()));
		ipAreaInfo.setCounty(parseString(data.getCounty()));
		ipAreaInfo.setCountyid(parseString(data.getCountyid()));
		ipAreaInfo.setIsp(parseString(data.getIsp()));
		ipAreaInfo.setIspid(parseString(data.getIspid()));
		ipAreaInfo.setDelmark(Constant.DELMARK);
		ipAreaInfo.setCreatetime(new Date());
		ipAreaInfo.setUpdatetime(new Date());
		return ipAreaInfo;
	}

	/**
	 * 将数据库中可提供数据包装出去
	 * 
	 * @param ipAreaInfo
	 * @param ip
	 * @return
	 */
	public ServiceIpInfo parseIpAreaToService(IpAreaInfo ipAreaInfo, String ip) {
		ServiceIpInfo serviceIpInfo = new ServiceIpInfo();
		serviceIpInfo.setArea(ipAreaInfo.getArea());
		serviceIpInfo.setCity(ipAreaInfo.getCity());
		serviceIpInfo.setCountry(ipAreaInfo.getCountry());
		serviceIpInfo.setCounty(ipAreaInfo.getCounty());
		serviceIpInfo.setIp(ip);
		serviceIpInfo.setIsp(ipAreaInfo.getIsp());
		serviceIpInfo.setRegion(ipAreaInfo.getRegion());
		return serviceIpInfo;
	}

	/**
	 * 淘宝返回的数据是unicode编码，转码保存入库
	 * 
	 * @param str
	 * @return
	 */
	public String parseString(String str) {
		String resultString = "";
		try {
			if (StringUtils.isNotBlank(str)) {
				resultString = new String(str.getBytes(), "utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			logger.info("转码失败", e);
		}
		return resultString;
	}
}
