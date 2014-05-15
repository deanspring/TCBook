package com.tcbook.ws.database.dao;

import java.sql.SQLException;

import com.tcbook.ws.bean.Country;

public interface CountryDAO {

	public Country findForID(Long idCountry);

	public Country findForName(String countryName);

	public void insert(Country country) throws SQLException;

	public void remove(Long idContry) throws SQLException;

	public Country findByRegion(Long idRegion);

}
