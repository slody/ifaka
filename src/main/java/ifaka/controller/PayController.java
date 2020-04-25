package ifaka.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import ifaka.paymentgateway.CodePay;
import ifaka.paymentgateway.GreenPay;
import ifaka.bean.Order;
import ifaka.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author xiaofeng
 * @date 2020/4/8 22:40
 */
@Controller
public class PayController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private GreenPay greenPay;
    @Autowired
    private CodePay codePay;

    @PostMapping("/createorder.do")
    @ResponseBody
    public Map<String,Object> createOrder(@RequestParam String contact ,@RequestParam int count , @RequestParam int paymentid,@RequestParam int productid,HttpServletRequest request){
        return orderService.createOrder(contact,count,paymentid,productid,request);
    }

    @PostMapping("/createorderwithdiscount.do")
    @ResponseBody
    public Map<String,Object> createOrderWidthDiscount(@RequestParam String contact ,@RequestParam int count , @RequestParam int paymentid,@RequestParam int productid,@RequestParam String discount,HttpServletRequest request){
        return orderService.createOrder(contact,count,paymentid,productid,discount,request);
    }

    @GetMapping("/pay")
    public Object pay(Model model,HttpServletRequest request){
        String ordernumber = (String)request.getSession().getAttribute("ordernumber");
        if (StrUtil.hasBlank(ordernumber)){
            return "redirect:/";
        }
        Order order = orderService.selectByOrderNumber(ordernumber);
        if (order==null || DateUtil.parse(DateUtil.now()).getTime() > DateUtil.parse(order.getExpireTime()).getTime()){
            model.addAttribute("error","订单已过期或不存在!!!");
        }else{
            model.addAttribute("qrcode",order.getQrcodeUrl());
            model.addAttribute("leftTime",DateUtil.parse(order.getExpireTime()).getTime()/1000 - DateUtil.parse(DateUtil.now()).getTime()/1000);
            model.addAttribute("money",order.getMoney());
            model.addAttribute("ordernumber",order.getOrderNumber());
        }
        return "pay";
    }

    @PostMapping("/notify/greenpaynotify.do")
    @ResponseBody
    public Object greenpayNotify(HttpServletRequest request){
        return greenPay.notify(request);
    }

    @RequestMapping("/notify/codepaynotify.do")
    @ResponseBody
    public String codepayNotify(HttpServletRequest request){
        return codePay.notify(request);
    }

    @GetMapping("/checkpay.do/{ordernumber}")
    @ResponseBody
    public Object checkPay(@PathVariable("ordernumber") String ordernumber){
        return orderService.checkPay(ordernumber);
    }
}
