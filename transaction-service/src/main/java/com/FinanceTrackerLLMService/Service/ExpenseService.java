package com.FinanceTrackerLLMService.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.util.Strings;

import com.FinanceTrackerLLMService.DTO.ExpenseDTO;
import com.FinanceTrackerLLMService.Entity.Expence;
import com.FinanceTrackerLLMService.Repo.ExpenseRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ExpenseService {

    private final ExpenseRepo expenseRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public ExpenseService(ExpenseRepo expenseRepository) {
        this.expenseRepository = expenseRepository;
    }
    public boolean createExpense(ExpenseDTO expenseDto) {
        setCurrency(expenseDto);

        try {
            Expence exp = new Expence();

            exp.setAmount(expenseDto.getAmount());
            exp.setCurrency(expenseDto.getCurrency());
            exp.setMerchant(expenseDto.getMerchant());
            exp.setExternalId(expenseDto.getExternalId());

            // 🔥 MOST IMPORTANT — This is what was missing
            exp.setUserId(expenseDto.getUserId());

            exp.setCreatedAt(expenseDto.getCreatedAt()); // if exists

            expenseRepository.save(exp);
            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public boolean updateExpense(ExpenseDTO expenseDto) {
        setCurrency(expenseDto);

        Optional<Expence> expenseFoundOpt =
                expenseRepository.findByUserIdAndExternalId(expenseDto.getUserId(), expenseDto.getExternalId());

        if (expenseFoundOpt.isEmpty()) {
            return false;
        }

        Expence expense = expenseFoundOpt.get();

        expense.setAmount(expenseDto.getAmount());
        expense.setMerchant(Strings.isNotBlank(expenseDto.getMerchant()) ? expenseDto.getMerchant() : expense.getMerchant());
        expense.setCurrency(Strings.isNotBlank(expenseDto.getCurrency()) ? expenseDto.getCurrency() : expense.getCurrency());

        expenseRepository.save(expense);
        return true;
    }

    public List<ExpenseDTO> getExpenses(String userId) {
        List<Expence> expenses = expenseRepository.findByUserId(userId);
        return objectMapper.convertValue(expenses, new TypeReference<List<ExpenseDTO>>() {});
    }

    private void setCurrency(ExpenseDTO expenseDto) {
        if (Objects.isNull(expenseDto.getCurrency())) {
            expenseDto.setCurrency("inr");
        }
    }
}
