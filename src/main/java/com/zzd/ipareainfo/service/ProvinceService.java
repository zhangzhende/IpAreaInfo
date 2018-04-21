package com.zzd.ipareainfo.service;

import com.zzd.ipareainfo.bean.Province;
import com.zzd.ipareainfo.mybatis.ProvinceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class ProvinceService {
	@Autowired
	private ProvinceMapper provinceMapper;

	public Province getProvinceByRegionid(String regionid) {
		return provinceMapper.getProvinceByRegionid(regionid);
	}
}
