package com.example.lyz.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lyz on 22.12.2017.
 */

public class FavoriteMovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="favoriteMovies.db";

    private static final int VERSION=1;

    FavoriteMovieDbHelper(Context context){
        super(context, DATABASE_NAME,null, VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE = "CREATE TABLE " + FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME+" ("+
                FavoriteMovieContract.FavoriteMovieEntry._ID+" INTEGER PRIMARY KEY, "+
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIEID +" INTEGER NOT NULL, "+
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RATING+ " DOUBLE PRECISION NOT NULL, "+
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_IMAGEPATH+" TEXT NOT NULL, "+
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TOTALIMAGEPATH+" TEXT NOT NULL, "+
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_DESCRIPTION+" TEXT NOT NULL, "+
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE+" TEXT NOT NULL, "+
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RATING+" TEXT NOT NULL);";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
