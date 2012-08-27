package mailsync.services;

import java.io.IOException;
import java.util.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

public class MessageDTO {
	String recipients;
	String from;
	String subject;
	String content;
	Date sent;
	Message m;

	public int hashCode() {
		return Objects.hashCode(recipients, from, subject, content, sent);
	}

	public boolean equals(MessageDTO other) {
		return other.hashCode() == this.hashCode();
	}

	static public Set<MessageDTO> buildDtos(Message[] msgs) {
		HashSet<MessageDTO> messages = new HashSet<MessageDTO>();
		for (Message m : msgs) {
			messages.add(new MessageDTO(m));
		}
		return messages;
	}

	static public Message[] recoverMessages(Set<MessageDTO> msgs) {
		Message[] messages = new Message[msgs.size()];
		Iterator<MessageDTO> it = msgs.iterator();
		for (int i = 0; i <= msgs.size() - 1; i++) {
			messages[i] = it.next().getMessage();
		}
		return messages;
	}

	static public Message[] difference(Message[] listA, Message[] listB) {
		SetView<MessageDTO> difference = Sets.difference(buildDtos(listA),
				buildDtos(listB));
		System.out.println("Difference found: " + difference.size());
		return recoverMessages(difference);
	}

	public MessageDTO(Message m) {
		try {
			this.m = m;
			recipients = Joiner.on(",").join(m.getAllRecipients());
			from = m.getFrom().toString();
			sent = m.getSentDate();
			subject = m.getSubject();
			content = findContent(m);
		} catch (IOException e) {
			e.fillInStackTrace();
			content = "";
		} catch (MessagingException me) {
			me.fillInStackTrace();
		}
	}

	private String findContent(Message m) throws IOException,
			MessagingException {
		if (m.getContent() instanceof String)
			return (String) m.getContent();
		return ((MimeMultipart) m.getContent()).getBodyPart(0).toString();
	}

	public Message getMessage() {
		return m;
	}

}
