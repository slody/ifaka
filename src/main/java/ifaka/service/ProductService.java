package ifaka.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import ifaka.dao.KeyDao;
import ifaka.dao.ProductDao;
import ifaka.bean.Product;
import ifaka.bean.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaofeng
 * @date 2020/3/28 18:39
 */
@Service
public class ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private TypeService typeService;
    @Autowired
    private KeyDao keyDao;

    public List<Product> selectAllProduct(){
        return productDao.selectList(null);
    }

    public Map<String, Object> insertProduct(Product product) {
        Map<String, Object> r = new HashMap<>();
        if (product.getTypeid() == 0 || StrUtil.hasBlank(product.getName()) || product.getPrice() == null || StrUtil.hasBlank(product.getDescription())) {
            r.put("status", "warning");
            r.put("text", "请填写所有内容");
            return r;
        } else {
            if (product.getPrice().compareTo(new BigDecimal(0)) == 0 || product.getPrice().compareTo(new BigDecimal(0)) == -1) {
                r.put("status", "error");
                r.put("text", "商品定价不符合规则");
                return r;
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("name", product.getName());
                List<Product> list = productDao.selectByMap(map);
                if (list.size() > 0) {
                    r.put("status", "error");
                    r.put("text", "当前商品名称已存在");
                    return r;
                } else {
                    product.setCreateTime(DateUtil.now());
                    product.setStatus(1);
                    product.setPrice(product.getPrice().setScale(2,BigDecimal.ROUND_DOWN));
                    int i = productDao.insert(product);
                    if (i > 0) {
                        r.put("status", "success");
                        r.put("text", "商品添加成功");
                        return r;
                    } else {
                        r.put("status", "error");
                        r.put("text", "数据库或后台发生异常");
                        return r;
                    }
                }
            }
        }
    }

    public Map<String, List<Product>> selectProduct() {
        Map<String, List<Product>> map = new HashMap<>();
        List<Type> list = typeService.selectType();
        List<Product> result = productDao.selectProducts();
        for (Type t : list) {
            long typeid = t.getId();
            List<Product> plist = new ArrayList<>();
            for(Product p:result){
                if (p.getTypeid() == typeid){
                    plist.add(p);
                }
            }
            map.put(t.getName(), plist);
        }
        return map;
    }

    public Map<String, Object> updateProduct(Product product) {
        Map<String, Object> r = new HashMap<>();
        if (product.getTypeid() == 0 || StrUtil.hasBlank(product.getName()) || product.getPrice() == null || StrUtil.hasBlank(product.getDescription())) {
            r.put("status", "warning");
            r.put("text", "请填写所有内容");
            return r;
        } else {
            if (product.getPrice().compareTo(new BigDecimal(0)) == 0 || product.getPrice().compareTo(new BigDecimal(0)) == -1) {
                r.put("status", "error");
                r.put("text", "商品定价不符合规则");
                return r;
            } else {
                Product p = productDao.selectById(product.getId());
                product.setPrice(product.getPrice().setScale(2,BigDecimal.ROUND_DOWN));
                product.setStatus(p.getStatus());
                product.setCreateTime(p.getCreateTime());
                int i = productDao.updateById(product);
                if (i > 0) {
                    r.put("status", "success");
                    r.put("text", "商品修改成功");
                    return r;
                } else {
                    r.put("status", "error");
                    r.put("text", "数据库或后台发生异常");
                    return r;
                }
            }
        }

    }

    public Map<String, Object> sale(int id){
        Map<String, Object> r = new HashMap<>();
        Product product = productDao.selectById(id);
        product.setStatus(1);
        int i = productDao.updateById(product);
        if (i > 0) {
            r.put("status", "success");
            r.put("text", "商品上架成功");
            return r;
        } else {
            r.put("status", "error");
            r.put("text", "数据库或后台发生异常");
            return r;
        }
    }

    public Map<String, Object> noSale(int id){
        Map<String, Object> r = new HashMap<>();
        Product product = productDao.selectById(id);
        product.setStatus(0);
        int i = productDao.updateById(product);
        if (i > 0) {
            r.put("status", "success");
            r.put("text", "商品下架成功");
            return r;
        } else {
            r.put("status", "error");
            r.put("text", "数据库或后台发生异常");
            return r;
        }
    }

    public List<Product> selectProductByTypeId(int id){
        Map<String,Object> map = new HashMap<>();
        map.put("typeid",id);
        List<Product> list = productDao.selectByMap(map);
        return list;
    }

    public Product selectProductById(int id){
        return productDao.selectProductById(id);
    }

    public Map<String, Object> deleteProductById(int id){
        Map<String, Object> r = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        map.put("productid",id);
        int i = productDao.deleteById(id);
        int k = keyDao.deleteByMap(map);
        r.put("status","success");
        r.put("text","成功删除了"+i+"个商品,"+k+"个卡密");
        return r;
    }
}
