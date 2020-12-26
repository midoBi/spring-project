package com.app.reddit.service;


import com.app.reddit.dto.VoteDto;
import com.app.reddit.exceptions.PostNotFoundException;
import com.app.reddit.exceptions.SpringRedditException;
import com.app.reddit.model.Post;
import com.app.reddit.model.Vote;
import com.app.reddit.model.VoteType;
import com.app.reddit.repository.PostRepository;
import com.app.reddit.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found With Id " + voteDto.getPostId()));

        Optional<Vote> voteByPostAndUser  = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

        if (voteByPostAndUser.isPresent()) {
            Vote vote = voteByPostAndUser.get();
            if (vote.getVoteType().equals(voteDto.getVoteType())) {
                throw new SpringRedditException("You have already "
                        + voteDto.getVoteType() + "'d for this post");
            }
        }
        if (VoteType.UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        }else {
            post.setVoteCount(post.getVoteCount() - 1);
        }

        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
