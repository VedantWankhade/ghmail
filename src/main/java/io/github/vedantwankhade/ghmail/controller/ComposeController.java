package io.github.vedantwankhade.ghmail.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.vedantwankhade.ghmail.model.Folder;
import io.github.vedantwankhade.ghmail.repository.FolderRepository;
import io.github.vedantwankhade.ghmail.service.FolderService;

@Controller
public class ComposeController {

	@Autowired
	private FolderRepository folderRepository;

	@Autowired
	private FolderService folderService;

	@GetMapping(value = "/compose")
	public String getComposePage(@RequestParam(required = false) String to,
			@AuthenticationPrincipal OAuth2User principal, Model model) {
		if (principal == null || !StringUtils.hasText(principal.getAttribute("login")))
			return "index";

		String userId = principal.getAttribute("login");

		model.addAttribute("userId", userId);

		List<Folder> userFolders = folderRepository.findAllByUserId(userId);
		model.addAttribute("userFolders", userFolders);

		List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
		model.addAttribute("defaultFolders", defaultFolders);

		if (StringUtils.hasText(to)) {
			List<String> uniqueToIds = Arrays.asList(to.split(",")).stream()
					.map(id -> StringUtils.trimWhitespace(id)).filter(id -> StringUtils.hasText(id)).distinct()
					.collect(Collectors.toList());

			model.addAttribute("toIds", String.join(", ", uniqueToIds));
		}
		return "compose-page";
	}

}
