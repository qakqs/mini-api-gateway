package test;

import cn.gateway.session.Configuration;
import cn.gateway.session.GenericReferenceSessionFactoryBuilder;
import cn.gateway.session.SessionServer;
import io.netty.channel.Channel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ApiTest {
        private final Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test() throws ExecutionException, InterruptedException {
        Configuration configuration = new Configuration();
        configuration.addGenericReference("api-gateway-test", "cn.bugstack.gateway.rpc.IActivityBooth", "sayHi");
        GenericReferenceSessionFactoryBuilder builder = new GenericReferenceSessionFactoryBuilder();
        Future<Channel> future = builder.build(configuration);

        logger.info("服务启动完成 {}", future.get().id());

        Thread.sleep(Long.MAX_VALUE);

    }
}
