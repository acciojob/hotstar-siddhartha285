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
        User returnedUser = optionalUser.get();

        SubscriptionType subscription = returnedUser.getSubscription().getSubscriptionType(); // keep it as enum
        int age = returnedUser.getAge();

        List<SubscriptionType> allowedTypes = new ArrayList<>();
        if (subscription == SubscriptionType.ELITE) {
            allowedTypes = List.of(SubscriptionType.BASIC, SubscriptionType.PRO, SubscriptionType.ELITE);
        } else if (subscription == SubscriptionType.PRO) {
            allowedTypes = List.of(SubscriptionType.BASIC, SubscriptionType.PRO);
        } else if (subscription == SubscriptionType.BASIC) {
            allowedTypes = List.of(SubscriptionType.BASIC);
        }

        return webSeriesRepository.countViewableWebSeries(age, allowedTypes);
    }


}
