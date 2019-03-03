package com.nath.codeworks.repochecker.model.dto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class GithubResultList<T> {

    private List<T> resultsList;
    private String nextPageUrl;

    public GithubResultList(List<T> resultsList, String nextPageUrl) {
        this.resultsList = resultsList;
        this.nextPageUrl = nextPageUrl;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public List<T> getResultsList() {
        return resultsList;
    }

    public void setResultsList(List<T> resultsList) {
        this.resultsList = resultsList;
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(this.resultsList);
    }

    public boolean hasNext() {
        return StringUtils.isNotBlank(nextPageUrl);
    }


}
