package ifaka.controller;

import ifaka.bean.Admin;
import ifaka.service.AdminService;
import ifaka.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaofeng
 * @date 2020/3/21 0:13
 */
@Controller
@RequestMapping("/:")
public class AdminController {
    @Autowired
    private ConfigService configService;
    @Autowired
    private AdminService adminService;

    @GetMapping({"","/", "/index"})
    public String index(Model model) {
        model.addAttribute("icon",configService.selectConfig().getIco());
        return "admin";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("icon",configService.selectConfig().getIco());
        return "login";
    }

    @PostMapping("/login.do")
    @ResponseBody
    public Map<String,String> loginDo(@RequestBody Admin admin, HttpServletRequest request, HttpServletResponse response){
        HashMap<String,String> map = new HashMap<>();
        if (admin.getUsername()==null || admin.getPassword()==null || "".equals(admin.getPassword()) || "".equals(admin.getUsername())){
            map.put("status","error");
            return map;
        }else{
            String result = adminService.login(admin.getUsername(),admin.getPassword(),request,response);
            map.put("status",result);
            return map;
        }
    }
}
