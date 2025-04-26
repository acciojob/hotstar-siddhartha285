package com.driver.services;


import com.driver.EntryDto.ProductionHouseEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductionHouseService {

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;

    public Integer addProductionHouseToDb(ProductionHouseEntryDto productionHouseEntryDto){
        ProductionHouse productionHouse=new ProductionHouse();
        productionHouse.setName(productionHouseEntryDto.getName());
        List<WebSeries> productionHouseWebseries=webSeriesRepository.findByProductionHouse_Name( productionHouseEntryDto.getName());
        double avg;
        int sum=0;
        int count=0;
        for( WebSeries webi : productionHouseWebseries)
        {
            sum+=webi.getRating();
            count++;
        }
        if(count==0)avg=0;
        else avg=sum/count;
        productionHouse.setRatings(avg);
        productionHouse.setWebSeriesList(productionHouseWebseries);
        ProductionHouse saved=productionHouseRepository.save(productionHouse);
        return saved.getId();

    }



}
