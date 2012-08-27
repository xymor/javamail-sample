package mailsync;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Session;

import mailsync.services.FolderManager;
import mailsync.services.SessionManager;


public class MailSyncMain {
	
    public static void main(String[] args) throws IOException, MessagingException {
    	
    	SessionManager sm = new SessionManager("SquadMail","imap.gmail.com",System.getProperty("gmailaddress"),System.getProperty("gmailpass"));
    	SessionManager sm2 = new SessionManager("SquadMail","imap.gmail.com",System.getProperty("gmailaddress2"),System.getProperty("gmailpass2"));
    	FolderManager fm = new FolderManager();
    	
    	fm.synchronizeFolders(sm.getImapFolder(), sm2.getImapFolder());

    	System.out.println("ok");
    }
}
