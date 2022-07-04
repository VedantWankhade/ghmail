package io.github.vedantwankhade.ghmail.model;

import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;

import io.github.vedantwankhade.ghmail.model.utilmodel.EmailListItemCompoundKey;

@Table(value = "messages_by_user_and_folder")
public class EmailListItem {

	@PrimaryKey
	private EmailListItemCompoundKey key;

	@CassandraType(type = Name.LIST, typeArguments = Name.TEXT)
	private List<String> to;

	@CassandraType(type = Name.TEXT)
	private String subject;

	@CassandraType(type = Name.BOOLEAN)
	private boolean isUnread;

	@Transient
	private String humanTime;
	
	public EmailListItemCompoundKey getKey() {
		return key;
	}

	public void setKey(EmailListItemCompoundKey key) {
		this.key = key;
	}

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public boolean isUnread() {
		return isUnread;
	}

	public void setUnread(boolean isUnread) {
		this.isUnread = isUnread;
	}

	public String getHumanTime() {
		return humanTime;
	}

	public void setHumanTime(String humanTime) {
		this.humanTime = humanTime;
	}
}
