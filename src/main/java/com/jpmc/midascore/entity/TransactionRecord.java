package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
public class TransactionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserRecord sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private UserRecord recipient;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private double incentive;

    protected TransactionRecord() {
    }

    public TransactionRecord(UserRecord sender, UserRecord recipient, double amount, double incentive) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.incentive = incentive;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public UserRecord getSender() {
        return sender;
    }

    public UserRecord getRecipient() {
        return recipient;
    }

    public double getAmount() {
        return amount;
    }

    public double getIncentive() {
        return incentive;
    }

    @Override
    public String toString() {
        return String.format("TransactionRecord[id=%d, sender=%s, recipient=%s, amount=%.2f, incentive=%.2f]", 
                           id, sender.getName(), recipient.getName(), amount, incentive);
    }
}