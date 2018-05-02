package com.zzd.ipareainfo.client;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by zhangzhende on 2018/4/23.
 */
@Configuration
public class ElasticSearchConfig {
    /**
     * 集群名称
     */
    @Value("${elasticSearch.cluster}")
    String cluster;
    /**
     * 服务地址
     */
    @Value("${elasticSearch.host}")
    String host;
    /**
     * 端口
     */
    @Value("${elasticSearch.port}")
    int port;
    public static TransportClient client = null;

    @SuppressWarnings("resource")
	public TransportClient getInstance() throws UnknownHostException {
        if (client == null) {
            Settings settings = Settings.builder()
                    .put("cluster.name", cluster)
                    .put("node.name","node-1")
                    .put("client.transport.sniff", true).build();
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host),port));
            return client;
        }
        return client;
    }
}
