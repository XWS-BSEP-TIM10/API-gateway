package com.apigateway.mapper;

import com.apigateway.dto.PostsResponseDTO;
import proto.PostProto;

import java.util.Base64;

public class PostMapper {
    public static PostsResponseDTO toDTO(PostProto post) {
        PostsResponseDTO postsResponseDTO = new PostsResponseDTO(post);
        postsResponseDTO.setImage(post.getImage());
        postsResponseDTO.setCreationDate(post.getCreationDate());
        return postsResponseDTO;
    }
}
