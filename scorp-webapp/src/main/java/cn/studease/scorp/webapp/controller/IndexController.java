package cn.studease.scorp.webapp.controller;

import cn.studease.scorp.core.service.UserService;
import cn.studease.scorp.dataaccess.entity.User;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author: liushaoping
 * Date: 2015/5/12.
 */
@Controller
public class IndexController {

    //@Autowired
    private UserService userService;

    {
        System.out.println("======================init IndexController===============");
    }

    @RequestMapping("/index")
    public void index(HttpServletResponse response) throws IOException {
        System.out.println("You are visiting the index page.");

        User user = userService.findUserById(1);
        System.out.println(user);

        response.getWriter().append("You are visiting the index page.").flush();
    }

    @RequestMapping("/addUser")
    public void addUser(HttpServletResponse response) throws IOException {
        System.out.println("You are visiting the addUser page.");

        User user = new User();
        user.setName("panda");
        user.setRemark("熊猫大侠");
        userService.addUser(user);

        response.getWriter().append("You are visiting the addUser page.").flush();
    }


}
