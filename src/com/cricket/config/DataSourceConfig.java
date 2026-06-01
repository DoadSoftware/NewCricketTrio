package com.cricket.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    /**
     * Builds the RoutingDataSource with all three target databases.
     * Kept as a separate bean so LazyConnectionDataSourceProxy can wrap it.
     */
    @Bean
    public DataSource routingDataSource() {

        // LOCAL (default)
        DriverManagerDataSource local = new DriverManagerDataSource();
        local.setDriverClassName("net.ucanaccess.jdbc.UcanaccessDriver");
        local.setUrl("jdbc:ucanaccess://C:\\Sports\\Cricket\\Database\\CricketTeams.mdb;");

        // MEN  — fixed: was \\\\ (double backslash), now correct single \\
        DriverManagerDataSource men = new DriverManagerDataSource();
        men.setDriverClassName("net.ucanaccess.jdbc.UcanaccessDriver");
        men.setUrl("jdbc:ucanaccess://C:\\Sports\\CricketMen\\Database\\CricketTeams.mdb;");

        // WOMEN — fixed: was \\\\ (double backslash), now correct single \\
        DriverManagerDataSource women = new DriverManagerDataSource();
        women.setDriverClassName("net.ucanaccess.jdbc.UcanaccessDriver");
        women.setUrl("jdbc:ucanaccess://C:\\Sports\\CricketWomen\\Database\\CricketTeams.mdb;");

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("LOCAL", local);
        targetDataSources.put("MEN",   men);
        targetDataSources.put("WOMEN", women);

        RoutingDataSource routing = new RoutingDataSource();
        routing.setTargetDataSources(targetDataSources);
        routing.setDefaultTargetDataSource(local);
        routing.afterPropertiesSet(); // required when building manually outside Spring lifecycle
        return routing;
    }

    /**
     * KEY FIX: Wraps the routing datasource in LazyConnectionDataSourceProxy.
     *
     * Without this, Hibernate acquires a physical connection at startup
     * (for hbm2ddl schema inspection) BEFORE DatabaseContextHolder.setDb()
     * is ever called. At that moment the routing key is null, so Spring falls
     * back to LOCAL and Hibernate caches that connection forever — meaning
     * every CricketService call always reads from LOCAL no matter what you
     * set later.
     *
     * LazyConnectionDataSourceProxy delays the physical connection until the
     * first SQL statement is actually executed, by which time the controller
     * has already called DatabaseContextHolder.setDb("MEN") or setDb("WOMEN"),
     * so the correct database is selected.
     */
    @Bean
    public DataSource dataSource() {
        return new LazyConnectionDataSourceProxy(routingDataSource());
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.cricket.model");

        Properties properties = new Properties();
        properties.put("hibernate.show_sql", "true");
        // Changed from "update" to "none".
        // "update" caused Hibernate to probe the schema at startup against
        // the wrong (LOCAL) database before any request had set the context.
        properties.put("hibernate.hbm2ddl.auto", "none");
        properties.put("hibernate.archive.autodetection", "class");
        properties.put("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
        sessionFactory.setHibernateProperties(properties);

        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }
}