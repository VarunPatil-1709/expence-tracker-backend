package com.FinanceTrackerLLMService.Repo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.FinanceTrackerLLMService.Entity.Expence;

@Repository
public interface ExpenseRepo extends CrudRepository<Expence, Long> {
	List<Expence> findByUserId(String userId);

    List<Expence> findByUserIdAndCreatedAtBetween(String userId, Timestamp startTime, Timestamp endTime);

    Optional<Expence> findByUserIdAndExternalId(String userId, String externalId);
}
