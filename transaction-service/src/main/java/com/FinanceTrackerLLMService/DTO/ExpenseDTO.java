package com.FinanceTrackerLLMService.DTO;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseDTO {

    private String externalId;

    @JsonProperty("amount")
    @NonNull
    private BigDecimal amount;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("merchant")
    private String merchant;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("created_at")
    private Timestamp createdAt;


    // STATIC METHOD for JSON parsing
    public static ExpenseDTO fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            return mapper.readValue(json, ExpenseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize ExpenseDTO from JSON", e);
        }
    }
}
