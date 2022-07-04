package io.github.vedantwankhade.ghmail.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import io.github.vedantwankhade.ghmail.model.Folder;

@Service
public class FolderService {

	public List<Folder> fetchDefaultFolders(String userId) {
		return Arrays.asList(
				new Folder(userId, "Inbox", "blue"),
				new Folder(userId, "Sent Items", "green"),
				new Folder(userId, "Important", "red")
		);
	}
}
