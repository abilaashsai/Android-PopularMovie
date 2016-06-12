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

    /*
This helper function deletes all records from both database tables using the ContentProvider.
It also queries the ContentProvider to make sure that the database has been successfully
deleted, so it cannot be used until the Query and Delete functions have been written
in the ContentProvider.

Students: Replace the calls to deleteAllRecordsFromDB with this one after you have written
the delete functionality in the ContentProvider.
*/
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

    /*
This helper function deletes all records from both database tables using the database
functions only.  This is designed to be used to reset the state of the database until the
delete functionality is available in the ContentProvider.
*/
    public void deleteAllRecordsFromDB() {
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(MovieEntry.TABLE_NAME, null, null);
        db.close();
    }

    /*
Student: Refactor this function to use the deleteAllRecordsFromProvider functionality once
you have implemented delete functionality there.
*/
    public void deleteAllRecords() {
        deleteAllRecordsFromDB();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

                /*
        This test checks to make sure that the content provider is registered correctly.
        Students: Uncomment this test to make sure you've correctly registered the WeatherProvider.
     */
        public void testProviderRegistry() {
            PackageManager pm = mContext.getPackageManager();

            // We define the component name based on the package name from the context and the
            // WeatherProvider class.
            ComponentName componentName = new ComponentName(mContext.getPackageName(),
                    MovieProvider.class.getName());
            try {
                // Fetch the provider info using the component name from the PackageManager
                // This throws an exception if the provider isn't registered.
                ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

                // Make sure that the registered authority matches the authority from the Contract.
                assertEquals("Error: WeatherProvider registered with authority: " + providerInfo.authority +
                        " instead of authority: " + MovieContract.CONTENT_AUTHORITY,
                        providerInfo.authority, MovieContract.CONTENT_AUTHORITY);
            } catch (PackageManager.NameNotFoundException e) {
                // I guess the provider isn't registered correctly.
                assertTrue("Error: WeatherProvider not registered at " + mContext.getPackageName(),
                        false);
            }
        }

                /*
           This test doesn't touch the database.  It verifies that the ContentProvider returns
            the correct type for each type of URI that it can handle.
            Students: Uncomment this test to verify that your implementation of GetType is
            functioning correctly.
         */
        public void testGetType() {
            // content://com.example.android.sunshine.app/weather/
            String type = mContext.getContentResolver().getType(MovieEntry.CONTENT_URI);
            // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
            assertEquals("Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE",
                    MovieEntry.CONTENT_TYPE, type);

            String movieUrl = "aaa";
            // content://com.example.android.sunshine.app/weather/94074
            type = mContext.getContentResolver().getType(
                    MovieEntry.buildMovieKey(movieUrl));
            // vnd.android.cursor.dir/com.example.android.sunshine.app/weather
            assertEquals("Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE",
                    MovieEntry.CONTENT_ITEM_TYPE, type);

            String movieName = "ABCD"; // December 21st, 2014
            // content://com.example.android.sunshine.app/weather/94074/20140612
            type = mContext.getContentResolver().getType(
                    MovieEntry.buildMovieKey(movieName));
            // vnd.android.cursor.item/com.example.android.sunshine.app/weather/1419120000
            assertEquals("Error: the WeatherEntry CONTENT_URI with location and date should return WeatherEntry.CONTENT_ITEM_TYPE",
                    MovieEntry.CONTENT_ITEM_TYPE, type);

             }


                /*
        This test uses the database directly to insert and then uses the ContentProvider to
        read out the data.  Uncomment this test to see if the basic weather query functionality
        given in the ContentProvider is working correctly.
     */
        public void testBasicWeatherQuery() {
            // insert our test records into the database
            MovieDbHelper dbHelper = new MovieDbHelper(mContext);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues testValues = TestUtilities.createNorthPoleLocationValues();
            long movieRowId = TestUtilities.insertNorthPoleLocationValues(mContext);
          //  long movieRowIdInsert = db.insert(MovieEntry.TABLE_NAME, null, testValues);

            assertTrue("Unable to Insert MovieEntry into the Database", movieRowId  != -1);


            db.close();

            // Test the basic content provider query
            Cursor movieCursor = mContext.getContentResolver().query(
                    MovieEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );

            // Make sure we get the correct cursor out of the database
            TestUtilities.validateCursor("testBasicWeatherQuery", movieCursor, testValues);
        }

                /*
        This test uses the database directly to insert and then uses the ContentProvider to
        read out the data.  Uncomment this test to see if your location queries are
        performing correctly.
     */
    //    public void testBasicLocationQueries() {
    //        // insert our test records into the database
    //        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
    //        SQLiteDatabase db = dbHelper.getWritableDatabase();
    //
    //        ContentValues testValues = TestUtilities.createNorthPoleLocationValues();
    //        long locationRowId = TestUtilities.insertNorthPoleLocationValues(mContext);
    //
    //        // Test the basic content provider query
    //        Cursor locationCursor = mContext.getContentResolver().query(
    //                LocationEntry.CONTENT_URI,
    //                null,
    //                null,
    //                null,
    //                null
    //        );
    //
    //        // Make sure we get the correct cursor out of the database
    //        TestUtilities.validateCursor("testBasicLocationQueries, location query", locationCursor, testValues);
    //
    //        // Has the NotificationUri been set correctly? --- we can only test this easily against API
    //        // level 19 or greater because getNotificationUri was added in API level 19.
    //        if ( Build.VERSION.SDK_INT >= 19 ) {
    //            assertEquals("Error: Location Query did not properly set NotificationUri",
    //                    locationCursor.getNotificationUri(), LocationEntry.CONTENT_URI);
    //        }
    //    }

                /*
        This test uses the provider to insert and then update the data. Uncomment this test to
        see if your update location is functioning correctly.
     */
        public void testUpdateLocation() {
            // Create a new map of values, where column names are the keys
            ContentValues values = TestUtilities.createNorthPoleLocationValues();

            Uri locationUri = mContext.getContentResolver().
                    insert(MovieEntry.CONTENT_URI, values);
            long locationRowId = ContentUris.parseId(locationUri);

            // Verify we got a row back.
            assertTrue(locationRowId != -1);
            Log.d(LOG_TAG, "New row id: " + locationRowId);

            ContentValues updatedValues = new ContentValues(values);
            updatedValues.put(MovieEntry._ID, locationRowId);
            updatedValues.put(MovieEntry.COLUMN_MOVIE_IMAGE, "ABB");

            // Create a cursor with observer to make sure that the content provider is notifying
            // the observers as expected
            Cursor locationCursor = mContext.getContentResolver().query(MovieEntry.CONTENT_URI, null, null, null, null);

            TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
            locationCursor.registerContentObserver(tco);

            int count = mContext.getContentResolver().update(
                    MovieEntry.CONTENT_URI, updatedValues, MovieEntry._ID + "= ?",
                    new String[] { Long.toString(locationRowId)});
            assertEquals(count, 1);

            // Test to make sure our observer is called.  If not, we throw an assertion.
            //
            // Students: If your code is failing here, it means that your content provider
            // isn't calling getContext().getContentResolver().notifyChange(uri, null);
            //tco.waitForNotificationOrFail();

            locationCursor.unregisterContentObserver(tco);
            locationCursor.close();

            // A cursor is your primary interface to the query results.
            Cursor cursor = mContext.getContentResolver().query(
                    MovieEntry.CONTENT_URI,
                    null,   // projection
                    MovieEntry._ID + " = " + locationRowId,
                    null,   // Values for the "where" clause
                    null    // sort order
            );

            TestUtilities.validateCursor("testUpdateLocation.  Error validating location entry update.",
                    cursor, updatedValues);

            cursor.close();
        }


    // Make sure we can still delete after adding/updating stuff
    //
    // Student: Uncomment this test after you have completed writing the insert functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.
        public void testInsertReadProvider() {
            ContentValues testValues = TestUtilities.createNorthPoleLocationValues();

            // Register a content observer for our insert.  This time, directly with the content resolver
            TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
            mContext.getContentResolver().registerContentObserver(MovieEntry.CONTENT_URI, true, tco);
            Uri locationUri = mContext.getContentResolver().insert(MovieEntry.CONTENT_URI, testValues);

            // Did our content observer get called?  Students:  If this fails, your insert location
            // isn't calling getContext().getContentResolver().notifyChange(uri, null);
            mContext.getContentResolver().unregisterContentObserver(tco);

            long locationRowId = ContentUris.parseId(locationUri);

            // Verify we got a row back.
            assertTrue(locationRowId != -1);

            // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
            // the round trip.

            // A cursor is your primary interface to the query results.
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

    // Make sure we can still delete after adding/updating stuff
    //
    // Student: Uncomment this test after you have completed writing the delete functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.
        public void testDeleteRecords() {
            testInsertReadProvider();

            // Register a content observer for our location delete.
            TestUtilities.TestContentObserver locationObserver = TestUtilities.getTestContentObserver();
            mContext.getContentResolver().registerContentObserver(MovieEntry.CONTENT_URI, true, locationObserver);

            // Register a content observer for our weather delete.
            TestUtilities.TestContentObserver weatherObserver = TestUtilities.getTestContentObserver();
            mContext.getContentResolver().registerContentObserver(MovieEntry.CONTENT_URI, true, weatherObserver);

            deleteAllRecordsFromProvider();

            // Students: If either of these fail, you most-likely are not calling the
            // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
            // delete.  (only if the insertReadProvider is succeeding)
            //locationObserver.waitForNotificationOrFail();
            //weatherObserver.waitForNotificationOrFail();

            mContext.getContentResolver().unregisterContentObserver(locationObserver);
            mContext.getContentResolver().unregisterContentObserver(weatherObserver);
        }


    static private final int BULK_INSERT_RECORDS_TO_INSERT = 10;

    static ContentValues[] createBulkInsertWeatherValues(String locationRowId) {
        String currentTestMovieName = "ABCD";
        long millisecondsInADay = 1000 * 60 * 60 * 24;
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, currentTestMovieName += millisecondsInADay) {
            ContentValues weatherValues = new ContentValues();
            weatherValues.put(MovieEntry.COLUMN_MOVIE_IMAGE, currentTestMovieName);
            weatherValues.put(MovieEntry.COLUMN_MOVIE_URL, locationRowId);
            returnContentValues[i] = weatherValues;
        }
        return returnContentValues;
    }

    // Student: Uncomment this test after you have completed writing the BulkInsert functionality
    // in your provider.  Note that this test will work with the built-in (default) provider
    // implementation, which just inserts records one-at-a-time, so really do implement the
    // BulkInsert ContentProvider function.
    //    public void testBulkInsert() {
    //        // first, let's create a location value
    //        ContentValues testValues = TestUtilities.createNorthPoleLocationValues();
    //        Uri locationUri = mContext.getContentResolver().insert(LocationEntry.CONTENT_URI, testValues);
    //        long locationRowId = ContentUris.parseId(locationUri);
    //
    //        // Verify we got a row back.
    //        assertTrue(locationRowId != -1);
    //
    //        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
    //        // the round trip.
    //
    //        // A cursor is your primary interface to the query results.
    //        Cursor cursor = mContext.getContentResolver().query(
    //                LocationEntry.CONTENT_URI,
    //                null, // leaving "columns" null just returns all the columns.
    //                null, // cols for "where" clause
    //                null, // values for "where" clause
    //                null  // sort order
    //        );
    //
    //        TestUtilities.validateCursor("testBulkInsert. Error validating LocationEntry.",
    //                cursor, testValues);
    //
    //        // Now we can bulkInsert some weather.  In fact, we only implement BulkInsert for weather
    //        // entries.  With ContentProviders, you really only have to implement the features you
    //        // use, after all.
    //        ContentValues[] bulkInsertContentValues = createBulkInsertWeatherValues(locationRowId);
    //
    //        // Register a content observer for our bulk insert.
    //        TestUtilities.TestContentObserver weatherObserver = TestUtilities.getTestContentObserver();
    //        mContext.getContentResolver().registerContentObserver(WeatherEntry.CONTENT_URI, true, weatherObserver);
    //
    //        int insertCount = mContext.getContentResolver().bulkInsert(WeatherEntry.CONTENT_URI, bulkInsertContentValues);
    //
    //        // Students:  If this fails, it means that you most-likely are not calling the
    //        // getContext().getContentResolver().notifyChange(uri, null); in your BulkInsert
    //        // ContentProvider method.
    //        weatherObserver.waitForNotificationOrFail();
    //        mContext.getContentResolver().unregisterContentObserver(weatherObserver);
    //
    //        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);
    //
    //        // A cursor is your primary interface to the query results.
    //        cursor = mContext.getContentResolver().query(
    //                WeatherEntry.CONTENT_URI,
    //                null, // leaving "columns" null just returns all the columns.
    //                null, // cols for "where" clause
    //                null, // values for "where" clause
    //                WeatherEntry.COLUMN_DATE + " ASC"  // sort order == by DATE ASCENDING
    //        );
    //
    //        // we should have as many records in the database as we've inserted
    //        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);
    //
    //        // and let's make sure they match the ones we created
    //        cursor.moveToFirst();
    //        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext() ) {
    //            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating WeatherEntry " + i,
    //                    cursor, bulkInsertContentValues[i]);
    //        }
    //        cursor.close();
    //    }
}