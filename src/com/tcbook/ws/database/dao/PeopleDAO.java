package com.tcbook.ws.database.dao;

import com.tcbook.ws.bean.People;

public interface PeopleDAO {

    public People find(Long id);

    public void insert(People people);

    public void remove(Long id);

}
