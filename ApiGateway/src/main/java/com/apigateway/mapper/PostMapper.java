package com.apigateway.mapper;

import com.apigateway.dto.PostsResponseDTO;
import proto.PostProto;
import proto.UserNamesResponseProto;

public class PostMapper {
    private PostMapper() {
    }

    public static PostsResponseDTO toDTO(PostProto post, UserNamesResponseProto names) {
        PostsResponseDTO postsResponseDTO = new PostsResponseDTO(post, names);
        postsResponseDTO.setImage(post.getImage());
        postsResponseDTO.setCreationDate(post.getCreationDate());
        return postsResponseDTO;
    }
}
