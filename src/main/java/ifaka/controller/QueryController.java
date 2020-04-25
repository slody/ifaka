package ifaka.controller;

import ifaka.bean.Config;
import ifaka.bean.Order;
import ifaka.service.ConfigService;
import ifaka.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xiaofeng
 * @date 2020/3/18 17:00
 */
@Controller
public class QueryController {
    @Autowired
    private ConfigService configService;
    @Autowired
    private OrderService orderService;

    @GetMapping(value = "/query")
    public String query(Model model){
        Config config = configService.selectConfig();
        model.addAttribute("title",config.getTitle());
        model.addAttribute("keyword",config.getKeyword());
        model.addAttribute("description",config.getDescription());
        model.addAttribute("qq",config.getQq());
        model.addAttribute("notice",config.getNotice());
        model.addAttribute("backgroundimage",config.getBackgroundImage());
        model.addAttribute("icon",config.getIco());
        model.addAttribute("logo",config.getLogo());
        return "query";
    }

    @GetMapping(value = "/query/{keyword}")
    public String query(Model model,@PathVariable("keyword") String keyword){
        Config config = configService.selectConfig();
        model.addAttribute("title",config.getTitle());
        model.addAttribute("keyword",config.getKeyword());
        model.addAttribute("description",config.getDescription());
        model.addAttribute("qq",config.getQq());
        model.addAttribute("notice",config.getNotice());
        model.addAttribute("backgroundimage",config.getBackgroundImage());
        model.addAttribute("icon",config.getIco());
        model.addAttribute("logo",config.getLogo());
        List<Order> results = orderService.searchOrder(keyword);
        model.addAttribute("results",results);
        return "query";
    }
}
