package com.skcc.ra.scheduler.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "quartzEntityManagerFactory",
        transactionManagerRef = "quartzTransactionManager",
        basePackages = {"com.skcc.ra.scheduler.repository"}
)
@EnableTransactionManagement
public class QuartzDBConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.quartz")
    public DataSourceProperties quartzDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public HikariDataSource quartzDataSource() {
        return (HikariDataSource) quartzDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean quartzEntityManagerFactory(@Qualifier("quartzDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan(new String[]{"com.skcc.ra.scheduler.domain"});
        factory.setPersistenceUnitName("quartz");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factory.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        properties.put("hibernate.hbm2ddl.auto", "none");
        properties.put("hibernate.id.new_generator_mappings", "false");
        properties.put("hibernate.show_sql", "true");
        factory.setJpaPropertyMap(properties);

        return factory;
    }

    @Bean
    public PlatformTransactionManager quartzTransactionManager(@Qualifier("quartzEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
