package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        Optional<User> optionalUser = userRepository.findById(subscriptionEntryDto.getUserId());
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found with id: " + subscriptionEntryDto.getUserId());
        }

        User user = optionalUser.get();
        Subscription subscription=new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        subscription.setStartSubscriptionDate(new Date());
        int totalAmount = 0;
        int screens = subscriptionEntryDto.getNoOfScreensRequired();

        switch (subscriptionEntryDto.getSubscriptionType()) {
            case BASIC:
                totalAmount = 500 + 200 * screens;
                break;
            case PRO:
                totalAmount = 800 + 250 * screens;
                break;
            case ELITE:
                totalAmount = 1000 + 350 * screens;
                break;
        }

        subscription.setTotalAmountPaid(totalAmount);
        subscription.setUser(user);
        user.setSubscription(subscription);
        System.out.println("Total Amount: " + totalAmount);  // Debugging line to check

        subscriptionRepository.save(subscription);


        return totalAmount;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        User user = optionalUser.get();
        Subscription subscription=user.getSubscription();
        int screens=subscription.getNoOfScreensSubscribed();
        int earlierPaidAmount=subscription.getTotalAmountPaid();
        int newAmountToBePaid=0;
        switch (subscription.getSubscriptionType()) {
            case BASIC:
                newAmountToBePaid = 800 + 250 * screens;
                subscription.setSubscriptionType(SubscriptionType.PRO);
                break;
            case PRO:
                newAmountToBePaid = 1000 + 350 * screens;
                subscription.setSubscriptionType(SubscriptionType.ELITE);

                break;
            case ELITE:
                throw new IllegalStateException("Already the best Subscription");

        }
        subscription.setTotalAmountPaid(newAmountToBePaid);

        subscriptionRepository.saveAndFlush(subscription);



        return newAmountToBePaid-earlierPaidAmount;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        int revenue=0;
        List<Subscription> subscriptions=subscriptionRepository.findAll();
        for(Subscription subscription:subscriptions)
        {
            revenue+=subscription.getTotalAmountPaid();
        }

        return revenue;
    }

}
