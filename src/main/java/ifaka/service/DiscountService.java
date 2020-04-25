package ifaka.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ifaka.dao.DiscountDao;
import ifaka.bean.Discount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaofeng
 * @date 2020/3/28 18:52
 */
@Service
public class DiscountService {
    @Autowired
    private DiscountDao discountDao;

    public Map<String, Object> insertDiscount(Map<String, Object> param) {
        Map<String, Object> r = new HashMap<>();
        String discount = (String) param.get("discount");
        String count = (String) param.get("count");
        if (StrUtil.hasBlank(discount) || StrUtil.hasBlank(count)) {
            r.put("status", "warning");
            r.put("text", "请填写所有内容");
            return r;
        } else {
            int c = Integer.parseInt(count);
            BigDecimal b = new BigDecimal(discount);
            if (c<=0){
                r.put("status", "warning");
                r.put("text", "生成数量不符合规则");
                return r;
            }else if (b.compareTo(new BigDecimal(100))==1){
                r.put("status", "warning");
                r.put("text", "折扣率不符合规则");
                return r;
            }else{
                String time = DateUtil.now();
                List<Discount> list = new ArrayList<>();
                for (int i = 0; i < c; i++){
                    Discount d = new Discount();
                    d.setCode(RandomUtil.randomString(20));
                    d.setCreateTime(time);
                    d.setDiscount(b.divide(new BigDecimal(100)));
                    d.setStatus(0);
                    list.add(d);
                }
                int i = discountDao.insertDiscounts(list);
                r.put("status", "success");
                r.put("text", "成功添加了"+i+"条优惠码");
                return r;
            }
        }
    }

    public IPage<Discount> selectDiscount(int p){
        QueryWrapper<Discount> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        Page<Discount> page = new Page<>(p,15);
        IPage<Discount> ipage = discountDao.selectPage(page,wrapper);
        return ipage;
    }

    public Map<String, Object> deleteDiscount(Map<String, List<Integer>> map){
        Map<String, Object> r = new HashMap<>();
        List<Integer> list = map.get("ids");
        if (list==null || list.size()<1){
            r.put("status","warning");
            r.put("text","请选择至少一个需要删除的对象");
            return r;
        }else{
            int i = discountDao.deleteDiscounts(list);
            r.put("status","success");
            r.put("text","已成功删除"+i+"条优惠码");
            return r;
        }
    }

    public Discount selectDiscountByCode(String code){
        return discountDao.selectDsicountByCode(code);
    }

    public void updateDiscount(Discount discount){
        discountDao.updateById(discount);
    }
}
