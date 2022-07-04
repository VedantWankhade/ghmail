package io.github.vedantwankhade.ghmail.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import io.github.vedantwankhade.ghmail.model.Folder;
import io.github.vedantwankhade.ghmail.repository.FolderRepository;
import io.github.vedantwankhade.ghmail.service.FolderService;

@Controller
public class InboxController {
	
	@Autowired
	private FolderRepository folderRepository;
	
	@Autowired
	private FolderService folderService;

	@GetMapping(value = "/")
	public String homePage(@AuthenticationPrincipal OAuth2User principal, Model model) {
		
		if (principal == null || !StringUtils.hasText(principal.getAttribute("login")))
			return "index";
		
		String userId = principal.getAttribute("login");
		
		model.addAttribute("userId", userId);
		
		List<Folder> userFolders = folderRepository.findAllByUserId(userId);
		model.addAttribute("userFolders", userFolders);
		
		List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
		model.addAttribute("defaultFolders", defaultFolders);
		
		return "inbox-page";
	}
}
