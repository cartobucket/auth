package com.cartobucket.auth.api.dto;

import com.revethq.iam.user.domain.MemberType;
import jakarta.json.bind.annotation.JsonbNillable;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@JsonbNillable(false)
public class GroupMemberResponse {

    private String id;
    private UUID authorizationServerId;
    private UUID groupId;
    private UUID memberId;
    private MemberType memberType;
    private OffsetDateTime createdOn;

    public GroupMemberResponse() {}

    public GroupMemberResponse id(String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GroupMemberResponse authorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
        return this;
    }

    public UUID getAuthorizationServerId() {
        return authorizationServerId;
    }

    public void setAuthorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
    }

    public GroupMemberResponse groupId(UUID groupId) {
        this.groupId = groupId;
        return this;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public GroupMemberResponse memberId(UUID memberId) {
        this.memberId = memberId;
        return this;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    public GroupMemberResponse memberType(MemberType memberType) {
        this.memberType = memberType;
        return this;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
    }

    public GroupMemberResponse createdOn(OffsetDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public OffsetDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(OffsetDateTime createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupMemberResponse that = (GroupMemberResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(authorizationServerId, that.authorizationServerId) &&
                Objects.equals(groupId, that.groupId) &&
                Objects.equals(memberId, that.memberId) &&
                Objects.equals(memberType, that.memberType) &&
                Objects.equals(createdOn, that.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authorizationServerId, groupId, memberId, memberType, createdOn);
    }

    @Override
    public String toString() {
        return "GroupMemberResponse{" +
                "id='" + id + '\'' +
                ", authorizationServerId=" + authorizationServerId +
                ", groupId=" + groupId +
                ", memberId=" + memberId +
                ", memberType=" + memberType +
                ", createdOn=" + createdOn +
                '}';
    }
}
