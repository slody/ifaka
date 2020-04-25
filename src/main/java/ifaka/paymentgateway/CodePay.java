package ifaka.paymentgateway;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import ifaka.bean.Config;
import ifaka.bean.Key;
import ifaka.bean.Order;
import ifaka.bean.Payment;
import ifaka.service.ConfigService;
import ifaka.service.KeyService;
import ifaka.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaofeng
 * @date 2020/4/8 22:44
 */

@Component
public class CodePay {
    @Autowired
    private ConfigService configService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private KeyService keyService;

    public Order createWeChatOrder(Payment payment, Order order){
        Map<String,Object> param = new HashMap<>();
        Config config = configService.selectConfig();
        param.put("id",payment.getCodepayId());
        param.put("type",3);
        param.put("price",order.getPrice());
        param.put("pay_id",order.getOrderNumber());
        param.put("param",order.getSecureNumber());
        param.put("notify_url","http://"+config.getDomain()+"/notify/codepaynotify.do");
        param.put("page",4);
        param.put("token",payment.getCodepayToken());
        try {
            String result = HttpUtil.post("https://api.xiuxiu888.com/creat_order/",param);
            JSONObject jsonObject = JSONObject.parseObject(result);
            if ((int)jsonObject.get("status")==0){
                order.setPayment(payment.getName());
                order.setExpireTime(DateUtil.format(new Date(Long.parseLong(jsonObject.get("endTime").toString())*1000),"yyyy-MM-dd HH:mm:ss"));
                order.setMoney(new BigDecimal((String)jsonObject.get("money")));
                order.setQrcodeUrl((String)jsonObject.get("qrcode"));
                return order;
            }else{
                System.out.println(DateUtil.now()+" 码支付接口出现异常:"+jsonObject.get("msg"));
                return order;
            }
        }catch (IORuntimeException e){
            System.out.println(DateUtil.now()+" 码支付接口无法访问");
            return order;
        }
    }

    public Order createAlipayOrder(Payment payment, Order order){
        Map<String,Object> param = new HashMap<>();
        Config config = configService.selectConfig();
        param.put("id",payment.getCodepayId());
        param.put("type",1);
        param.put("price",order.getPrice());
        param.put("pay_id",order.getOrderNumber());
        param.put("param",order.getSecureNumber());
        param.put("notify_url","http://"+config.getDomain()+"/notify/codepaynotify.do");
        param.put("page",4);
        param.put("token",payment.getCodepayToken());
        try {
            String result = HttpUtil.post("https://api.xiuxiu888.com/creat_order/",param);
            JSONObject jsonObject = JSONObject.parseObject(result);
            if ((int)jsonObject.get("status")==0){
                order.setPayment(payment.getName());
                order.setExpireTime(DateUtil.format(new Date(Long.parseLong(jsonObject.get("endTime").toString())*1000),"yyyy-MM-dd HH:mm:ss"));
                order.setMoney(new BigDecimal((String)jsonObject.get("money")));
                order.setQrcodeUrl((String)jsonObject.get("qrcode"));
                return order;
            }else{
                System.out.println(DateUtil.now()+" 码支付接口出现异常:"+jsonObject.get("msg"));
                return order;
            }
        }catch (IORuntimeException e){
            System.out.println(DateUtil.now()+" 码支付接口无法访问");
            return order;
        }
    }

    public Order createQqOrder(Payment payment, Order order){
        Map<String,Object> param = new HashMap<>();
        Config config = configService.selectConfig();
        param.put("id",payment.getCodepayId());
        param.put("type",2);
        param.put("price",order.getPrice());
        param.put("pay_id",order.getOrderNumber());
        param.put("param",order.getSecureNumber());
        param.put("notify_url","http://"+config.getDomain()+"/notify/codepaynotify.do");
        param.put("page",4);
        param.put("token",payment.getCodepayToken());
        try {
            String result = HttpUtil.post("https://api.xiuxiu888.com/creat_order/",param);
            JSONObject jsonObject = JSONObject.parseObject(result);
            if ((int)jsonObject.get("status")==0){
                order.setPayment(payment.getName());
                order.setExpireTime(DateUtil.format(new Date(Long.parseLong(jsonObject.get("endTime").toString())*1000),"yyyy-MM-dd HH:mm:ss"));
                order.setMoney(new BigDecimal((String)jsonObject.get("money")));
                order.setQrcodeUrl((String)jsonObject.get("qrcode"));
                return order;
            }else{
                System.out.println(DateUtil.now()+" 码支付接口出现异常:"+jsonObject.get("msg"));
                return order;
            }
        }catch (IORuntimeException e){
            System.out.println(DateUtil.now()+" 码支付接口无法访问");
            return order;
        }
    }

    public String notify(HttpServletRequest request){
        String ordernumber = request.getParameter("pay_id");
        String securenumber = request.getParameter("param");
        String tradingNumber = request.getParameter("pay_no");
        if (StrUtil.hasBlank(ordernumber) || StrUtil.hasBlank(securenumber) || StrUtil.hasBlank(tradingNumber)){
            return "fail";
        }else{
            Order order = orderService.selectByOrderNumber(ordernumber);
            if (order==null || !(order.getSecureNumber().equals(securenumber)) || DateUtil.parse(DateUtil.now()).getTime() > DateUtil.parse(order.getExpireTime()).getTime()){
                return "fail";
            }else{
                List<Key> keys = keyService.selectKeyByProductId(order.getProductid(),order.getCount());
                if (keys.size() < order.getCount()) {
                    order.setKey("网站库存不足,请联系管理员");
                } else {
                    String key = "";
                    for (Key k : keys) {
                        key = key + k.getCode() + "<br>";
                    }
                    order.setKey(key);
                }
                order.setTradingNumber(tradingNumber);
                order.setStatus(1);
                orderService.updateOrder(order);
                return "success";
            }
        }
    }
}
