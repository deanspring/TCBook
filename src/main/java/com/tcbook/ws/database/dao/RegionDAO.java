package com.tcbook.ws.database.dao;

import com.tcbook.ws.bean.Region;

import java.sql.SQLException;

/**
 * Created by caiouvini on 5/13/14.
 */
public interface RegionDAO {

    public void insert(Region region) throws SQLException;

    public Region findForCityAndCountry(String cityName, String countryName);

}
