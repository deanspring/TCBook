package com.tcbook.ws.database.dao;

import com.tcbook.ws.bean.Region;

import java.sql.SQLException;
import java.util.Map;

public interface RegionDAO {

	public void insert(Region region) throws SQLException;

	public Region findForCityAndCountry(String cityName, String countryName);

    public Map<Long, Integer> topTenCountriesWithMostArtists();

}
