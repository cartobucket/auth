package com.cartobucket.auth.api.dto;

import com.revethq.iam.user.domain.MemberType;
import jakarta.json.bind.annotation.JsonbNillable;
import java.util.Objects;
import java.util.UUID;

@JsonbNillable(false)
public class GroupMemberRequest {

    private UUID authorizationServerId;
    private UUID memberId;
    private MemberType memberType;

    public GroupMemberRequest() {}

    public GroupMemberRequest authorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
        return this;
    }

    public UUID getAuthorizationServerId() {
        return authorizationServerId;
    }

    public void setAuthorizationServerId(UUID authorizationServerId) {
        this.authorizationServerId = authorizationServerId;
    }

    public GroupMemberRequest memberId(UUID memberId) {
        this.memberId = memberId;
        return this;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    public GroupMemberRequest memberType(MemberType memberType) {
        this.memberType = memberType;
        return this;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupMemberRequest that = (GroupMemberRequest) o;
        return Objects.equals(authorizationServerId, that.authorizationServerId) &&
                Objects.equals(memberId, that.memberId) &&
                Objects.equals(memberType, that.memberType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorizationServerId, memberId, memberType);
    }

    @Override
    public String toString() {
        return "GroupMemberRequest{" +
                "authorizationServerId=" + authorizationServerId +
                ", memberId=" + memberId +
                ", memberType=" + memberType +
                '}';
    }
}
