package com.wupx.controller;

import com.wupx.beans.Staff;
import com.wupx.beans.StaffStatus;
import com.wupx.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author wupx
 * @date 2020/08/12
 */
@RestController
public class MailController {

    @Autowired
    private MailService mailService;

    @GetMapping("/getDataStr")
    @CrossOrigin
    public String getDataStr() {
        return mailService.getDateStr();
    }

    @GetMapping("/getStaffStatus")
    @CrossOrigin
    public List<StaffStatus> getStaffStatus() {
        return mailService.getStaffStatus();
    }

    @GetMapping("/getSalaryStaffs")
    @CrossOrigin
    public List<Staff> getSalaryStaffs() {
        try {
            return mailService.getSalaryStaffs();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/getBonusStaffs")
    @CrossOrigin
    public List<Staff> getBonusStaffs() {
        try {
            return mailService.getBonusStaffs();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/getSourceTicketStaffs")
    @CrossOrigin
    public List<Staff> getSourceTicketStaffs() {
        try {
            return mailService.getSourceTicketStaffs();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/clearStaffStatus")
    @CrossOrigin
    public void clearStaffStatus() {
        mailService.clearStaffStatus();
    }


    /**
     * 发送给与邮件
     *
     * @param
     * @return
     */
    @GetMapping("/sendAllMail")
    @CrossOrigin
    public String sendAllMail(String template) {
        return mailService.sendAllMail(template);
    }

    /**
     * 发送给与邮件
     *
     * @param
     * @return
     */
    @GetMapping("/sendSalaryMail")
    @CrossOrigin
    public String sendSalaryMail() {
        return mailService.sendSalaryMail();
    }

    /**
     * 发送赏与邮件
     *
     * @param
     * @return
     */
    @GetMapping("/sendBonusMail")
    @CrossOrigin
    public String sendBonusMail() {
        return mailService.sendBonusMail();
    }

    /**
     * 发送源泉票邮件
     *
     * @param
     * @return
     */
    @GetMapping("/sendSourceTicketMail")
    @CrossOrigin
    public String sendSourceTicketMail() {
        return mailService.sendSourceTicketMail();
    }

}
