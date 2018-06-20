package cn.tangxinyao.thrift.business.annotation;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;

@Configuration
public class ThriftControllerProcessor implements BeanPostProcessor {

    @Autowired
    private TMultiplexedProcessor processor;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        ThriftController annotation = AnnotationUtils.findAnnotation(bean.getClass(), ThriftController.class);
        if (annotation != null) try {
            Class[] arg = bean.getClass().getInterfaces();
            TProcessor singleProcessor = (TProcessor) annotation.processor().getConstructor(arg).newInstance(bean);
            processor.registerProcessor(annotation.service(), singleProcessor);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bean;
    }

    @Bean("multiplexedProcessor")
    public TMultiplexedProcessor getProcessor() {
        return new TMultiplexedProcessor();
    }
}
