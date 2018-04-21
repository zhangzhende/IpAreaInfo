package com.zzd.ipareainfo.schedule;

import com.zzd.ipareainfo.service.HandleService;
import com.zzd.ipareainfo.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@Configuration
@EnableScheduling
public class SchedulingVisite {
	private static final Logger logger = LoggerFactory
			.getLogger(SchedulingVisite.class);

	@Autowired
	private HandleService handleService;

	/**
	 * 定时更新ip信息
	 */
	@Scheduled(cron = "${scheduler.updatetaskcron}")
	public void UpdateIps() {
		if (Boolean.parseBoolean(Constant.switchupdatetask)) {
			logger.info("-----定时开关为【开】----更新ip任务---------");
			if (Constant.isUpdating == null || Constant.isUpdating == false) {
				logger.info("-----任务空闲----开始更新ip任务---------");
				Constant.isUpdating = true;
				handleService.handleData();
				Constant.isUpdating = false;
			} else {
				logger.info("-----任务忙碌----等待下一次更新ip任务---------");
			}
			logger.info("-----定时开关为【开】----更新ip任务结束---------");
		} else {
			logger.info("-----定时开关为【关】----更新ip任务不启动---------");
		}
	}

}
