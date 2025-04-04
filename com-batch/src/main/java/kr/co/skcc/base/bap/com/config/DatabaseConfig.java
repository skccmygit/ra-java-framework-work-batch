package kr.co.skcc.base.bap.com.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@Profile("!test")
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager",
        basePackages = {"kr.co.skcc.base.bap.com.repository"}
)
@MapperScan(basePackages = {"kr.co.skcc.base.bap.com.repository"}, sqlSessionFactoryRef = "sqlSessionFactory")
@EnableTransactionManagement
public class DatabaseConfig {

    @Autowired
    ApplicationContext applicationContext;

    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.batch")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean
    public HikariDataSource dataSource() {
        return (HikariDataSource) dataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("dataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan(new String[]{"kr.co.skcc.base.bap.com.domain"
                                                ,"kr.co.skcc.base.com.common.domain"
                                                ,"kr.co.skcc.base.com.account.domain"});
        factory.setPersistenceUnitName("batch");

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
    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
        sessionFactory.setMapperLocations(applicationContext.getResources("classpath:mybatis/*.xml"));

        sessionFactory.setTypeAliasesPackage("kr.co.skcc.base.bap.com.domain");
        sessionFactory.setVfs(SpringBootVFS.class);

        return sessionFactory.getObject();
    }
    @Primary
    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}