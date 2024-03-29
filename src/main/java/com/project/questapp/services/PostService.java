package com.project.questapp.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.questapp.entities.Post;
import com.project.questapp.entities.User;
import com.project.questapp.repos.PostRepository;
import com.project.questapp.requests.PostCreateRequest;
import com.project.questapp.requests.PostUpdateRequest;
import com.project.questapp.responses.LikeResponse;
import com.project.questapp.responses.PostResponse;

@Service
public class PostService {

	private PostRepository postRepository;
	private UserService userService;
	private LikeService likeService;

	public PostService(PostRepository postRepository, UserService userService) {
		this.postRepository = postRepository;
		this.userService = userService;
	}
	
	@Autowired
	public void setLikeService(LikeService likeService) {
		this.likeService=likeService;
	}
	

	public List<PostResponse> getAllPosts(Optional<Long> userId) {
		List<Post> list;
		if (userId.isPresent()) {
			list = postRepository.findByUserId(userId.get());
		} else {
			list = postRepository.findAll();
		}

		return list.stream().map(p -> {
			List<LikeResponse> likeList;
			likeList=likeService.getAllLikes(Optional.of(p.getId()), Optional.ofNullable(null));
			return new PostResponse(p, likeList);
		}).collect(Collectors.toList());
	}

	public Post createOnePost(PostCreateRequest newPostCreateRequest) {
		User user = userService.getOneUserById(newPostCreateRequest.getUserId());
		if (user == null)
			return null;
		Post toSave = new Post();
		toSave.setId(newPostCreateRequest.getId());
		toSave.setTitle(newPostCreateRequest.getTitle());
		toSave.setText(newPostCreateRequest.getText());
		toSave.setUser(user);
		toSave.setCreateDate(new Date());
		return postRepository.save(toSave);
	}

	public Post getOnePostById(Long postId) {
		return postRepository.findById(postId).orElse(null);
	}
	
	public PostResponse getOnePostByIdWithLikes(Long postId) {
		Post post = new Post();
		post = postRepository.findById(postId).orElse(null);
		List<LikeResponse> likes = likeService.getAllLikes(Optional.of(postId), Optional.ofNullable(null));
		
		return new PostResponse(post, likes);
	}

	public Post updateOnePostById(Long postId, PostUpdateRequest updatePost) {
		Optional<Post> post = postRepository.findById(postId);
		if (post.isPresent()) {
			Post toUpdate = post.get();
			toUpdate.setTitle(updatePost.getTitle());
			toUpdate.setText(updatePost.getText());
			return postRepository.save(toUpdate);
		}
		return null;
	}

	public void deleteOnePostById(Long postId) {
		this.postRepository.deleteById(postId);
	}

}
