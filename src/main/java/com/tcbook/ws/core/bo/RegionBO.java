package com.tcbook.ws.core.bo;

import com.tcbook.ws.bean.City;
import com.tcbook.ws.bean.Country;
import com.tcbook.ws.bean.Region;
import com.tcbook.ws.database.dao.CityDAOImpl;
import com.tcbook.ws.database.dao.CountryDAOImpl;
import com.tcbook.ws.database.dao.RegionDAOImpl;
import com.tcbook.ws.util.TCBookConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by caiouvini on 5/13/14.
 */
public class RegionBO {

    private static final Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

    private static RegionDAOImpl regionDAO = RegionDAOImpl.getInstance();
    private static CityDAOImpl cityDAO = CityDAOImpl.getInstance();
    private static CountryDAOImpl countryDAO = CountryDAOImpl.getInstance();

    public Region getRegion(String cityName, String countryName) {
        return regionDAO.findForCityAndCountry(cityName, countryName);
    }

    public void createRegion(City city, Country country) {
        try {
            Region regionFromDB = regionDAO.findForCityAndCountry(city.getName(), country.getName());
            if (regionFromDB == null) {
                if (city.getId() != null) {
                    City cityFromDB = cityDAO.findForID(city.getId());
                    if (cityFromDB == null) {
                        cityDAO.insert(city);
                    } else {
                        city = cityFromDB;
                    }
                } else {
                    City cityFromDB = cityDAO.findForName(city.getName());
                    if (cityFromDB == null) {
                        cityDAO.insert(city);
                        city = cityDAO.findForName(city.getName());
                    } else {
                        city = cityFromDB;
                    }
                }

                if (country.getId() != null) {
                    Country countryFromDB = countryDAO.findForID(country.getId());
                    if (countryFromDB == null) {
                        countryDAO.insert(country);
                    } else {
                        country = countryFromDB;
                    }
                } else {
                    Country countryFromDB = countryDAO.findForName(country.getName());
                    if (countryFromDB == null) {
                        countryDAO.insert(country);
                        country = countryDAO.findForName(country.getName());
                    } else {
                        country = countryFromDB;
                    }
                }

                Region region = new Region();
                region.setIdCountry(country.getId());
                region.setIdCity(city.getId());
                regionDAO.insert(region);
            }
        } catch (Exception e) {
            logEx.error("Error creating Region.", e);
        }
    }

}
