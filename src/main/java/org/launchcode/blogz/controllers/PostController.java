package org.launchcode.blogz.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.launchcode.blogz.models.Post;
import org.launchcode.blogz.models.User;
import org.launchcode.blogz.models.dao.PostDao;
import org.launchcode.blogz.models.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PostController extends AbstractController {
	
	@Autowired
	private PostDao postdao;
	
	@Autowired
	private UserDao userdao;
	
	@RequestMapping(value = "/blog/newpost", method = RequestMethod.GET)
	public String newPostForm() {
		return "newpost";
	}
	
	@RequestMapping(value = "/blog/newpost", method = RequestMethod.POST)
	public String newPost(HttpServletRequest request, Model model) {
		
		// TODO - implement newPost
		
		// get request parameters, validate parameters
		// if valid create a new post, then save to the database using post dao
		// if invalid send them back to the form with an error message
		
		String title = request.getParameter("title");
		String body = request.getParameter("body");
		if (title == null || body == null) {
			String error = "";
			if (title == null) {
				error += "No Title.";
			}
			if (body == null) {
				error += "  No Body";
			}
			model.addAttribute("error", error);
			model.addAttribute("body", body);
			model.addAttribute("title", title);
			return "newpost";
		}
		HttpSession thisSession = request.getSession();
		User author = getUserFromSession(thisSession);
		String username = author.getUsername();
		Post newpost = new Post (title, body, author);
		int uid = newpost.getUid();
		postdao.save(newpost);
		
		
		return "redirect:blog/{username}/{uid}"; // TODO - this redirect should go to the new post's page  		
	}
	
	@RequestMapping(value = "/blog/{username}/{uid}", method = RequestMethod.GET)
	public String singlePost(@PathVariable String username, @PathVariable int uid, Model model) {
		
		// TODO - implement singlePost
		
		// get the given post based on uid 
		// paste the given post into the template
		
		Post post = postdao.findByUid(uid);
		model.addAttribute("post", post);
		
		return "post";
	}
	
	@RequestMapping(value = "/blog/{username}", method = RequestMethod.GET)
	public String userPosts(@PathVariable String username, Model model) {
		
		// TODO - implement userPosts
		
		User author = userdao.findByUsername(username);
		int authorId = author.getUid();
		List<Post> listofposts = postdao.findByAuthor(authorId);
		model.addAttribute("posts", listofposts);
		
		return "blog";
	}
	
}
