package org.launchcode.blogz.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.launchcode.blogz.models.User;
import org.launchcode.blogz.models.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthenticationController extends AbstractController {
	
	@Autowired
	private UserDao userdao;
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signupForm() {
		return "signup";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(HttpServletRequest request, Model model) {
		
		// TODO - implement signup
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String verify = request.getParameter("verify");
		
		if (!User.isValidPassword(password) || !User.isValidUsername(username) || !password.equals(verify)) {
			model.addAttribute("username", username);
			if (!User.isValidUsername(username)) {
				String username_error = "Invalid Username.";
				model.addAttribute("username_error", username_error);
			}
			
			if (!User.isValidPassword(password)) {
				String password_error = "Invalid Password.";
				model.addAttribute("password_error", password_error);
			}
			if (!password.equals(verify)) {
				String verify_error = "Passwords Do Not Match.";
				model.addAttribute("verify_error", verify_error);
			}
			return "signup";
		}
		
		User newuser = new User (username, password);
		userdao.save(newuser);
		HttpSession thisSession = request.getSession();
		setUserInSession (thisSession, newuser);
		
		
		return "redirect:blog/newpost";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm() {
		return "login";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, Model model) {
		
		// TODO - implement login
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User user = userdao.findByUsername(username);
		if (!user.isMatchingPassword(password)) {
			String error = "Incorrect Password.";
			model.addAttribute("error", error);
			model.addAttribute("username", username);
			return "login";
		}
		HttpSession thisSession = request.getSession();
		setUserInSession (thisSession, user);
		
		return "redirect:blog/newpost";
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
        request.getSession().invalidate();
		return "redirect:/";
	}
}
