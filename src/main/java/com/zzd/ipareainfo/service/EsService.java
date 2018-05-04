package com.zzd.ipareainfo.service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zzd.ipareainfo.bean.IpAreaInfo;
import com.zzd.ipareainfo.client.ElasticSearchConfig;
import com.zzd.ipareainfo.util.Constant;
import com.zzd.ipareainfo.util.UtilTool;

@Service
public class EsService {
	@Autowired
	private ElasticSearchConfig elasticSearchConfig;
	@Autowired
	private IpAreaInfoService areaInfoService;
	private static final Logger logger = LoggerFactory.getLogger(EsService.class);

	/**
	 * 批量导入
	 * 
	 * @param list
	 * @throws UnknownHostException
	 */
	public void BulkRequest(List<Map<Object, Object>> list) throws UnknownHostException {
		TransportClient client = elasticSearchConfig.getInstance();
		JSONObject json=new JSONObject();
		BulkRequestBuilder bulkRequest = null;
		Long i = 0L;
		for (Map<Object, Object> map : list) {
			if (bulkRequest == null) {
				bulkRequest = client.prepareBulk();
			}
			bulkRequest.add(client.prepareIndex(Constant.ES_INDEX, Constant.ES_TYPE, map.get("id").toString())
					.setSource(json.toJSONString(map.get("value"))));
			i++;
			if (i % Constant.BULKSIZE == 0) {
				BulkResponse bulkResponse = bulkRequest.execute().actionGet();
				if (bulkResponse.hasFailures()) {
					// 处理错误
					logger.info("something wrong!!!");
				}
				bulkRequest = null;
			}
		}
		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
		if (bulkResponse.hasFailures()) {
			// 处理错误
			logger.info("something wrong!!!");
		}
		bulkRequest = null;

	}

	/**
	 * 将ipareainfo 转换为Map，方便批量导入
	 * 
	 * @param limit
	 * @param offset
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Map<Object, Object>> getHandledIpToMap(Long limit, Long offset, Date start, Date end) {
		List<Map<Object, Object>> listResult = new ArrayList<Map<Object, Object>>();
		List<IpAreaInfo> list = areaInfoService.getHandledIp(limit, offset, start, end);
		for (IpAreaInfo ipAreaInfo : list) {
			Map<Object, Object> map = new HashMap<>();
			map.put("id", UtilTool.parseIpToId(ipAreaInfo.getIp() + ".1"));
			map.put("value", ipAreaInfo);
			listResult.add(map);
		}
		return listResult;
	}
}
