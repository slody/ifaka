package ifaka.paymentgateway;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author xiaofeng
 * @date 2020/4/8 22:44
 */
@Component
public class GreenPay {
    @Autowired
    private ConfigService configService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private KeyService keyService;

    public Order createWeChatOrder(Payment payment, Order order){
        Map<String,Object> param = new HashMap<>();
        Config config = configService.selectConfig();
        param.put("uid",payment.getGreenpayUid());
        param.put("mode",2);
        param.put("type",2);
        param.put("notifyurl","http://"+config.getDomain()+"/notify/greenpaynotify.do");
        param.put("ordernum", order.getSecureNumber());
        param.put("amount",order.getPrice());
        param.put("key", SecureUtil.md5(param.get("type")+ (String)param.get("notifyurl")+param.get("ordernum")+ param.get("amount")+param.get("mode")+payment.getGreenpayToken() + payment.getGreenpayUid()));
        try {
            String result = HttpUtil.post("https://www.greenyep.com/api/index",param);
            JSONObject jsonObject = JSONObject.parseObject(result);
            if ((int)jsonObject.get("code")==200){
                JSONObject object = jsonObject.getJSONObject("content");
                order.setPayment(payment.getName());
                order.setExpireTime(DateUtil.format(DateUtil.offsetSecond(DateUtil.parse(DateUtil.now()), Integer.parseInt((String)object.get("timeout"))),"yyyy-MM-dd HH:mm:ss"));
                order.setMoney(new BigDecimal((String)object.get("realamount")));
                order.setQrcodeUrl((String)object.get("payurl"));
                return order;
            }else{
                System.out.println(DateUtil.now()+" 绿点支付接口出现异常:"+jsonObject.get("content"));
                return order;
            }
        }catch (IORuntimeException e){
            System.out.println(DateUtil.now()+" 绿点支付接口无法访问");
            return order;
        }
    }

    public Order createAlipayOrder(Payment payment, Order order){
        Map<String,Object> param = new HashMap<>();
        Config config = configService.selectConfig();
        param.put("uid",payment.getGreenpayUid());
        param.put("mode",2);
        param.put("type",1);
        param.put("notifyurl","http://"+config.getDomain()+"/notify/greenpaynotify.do");
        param.put("ordernum", order.getSecureNumber());
        param.put("amount",order.getPrice());
        param.put("key", SecureUtil.md5(param.get("type")+ (String)param.get("notifyurl")+param.get("ordernum")+ param.get("amount")+param.get("mode")+payment.getGreenpayToken() + payment.getGreenpayUid()));
        try {
            String result = HttpUtil.post("https://www.greenyep.com/api/index",param);
            JSONObject jsonObject = JSONObject.parseObject(result);
            if ((int)jsonObject.get("code")==200){
                JSONObject object = jsonObject.getJSONObject("content");
                order.setPayment(payment.getName());
                order.setExpireTime(DateUtil.format(DateUtil.offsetSecond(DateUtil.parse(DateUtil.now()), Integer.parseInt((String)object.get("timeout"))),"yyyy-MM-dd HH:mm:ss"));
                order.setMoney(new BigDecimal((String)object.get("realamount")));
                order.setQrcodeUrl((String)object.get("payurl"));
                return order;
            }else{
                System.out.println(DateUtil.now()+" 绿点支付接口出现异常:"+jsonObject.get("content"));
                return order;
            }
        }catch (IORuntimeException e){
            System.out.println(DateUtil.now()+" 绿点支付接口无法访问");
            return order;
        }
    }

    public Map<String,Object> notify(HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        String securenum = request.getParameter("ordernum");
        String greenpay_id = request.getParameter("greenpay_id");
        if (StrUtil.hasBlank(securenum) || StrUtil.hasBlank(greenpay_id)){
            map.put("code",400);
            map.put("content","参数不完整");
            return map;
        }else{
            Order order = orderService.selectBySecureNumber(securenum);
            if (order==null || DateUtil.parse(DateUtil.now()).getTime() > DateUtil.parse(order.getExpireTime()).getTime()){
                map.put("code",400);
                map.put("content","订单不存在或已过期");
                return map;
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
                order.setTradingNumber(greenpay_id);
                order.setStatus(1);
                orderService.updateOrder(order);
                map.put("code",200);
                map.put("content","ok");
                return map;
            }
        }
    }
}
