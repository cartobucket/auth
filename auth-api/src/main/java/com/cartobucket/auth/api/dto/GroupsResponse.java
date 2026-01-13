package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@JsonbNillable(false)
public class GroupsResponse {

    private List<GroupResponse> groups;
    @NotNull
    private Page page;

    public GroupsResponse() {}

    public GroupsResponse groups(List<GroupResponse> groups) {
        this.groups = groups;
        return this;
    }

    public List<GroupResponse> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupResponse> groups) {
        this.groups = groups;
    }

    public GroupsResponse page(Page page) {
        this.page = page;
        return this;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupsResponse that = (GroupsResponse) o;
        return Objects.equals(groups, that.groups) &&
                Objects.equals(page, that.page);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groups, page);
    }

    @Override
    public String toString() {
        return "GroupsResponse{" +
                "groups=" + groups +
                ", page=" + page +
                '}';
    }
}
