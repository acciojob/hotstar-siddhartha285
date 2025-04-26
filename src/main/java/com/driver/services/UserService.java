package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository
//        user.getSubscription().setUser(user);
        User savedUser=userRepository.save(user);
        return savedUser.getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) return 0;
        User user = optionalUser.get();

        int age = user.getAge();
        SubscriptionType userSubscription = user.getSubscription().getSubscriptionType();

        List<WebSeries> allWebSeries = webSeriesRepository.findAll();

        int count = 0;
        for (WebSeries ws : allWebSeries) {
            if (ws.getAgeLimit() <= age &&
                    isSubscriptionAllowed(userSubscription, ws.getSubscriptionType())) {
                count++;
            }
        }
        return count;
    }

    private boolean isSubscriptionAllowed(SubscriptionType userSub, SubscriptionType seriesSub) {
        if (userSub == SubscriptionType.ELITE) return true;
        if (userSub == SubscriptionType.PRO && seriesSub != SubscriptionType.ELITE) return true;
        return userSub == seriesSub;
    }


}
