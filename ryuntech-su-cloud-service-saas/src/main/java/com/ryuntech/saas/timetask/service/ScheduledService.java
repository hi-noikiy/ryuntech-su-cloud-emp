package com.ryuntech.saas.timetask.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 定时任务类
 * </p>
 *
 * @author wanh
 * @since 2019-12-06
 */
@Slf4j
@Component
public class ScheduledService {

//    @Autowired
//    private MailSendService mailSendService;
//
//    @Autowired
//    private IPlanExpireRemindService iPlanExpireRemindService;
//
//    @Autowired
//    private IPlanOverdueRemindService iPlanOverdueRemindService;
//
//    @Scheduled(cron = "0 30 8 * * *")
//    public void scheduled(){
//
//        System.out.println("============应收计划到期提醒===============");
//        List<Email> emailList = iPlanExpireRemindService.getEmails();
//        if (emailList == null || emailList.size() == 0) {
//            return;
//        }
//        for(Email email : emailList) {
//            mailSendService.sendWithHTMLTemplate(email);
//        }
//        System.out.println("==========================================");
//
//        System.out.println("============应收计划逾期提醒===============");
//        List<Email> emailList1 = iPlanOverdueRemindService.getEmails();
//        if (emailList1 == null || emailList1.size() == 0) {
//            return;
//        }
//        for(Email email : emailList1) {
//            mailSendService.sendWithHTMLTemplate(email);
//        }
//        System.out.println("==========================================");
//    }
}
