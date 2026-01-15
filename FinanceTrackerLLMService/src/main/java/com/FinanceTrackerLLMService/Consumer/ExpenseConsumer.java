package com.FinanceTrackerLLMService.Consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.FinanceTrackerLLMService.DTO.ExpenseDTO;
import com.FinanceTrackerLLMService.Service.ExpenseService;

@Service
public class ExpenseConsumer {

    private final ExpenseService expenseService;

    public ExpenseConsumer(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @KafkaListener(
            topics = "${spring.kafka.topic-json.name}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(ExpenseDTO expense) {
        try {
            System.out.println("✔ Received ExpenseDTO from Kafka: " + expense);
            expenseService.createExpense(expense);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error processing Kafka message");
        }
    }
}
