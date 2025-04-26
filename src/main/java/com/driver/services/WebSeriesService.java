package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo
        if (webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName()) != null) {
            throw new Exception("Series is already present");
        }

        // Create new WebSeries
        WebSeries webSeries = new WebSeries();
        webSeries.setSeriesName(webSeriesEntryDto.getSeriesName());
        webSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webSeries.setRating(webSeriesEntryDto.getRating());
        webSeries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());

        // Get production house
        Optional<ProductionHouse> optionalProductionHouse = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId());
        if (!optionalProductionHouse.isPresent()) {
            throw new RuntimeException("ProductionHouse not found with id: " + webSeriesEntryDto.getProductionHouseId());
        }

        ProductionHouse productionHouse = optionalProductionHouse.get();

        // Set the relationship
        webSeries.setProductionHouse(productionHouse); // many-to-one
        productionHouse.getWebSeriesList().add(webSeries); // one-to-many

        // Recalculate the average rating
        double totalRating = 0;
        int seriesCount = productionHouse.getWebSeriesList().size();
        for (WebSeries ws : productionHouse.getWebSeriesList()) {
            totalRating += ws.getRating();
        }
        double averageRating = totalRating / seriesCount;
        productionHouse.setRatings(averageRating);

        // Save the webSeries (cascade = ALL ensures ProductionHouse changes are also saved)
        WebSeries savedSeries = webSeriesRepository.save(webSeries);

        return savedSeries.getId();
    }

}
