package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @KafkaListener(topics = "${general.kafka-topic}")
    public void handleTransaction(Transaction transaction) {
        System.out.println("Received transaction: " + transaction);
        
        // Validate and process transaction
        if (processTransaction(transaction)) {
            System.out.println("Transaction processed successfully");
        } else {
            System.out.println("Transaction rejected - validation failed");
        }
        
        // Debug: Print waldorf's balance after each transaction
        printWaldorfBalance();
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

        // Process valid transaction
        sender.setBalance(sender.getBalance() - (float)transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + (float)transaction.getAmount());
        
        // Save updated balances
        userRepository.save(sender);
        userRepository.save(recipient);
        
        // Save transaction record
        TransactionRecord record = new TransactionRecord(sender, recipient, transaction.getAmount());
        transactionRepository.save(record);
        
        return true;
    }

    public void printWaldorfBalance() {
        UserRecord waldorf = userRepository.findByName("waldorf");
        if (waldorf != null) {
            int balanceRoundedDown = (int) Math.floor(waldorf.getBalance());
            System.out.println("=== WALDORF'S BALANCE: " + waldorf.getBalance() + " (rounded down: " + balanceRoundedDown + ") ===");
        } else {
            System.out.println("=== WALDORF NOT FOUND ===");
        }
    }
}