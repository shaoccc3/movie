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
    
    @Value("${front.url}")
    private String frontUrl;
    
    public void sendEmail(String useremail,String token) {

        // 連接郵件服務器
        Mailer mailer = MailerBuilder
                .withSMTPServer(smtpHost, smtpPort, smtpUsername, smtpPassword)
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .buildMailer();
        

        // 建構郵件模板
        Email email = EmailBuilder.startingBlank()
                .from(smtpUsername)
                .to(useremail)
                .withSubject("星爆影城認證信")
                .withHTMLText("<html><body>" +
                        "<p>敬愛的客戶您好，歡迎註冊星爆影城會員:</p>" +
                        "<a href=\"" + frontUrl+"/verify-email/" + token + "\">點擊進行email認證</a>" +
                        "</body></html>")
                .buildEmail();

        // 發送郵件
        mailer.sendMail(email,/* async = */true);
    	
    }
    
    public void sendForgetPasswordEmail(String useremail,String token) {
        // 配置邮件服务器
        // 連接郵件服務器
        Mailer mailer = MailerBuilder
                .withSMTPServer(smtpHost, smtpPort, smtpUsername, smtpPassword)
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .buildMailer();

        // 建構郵件模板
        Email email = EmailBuilder.startingBlank()
                .from(smtpUsername)
                .to(useremail)
                .withSubject("星爆影城:忘記密碼重製信")
                .withHTMLText("<html><body>" +
                        "<p>敬愛的客戶您好:</p>" +
                        "<a href=\"" + frontUrl+"/reset-password/" + token + "\">點擊進行密碼重置</a>" +
                        "</body></html>")
//                .withPlainText("請點擊以下連接進行忘記密碼重製：http://localhost:5173/reset-password/" + token)
                .buildEmail();

        // 發送郵件
        mailer.sendMail(email,/* async = */true);
    	
    }
    
    
    

}
