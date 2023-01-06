package com.example.crudpersional.controller;

import com.example.crudpersional.domain.dto.Response;
import com.example.crudpersional.domain.entity.User;
import com.example.crudpersional.exceptionManager.ErrorCode;
import com.example.crudpersional.exceptionManager.UserException;
import com.example.crudpersional.mvc.dto.LikeRequest;
import com.example.crudpersional.service.PostService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@Slf4j
public class LikeController {
    private final PostService postService;




    @ApiOperation(value = "해당 글 좋아요", notes = "정상적인 JWT토큰 발급 받은 사용자만 해당 글 좋아요 가능")
    @PostMapping("/{postId}/likes")
    public Response like(@PathVariable Long postId, @ApiIgnore Authentication authentication) {
        postService.like(postId, authentication.getName());
        return Response.success("좋아요 성공");
    }

    @ApiOperation(value = "해당 글 좋아요 갯수", notes = "해당 postId에 해당하는 글의 좋아요 count 구하는 API")
    @GetMapping("/{postId}/likes")
    public Response<String> getLikeCount(@PathVariable Long postId) {
        Integer likeCount = postService.getLikeCount(postId);
        return Response.successToMessage(String.format("%s번 게시글의 좋아요 개수 : %d", postId, likeCount));
    }



    @ApiOperation(value = "해당 글 좋아요", notes = "정상적인 JWT토큰 발급 받은 사용자만 해당 글 좋아요 가능")
    @PostMapping("/mvc/likes")
    public Response likeMvc(@RequestBody LikeRequest request, @SessionAttribute(name = "loginMember", required = false) User loginMember, HttpServletResponse response) throws Exception {
        log.info("좋아요 버튼 클릭 후 값 :{} / {}",request.getPostId(),loginMember);
        //세션에 저장된 user의 정보
        if (loginMember == null) {
            throw new UserException(ErrorCode.USERNAME_NOT_FOUND);
        }
        postService.like(request.getPostId(), loginMember.getUsername());
        return Response.success("좋아요를 눌렀습니다.");
    }






}
