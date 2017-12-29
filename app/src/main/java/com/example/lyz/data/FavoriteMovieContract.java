package com.example.lyz.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Lyz on 22.12.2017.
 */

public class FavoriteMovieContract {

    public static final String AUTHORITY= "com.example.lyz";

    public static final Uri BASE_CONTENT_URI= Uri.parse("content://"+AUTHORITY);

    public static final String PATH_FAVORITEMOVIES="favoriteMovies";

    public static final class FavoriteMovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITEMOVIES).build();

        public static final String TABLE_NAME = "favoriteMovies";

        public static final String COLUMN_MOVIEID="movieId";

        public static final String COLUMN_RATING="rating";

        public static final String COLUMN_IMAGEPATH="imagePath";

        public static final String COLUMN_TOTALIMAGEPATH="totalImagePath";

        public static final String COLUMN_DESCRIPTION="description";

        public static final String COLUMN_TITLE="title";

        public static final String COLUMN_RELEASEDATE="releaseDate";

    }
}
