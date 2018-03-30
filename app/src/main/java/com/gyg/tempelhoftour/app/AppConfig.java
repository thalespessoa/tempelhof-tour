package com.gyg.tempelhoftour.app;

/**
 * Config constants
 *
 * Created by thalespessoa on 3/29/18.
 */

public class AppConfig {

    static public final String API_DATE_FORMAT = "MMMM dd, yyyy";
    static public final String APP_DATE_FORMAT = "MMMM dd, yyyy";

    public static final String API_URL = "https://www.getyourguide.com/berlin-l17/tempelhof-2-hour-airport-history-tour-berlin-airlift-more-t23776/";

    public static final String GET_REVIEWS = "reviews.json?type=&sortBy=date_of_review&direction=DESC";
    public static final String POST_REVIEW = "save_review.json";

    static final String DB_NAME = "reviewsDB";

    public static final int API_PAGE_SIZE = 20;
    public static final int DB_PAGE_SIZE = 30;
}
