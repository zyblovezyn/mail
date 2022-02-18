package com.wupx.service;

import com.wupx.beans.Staff;
import com.wupx.beans.StaffStatus;

import java.io.IOException;
import java.util.List;

/**
 * @author Miles
 * @version 1.0.0
 * @Description MailService
 * @ClassName MailService.java
 * @createTime 2022-02-15 14:52:00
 */
public interface MailService {
    List<StaffStatus> getStaffStatus();

    String sendAllMail(String template);

    String sendSalaryMail();

    String sendBonusMail();

    String sendSourceTicketMail();

    List<Staff> getSalaryStaffs() throws IOException;

    List<Staff> getBonusStaffs() throws IOException;

    List<Staff> getSourceTicketStaffs() throws IOException;

    String getDateStr();

    void clearStaffStatus();
}
