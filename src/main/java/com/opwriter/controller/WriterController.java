package com.opwriter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.opwriter.services.ChatGptService;

@Controller
public class WriterController {

	@Autowired
	private ChatGptService chatGptService;
	public WriterController(ChatGptService chatGptService) {
		this.chatGptService = chatGptService;
	}

	@GetMapping("/")
	String showForm() {
		return "index";
	}
	
	   @PostMapping("/generate")
	    public String generateEssay(@RequestParam String TOPIC,@RequestParam Integer WORDS,@RequestParam String FORMAT, Model model) {
		   
		  		   
	        String essay = chatGptService.getAnswer(TOPIC,WORDS,FORMAT);
	        model.addAttribute("essay", essay);
	        return "index";
	    }

}
