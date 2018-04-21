package com.zzd.ipareainfo.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.reflect.TypeToken;
import com.zzd.ipareainfo.bean.Result;
import com.zzd.ipareainfo.bean.ServiceIpInfo;
import com.zzd.ipareainfo.service.SolveService;
import com.zzd.ipareainfo.util.Constant;
import com.zzd.ipareainfo.util.UtilTool;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 不控制的查询
 * 
 * 大家都是自己人，没必要管这么严吧=_=
 * 
 * @author Administrator
 * 
 */
@RestController
@RequestMapping("/freeapi")
public class FreeServiceController {
	private static final Logger logger = LoggerFactory
			.getLogger(FreeServiceController.class);
	@Autowired
	private SolveService solveService;

	/**
	 * 请求单个ip，
	 * 
	 * @param ip
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getipinfo/info", method = RequestMethod.GET)
	public Object getResult(
			@RequestParam(value = "ip", required = true) String ip,
			HttpServletRequest request) {
		String requestip = request.getRemoteAddr();
		logger.info("接收到请求:requestip=" + requestip + ";ip=" + ip);
		Result result = null;
		if (StringUtils.isBlank(ip)) {
			result = new Result(
					Constant.ResultStatusCode.NOTHINGTOSEARCH_ERROR.getCode(),
					Constant.ResultStatusCode.NOTHINGTOSEARCH_ERROR
							.getMessage(), null);
		} else if (!UtilTool.isboolIp(ip)) {
			result = new Result(Constant.ResultStatusCode.IP_ERROR.getCode(),
					Constant.ResultStatusCode.IP_ERROR.getMessage(), null);
		} else {
			result = new Result(Constant.ResultStatusCode.OK.getCode(),
					Constant.ResultStatusCode.OK.getMessage(),
					solveService.getServiceIpInfoByIp(ip));
		}
		logger.info("获得结果");
		return result;
	}

	/**
	 * 批量查询ip接口
	 * 
	 * @param object
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "static-access", "rawtypes", "serial", "unchecked" })
	@RequestMapping(value = "/getipinfo/infos", method = RequestMethod.POST)
	public Object getResults(@RequestBody Object object,
			HttpServletRequest request) {
		JSONObject json = new JSONObject();
		String requestip = request.getRemoteAddr();
		Map map = json.parseObject(json.toJSONString(object),
				new TypeToken<Map>() {
				}.getType());
		List<String> ipList = (List<String>) map.get("ips");
		logger.info("接收到请求:requestip=" + requestip + ";ips="
				+ json.toJSONString(ipList));
		Result result = null;
		if (ipList.size() < 1) {
			result = new Result(
					Constant.ResultStatusCode.NOTHINGTOSEARCH_ERROR.getCode(),
					Constant.ResultStatusCode.NOTHINGTOSEARCH_ERROR
							.getMessage(), null);
		} else {
			List<ServiceIpInfo> list = new ArrayList();
			List<String> notInList = new ArrayList();
			solveService.getServiceIpInfoByIps(ipList, list, notInList);
			if (notInList.size() > 0) {
				result = new Result(Constant.ResultStatusCode.OK.getCode(),
						"查询成功，但是以下暂未查询到，可在第二天尝试一下："
								+ json.toJSONString(notInList), list);
			} else {
				result = new Result(Constant.ResultStatusCode.OK.getCode(),
						Constant.ResultStatusCode.OK.getMessage(), list);
			}
		}
		logger.info("获得结果");
		return result;
	}

	/**
	 * 预更新IP，提前将数据库中没有的IP放到数据库中，等定时任务来刷新更新，尽量快的更新数据
	 * 
	 * @param servicebh
	 * @param object
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "static-access", "rawtypes", "serial", "unchecked" })
	@RequestMapping(value = "/ipinfo/info", method = RequestMethod.POST)
	public Object PrepareIPs(@RequestBody Object object,
			HttpServletRequest request) {
		JSONObject json = new JSONObject();
		String requestip = request.getRemoteAddr();
		Map map = json.parseObject(json.toJSONString(object),
				new TypeToken<Map>() {
				}.getType());
		List<String> ipList = (List<String>) map.get("ips");
		logger.info("接收到请求:requestip=" + requestip + ";ips="
				+ json.toJSONString(ipList));
		Result result = null;
		if (ipList.size() < 1) {
			result = new Result(
					Constant.ResultStatusCode.NOTHINGTOSEARCH_ERROR.getCode(),
					Constant.ResultStatusCode.NOTHINGTOSEARCH_ERROR
							.getMessage(), null);
		} else {
			solveService.PrepareIps(ipList);
			result = new Result(Constant.ResultStatusCode.OK.getCode(),
					Constant.ResultStatusCode.OK.getMessage(), null);
		}
		logger.info("获得结果");
		return result;
	}

	public Date getFormateTime(String timeString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.parse(timeString);
	}

	// public boolean isboolIp(String ipAddress) {
	// String ip =
	// "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
	// Pattern pattern = Pattern.compile(ip);
	// Matcher matcher = pattern.matcher(ipAddress);
	// return matcher.matches();
	// }
}
