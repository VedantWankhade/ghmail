package io.github.vedantwankhade.ghmail.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import io.github.vedantwankhade.ghmail.model.Email;
import io.github.vedantwankhade.ghmail.model.EmailListItem;
import io.github.vedantwankhade.ghmail.model.utilmodel.EmailListItemCompoundKey;
import io.github.vedantwankhade.ghmail.repository.EmailListItemRepository;
import io.github.vedantwankhade.ghmail.repository.EmailRepository;
import io.github.vedantwankhade.ghmail.repository.UnreadEmailStatsRepository;

@Service
public class EmailService {

	@Autowired
	private EmailRepository emailRepository;
	
	@Autowired
	private EmailListItemRepository emailListItemRepository;
	
	@Autowired
	private UnreadEmailStatsRepository unreadEmailStatsRepository;
	
	public void sendEmail(String from, List<String> to, String subject, String body) {
		
		Email email = new Email();
		email.setTo(to);
		email.setFrom(from);
		email.setSubject(subject);
		email.setBody(body);
		email.setTimeUUID(Uuids.timeBased());
		
		emailRepository.save(email);
		
		to.forEach(toId -> {
			EmailListItem item = createEmailListItem(to, subject, email, toId, "Inbox");
			emailListItemRepository.save(item);
			unreadEmailStatsRepository.incrementUnreadCount(toId, "Inbox");
		});
		
		EmailListItem sentItem = createEmailListItem(to, subject, email, from, "Sent Items");
		sentItem.setUnread(false);
		emailListItemRepository.save(sentItem);
	}

	private EmailListItem createEmailListItem(List<String> to, String subject, Email email, String itemOwner, String folder) {
		EmailListItemCompoundKey key = new EmailListItemCompoundKey();
		
		key.setUserId(itemOwner);
		key.setLabel(folder);
		key.setTimeUUID(email.getTimeUUID());
		
		EmailListItem item = new EmailListItem();
		
		item.setKey(key);
		item.setTo(to);
		item.setSubject(subject);
		item.setUnread(true);
		return item;
	}
}
