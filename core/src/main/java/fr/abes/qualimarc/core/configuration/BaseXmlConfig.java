package fr.abes.qualimarc.core.configuration;


import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "baseXmlEntityManager",
        basePackages = "fr.abes.qualimarc.core.repository.basexml")
@NoArgsConstructor
@BaseXMLConfiguration
public class BaseXmlConfig {
    @Value("${spring.jpa.basexml.show-sql}")
    protected String showsql;
    @Value("${spring.jpa.basexml.properties.hibernate.dialect}")
    protected String dialect;
    @Value("${spring.jpa.basexml.hibernate.ddl-auto}")
    protected String ddlAuto;
    @Value("${spring.jpa.basexml.database-platform}")
    protected String platform;


    protected void configHibernate(LocalContainerEntityManagerFactoryBean em) {
        HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("spring.jpa.database-platform", platform);
        properties.put("hibernate.show_sql", showsql);
        properties.put("hibernate.format_sql", true);
        properties.put("hibernate.dialect", dialect);
        properties.put("logging.level.org.hibernate", "DEBUG");
        properties.put("hibernate.type", "trace");
        em.setJpaPropertyMap(properties);
    }

    @Bean
    @ConfigurationProperties("spring.datasource.basexml")
    public DataSourceProperties baseXmlDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource baseXmlDataSource() {
        return baseXmlDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean baseXmlEntityManager() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(baseXmlDataSource());
        em.setPackagesToScan(
                new String[]{"fr.abes.qualimarc.core.model.entity.basexml"});
        configHibernate(em);
        return em;
    }
}