package dubbo.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.alibaba.dubbo.rpc.Exporter;
import dubbo.properties.DubboApplication;
import dubbo.properties.DubboProtocol;
import dubbo.properties.DubboProvider;
import dubbo.properties.DubboRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnClass(Exporter.class)
@EnableConfigurationProperties({DubboApplication.class, DubboProtocol.class, DubboRegistry.class, DubboProvider.class})
public class DubboConfiguration {

    @Autowired
    private DubboApplication dubboApplication;

    @Autowired
    private DubboProtocol dubboProtocol;

    @Autowired
    private DubboProvider dubboProvider;

    @Autowired
    private DubboRegistry dubboRegistry;

    @Bean
    public static AnnotationBean annotationBean(@Value("${dubbo.annotation.package}") String packageName) {
        log.debug("[DubboAutoConfiguration] AnnotationBean:{}", packageName);
        AnnotationBean annotationBean = new AnnotationBean();
        annotationBean.setPackage(packageName);
        return annotationBean;
    }

    @Bean
    public ApplicationConfig applicationConfig() {
        log.debug("[DubboAutoConfiguration] ApplicationConfig:{}", dubboApplication);
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(dubboApplication.getName());
        applicationConfig.setLogger(dubboApplication.getLogger());
        return applicationConfig;
    }

    @Bean
    public ProtocolConfig protocolConfig() {
        log.debug("[DubboAutoConfiguration] ProtocolConfig:{}", dubboProtocol);
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName(dubboProtocol.getName());
        protocolConfig.setPort(dubboProtocol.getPort());
        protocolConfig.setAccesslog(String.valueOf(dubboProtocol.isAccessLog()));

        return protocolConfig;
    }

    @Bean
    public ProviderConfig providerConfig(ApplicationConfig applicationConfig,
                                         RegistryConfig registryConfig,
                                         ProtocolConfig protocolConfig) {
        log.debug("[DubboAutoConfiguration] ProviderConfig:{}", dubboProvider);

        ProviderConfig providerConfig = new ProviderConfig();

        providerConfig.setTimeout(dubboProvider.getTimeout());
        providerConfig.setRetries(dubboProvider.getRetries());
        providerConfig.setDelay(dubboProvider.getDelay());

        providerConfig.setApplication(applicationConfig);
        providerConfig.setRegistry(registryConfig);
        providerConfig.setProtocol(protocolConfig);

        return providerConfig;
    }

    @Bean
    public RegistryConfig registryConfig() {
        log.debug("[DubboAutoConfiguration] RegistryConfig:{}", dubboRegistry);

        RegistryConfig registryConfig = new RegistryConfig();

        registryConfig.setProtocol(dubboRegistry.getProtocol());
        registryConfig.setAddress(dubboRegistry.getAddress());
        registryConfig.setRegister(dubboRegistry.isRegister());
        registryConfig.setSubscribe(dubboRegistry.isSubscribe());
        return registryConfig;
    }
}
