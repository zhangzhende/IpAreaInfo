package com.zzd.ipareainfo.mybatis;

import com.zzd.ipareainfo.bean.ServiceGover;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ServiceGoverMapper {

	void addServiceGover(ServiceGover serviceGover);
	
	void updateServiceGover(ServiceGover serviceGover);
	
	ServiceGover getServiceGoverByTicket(String ticket);
}
