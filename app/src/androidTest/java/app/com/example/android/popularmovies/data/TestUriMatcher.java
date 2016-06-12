package app.com.example.android.popularmovies.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/*
Uncomment this class when you are ready to test your UriMatcher.  Note that this class utilizes
constants that are declared with package protection inside of the UriMatcher, which is why
the test must be in the same data package as the Android app code.  Doing the test this way is
a nice compromise between data hiding and testability.
*/
public class TestUriMatcher extends AndroidTestCase {
    private static final String MOVIENAME = "ABCD";
    private static final String MOVIEURL = "aaa";

    // content://com.example.android.sunshine.app/weather"
    private static final Uri TEST_MOVIE_DIR = MovieContract.MovieEntry.CONTENT_URI;
    private static final Uri TEST_MOVIE_WITH_NAME_DIR =MovieContract.MovieEntry.buildMovieKey(MOVIENAME);
    private static final Uri TEST_MOVIE_URL_DIR = MovieContract.MovieEntry.buildMovieKey(MOVIEURL);


                /*
        Students: This function tests that your UriMatcher returns the correct integer value
        for each of the Uri types that our ContentProvider can handle.  Uncomment this when you are
        ready to test your UriMatcher.
     */
        public void testUriMatcher() {
            UriMatcher testMatcher = MovieProvider.buildUriMatcher();

            assertEquals("Error: The MOVIE URL was matched incorrectly.",
                    testMatcher.match(TEST_MOVIE_DIR), MovieProvider.MOVIE);
            assertEquals("Error: The MOVIE NAME matched incorrectly.",
                    testMatcher.match(TEST_MOVIE_WITH_NAME_DIR), MovieProvider.MOVIE_WITH_URL);
            assertEquals("Error: The MOVIE URL was matched incorrectly.",
                    testMatcher.match(TEST_MOVIE_URL_DIR), MovieProvider.MOVIE_WITH_URL);
        }
}