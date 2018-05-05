package com.activedge.report.db;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.activedge.report.constant.CustomConstants;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.activedge.report.model", "com.activedge.report.repo"})
public class DataSourceConfig {
	@Value("classpath:db-script/test-data.sql")
	private Resource MYSQL_DATA_SCRIPT;
	
	@Bean	
    public DataSource devDataSource() {
        BasicDataSource dataSourceConfig = new BasicDataSource();
        dataSourceConfig.setDriverClassName(CustomConstants.APP_DB_DRIVER);

        dataSourceConfig.setUrl(CustomConstants.APP_DB_URL);
        dataSourceConfig.setUsername(CustomConstants.APP_DB_USER);
        dataSourceConfig.setValidationQuery("SELECT 1");
        dataSourceConfig.setPassword(CustomConstants.APP_DB_PASSWORD);
        return dataSourceConfig;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

    @Bean
    public HibernateJpaVendorAdapter devJpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(CustomConstants.APP_DATABASE);
        adapter.setShowSql(false);
        adapter.setDatabasePlatform(CustomConstants.APP_DB_HIBERNATE_PLATFORM);
        adapter.setGenerateDdl(true);
        return adapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean emfb =
                new LocalContainerEntityManagerFactoryBean();
        emfb.setDataSource(dataSource);
        emfb.setJpaVendorAdapter(jpaVendorAdapter);
        emfb.setPackagesToScan("com.activedge.report.model", "com.activedge.report.repo");
        emfb.setJpaProperties(additionalProperties());
        return emfb;
    }
    
    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {

        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator());
        return initializer;
    }


    private DatabasePopulator databasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(MYSQL_DATA_SCRIPT);
        return populator;
    }


    private Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", 
        		CustomConstants.APP_DB_HIBERNATE_DDL_AUTO);
        return properties;
    }
}
