package cn.gateway.bind;

import cn.gateway.session.Configuration;
import org.apache.dubbo.common.config.ReferenceCache;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.SimpleReferenceCache;
import org.apache.dubbo.rpc.service.GenericService;


import java.util.HashMap;
import java.util.Map;

public class GenericReferenceRegistry {
    private final Configuration configuration;

    public GenericReferenceRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    // 泛化调用静态代理工厂
    /**
     * k: methodName
     * v: GenericReferenceProxyFactory
     */
    private final Map<String, GenericReferenceProxyFactory> knownGenericReferences = new HashMap<>();

    public IGenericReference getGenericReference(String methodName) {
        GenericReferenceProxyFactory genericReferenceProxyFactory = knownGenericReferences.get(methodName);
        if (genericReferenceProxyFactory == null) {
            throw new RuntimeException("Type " + methodName + " is not known to the GenericReferenceRegistry.");
        }
        return genericReferenceProxyFactory.newInstance(methodName);
    }

    public void addGenericReference(String application, String interfaceName, String methodName) {
        ApplicationConfig applicationConfig = configuration.getApplicationConfig(application);
        RegistryConfig registryConfig = configuration.getRegistryConfig(application);
        ReferenceConfig<GenericService> reference = configuration.getReferenceConfig(interfaceName);
        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        bootstrap.application(applicationConfig).registry(registryConfig).reference(reference).start();
        // 获取泛化调用服务 3.0用SimpleReferenceCache获取
        SimpleReferenceCache cache = SimpleReferenceCache.getCache();
        GenericService genericService = cache.get(reference);
        // 创建并保存泛化工厂
        knownGenericReferences.put(methodName, new GenericReferenceProxyFactory(genericService));


    }
}
