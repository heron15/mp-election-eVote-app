package com.appkrafty.evote.model;

public class VoteCandidateModel {

    private String id;
    private String person_image;
    private String name;
    private String party;
    private String symbol;
    private String symbol_image;
    private String districtId;
    private String areaId;

    public VoteCandidateModel(String id,
                              String person_image,
                              String name,
                              String party,
                              String symbol,
                              String symbol_image,
                              String districtId,
                              String areaId) {
        this.id = id;
        this.person_image = person_image;
        this.name = name;
        this.party = party;
        this.symbol = symbol;
        this.symbol_image = symbol_image;
        this.districtId = districtId;
        this.areaId = areaId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPerson_image(String person_image) {
        this.person_image = person_image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setSymbol_image(String symbol_image) {
        this.symbol_image = symbol_image;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getId() {
        return id;
    }

    public String getPerson_image() {
        return person_image;
    }

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getSymbol_image() {
        return symbol_image;
    }

    public String getDistrictId() {
        return districtId;
    }

    public String getAreaId() {
        return areaId;
    }
}
