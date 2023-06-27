package com.gq.crm.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @date 2023/5/30
 */


@Configuration
@ComponentScan({"com.gq.crm.service","com.gq.crm.task"})
@PropertySource("classpath:db.properties")
@Import({JdbcConfig.class,MybatisConfig.class})
/*开启事务*/
@EnableTransactionManagement
/*开启任务调度   六个月 执行一次*/
@EnableScheduling
public class SpringConfig {
}
