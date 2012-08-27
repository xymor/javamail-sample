package mailsync.services;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import com.sun.mail.imap.IMAPFolder;

public class SessionManager {

	private String[] serverSettings;

	private String server;
	private String email;
	private String password;

	private String folderName;

	private Session session;

	public SessionManager(String folder, String server, String email,
			String password) {
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		props.put("mail.smtp.connectiontimeout", "10");
		props.put("mail.smtp.timeout", "1000");
		this.server = server;
		this.password = password;
		this.email = email;
		this.folderName = folder;
		session = Session.getDefaultInstance(props, null);
	}

	public IMAPFolder getImapFolder() throws MessagingException {
		IMAPFolder folder;
		Store sourceStore = session.getStore("imaps");
		sourceStore.connect(server, email, password);
		if (folderName == null) {
			folder = (IMAPFolder) sourceStore.getDefaultFolder();
		} else {
			folder = (IMAPFolder) sourceStore.getFolder(folderName);
		}
		return folder;
	}
}
