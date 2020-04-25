package ifaka.controller;

import ifaka.bean.Config;
import ifaka.bean.Payment;
import ifaka.bean.Product;
import ifaka.bean.Type;
import ifaka.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaofeng
 * @date 2020/3/18 17:00
 */
@Controller
public class IndexController {
    @Autowired
    private ProductService productService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private PaymentService paymentService;

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        Config config = configService.selectConfig();
        List<Type> list = typeService.selectType();
        List<Payment> payments = paymentService.selectPayment();
        model.addAttribute("title",config.getTitle());
        model.addAttribute("keyword",config.getKeyword());
        model.addAttribute("description",config.getDescription());
        model.addAttribute("qq",config.getQq());
        model.addAttribute("notice",config.getNotice());
        model.addAttribute("backgroundimage",config.getBackgroundImage());
        model.addAttribute("icon",config.getIco());
        model.addAttribute("logo",config.getLogo());
        model.addAttribute("products",list);
        model.addAttribute("payments",payments);
        return "index";
    }

    @GetMapping("/selectproductbytypeid.do/{id}")
    @ResponseBody
    public List<Map<String,Object>> selectProduct(@PathVariable int id){
        List<Product> products = productService.selectProductByTypeId(id);
        List<Map<String,Object>> list = new ArrayList<>();
        for (Product p : products){
            Map<String,Object> map = new HashMap<>();
            map.put("name",p.getName());
            map.put("id",p.getId());
            list.add(map);
        }
        return list;
    }

    @GetMapping("/selectproductinfo.do/{id}")
    @ResponseBody
    public Product selectProductInfo(@PathVariable int id){
        return productService.selectProductById(id);
    }
}
