
package com.example.amit.moviebuzz;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchedData {

    @SerializedName("Search")
    @Expose
    private List<Search> search = null;
    @SerializedName("totalResults")
    @Expose
    private String totalResults;
    @SerializedName("Response")
    @Expose
    private String response;

    /**
     * No args constructor for use in serialization
     * 
     */
    public SearchedData() {
    }

    /**
     * 
     * @param response
     * @param totalResults
     * @param search
     */
    public SearchedData(List<Search> search, String totalResults, String response) {
        super();
        this.search = search;
        this.totalResults = totalResults;
        this.response = response;
    }

    public List<Search> getSearch() {
        return search;
    }

    public void setSearch(List<Search> search) {
        this.search = search;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}
