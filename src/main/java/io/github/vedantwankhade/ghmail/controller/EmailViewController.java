package io.github.vedantwankhade.ghmail.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.vedantwankhade.ghmail.model.Email;
import io.github.vedantwankhade.ghmail.model.EmailListItem;
import io.github.vedantwankhade.ghmail.model.Folder;
import io.github.vedantwankhade.ghmail.model.utilmodel.EmailListItemCompoundKey;
import io.github.vedantwankhade.ghmail.repository.EmailListItemRepository;
import io.github.vedantwankhade.ghmail.repository.EmailRepository;
import io.github.vedantwankhade.ghmail.repository.FolderRepository;
import io.github.vedantwankhade.ghmail.repository.UnreadEmailStatsRepository;
import io.github.vedantwankhade.ghmail.service.FolderService;

@Controller
public class EmailViewController {
	
	@Autowired
	private FolderRepository folderRepository;

	@Autowired
	private EmailRepository emailRepository;
	
	@Autowired
	private FolderService folderService;
	
	@Autowired
	private EmailListItemRepository emailListItemRepository;
	
	@Autowired
	private UnreadEmailStatsRepository unreadEmailStatsRepository;
	
	@GetMapping(value = "/emails/{id}")
	public String emailView(@RequestParam String folder, @PathVariable UUID id, @AuthenticationPrincipal OAuth2User principal, Model model) {

		if (principal == null || !StringUtils.hasText(principal.getAttribute("login")))
			return "index";

		String userId = principal.getAttribute("login");

		model.addAttribute("userId", userId);

		List<Folder> userFolders = folderRepository.findAllByUserId(userId);
		model.addAttribute("userFolders", userFolders);

		List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
		model.addAttribute("defaultFolders", defaultFolders);

		Optional<Email> optionalEmail = emailRepository.findById(id);
		
		if (optionalEmail.isEmpty())
			return "inbox-page";
		
		Email email = optionalEmail.get();
		
		if (!(userId.equals(email.getFrom()) || email.getTo().contains(userId)))
			return "redirect:/";
		
		model.addAttribute("email", email);
		String toIds = String.join(", ", email.getTo());
		
		model.addAttribute("toIds", toIds);
		
		EmailListItemCompoundKey key = new EmailListItemCompoundKey();
		key.setUserId(userId);
		key.setLabel(folder);
		key.setTimeUUID(email.getTimeUUID());
		
		Optional<EmailListItem> optionalEmailListItem = emailListItemRepository.findById(key);
		
		if (optionalEmailListItem.isPresent()) {
			EmailListItem emailListItem = optionalEmailListItem.get();
			
			if (emailListItem.isUnread()) {
				emailListItem.setUnread(false);
				emailListItemRepository.save(emailListItem);
				
				unreadEmailStatsRepository.decrementUnreadCount(userId, folder);
			}
		}
		model.addAttribute("unreadStats", folderService.mapCountToLabels(userId));
		return "email-page";
	}
}
