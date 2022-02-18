package com.wupx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wupx.beans.*;
import com.wupx.mapper.*;
import com.wupx.service.MailService;
import com.wupx.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * @author wupx
 * @date 2020/08/12
 */
@Service
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String from;

    @Value("${server.port}")
    private String port;

    @Value("${fileFromDb}")
    private boolean fileFromDb;

    @Value("${deletePDF}")
    private boolean deletePDF;

    @Value("${jpyear}")
    private String jpyear;

    private String name_last = "さん";
    private String salary_last = "給与明細";
    private String bonus_last = "賞与明細";
    private String sourceTicket_last = "源泉徴収票";

    private String salary = "salary";
    private String bonus = "bonus";
    private String sourceTicket = "sourceTicket";

    private HashMap<String, Staff> errHashMap = new HashMap<>();

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Resource
    private MailDao mailDao;

    @Resource
    private StaffStatusDao staffStatusDao;

    @Resource
    private BonusDao bonusDao;

    @Resource
    private SalaryDao salaryDao;

    @Resource
    private SourceTicketDao sourceTicketDao;


    /**
     * 发送简单邮件
     *
     * @param from
     * @param to
     * @param subject
     * @param text
     */
    public void sendSimpleMail(String from, String to, String subject, String text) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        // 发件人
        simpleMailMessage.setFrom(from);
        // 收件人
        simpleMailMessage.setTo(to);
        // 邮件主题
        simpleMailMessage.setSubject(subject);
        // 邮件内容
        simpleMailMessage.setText(text);
        javaMailSender.send(simpleMailMessage);
    }

    /**
     * 发送复杂邮件
     *
     * @return
     */
    public ResponseEntity<String> sendMimeMail() {
        String from = "roumu2@nisshin-sci.co.jp";
        String to = "pual.office@gmail.com";
        String subject = "HHHH";
        String text = "123456789";
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            // 设置邮件内容，第二个参数设置是否支持 text/html 类型
            helper.setText(text, true);
            helper.addInline("logo", new ClassPathResource("doc/0198.pdf"));
            //helper.addAttachment("logo.pdf", new ClassPathResource("doc/logo.pdf"));
            javaMailSender.send(mimeMessage);
            return ResponseEntity.status(HttpStatus.CREATED).body("送信成功");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("e.getMessage()");
        }
    }

    /**
     * 发送给与邮件
     *
     * @param from
     * @param to
     * @param subject
     * @param context
     * @return
     */
    public ResponseEntity<String> sendTemplateMail(String from, String to, String subject, Context context) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            // 解析邮件模板
            String text = templateEngine.process("salary", context);
            helper.setText(text, true);
            helper.addAttachment("0198.pdf", new ClassPathResource("doc/0198.pdf"));
            javaMailSender.send(message);
            return ResponseEntity.status(HttpStatus.CREATED).body("送信成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("e.getMessage()");
        }
    }

    @Override
    public List<StaffStatus> getStaffStatus() {
        QueryWrapper queryWrapper = new QueryWrapper();
        List<StaffStatus> staffList = staffStatusDao.selectList(queryWrapper);
        return staffList;
    }

    @Override
    public String sendAllMail(String template) {
        try {
            List<Staff> list;
            if (fileFromDb) {
                list = getStaffsByDb(template);
            } else {
                list = getStaffs(template);
            }
            list.stream().forEach(it -> {
                boolean sended = true;
                try {
                    sendMail(it, template);
                } catch (Exception e) {
                    e.printStackTrace();
                    errHashMap.put(it.getEmployeeId(), it);
                    sended = false;
                    try {
                        sendMail(it, template);
                        errHashMap.remove(it.getEmployeeId());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        errHashMap.put(it.getEmployeeId(), it);
                        sended = false;
                    }
                }
                if (!sended) {
                    try {
                        StaffStatus staffStatus = StaffStatus.builder()
                                .email(it.getEmail())
                                .name(it.getName())
                                .employeeId(it.getEmployeeId())
                                .status("送信失敗").build();
                        QueryWrapper<StaffStatus> queryWrapper = new QueryWrapper<>();
                        queryWrapper.setEntity(staffStatus);
                        staffStatusDao.delete(queryWrapper);
                        staffStatusDao.insert(staffStatus);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(errHashMap.size());
    }

    /**
     * 发送给与邮件
     *
     * @return
     */
    @Override
    public String sendSalaryMail() {
        return sendAllMail(salary);
    }

    /**
     * 发送bonus邮件
     *
     * @return
     */
    @Override
    public String sendBonusMail() {
        return sendAllMail(bonus);
    }

    /**
     * 发送bonus邮件
     *
     * @return
     */
    @Override
    public String sendSourceTicketMail() {
        return sendAllMail(sourceTicket);
    }

    @Override
    public List<Staff> getSalaryStaffs() throws IOException {
        if (fileFromDb) {
            return getStaffsByDb(salary);
        } else {
            return getStaffs(salary);
        }
    }

    @Override
    public List<Staff> getBonusStaffs() throws IOException {
        if (fileFromDb) {
            return getStaffsByDb(bonus);
        } else {
            return getStaffs(bonus);
        }
    }

    @Override
    public List<Staff> getSourceTicketStaffs() throws IOException {
        if (fileFromDb) {
            return getStaffsByDb(sourceTicket);
        } else {
            return getStaffs(sourceTicket);
        }
    }

    @Override
    public String getDateStr() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月度";
    }

    @Override
    public void clearStaffStatus() {
        staffStatusDao.deleteAll();
    }

    private String getDateStr1(String template) {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) + "年";
    }

    private List<Staff> getStaffs(String template) throws IOException {
        String dirPath = System.getProperty("user.dir");
//        String path = dirPath + "/" + template + "/staff_list.txt";
        String path = dirPath + "/staff_list.txt";
        System.out.println(path);
        File file = new File(path);
        /*ClassPathResource classPathResource = new ClassPathResource(dirPath + "/" + template + "/staff_list.txt");
        System.out.println(classPathResource.getPath());
        InputStream inputStream = classPathResource.getInputStream();*/
        List<String> staffArr = FileUtils.getListLines(file);

        List<Staff> staffList = new ArrayList<>();

        staffArr.stream().forEach(it -> {
            if (!StringUtils.isEmpty(it)) {
                String[] strs = it.split(",");
                if (strs.length == 3) {
                    Staff staff = Staff.builder()
                            .employeeId(strs[0])
                            .name(strs[1])
                            .email(strs[2])
                            .build();
                    setSubject(staff, template);
                    staffList.add(staff);
                }
            }
        });
        return staffList;
    }

    private void setSubject(Staff staff, String template) {
        String subject = staff.getEmployeeId() + " " + staff.getName() + name_last + " " + getDateStr();
        switch (template) {
            case "salary":
                subject += salary_last;
                break;
            case "bonus":
                subject += bonus_last;
                break;
            case "sourceTicket":
                subject = staff.getEmployeeId() + " " + staff.getName() + name_last + " " + getDateStr1(template) + sourceTicket_last;
                break;
        }
        staff.setSubject(subject);
    }

    private List<Staff> getStaffsByDb(String template) {
        List<Staff> staffList = new ArrayList<>();
        switch (template) {
            case "salary":
                List<Salary> salaryList = salaryDao.selectList(new QueryWrapper<>());
                salaryList.stream().forEach(it -> {
                    Staff temp = Staff.builder()
                            .employeeId(it.getEmployeeId())
                            .name(it.getName())
                            .email(it.getEmail())
                            .build();
                    staffList.add(temp);
                });
                break;
            case "bonus":
                List<Bonus> bonusList = bonusDao.selectList(new QueryWrapper<>());
                bonusList.stream().forEach(it -> {
                    Staff temp = Staff.builder()
                            .employeeId(it.getEmployeeId())
                            .name(it.getName())
                            .email(it.getEmail())
                            .build();
                    staffList.add(temp);
                });
                break;
            case "sourceTicket":
                List<SourceTicket> sourceTicketList = sourceTicketDao.selectList(new QueryWrapper<>());
                sourceTicketList.stream().forEach(it -> {
                    Staff temp = Staff.builder()
                            .employeeId(it.getEmployeeId())
                            .name(it.getName())
                            .email(it.getEmail())
                            .build();
                    staffList.add(temp);
                });
                break;
        }
        staffList.stream().forEach(it -> setSubject(it, template));
        return staffList;
    }

    public String sendMail(Staff it, String template) {
        try {
            Context context = new Context();
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(this.from);
            helper.setTo(it.getEmail());
            helper.setSubject(it.getSubject());
            // 解析邮件模板
            context.setVariable("username", it.getName() + name_last);

            setContext(context, template);

            String text = templateEngine.process(template, context);
            helper.setText(text, true);

            //附件PDF
            String dirPath = System.getProperty("user.dir");
            String path = dirPath + File.separator + "給与" + File.separator + it.getEmployeeId() + ".pdf";
            System.out.println(path);
            File file = new File(path);
            if (file.exists()) {
                helper.addAttachment(it.getEmployeeId() + ".PDF", file);
                javaMailSender.send(message);
                if (deletePDF) {
                    FileUtils.moveTotherFolders(path, template);
                }
            } else {
                StaffStatus staffStatus = StaffStatus.builder()
                        .employeeId(it.getEmployeeId())
                        .name(it.getName())
                        .email(it.getEmail())
                        .status(it.getEmployeeId() + " PDF不存在").build();
                QueryWrapper<StaffStatus> queryWrapper = new QueryWrapper<>();
                queryWrapper.setEntity(staffStatus);
                staffStatusDao.delete(queryWrapper);
                staffStatusDao.insert(staffStatus);
            }
            return "送信成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "送信失敗";
        }
    }

    private void setContext(Context context, String template) {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        switch (template) {
            case "salary":
                context.setVariable("month", month);
                context.setVariable("year", year);
                break;
            case "bonus":
                if (month < 8) {
                    context.setVariable("period", "夏期");
                } else {
                    context.setVariable("period", "冬期");
                }

                break;
            case "sourceTicket":
                context.setVariable("jpyear", year - Integer.valueOf(jpyear));
                break;
        }
    }
}
