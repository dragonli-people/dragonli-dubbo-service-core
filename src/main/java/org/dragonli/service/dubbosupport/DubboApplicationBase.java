/**
 *
 */
package org.dragonli.service.dubbosupport;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author kangzhijie
 *
 */
public class DubboApplicationBase implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    protected String applicationName;
    protected String registryAddr;
    protected String protocolName;
    protected Integer protocolPort;
    protected String registryId;

//	private NettyService nettyService;
    protected int port;
    protected String localAddress;
    protected String group;

//	public String getApplicationName() { return null; }
//	public String getApplicationName() { return null; }
//	public String getApplicationName() { return null; }
//	public String getApplicationName() { return null; }
//	public String getApplicationName() { return null; }

    public DubboApplicationBase(String applicationName, String registryAddr, String protocolName, Integer protocolPort,
            String registryId, int port) {
        this.init(applicationName, registryAddr, protocolName, protocolPort, registryId, port, null,null);
    }

    public DubboApplicationBase(String applicationName, String registryAddr, String protocolName, Integer protocolPort,
            String registryId, int port, String localAddress,String group) {
        this.init(applicationName, registryAddr, protocolName, protocolPort, registryId, port, localAddress,group);
    }

    protected void init(String applicationName, String registryAddr, String protocolName, Integer protocolPort,
            String registryId, int port, String localAddress,String group) {
        this.applicationName = applicationName;
        this.registryAddr = registryAddr;
        this.protocolName = protocolName;
        this.protocolPort = protocolPort;
        this.registryId = registryId;
        this.port = port;
        this.group = group;
        this.localAddress = localAddress;
        System.out.println(
                "~~~~~~~~~~:" + applicationName + "|" + registryAddr + "|" + protocolName + "|" + protocolPort + "|" +
                        registryId + "|" + localAddress+"|"+group);
    }

//	@Override
//	public void customize(ConfigurableEmbeddedServletContainer container) {
//		container.setPort(this.port);
//	}

    @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setQosEnable(false);
        applicationConfig.setName(applicationName);
        return applicationConfig;
    }

    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(registryAddr);
        registryConfig.setClient("zkclient");
        if (null != group && !"".equals(group = group.trim()))
            registryConfig.setGroup(group);
        return registryConfig;
    }

    @Bean
    public ProtocolConfig protocolConfig() {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName(protocolName);
        protocolConfig.setPort(protocolPort);
        if (null != localAddress && !"".equals(localAddress = localAddress.trim()))
            protocolConfig.setHost(localAddress);
        return protocolConfig;
    }

//	public void startNetty(int port,INettySocketHandler handler) throws Exception
//	{
////		NettyServiceConfig config = new NettyServiceConfig(port,handler);
////		this.startNetty(config);
//	}
//	
//	public void startNetty(NettyServiceConfig config) throws Exception
//	{
//		this.nettyService = new NettyService(config);
//	}

//	@Value("${crypto.basePackage:com.itranswarp.crypto.ui}")
//	String basePackage;
//
//	@Value("${crypto.ui.view.cache:false}")
//	boolean viewCache;

//	@Value("${crypto.ui.api.endpoint}")
//	String apiEndpoint;

//	@Value("${crypto.ui.api.api-key:UIInternalKey}")
//	String apiKey;

//	@Value("${crypto.ui.api.api-secret:UIInternalSecret}")
//	String apiSecret;

    /**
     * Create a rest client.
     *
     * @return RestClient object.
     */
//	@Bean
//	public RestClient createRestClient() {
//		return new RestClient.Builder(apiEndpoint).authenticate(apiKey, apiSecret).build();
//	}

    /**
     * Create a proxy client.
     *
     * @return ProxyClient object.
     */
//	@Bean
//	public ProxyClientC2C createProxyClient() {
//		return new ProxyClientC2C.Builder(apiEndpoint).build();
//	}

    /**
     * Create JSON object mapper.
     *
     * @return Shared static object mapper from JsonUtil.
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer(@Autowired ObjectMapper objectMapper) {
        return new WebMvcConfigurerAdapter() {
            /**
             * Keep /static/ prefix
             */
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                super.addResourceHandlers(registry);
                registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
            }

            @Override
            public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
                final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
                converter.setObjectMapper(objectMapper);
                converters.add(converter);
                super.configureMessageConverters(converters);
            }
        };
    }


    /**
     * ManageRedis
     */
//	public static final String MANAGE_REDIS = "manageRedis";
//	@Bean(MANAGE_REDIS)
//	public RedissonClient createRedisClientNew(@Autowired RedisConfigurationManageC2C rc) {
//		return RedisClientBuilderC2C.buildRedissionClient(rc);
//	}
    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        // TODO Auto-generated method stub
        factory.setPort(this.port);
    }
}
