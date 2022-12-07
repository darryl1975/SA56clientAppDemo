package sg.edu.nus.iss.demoapp.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	@GetMapping("/login")
    public String login() {
        return "login";
    }
	
    @GetMapping("/home")
	public String getHomePage() {
		return "homePage";
	}
	
    @GetMapping("/welcome")
	public String getWelcomePage() {
		return "welcomePage";
	}
    
	@GetMapping("/admin")
	public String getAdminPage() {
		return "adminPage";
	}
	
	@GetMapping("/emp")
	public String getEmployeePage() {
		return "empPage";
	}
	
	@GetMapping("/mgr")
	public String getManagerPage() {
		return "mgrPage";
	}
	
    @GetMapping("/accessDenied")
	public String getAccessDeniedPage() {
		return "accessDeniedPage";
	}
}
