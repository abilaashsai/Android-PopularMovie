package app.com.example.android.popularmovies.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import app.com.example.android.popularmovies.data.MovieContract.MovieEntry;


public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();


    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                MovieEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Weather table during delete", 0, cursor.getCount());
        cursor.close();

    }

    public void deleteAllRecordsFromDB() {
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(MovieEntry.TABLE_NAME, null, null);
        db.close();
    }

    public void deleteAllRecords() {
        deleteAllRecordsFromDB();
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

        public void testProviderRegistry() {
            PackageManager pm = mContext.getPackageManager();

            ComponentName componentName = new ComponentName(mContext.getPackageName(),
                    MovieProvider.class.getName());
            try {
                ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

                assertEquals("Error: WeatherProvider registered with authority: " + providerInfo.authority +
                        " instead of authority: " + MovieContract.CONTENT_AUTHORITY,
                        providerInfo.authority, MovieContract.CONTENT_AUTHORITY);
            } catch (PackageManager.NameNotFoundException e) {
                assertTrue("Error: WeatherProvider not registered at " + mContext.getPackageName(),
                        false);
            }
        }

        public void testGetType() {
            String type = mContext.getContentResolver().getType(MovieEntry.CONTENT_URI);
            assertEquals("Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE",
                    MovieEntry.CONTENT_TYPE, type);

            String movieUrl = "aaa";
            type = mContext.getContentResolver().getType(
                    MovieEntry.buildMovieKey(movieUrl));
            assertEquals("Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE",
                    MovieEntry.CONTENT_ITEM_TYPE, type);

            String movieName = "ABCD";
            type = mContext.getContentResolver().getType(
                    MovieEntry.buildMovieKey(movieName));
            assertEquals("Error: the WeatherEntry CONTENT_URI with location and date should return WeatherEntry.CONTENT_ITEM_TYPE",
                    MovieEntry.CONTENT_ITEM_TYPE, type);

             }


        public void testBasicMovieQuery() {
            MovieDbHelper dbHelper = new MovieDbHelper(mContext);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues testValues = TestUtilities.createMovieValues();
            long movieRowId = TestUtilities.insertMovieValues(mContext);

            assertTrue("Unable to Insert MovieEntry into the Database", movieRowId  != -1);
            db.close();

            Cursor movieCursor = mContext.getContentResolver().query(
                    MovieEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );

            TestUtilities.validateCursor("testBasicMovieQuery", movieCursor, testValues);
        }

        public void testUpdate() {
            ContentValues values = TestUtilities.createMovieValues();

            Uri locationUri = mContext.getContentResolver().
                    insert(MovieEntry.CONTENT_URI, values);
            long locationRowId = ContentUris.parseId(locationUri);

            assertTrue(locationRowId != -1);
            Log.d(LOG_TAG, "New row id: " + locationRowId);

            ContentValues updatedValues = new ContentValues(values);
            updatedValues.put(MovieEntry._ID, locationRowId);
            Cursor locationCursor = mContext.getContentResolver().query(MovieEntry.CONTENT_URI, null, null, null, null);

            TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
            locationCursor.registerContentObserver(tco);

            int count = mContext.getContentResolver().update(
                    MovieEntry.CONTENT_URI, updatedValues, MovieEntry._ID + "= ?",
                    new String[] { Long.toString(locationRowId)});
            assertEquals(count, 1);

            locationCursor.unregisterContentObserver(tco);
            locationCursor.close();

            Cursor cursor = mContext.getContentResolver().query(
                    MovieEntry.CONTENT_URI,
                    null,   // projection
                    MovieEntry._ID + " = " + locationRowId,
                    null,   // Values for the "where" clause
                    null    // sort order
            );

            TestUtilities.validateCursor("testUpdate.  Error validating location entry update.",
                    cursor, updatedValues);

            cursor.close();
        }

        public void testInsertReadProvider() {
            ContentValues testValues = TestUtilities.createMovieValues();

            TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
            mContext.getContentResolver().registerContentObserver(MovieEntry.CONTENT_URI, true, tco);
            Uri locationUri = mContext.getContentResolver().insert(MovieEntry.CONTENT_URI, testValues);

            mContext.getContentResolver().unregisterContentObserver(tco);

            long locationRowId = ContentUris.parseId(locationUri);

            assertTrue(locationRowId != -1);

            Cursor cursor = mContext.getContentResolver().query(
                    MovieEntry.CONTENT_URI,
                    null, // leaving "columns" null just returns all the columns.
                    null, // cols for "where" clause
                    null, // values for "where" clause
                    null  // sort order
            );

            TestUtilities.validateCursor("testInsertReadProvider. Error validating LocationEntry.",
                    cursor, testValues);

        }

        public void testDeleteRecords() {
            testInsertReadProvider();

            TestUtilities.TestContentObserver locationObserver = TestUtilities.getTestContentObserver();
            mContext.getContentResolver().registerContentObserver(MovieEntry.CONTENT_URI, true, locationObserver);

            TestUtilities.TestContentObserver weatherObserver = TestUtilities.getTestContentObserver();
            mContext.getContentResolver().registerContentObserver(MovieEntry.CONTENT_URI, true, weatherObserver);

            deleteAllRecordsFromProvider();

            mContext.getContentResolver().unregisterContentObserver(locationObserver);
            mContext.getContentResolver().unregisterContentObserver(weatherObserver);
        }


    static private final int BULK_INSERT_RECORDS_TO_INSERT = 10;

    static ContentValues[] createBulkInsertWeatherValues(String rowId) {
        String currentTestMovieName = "ABCD";
        long millisecondsInADay = 1000 * 60 * 60 * 24;
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, currentTestMovieName += millisecondsInADay) {
            ContentValues movieValues = new ContentValues();
            movieValues.put(MovieEntry.COLUMN_MOVIE_URL, rowId);
            returnContentValues[i] = movieValues;
        }
        return returnContentValues;
    }

}