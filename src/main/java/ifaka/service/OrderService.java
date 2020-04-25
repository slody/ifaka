package ifaka.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ifaka.dao.OrderDao;
import ifaka.paymentgateway.CodePay;
import ifaka.paymentgateway.GreenPay;
import ifaka.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaofeng
 * @date 2020/4/8 16:48
 */
@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductService productService;
    @Autowired
    private CodePay codePay;
    @Autowired
    private GreenPay greenPay;
    @Autowired
    private DiscountService discountService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private KeyService keyService;

    //支付接口
    private final String green = "greenpay";
    private final String code = "codepay";

    //支付方式
    private final String alipay = "zfb";
    private final String wechatpay = "wx";
    private final String qqpay = "qq";


    private Map<String, Object> baseCheck(Payment payment, Product product, int count) {
        Map<String, Object> r = new HashMap<>();
        if (payment == null) {
            r.put("status", "error");
            r.put("text", "支付方式不存在");
            return r;
        }
        if (product == null) {
            r.put("status", "error");
            r.put("text", "商品不存在");
            return r;
        }
        if (count < 1) {
            r.put("status", "error");
            r.put("text", "无效的购买数量");
            return r;
        }
        if (product.getStock() < count) {
            r.put("status", "error");
            r.put("text", "网站商品库存不足");
            return r;
        }
        return null;
    }

    private Map<String, Object> baseCheck(Payment payment, Product product, int count, Discount discount) {
        Map<String, Object> r = new HashMap<>();
        if (payment == null) {
            r.put("status", "error");
            r.put("text", "支付方式不存在");
            return r;
        }
        if (product == null) {
            r.put("status", "error");
            r.put("text", "商品不存在");
            return r;
        }
        if (discount == null) {
            r.put("status", "error");
            r.put("text", "优惠码不存在");
            return r;
        }
        if (count < 1) {
            r.put("status", "error");
            r.put("text", "无效的购买数量");
            return r;
        }
        if (product.getStock() < count) {
            r.put("status", "error");
            r.put("text", "网站商品库存不足");
            return r;
        }
        return null;
    }

    private Order noPay(Product product, Order order) {
        List<Key> keys = keyService.selectKeyByProductId(product.getId(), order.getCount());
        order.setStatus(1);
        order.setExpireTime(DateUtil.now());
        order.setMoney(new BigDecimal(0));
        order.setPayment("无");
        order.setQrcodeUrl("无");
        if (keys.size() < order.getCount()) {
            order.setKey("网站库存不足,请联系管理员");
        } else {
            String key = "";
            for (Key k : keys) {
                key = key + k.getCode() + "<br>";
            }
            order.setKey(key);
        }
        return order;
    }

    public Map<String, Object> createOrder(String contact, int count, int paymentid, int productid, HttpServletRequest request) {
        Payment payment = paymentService.selectPaymentById(paymentid);
        Product product = productService.selectProductById(productid);
        Map<String, Object> check = baseCheck(payment, product, count);
        if (check != null) {
            return check;
        }
        Order order = new Order();
        order.setCount(count);
        order.setDiscount("0");
        order.setCreateTime(DateUtil.now());
        order.setOrderNumber(RandomUtil.randomString(40).toUpperCase());
        order.setSecureNumber(RandomUtil.randomString(50).toUpperCase());
        order.setContact(contact);
        order.setStatus(0);
        order.setProductid(product.getId());
        BigDecimal price = product.getPrice().multiply(new BigDecimal(count)).setScale(2, BigDecimal.ROUND_DOWN);
        order.setPrice(price);
        Map<String, Object> r = new HashMap<>();
        if (price.compareTo(new BigDecimal(0)) < 1) {
            order = noPay(product, order);
            orderDao.insert(order);
            r.put("status", "success");
            r.put("text", "/query/" + order.getOrderNumber());
            return r;
        } else {
            String source = payment.getSource();
            String type = payment.getType();
            switch (source) {
                case green:
                    switch (type) {
                        case alipay:
                            order = greenPay.createAlipayOrder(payment, order);
                            if (StrUtil.hasBlank(order.getQrcodeUrl())) {
                                r.put("status", "error");
                                r.put("text", "订单创建失败");
                                return r;
                            } else {
                                int i = orderDao.insert(order);
                                if (i > 0) {
                                    r.put("status", "success");
                                    r.put("text", "/pay");
                                    request.getSession().setAttribute("ordernumber",order.getOrderNumber());
                                    return r;
                                } else {
                                    r.put("status", "error");
                                    r.put("text", "订单创建失败");
                                    return r;
                                }
                            }
                        case wechatpay:
                            order = greenPay.createWeChatOrder(payment, order);
                            if (StrUtil.hasBlank(order.getQrcodeUrl())) {
                                r.put("status", "error");
                                r.put("text", "订单创建失败");
                                return r;
                            } else {
                                int i = orderDao.insert(order);
                                if (i > 0) {
                                    r.put("status", "success");
                                    r.put("text", "/pay");
                                    request.getSession().setAttribute("ordernumber",order.getOrderNumber());
                                    return r;
                                } else {
                                    r.put("status", "error");
                                    r.put("text", "订单创建失败");
                                    return r;
                                }
                            }
                        default:
                            r.put("status", "error");
                            r.put("text", "后台出现未知错误");
                            return r;
                    }
                case code:
                    switch (type) {
                        case alipay:
                            order = codePay.createAlipayOrder(payment, order);
                            if (StrUtil.hasBlank(order.getQrcodeUrl())) {
                                r.put("status", "error");
                                r.put("text", "订单创建失败");
                                return r;
                            } else {
                                int i = orderDao.insert(order);
                                if (i > 0) {
                                    r.put("status", "success");
                                    r.put("text", "/pay");
                                    request.getSession().setAttribute("ordernumber",order.getOrderNumber());
                                    return r;
                                } else {
                                    r.put("status", "error");
                                    r.put("text", "订单创建失败");
                                    return r;
                                }
                            }
                        case wechatpay:
                            order = codePay.createWeChatOrder(payment, order);
                            if (StrUtil.hasBlank(order.getQrcodeUrl())) {
                                r.put("status", "error");
                                r.put("text", "订单创建失败");
                                return r;
                            } else {
                                int i = orderDao.insert(order);
                                if (i > 0) {
                                    r.put("status", "success");
                                    r.put("text", "/pay");
                                    request.getSession().setAttribute("ordernumber",order.getOrderNumber());
                                    return r;
                                } else {
                                    r.put("status", "error");
                                    r.put("text", "订单创建失败");
                                    return r;
                                }
                            }
                        case qqpay:
                            order = codePay.createQqOrder(payment, order);
                            if (StrUtil.hasBlank(order.getQrcodeUrl())) {
                                r.put("status", "error");
                                r.put("text", "订单创建失败");
                                return r;
                            } else {
                                int i = orderDao.insert(order);
                                if (i > 0) {
                                    r.put("status", "success");
                                    r.put("text", "/pay");
                                    request.getSession().setAttribute("ordernumber",order.getOrderNumber());
                                    return r;
                                } else {
                                    r.put("status", "error");
                                    r.put("text", "订单创建失败");
                                    return r;
                                }
                            }
                        default:
                            r.put("status", "error");
                            r.put("text", "后台出现未知错误");
                            return r;
                    }
                default:
                    r.put("status", "error");
                    r.put("text", "后台出现未知错误");
                    return r;
            }
        }

    }

    public Map<String, Object> createOrder(String contact, int count, int paymentid, int productid, String discountcode,HttpServletRequest request) {
        Payment payment = paymentService.selectPaymentById(paymentid);
        Product product = productService.selectProductById(productid);
        Discount discount = discountService.selectDiscountByCode(discountcode);
        Map<String, Object> check = baseCheck(payment, product, count, discount);
        if (check != null) {
            return check;
        }
        Order order = new Order();
        order.setCount(count);
        order.setDiscount(discount.getDiscount().toString());
        order.setCreateTime(DateUtil.now());
        order.setOrderNumber(RandomUtil.randomString(40).toUpperCase());
        order.setSecureNumber(RandomUtil.randomString(50).toUpperCase());
        order.setContact(contact);
        order.setStatus(0);
        order.setProductid(product.getId());
        BigDecimal price = product.getPrice().multiply(new BigDecimal(count)).setScale(2, BigDecimal.ROUND_DOWN).subtract(product.getPrice().multiply(new BigDecimal(count)).multiply(discount.getDiscount()).setScale(2, BigDecimal.ROUND_DOWN));
        order.setPrice(price);
        Map<String, Object> r = new HashMap<>();
        if (price.compareTo(new BigDecimal(0)) < 1) {
            order = noPay(product, order);
            int i = orderDao.insert(order);
            if (i > 0) {
                discount.setStatus(1);
                discountService.updateDiscount(discount);
                r.put("status", "success");
                r.put("text", "/query/" + order.getOrderNumber());
                return r;
            } else {
                r.put("status", "error");
                r.put("text", "订单创建失败");
                return r;
            }

        } else {
            String source = payment.getSource();
            String type = payment.getType();
            switch (source) {
                case green:
                    switch (type) {
                        case alipay:
                            order = greenPay.createAlipayOrder(payment, order);
                            if (StrUtil.hasBlank(order.getQrcodeUrl())) {
                                r.put("status", "error");
                                r.put("text", "订单创建失败");
                                return r;
                            } else {
                                int i = orderDao.insert(order);
                                if (i > 0) {
                                    discount.setStatus(1);
                                    discountService.updateDiscount(discount);
                                    r.put("status", "success");
                                    r.put("text", "/pay");
                                    request.getSession().setAttribute("ordernumber",order.getOrderNumber());
                                    return r;
                                } else {
                                    r.put("status", "error");
                                    r.put("text", "订单创建失败");
                                    return r;
                                }
                            }
                        case wechatpay:
                            order = greenPay.createWeChatOrder(payment, order);
                            if (StrUtil.hasBlank(order.getQrcodeUrl())) {
                                r.put("status", "error");
                                r.put("text", "订单创建失败");
                                return r;
                            } else {
                                int i = orderDao.insert(order);
                                if (i > 0) {
                                    discount.setStatus(1);
                                    discountService.updateDiscount(discount);
                                    r.put("status", "success");
                                    r.put("text", "/pay");
                                    request.getSession().setAttribute("ordernumber",order.getOrderNumber());
                                    return r;
                                } else {
                                    r.put("status", "error");
                                    r.put("text", "订单创建失败");
                                    return r;
                                }
                            }
                        default:
                            r.put("status", "error");
                            r.put("text", "后台出现未知错误");
                            return r;
                    }
                case code:
                    switch (type) {
                        case alipay:
                            order = codePay.createAlipayOrder(payment, order);
                            if (StrUtil.hasBlank(order.getQrcodeUrl())) {
                                r.put("status", "error");
                                r.put("text", "订单创建失败");
                                return r;
                            } else {
                                int i = orderDao.insert(order);
                                if (i > 0) {
                                    r.put("status", "success");
                                    r.put("text", "/pay");
                                    request.getSession().setAttribute("ordernumber",order.getOrderNumber());
                                    return r;
                                } else {
                                    r.put("status", "error");
                                    r.put("text", "订单创建失败");
                                    return r;
                                }
                            }
                        case wechatpay:
                            order = codePay.createWeChatOrder(payment, order);
                            if (StrUtil.hasBlank(order.getQrcodeUrl())) {
                                r.put("status", "error");
                                r.put("text", "订单创建失败");
                                return r;
                            } else {
                                int i = orderDao.insert(order);
                                if (i > 0) {
                                    r.put("status", "success");
                                    r.put("text", "/pay");
                                    request.getSession().setAttribute("ordernumber",order.getOrderNumber());
                                    return r;
                                } else {
                                    r.put("status", "error");
                                    r.put("text", "订单创建失败");
                                    return r;
                                }
                            }
                        case qqpay:
                            order = codePay.createQqOrder(payment, order);
                            if (StrUtil.hasBlank(order.getQrcodeUrl())) {
                                r.put("status", "error");
                                r.put("text", "订单创建失败");
                                return r;
                            } else {
                                int i = orderDao.insert(order);
                                if (i > 0) {
                                    r.put("status", "success");
                                    r.put("text", "/pay");
                                    request.getSession().setAttribute("ordernumber",order.getOrderNumber());
                                    return r;
                                } else {
                                    r.put("status", "error");
                                    r.put("text", "订单创建失败");
                                    return r;
                                }
                            }
                        default:
                            r.put("status", "error");
                            r.put("text", "后台出现未知错误");
                            return r;
                    }
                default:
                    r.put("status", "error");
                    r.put("text", "后台出现未知错误");
                    return r;
            }
        }

    }

    public Order selectByOrderNumber(String odernumber) {
        Order order = orderDao.selectByOrderNumberAndStatus(odernumber);
        return order;
    }

    public Order selectBySecureNumber(String securenumber) {
        return orderDao.selectBySecureNumber(securenumber);
    }

    public List<Order> searchOrder(String keyword) {
        return orderDao.searchOrder(keyword);
    }

    public Map<String, String> checkPay(String ordernumber) {
        Order order = orderDao.selectByOrderNumber(ordernumber);
        Map<String, String> map = new HashMap<>();
        if (order == null) {
            map.put("status", "500");
            return map;
        }
        if (order.getStatus() == 1) {
            map.put("status", "200");
            return map;
        } else {
            map.put("status", "400");
            return map;
        }
    }

    public int updateOrder(Order order) {
        return orderDao.updateById(order);
    }

    public IPage<Order> selectOrderPage(int p) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        Page<Order> page = new Page<>(p, 15);
        IPage<Order> iPage = orderDao.selectOrderPage(page, wrapper);
        return iPage;
    }

    public List<Order> selectLast10Order() {
        QueryWrapper<Order> queryWrapper = new QueryWrapper();
        queryWrapper.last("limit 10");
        queryWrapper.orderByDesc("id");
        return orderDao.selectList(queryWrapper);
    }

    public BigDecimal selectTodayIncome() {
        BigDecimal b = orderDao.selectTodayIncome("%" + DateUtil.today() + "%");
        if (b==null){
            return new BigDecimal(0);
        }
        return b;
    }

    public BigDecimal selectAllIncome() {
        BigDecimal b = orderDao.selectAllIncome();
        if (b==null){
            return new BigDecimal(0);
        }
        return b;
    }

    public long selectTodayOrderCount() {
        return orderDao.selectTodayOrderCount("%"+DateUtil.today()+"%");
    }

    public long selectAllOrderCount() {
        return orderDao.selectAllOrderCount();
    }
}
