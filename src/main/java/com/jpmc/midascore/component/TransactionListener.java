package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TransactionListener {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String INCENTIVE_API_URL = "http://localhost:8080/incentive";

    @KafkaListener(topics = "${general.kafka-topic}")
    public void handleTransaction(Transaction transaction) {
        System.out.println("Received transaction: " + transaction);
        
        // Validate and process transaction
        if (processTransaction(transaction)) {
            System.out.println("Transaction processed successfully");
        } else {
            System.out.println("Transaction rejected - validation failed");
        }
        
        // Debug: Print wilbur's balance after each transaction
        printWilburBalance();
    }

    private boolean processTransaction(Transaction transaction) {
        // Find sender and recipient
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());

        // Validate transaction
        if (sender == null) {
            System.out.println("Invalid sender ID: " + transaction.getSenderId());
            return false;
        }
        
        if (recipient == null) {
            System.out.println("Invalid recipient ID: " + transaction.getRecipientId());
            return false;
        }
        
        if (sender.getBalance() < transaction.getAmount()) {
            System.out.println("Insufficient balance. Sender balance: " + sender.getBalance() + 
                             ", Transaction amount: " + transaction.getAmount());
            return false;
        }

        // Get incentive from API
        Incentive incentive = getIncentive(transaction);
        double incentiveAmount = incentive != null ? incentive.getAmount() : 0.0;
        
        System.out.println("Incentive amount: " + incentiveAmount);

        // Process valid transaction
        sender.setBalance(sender.getBalance() - (float)transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + (float)transaction.getAmount() + (float)incentiveAmount);
        
        // Save updated balances
        userRepository.save(sender);
        userRepository.save(recipient);
        
        // Save transaction record with incentive
        TransactionRecord record = new TransactionRecord(sender, recipient, transaction.getAmount(), incentiveAmount);
        transactionRepository.save(record);
        
        return true;
    }

    private Incentive getIncentive(Transaction transaction) {
        try {
            return restTemplate.postForObject(INCENTIVE_API_URL, transaction, Incentive.class);
        } catch (Exception e) {
            System.out.println("Failed to get incentive: " + e.getMessage());
            return new Incentive(0.0);
        }
    }

    public void printWilburBalance() {
        UserRecord wilbur = userRepository.findByName("wilbur");
        if (wilbur != null) {
            int balanceRoundedDown = (int) Math.floor(wilbur.getBalance());
            System.out.println("=== WILBUR'S BALANCE: " + wilbur.getBalance() + " (rounded down: " + balanceRoundedDown + ") ===");
        } else {
            System.out.println("=== WILBUR NOT FOUND ===");
        }
    }
}