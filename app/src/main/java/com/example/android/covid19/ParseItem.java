package com.example.android.covid19;

public class ParseItem {

    private String ImgUrl;
    private String Country_name;
    private String Total_cases;
    private String Total_deaths;

    public ParseItem(){

    }

    public ParseItem(String imgUrl, String country_name, String total_cases, String total_deaths) {
        ImgUrl =imgUrl;
        Country_name = country_name;
        Total_cases = total_cases;
        Total_deaths = total_deaths;
    }

    public String getCountry_name() {
        return Country_name;
    }

    public void setCountry_name(String country_name) {
        Country_name = country_name;
    }

    public String getTotal_cases() {
        return Total_cases;
    }

    public void setTotal_cases(String total_cases) {
        Total_cases = total_cases;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public String getTotal_deaths() {
        return Total_deaths;
    }

    public void setTotal_deaths(String total_deaths) {
        Total_deaths = total_deaths;
    }
}
