package io.github.vedantwankhade.ghmail.repository;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import io.github.vedantwankhade.ghmail.model.Folder;

@Repository
public interface FolderRepository extends CassandraRepository<Folder, String> {
	List<Folder> findAllByUserId(String userId);
}
