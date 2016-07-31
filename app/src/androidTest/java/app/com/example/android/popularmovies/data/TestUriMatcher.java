package app.com.example.android.popularmovies.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

public class TestUriMatcher extends AndroidTestCase {
    private static final String MOVIENAME = "ABCD";
    private static final String MOVIEURL = "aaa";

    private static final Uri TEST_MOVIE_DIR = MovieContract.MovieEntry.CONTENT_URI;
    private static final Uri TEST_MOVIE_WITH_NAME_DIR = MovieContract.MovieEntry.buildMovieKey(MOVIENAME);
    private static final Uri TEST_MOVIE_URL_DIR = MovieContract.MovieEntry.buildMovieKey(MOVIEURL);


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