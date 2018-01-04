package com.example.lyz.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.lyz.data.FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME;

/**
 * Created by Lyz on 22.12.2017.
 */

public class FavoriteMovieContentProvider extends ContentProvider {

    public static final int MOVIES=100;
    public static final int MOVIE_WITH_ID=101;

    private FavoriteMovieDbHelper movieDbHelper;

    private static final UriMatcher mUriMatcher=buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavoriteMovieContract.AUTHORITY, FavoriteMovieContract.PATH_FAVORITEMOVIES, MOVIES);
        uriMatcher.addURI(FavoriteMovieContract.AUTHORITY, FavoriteMovieContract.PATH_FAVORITEMOVIES+"/#",MOVIE_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        movieDbHelper=new FavoriteMovieDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortorder) {
        final SQLiteDatabase db = movieDbHelper.getReadableDatabase();
        int match = mUriMatcher.match(uri);
        Cursor retCursor;

        switch (match){
            case MOVIES:
                retCursor=db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortorder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),null);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        int match= mUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case MOVIES:
                long id = db.insert(TABLE_NAME, null, contentValues);
                if (id>0){
                    returnUri =ContentUris.withAppendedId(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,id);
                }
                else {
                    throw new SQLException("Failed to insert row into "+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        int match = mUriMatcher.match(uri);
        int moviesDeleted;

        switch (match){
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                moviesDeleted=db.delete(TABLE_NAME, "_id?=",new String[]{id});
                break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        if (moviesDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return moviesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
