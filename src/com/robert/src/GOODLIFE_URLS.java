package com.robert.src;

public enum GOODLIFE_URLS {

    URL_LOGIN("https://www.goodlifefitness.com/members/login?redirectpath=%2fmembers%2f"),
    URL_MAIN_PAGE("https://www.goodlifefitness.com/members/"),
    URL_API_MAIN("https://www.goodlifefitness.com/club-occupancy/club-workout-schedule?club="),
    URL_API_UNTIL_CLUB("&day="),
    URL_API_END("&studio=");

    private final String url;

    public String getString() {

        return this.url;
    }

    GOODLIFE_URLS(String url) {

        this.url = url;

    }


}
