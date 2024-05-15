package com.ispan.theater.util;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderComponent {
	

    @Value("${mail.smtp.host}")
    private String smtpHost;

    @Value("${mail.smtp.port}")
    private int smtpPort;

    @Value("${mail.smtp.username}")
    private String smtpUsername;

    @Value("${mail.smtp.password}")
    private String smtpPassword;

    @Value("${mail.smtp.tls}")
    private boolean smtpTLS;
    
    public void sendEmail(String useremail,String token) {
        // 配置邮件服务器
        Mailer mailer = MailerBuilder
                .withSMTPServer(smtpHost, smtpPort, smtpUsername, smtpPassword)
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .buildMailer();
        
        // 构建邮件
        Email email = EmailBuilder.startingBlank()
                .from(smtpUsername)
                .to(useremail)
                .withSubject("ispanmove認證信")
                .withPlainText("請點擊以下連接進行驗證：http://localhost:5173/user/verify-email/" + token)
                .buildEmail();
        // 发送邮件
        mailer.sendMail(email,/* async = */true);
    	
    }
    
    
    
    
    
    

}
