package com.tcbook.ws.bean;

/**
 * Created by caiouvini on 5/20/14.
 */
public class ColleagueMusicalArtistSharingInfo {

    private Long idPerson;

    private Long idColleague;

    private Integer amount;

    public Long getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(Long idPerson) {
        this.idPerson = idPerson;
    }

    public Long getIdColleague() {
        return idColleague;
    }

    public void setIdColleague(Long idColleague) {
        this.idColleague = idColleague;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
