package com.zzd.ipareainfo.service;

import com.zzd.ipareainfo.bean.ServiceGover;
import com.zzd.ipareainfo.mybatis.ServiceGoverMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ServiceGoverService {
	@Autowired
	private ServiceGoverMapper serviceGoverMapper;

	public void addServiceGover(ServiceGover serviceGover){
		serviceGoverMapper.addServiceGover(serviceGover);
	}
	public void updateServiceGover(ServiceGover serviceGover){
		serviceGoverMapper.updateServiceGover(serviceGover);
	}
	public ServiceGover getServiceGoverByTicket(String ticket){
	return serviceGoverMapper.getServiceGoverByTicket(ticket);	
	}
}
