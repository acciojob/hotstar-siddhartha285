package com.driver.repository;

import com.driver.model.SubscriptionType;
import com.driver.model.WebSeries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WebSeriesRepository extends JpaRepository<WebSeries,Integer> {
    @Query("SELECT COUNT(w) FROM WebSeries w WHERE w.ageLimit <= :age AND w.subscriptionType IN :allowedTypes")
    Integer countViewableWebSeries(@Param("age") Integer age, @Param("allowedTypes") List<SubscriptionType> allowedTypes);

    WebSeries findBySeriesName(String seriesName);

    List<WebSeries> findByProductionHouse_Name(String name);
}
