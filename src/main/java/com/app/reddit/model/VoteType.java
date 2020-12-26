package com.app.reddit.model;

import com.app.reddit.exceptions.SpringRedditException;

import java.util.Arrays;

public enum VoteType {

    UPVOTE(1), DOWNVOTE(-1);

    private int direction;

    VoteType(int direction) {}

    public static VoteType lookup(Integer direction) {
        return Arrays.stream(VoteType.values()).filter(voteType -> voteType.getDirection().equals(direction))
                .findAny()
                .orElseThrow(() -> new SpringRedditException("Vote not found"));
    }

    public Integer getDirection() {
        return direction;
    }
}
