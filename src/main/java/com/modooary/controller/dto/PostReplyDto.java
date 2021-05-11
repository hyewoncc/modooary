package com.modooary.controller.dto;

import com.modooary.domain.PostReply;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostReplyDto {
    private Long id;
    private String name;
    private String picture;
    private String content;

    public PostReplyDto(PostReply postReply){
        id = postReply.getId();
        name = postReply.getMember().getName();
        picture = postReply.getMember().getPicture();
        content = postReply.getContent();
    }
}
