package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@JsonbNillable(false)
public class UsersResponse {

    private List<UserResponse> users;
    @NotNull
    private Page page;

    public UsersResponse() {}

    public UsersResponse users(List<UserResponse> users) {
        this.users = users;
        return this;
    }

    public List<UserResponse> getUsers() {
        return users;
    }

    public void setUsers(List<UserResponse> users) {
        this.users = users;
    }

    public UsersResponse page(Page page) {
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
        UsersResponse that = (UsersResponse) o;
        return Objects.equals(users, that.users) &&
                Objects.equals(page, that.page);
    }

    @Override
    public int hashCode() {
        return Objects.hash(users, page);
    }

    @Override
    public String toString() {
        return "UsersResponse{" +
                "users=" + users +
                ", page=" + page +
                '}';
    }
}