package algz.platform.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/userController")
public class UserController {
	private IUserService userService;

    public IUserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }
    
//    @RequestMapping("/{id}/index")
//    public String showUser(@PathVariable int id, HttpServletRequest request) {
////        User u = userService.getUserById(id);
////        request.setAttribute("user", u);
//        return "index";
//    }
    
    @RequestMapping("/{id}/index")
    public String showUser(@PathVariable int id) {
    	org.springframework.web.context.ContextLoaderListener d;
//        User u = userService.getUserById(id);
//        request.setAttribute("user", u);
        return "index";
    }
}
