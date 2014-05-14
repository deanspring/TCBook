package com.tcbook.ws.database.dao;

import com.tcbook.ws.bean.City;

import java.sql.SQLException;

public interface CityDAO {

	public City findForID(Long idCity);

	public City findForName(String cityName);

	public void insert(City city) throws SQLException;

	public void remove(Long idCity) throws SQLException;

}
