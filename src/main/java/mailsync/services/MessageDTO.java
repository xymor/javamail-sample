package mailsync.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

public class MessageDTO {
	private String recipients;
	private String from;
	private String subject;
	private String content;
	private Date sent;
	private Message message;

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
	
	static public Message[] differenceWithNewSession(Session s, Message[] listA, Message[] listB){
		Message[] messages = difference(listA,listB); 
		Message[] newSessionMessages = new Message[messages.length];
		for(int i = 0; i <= messages.length -1; i++){
			newSessionMessages[i] = changeSession(messages[1], s);
		}
		return newSessionMessages;
	}

	public MessageDTO(Message m) {
		try {
			this.message = m;
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
	
	
	public static Message changeSession(Message m, Session s){
		Message newMessage = null;
		try{
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			m.writeTo(outputStream);
			ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
			newMessage = new MimeMessage(s, inputStream);
		}catch(IOException e){ e.printStackTrace(); }
		catch(MessagingException e){ e.printStackTrace(); }
		return newMessage;
	}
	
	private String findContent(Message m) throws IOException,
			MessagingException {
		if (m.getContent() instanceof String)
			return (String) m.getContent();
		return ((MimeMultipart) m.getContent()).getBodyPart(0).toString();
	}

	public Message getMessage() {
		return message;
	}

}
