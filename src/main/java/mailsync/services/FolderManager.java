package mailsync.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.mail.Folder;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.sun.mail.imap.IMAPFolder;

public class FolderManager {

	public void synchronizeFolders(Session leftSession, Session rightSession, Folder left, Folder right)
			throws MessagingException  {
		openFolder(left);
		openFolder(right);
		Message[] leftMessages = left.getMessages();
		Message[] rightMessages = right.getMessages();

		Message[] messagesToCopyRight = MessageDTO.differenceWithNewSession(rightSession , leftMessages,
				rightMessages);
		Message[] messagesToCopyLeft = MessageDTO.differenceWithNewSession(leftSession, rightMessages,
				leftMessages);
		
		System.out.println("Messages to be synched: "
				+ (messagesToCopyLeft.length + messagesToCopyRight.length));

		left.appendMessages(messagesToCopyLeft);
		right.appendMessages(messagesToCopyRight);
		
		left.close(false);
		right.close(false);
		System.out.println("Done.");
	}

	private void openFolder(Folder f) throws MessagingException {
		if (!f.exists()) {
			f.create(Folder.HOLDS_MESSAGES);
		}
		f.open(Folder.READ_WRITE);
	}
}
