package com.zzd.ipareainfo.controller;

import com.zzd.ipareainfo.bean.Result;
import com.zzd.ipareainfo.service.HandleService;
import com.zzd.ipareainfo.util.Constant;
import com.zzd.ipareainfo.util.UtilTool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
	@Autowired
	private HandleService handleService;
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);

	/**
	 * 手动更新ip库的接口，将没有更新的ip查询更新一波
	 */
	@RequestMapping("/handle/ipinfo")
	public void handle() {
		logger.info("接收到请求");
		handleService.handleData();
		logger.info("获得结果");
	}

	/**
	 * 手动更新ip库的接口，将没有更新的ip查询更新一波
	 */
	@RequestMapping("/handle/changestatus")
	public void changestatus() {
		logger.info("接收到请求,修改状态");
		Constant.isUpdating = false;
		logger.info("获得结果");
	}

	@RequestMapping("/handle/createIps")
	public void createIp(@RequestParam(value = "ipstart", required = true) String ipstart,
			@RequestParam(value = "ipend", required = true) String ipend) {
		logger.info("接收到请求");
		if (UtilTool.isboolIp(ipstart) && UtilTool.isboolIp(ipend)) {
			handleService.makingIps(ipstart, ipend);
		} else {
			logger.info("Ip地址传参错误：ipstart=" + ipstart + ",ipend=" + ipend);
		}
		logger.info("获得结果");
	}
}
