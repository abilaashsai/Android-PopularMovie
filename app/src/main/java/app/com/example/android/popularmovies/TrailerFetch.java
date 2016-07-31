
package app.com.example.android.popularmovies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TrailerFetch {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<TrailerResult> results = new ArrayList<TrailerResult>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<TrailerResult> getResults() {
        return results;
    }

    public void setResults(List<TrailerResult> results) {
        this.results = results;
    }

}
