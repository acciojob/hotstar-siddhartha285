package com.driver.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private SubscriptionType subscriptionType;

    private int noOfScreensSubscribed;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startSubscriptionDate;

    private int totalAmountPaid;

    @OneToOne
    @JoinColumn
    @JsonIgnore
    private User user;

    public Subscription(SubscriptionType subscriptionType, int noOfScreensSubscribed, Date startSubscriptionDate, int totalAmountPaid) {
        this.subscriptionType = subscriptionType;
        this.noOfScreensSubscribed = noOfScreensSubscribed;
        this.startSubscriptionDate = startSubscriptionDate;
        this.totalAmountPaid = totalAmountPaid;
    }

    public Subscription() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Enumerated(EnumType.STRING)
    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public int getNoOfScreensSubscribed() {
        return noOfScreensSubscribed;
    }

    public void setNoOfScreensSubscribed(int noOfScreensSubscribed) {
        this.noOfScreensSubscribed = noOfScreensSubscribed;
    }

    public Date getStartSubscriptionDate() {
        return startSubscriptionDate;
    }

    public void setStartSubscriptionDate(Date durationOfSubscription) {
        this.startSubscriptionDate = durationOfSubscription;
    }

    public int getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public void setTotalAmountPaid(int totalAmountPaid) {
        this.totalAmountPaid = totalAmountPaid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
