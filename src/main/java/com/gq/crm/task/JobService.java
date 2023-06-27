package com.gq.crm.task;

import com.gq.crm.service.impl.CustomerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @date 2023/6/19
 */

@Service
public class JobService {
    @Resource
    private CustomerService customerService;

    /**
     * 开启定时任务  通过定时任务盗用方法，流转流失的客户数据到客户流失表中
     */

    /*每六个月执行一次*/
    @Scheduled(cron ="0 0 0 6 */6 ?")
//    @Scheduled(cron ="0/10 * * * * ? ")
    public void job(){
        System.out.println("定时任务开始执行 --> " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        customerService.updateCustomerState();
    }
}
