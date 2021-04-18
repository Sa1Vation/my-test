package cn.zjut;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
public class DemoController {
	@RequestMapping("/")
	@ResponseBody
	public String greeting() {
		return "Hello, World";
	}

	@RequestMapping("/sendMeASecret")
	@ResponseBody
	public UUID sendMeASecret(@RequestBody UUID secret){
		return secret;
	}
}
