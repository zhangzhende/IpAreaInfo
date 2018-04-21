package com.zzd.ipareainfo.mybatis;

import com.zzd.ipareainfo.bean.Province;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProvinceMapper {

	Province getProvinceByRegionid(String regionid);

}
