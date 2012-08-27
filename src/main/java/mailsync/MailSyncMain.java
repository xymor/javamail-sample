package mailsync;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Session;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import mailsync.services.FolderManager;
import mailsync.services.SessionManager;


public class MailSyncMain {
	
    public static void main(String[] args) throws IOException, MessagingException {
    	
    	ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
    	
    	SessionManager sm = (SessionManager) context.getBean("sessionManager");
    	SessionManager sm2 = (SessionManager) context.getBean("sessionManager2");
    	FolderManager fm = (FolderManager) context.getBean("folderManager");
    	
    	fm.synchronizeFolders(sm.getImapFolder(), sm2.getImapFolder());

    	System.out.println("ok");
    }
}
