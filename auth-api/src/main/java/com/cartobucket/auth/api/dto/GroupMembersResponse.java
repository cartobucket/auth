package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@JsonbNillable(false)
public class GroupMembersResponse {

    private List<GroupMemberResponse> groupMembers;
    @NotNull
    private Page page;

    public GroupMembersResponse() {}

    public GroupMembersResponse groupMembers(List<GroupMemberResponse> groupMembers) {
        this.groupMembers = groupMembers;
        return this;
    }

    public List<GroupMemberResponse> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<GroupMemberResponse> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public GroupMembersResponse page(Page page) {
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
        GroupMembersResponse that = (GroupMembersResponse) o;
        return Objects.equals(groupMembers, that.groupMembers) &&
                Objects.equals(page, that.page);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupMembers, page);
    }

    @Override
    public String toString() {
        return "GroupMembersResponse{" +
                "groupMembers=" + groupMembers +
                ", page=" + page +
                '}';
    }
}
