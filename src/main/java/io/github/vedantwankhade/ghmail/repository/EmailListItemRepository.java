package io.github.vedantwankhade.ghmail.repository;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import io.github.vedantwankhade.ghmail.model.EmailListItem;
import io.github.vedantwankhade.ghmail.model.utilmodel.EmailListItemCompoundKey;

@Repository
public interface EmailListItemRepository extends CassandraRepository<EmailListItem, EmailListItemCompoundKey> {
	List<EmailListItem> findAllByKey_UserIdAndKey_Label(String userId, String label);
}
