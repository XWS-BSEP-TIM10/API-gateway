package com.apigateway.service;

import proto.AddReactionResponseProto;

public interface PostService {
    AddReactionResponseProto addReaction(String postId, String userId, Boolean like);
}
