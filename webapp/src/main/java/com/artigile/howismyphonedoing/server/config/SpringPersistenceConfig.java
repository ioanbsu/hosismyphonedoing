package com.artigile.howismyphonedoing.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jdo.JdoTransactionManager;
import org.springframework.orm.jdo.LocalPersistenceManagerFactoryBean;
import org.springframework.orm.jdo.TransactionAwarePersistenceManagerFactoryProxy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

/**
 * User: ioanbsu
 * Date: 6/13/13
 * Time: 9:40 AM
 */
@Configuration
@EnableTransactionManagement
public class SpringPersistenceConfig implements TransactionManagementConfigurer {
/*
    @Bean(name = "transactionManager")
    public JdoTransactionManager jdoTransactionManager() throws PropertyVetoException {
        return new JdoTransactionManager();
    }*/

    @Bean
    public LocalPersistenceManagerFactoryBean localPersistenceManagerFactoryBean() {
        LocalPersistenceManagerFactoryBean localPersistenceManagerFactoryBean = new LocalPersistenceManagerFactoryBean();
        localPersistenceManagerFactoryBean.setPersistenceManagerFactoryName("transactions-optional");
        return localPersistenceManagerFactoryBean;
    }

    @Bean(name = "pmfTransactionAware")
    public TransactionAwarePersistenceManagerFactoryProxy transactionAwarePersistenceManagerFactoryProxy() {
        TransactionAwarePersistenceManagerFactoryProxy transactionAwarePersistenceManagerFactoryProxy = new TransactionAwarePersistenceManagerFactoryProxy();
        transactionAwarePersistenceManagerFactoryProxy.setTargetPersistenceManagerFactory(localPersistenceManagerFactoryBean().getObject());
        transactionAwarePersistenceManagerFactoryProxy.setAllowCreate(false);
        return transactionAwarePersistenceManagerFactoryProxy;
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        JdoTransactionManager jdoTransactionManager = new JdoTransactionManager();
        jdoTransactionManager.setPersistenceManagerFactory(localPersistenceManagerFactoryBean().getObject());
        return jdoTransactionManager;
    }
}
