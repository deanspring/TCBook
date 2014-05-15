package com.tcbook.ws.core.bo;

import com.tcbook.ws.bean.MusicalGenre;
import com.tcbook.ws.database.dao.MusicalGenresDAO;
import com.tcbook.ws.database.dao.MusicalGenresDAOImpl;
import com.tcbook.ws.util.TCBookConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MusicalGenreBO {

	private static final Logger logEx = LoggerFactory.getLogger(TCBookConstants.LOG_NAME_EXCEPTIONS);

	private MusicalGenresDAO musicalGenresDAO = MusicalGenresDAOImpl.getInstance();

	public void insert(String genreName) throws Exception {
		try {
			musicalGenresDAO.insert(genreName);
		} catch (Exception e) {
			throw new Exception("Error inserting genre. " + e.getMessage());
		}
	}

	public List<MusicalGenre> getAll() {
		List<MusicalGenre> result = null;
		try {
			result = musicalGenresDAO.findAll();
		} catch (Exception e) {
			logEx.error("Error retrieving all musical genres.", e);
		}
		return result;
	}

	public List<String> getAllNamesFromList(List<MusicalGenre> genres) {
		if (genres == null)
			return new ArrayList<String>();
		List<String> genreNames = new ArrayList<String>();

		for (MusicalGenre genre : genres) {
			genreNames.add(genre.getName());
		}

		return genreNames;
	}

	public MusicalGenre findForName(String genreName) {
		MusicalGenre result = null;
		try {
			result = musicalGenresDAO.getForName(genreName);
		} catch (Exception e) {
			logEx.error("Error searching musical genre.", e);
		}
		return result;
	}

}
