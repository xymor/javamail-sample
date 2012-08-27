package mailsync;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Session;

import mailsync.services.FolderManager;
import mailsync.services.SessionManager;


public class MailSyncMain {
	
    public static void main(String[] args) throws IOException, MessagingException {
    	
    	SessionManager sm = new SessionManager("SquadMail","imap.gmail.com","raphaelmiranda@gmail.com",System.getProperty("gmailpass"));
    	SessionManager sm2 = new SessionManager("SquadMail","imap.gmail.com","raphael@gimajam.com",System.getProperty("gimapass"));
    	FolderManager fm = new FolderManager();
    	
    	fm.synchronizeFolders(sm.getImapFolder(), sm2.getImapFolder());

    	System.out.println("ok");
    }
}
