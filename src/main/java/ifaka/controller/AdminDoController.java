package ifaka.controller;

import ifaka.bean.*;
import ifaka.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaofeng
 * @date 2020/3/28 17:35
 */
@RequestMapping(value = "/do")
@RestController
public class AdminDoController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private ProductService productService;
    @Autowired
    private DiscountService discountService;
    @Autowired
    private LogService logService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private KeyService keyService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/logout.do")
    public void logOut(HttpServletRequest request){
        request.getSession().invalidate();
    }

//    日志管理
    @GetMapping("/getlog.do/{page}")
    public Object getLogs(@PathVariable("page")int page){
        return logService.getLogs(page);
    }

    @GetMapping("/getlastlogininfo.do")
    public Object getLastLoginInfo(){
        return logService.getLastLoginInfo();
    }

//    密码管理
    @PostMapping("/updatepassword.do")
    public Object updatePassword(HttpServletRequest request, @RequestBody Map<String,Object> param){
        return adminService.updatePassword(param);
    }

//    设置管理
    @GetMapping("/selectconfig.do")
    public Object selectConfig(){
        return configService.selectConfig();
    }

    @PostMapping("/updateconfig.do")
    public Object updateConfig(@RequestBody Config config){
        return configService.updateConfig(config);
    }

//    优惠券管理
    @PostMapping("/insertdiscount.do")
    public Object insertDiscount(@RequestBody Map<String,Object> param){
        return discountService.insertDiscount(param);
    }

    @GetMapping("/selectdiscount.do/{page}")
    public Object selectDiscount(@PathVariable("page")int page){
        return discountService.selectDiscount(page);
    }

    @PostMapping("/deletediscount.do")
    public Object deleteDiscount(@RequestBody Map<String,List<Integer>> map){
        return discountService.deleteDiscount(map);
    }

//    分类管理
    @PostMapping("/inserttype.do")
    public Object insertType(@RequestBody Map<String, Object> param){
        return typeService.insertType(param);
    }

    @PostMapping("/updatetype.do")
    public Object updateType(@RequestBody Type type){
        return typeService.updateType(type);
    }

    @GetMapping("/deletetype.do/{id}")
    public Object deleteType(@PathVariable int id){
        return typeService.deleteType(id);
    }

    @GetMapping("/selecttype.do")
    public Object selectType(){
        return  typeService.selectType();
    }

//    商品管理
    @PostMapping("/insertproduct.do")
    public Object insertProduct(@RequestBody Product product){
        return productService.insertProduct(product);
    }

    @GetMapping("/selectproduct.do")
    public Object selectProduct(){
        return productService.selectProduct();
    }

    @GetMapping("/selectallproduct.do")
    public Object selectAllProduct(){
        return productService.selectAllProduct();
    }

    @PostMapping("/updateproduct.do")
    public Object updateProduct(@RequestBody Product product){
        return productService.updateProduct(product);
    }

    @GetMapping("/nosale.do/{id}")
    public Object noSale(@PathVariable("id") int id){
        return productService.noSale(id);
    }

    @GetMapping("/sale.do/{id}")
    public Object sale(@PathVariable("id") int id){
        return productService.sale(id);
    }

    @GetMapping("/selectproductbytypeid.do/{id}")
    public Object selectProductByTypeId(@PathVariable("id") int id){
        return productService.selectProductByTypeId(id);
    }

    @GetMapping("/deleteproduct/{id}")
    public Object deleteProduct(@PathVariable int id){
        return productService.deleteProductById(id);
    }

//    卡密管理
    @PostMapping("/insertkey.do")
    public Object insertKey(@RequestBody Map<String,String> map){
        return keyService.insertKey(map);
    }

    @GetMapping("/selectkey.do/{page}")
    public Object selectKey(@PathVariable("page")int page){
        return keyService.selectkey(page);
    }

    @PostMapping("/deletekey.do")
    public Object deleteKey(@RequestBody Map<String,List<Integer>> map){
        return keyService.deleteKey(map);
    }

//    支付方式管理
    @PostMapping("/insertpayment.do")
    public Object insertPayment(@RequestBody Payment payment){
        return paymentService.insertPayment(payment);
    }

    @GetMapping("/selectpayment.do")
    public Object selectPayment(){
        return paymentService.selectPayment();
    }

    @PostMapping("/updatepayment.do")
    public Object updatePayment(@RequestBody Payment payment){
        return paymentService.updatePayment(payment);
    }

    @GetMapping("/deletepayment.do/{id}")
    public Object deletePayment(@PathVariable("id")int id){
        return paymentService.deleteById(id);
    }

//    订单管理
    @GetMapping("/selectorder.do/{page}")
    public Object selectOrder(@PathVariable("page")int page){
        return orderService.selectOrderPage(page);
    }

    @GetMapping("/selectlast10order.do")
    public Object selectLast10Order(){
        return orderService.selectLast10Order();
    }

    @GetMapping("/getbasicinfo.do")
    public Object getBasicInfo(){
        Map<String,Object> map = new HashMap<>();
        map.put("TodayIncome",orderService.selectTodayIncome());
        map.put("TodayOrderCount",orderService.selectTodayOrderCount());
        map.put("AllIncome",orderService.selectAllIncome());
        map.put("AllOrderCount",orderService.selectAllOrderCount());
        return map;
    }
}
