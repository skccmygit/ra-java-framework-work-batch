package kr.co.skcc.base.com.common.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

// routing database 설정
@Configuration
@EnableTransactionManagement
public class RoutingDatabaseConfig {
//
//    @Primary
//    @Bean
//    @DependsOn({"routingDataSource"})
//    public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
//        return new LazyConnectionDataSourceProxy(routingDataSource);
//    }
//    @Bean(name = "routingDataSource")
//    public DataSource routingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,@Qualifier("slaveDataSource") DataSource slaveDataSource) {
//        RoutingDataSource routingDataSource = new RoutingDataSource();
//        HashMap<Object, Object> dataSourceMap = new HashMap<>();
//        dataSourceMap.put("master", masterDataSource);
//        dataSourceMap.put("slave", slaveDataSource);
//        routingDataSource.setTargetDataSources(dataSourceMap);
//        routingDataSource.setDefaultTargetDataSource(masterDataSource);
//        return routingDataSource;
//    }
//    @Bean(name = "masterDataSource")
//    @ConfigurationProperties(prefix="spring.datasource.master")
//    public DataSource masterDataSource() {
//        return DataSourceBuilder.create()
//                .type(HikariDataSource.class)
//                .build();
//    }
//    @Bean(name = "slaveDataSource")
//    @ConfigurationProperties(prefix="spring.datasource.slave")
//    public DataSource slaveDataSource() {
//        return DataSourceBuilder.create()
//                .type(HikariDataSource.class)
//                .build();
//    }
}
