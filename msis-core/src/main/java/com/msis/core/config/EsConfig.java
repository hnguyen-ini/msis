package com.msis.core.config;

import java.net.InetAddress;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.msis.common.service.ServiceException;
import com.msis.common.service.ServiceStatus;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.msis.core.repository")
@ComponentScan(value = "com.msis.core.config")
public class EsConfig {
	static Logger log = LogManager.getLogger(EsConfig.class.getName());
	@Autowired
    private CoreConfig coreConfig;
    
    @Bean
    public TransportClient client() throws ServiceException {
    	String host = coreConfig.esHost();
    	String cluster = coreConfig.esCluster();
    	int port = coreConfig.esPort();

    	try {
	        Settings esSettings = Settings.settingsBuilder()
	        		.put("client.transport.sniff", true)
	        		.put("cluster.name", cluster)
	                .build();
	
	        //https://www.elastic.co/guide/en/elasticsearch/guide/current/_transport_client_versus_node_client.html
	        
	        TransportClient client = TransportClient.builder()
	                .settings(esSettings)
	                .build()
	                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
	        System.out.println(client.nodeName());
	        log.info("TransportClient connected to " + host + ":" + port + "/" + cluster);
	        return client;
    	} catch (Exception e) {
    		throw new ServiceException(ServiceStatus.SERVER_CONNECTION, "Initial TransportClient: Connection Failed, " + e.getMessage());
    	}
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws ServiceException {
        try {
        	return new ElasticsearchTemplate(client());
        } catch (Exception e) {
        	throw new ServiceException(ServiceStatus.SERVER_CONNECTION, "Initial ElasticsearchOperations: Connection Failed, " + e.getMessage());
        }
    }
    


}
