package cn.gateway.bind;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.dubbo.rpc.service.GenericService;

import java.lang.reflect.Method;

public class GenericReferenceProxy implements MethodInterceptor {
    private final GenericService genericService;
    private final String methodName;

    public GenericReferenceProxy(GenericService genericService, String methodName) {
        this.genericService = genericService;
        this.methodName = methodName;
    }

    /**
     * 做一层代理控制，后续不止是可以使用 Dubbo 泛化调用，也可以是其他服务的泛化调用
     *
     * @param o           “这个”，增强的对象
     * @param method      截获方法
     * @param objects     参数数组;基元类型被包装
     * @param methodProxy 用于调用超级（非拦截方法）;可以根据需要调用多次
     * @return 与代理方法的签名兼容的任何值。返回 void 的方法将忽略此值
     * @throws Throwable 可能会抛出任何异常;如果是这样，将不会调用超级方法
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] parameters = new String[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            parameters[i] = parameterTypes[i].getName();
        }
        return genericService.$invoke(methodName, parameters, objects);
    }
}
