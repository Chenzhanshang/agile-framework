package com.agile.framework.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * 获取bean的工具类，常用于非容器内对象使用
 * @author chenzhanshang
 */
@Slf4j
@Configuration
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
    	log.info("SpringUtil initialize ApplicationContext");
    	SpringUtil.applicationContext = context;
    }

    //获取applicationContext 
    public static ApplicationContext getApplicationContext() {
    	if (SpringUtil.applicationContext == null) {
    		throw new RuntimeException("SpringUtil.applicationContext is null");
    	}
        return SpringUtil.applicationContext;
    }

    //通过name获取 Bean. 
	public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    //通过class获取Bean. 
	public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    //通过name,以及Clazz返回指定的Bean 
	public static <T> T getBean(String name, Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }

	public static String property(String name) {
		return getApplicationContext().getEnvironment().getProperty(name);
	}

    /**
     *  动态注入单例bean实例
     * @param beanName bean名称
     * @param singletonObject 单例bean实例
     * @return 注入实例
     */
    @SuppressWarnings("unchecked")
	public static <T>T registerBean(String beanName, T singletonObject){

        //将applicationContext转换为ConfigurableApplicationContext
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;

        //获取BeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getAutowireCapableBeanFactory();

        //动态注册bean.
        defaultListableBeanFactory.registerSingleton(beanName, singletonObject);

        //获取动态注册的bean.
        return (T) configurableApplicationContext.getBean(beanName);
    }
    /**
    * 主动向Spring容器中注册bean
    *
    * @param name               BeanName
    * @param clazz              注册的bean的类性
    * @param args               构造方法的必要参数，顺序和类型要求和clazz中定义的一致
    * @param <T>
    * @return 返回注册到容器中的bean对象
    */
   @SuppressWarnings("unchecked")
   public static <T> T registerBean(String name, Class<T> clazz,
           Object... args) {
       if(applicationContext.containsBean(name)) {
           Object bean = applicationContext.getBean(name);
           if (bean.getClass().isAssignableFrom(clazz)) {
               return (T) bean;
           } else {
               throw new RuntimeException("BeanName 重复 " + name);
           }
       }

       BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
       for (Object arg : args) {
           beanDefinitionBuilder.addConstructorArgValue(arg);
       }
       BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();

       BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
       beanFactory.registerBeanDefinition(name, beanDefinition);
       return applicationContext.getBean(name, clazz);
   }
    public static void dispatch(ApplicationEvent event) {
 	   applicationContext.publishEvent(event);
    }
}