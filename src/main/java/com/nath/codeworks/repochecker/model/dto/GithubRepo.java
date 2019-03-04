package com.nath.codeworks.repochecker.model.dto;

import com.nath.codeworks.repochecker.model.ghresponse.Contributor;

import java.util.List;

public class GithubRepo {

    private String repoName;
    private List<Contributor> contributors;

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public List<Contributor> getContributors() {
        return contributors;
    }

    public void setContributors(List<Contributor> contributors) {
        this.contributors = contributors;
    }
}
