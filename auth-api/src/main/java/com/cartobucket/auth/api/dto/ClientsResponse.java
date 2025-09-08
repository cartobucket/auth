package com.cartobucket.auth.api.dto;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@JsonbNillable(false)
public class ClientsResponse {
    
    @JsonbProperty("clients")
    private List<ClientResponse> clients;
    
    @JsonbProperty("page")
    @NotNull
    private Page page;

    public ClientsResponse() {}

    public ClientsResponse clients(List<ClientResponse> clients) {
        this.clients = clients;
        return this;
    }

    public List<ClientResponse> getClients() {
        return clients;
    }

    public void setClients(List<ClientResponse> clients) {
        this.clients = clients;
    }

    public ClientsResponse page(Page page) {
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
        ClientsResponse that = (ClientsResponse) o;
        return Objects.equals(clients, that.clients) &&
                Objects.equals(page, that.page);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clients, page);
    }

    @Override
    public String toString() {
        return "ClientsResponse{" +
                "clients=" + clients +
                ", page=" + page +
                '}';
    }
}