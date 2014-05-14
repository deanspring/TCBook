package com.tcbook.ws.database.dao;

import com.tcbook.ws.bean.Country;

import java.sql.SQLException;

public interface CountryDAO {

	public Country findForID(Long idCountry);

	public Country findForName(String countryName);

	public void insert(Country country) throws SQLException;

	public void remove(Long idContry) throws SQLException;

}
