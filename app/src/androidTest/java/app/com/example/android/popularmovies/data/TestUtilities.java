package app.com.example.android.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.HandlerThread;

import java.util.Map;
import java.util.Set;

import app.com.example.android.popularmovies.utils.PollingCheck;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TestUtilities {

    public static final String TEST_TABLE_NAME = "movie";
    public static final String TEST_COLUMN_MOVIE_TITLE = "movie_title";
    public static final String TEST_COLUMN_MOVIE_URL = "movie_url";
    public static final String TEST_COLUMN_MOVIE_POSTER = "movie_poster";
    public static final String TEST_COLUMN_MOVIE_SYNOPSIS = "movie_synopsis";
    public static final String TEST_COLUMN_MOVIE_USER_RATING = "movie_user_rating";
    public static final String TEST_COLUMN_MOVIE_RELEASE_DATE = "movie_release_date";
    public static final String TEST_COLUMN_MOVIE_TRAILER = "movie_trailer";

    public static final String COLUMN_MOVIE_ID = "movie_id";

    static ContentValues createMovieValues() {
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, TEST_COLUMN_MOVIE_TITLE);
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_URL, TEST_COLUMN_MOVIE_URL);
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, TEST_COLUMN_MOVIE_POSTER);
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS, TEST_COLUMN_MOVIE_SYNOPSIS);
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_USER_RATING, TEST_COLUMN_MOVIE_USER_RATING);
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, TEST_COLUMN_MOVIE_RELEASE_DATE);
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TRAILER, TEST_COLUMN_MOVIE_TRAILER);
        return testValues;
    }

    static long insertMovieValues(Context context) {
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createMovieValues();

        long locationRowId;
        locationRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);

        assertTrue("Error: Failure to insert North Pole Location Values", locationRowId != -1);

        return locationRowId;
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
