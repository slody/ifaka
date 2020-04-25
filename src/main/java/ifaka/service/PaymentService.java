package ifaka.service;

import cn.hutool.core.util.StrUtil;
import ifaka.dao.PaymentDao;
import ifaka.bean.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaofeng
 * @date 2020/4/6 22:12
 */
@Service
public class PaymentService {
    @Autowired
    private PaymentDao paymentDao;

    public Map<String,Object> insertPayment(Payment payment){
        Map<String,Object> r = new HashMap<>();
        if (StrUtil.hasBlank(payment.getName()) || StrUtil.hasBlank(payment.getType()) ||StrUtil.hasBlank(payment.getSource())){
            r.put("status","warning");
            r.put("text", "请填写所有内容");
            return r;
        }else{
            if ("codepay".equals(payment.getSource())){
                if (StrUtil.hasBlank(payment.getCodepayId()) || StrUtil.hasBlank(payment.getCodepayToken()) || StrUtil.hasBlank(payment.getCodepayKey())){
                    r.put("status","warning");
                    r.put("text", "请填写所有内容");
                    return r;
                }
            }
            if ("greenpay".equals(payment.getSource())){
                if (StrUtil.hasBlank(payment.getGreenpayUid()) || StrUtil.hasBlank(payment.getGreenpayToken())){
                    r.put("status","warning");
                    r.put("text", "请填写所有内容");
                    return r;
                }
            }
            int i = paymentDao.insert(payment);
            if (i>0){
                r.put("status","success");
                r.put("text","添加成功");
                return r;
            }else{
                r.put("status","error");
                r.put("text","数据库或后台发生异常");
                return r;
            }
        }
    }

    public List<Payment> selectPayment(){
        return paymentDao.selectList(null);
    }

    public Map<String,Object> updatePayment(Payment payment){
        Map<String,Object> r = new HashMap<>();
        if ("codepay".equals(payment.getSource())){
            if (StrUtil.hasBlank(payment.getName()) || StrUtil.hasBlank(payment.getCodepayId()) || StrUtil.hasBlank(payment.getCodepayToken()) || StrUtil.hasBlank(payment.getCodepayKey())){
                r.put("status","warning");
                r.put("text", "请填写所有内容");
                return r;
            }else{
                int i = paymentDao.updateById(payment);
                if (i>0){
                    r.put("status","success");
                    r.put("text", "修改成功");
                    return r;
                }else{
                    r.put("status","warning");
                    r.put("text", "数据库或后台发生异常");
                    return r;
                }
            }
        }else if ("greenpay".equals(payment.getSource())){
            if (StrUtil.hasBlank(payment.getName()) || StrUtil.hasBlank(payment.getGreenpayToken()) || StrUtil.hasBlank(payment.getGreenpayUid())){
                r.put("status","warning");
                r.put("text", "请填写所有内容");
                return r;
            }else{
                int i = paymentDao.updateById(payment);
                if (i>0){
                    r.put("status","success");
                    r.put("text", "修改成功");
                    return r;
                }else{
                    r.put("status","warning");
                    r.put("text", "数据库或后台发生异常");
                    return r;
                }
            }
        }else{
            r.put("status","warning");
            r.put("text", "请填写所有内容");
            return r;
        }
    }

    public Map<String,Object> deleteById(int id){
        Map<String,Object> r = new HashMap<>();
        int i = paymentDao.deleteById(id);
        if (i>0){
            r.put("status","success");
            r.put("text", "删除成功");
            return r;
        }else{
            r.put("status","error");
            r.put("text", "数据库或后台发生异常");
            return r;
        }
    }

    public Payment selectPaymentById(int id){
        return paymentDao.selectById(id);
    }
}
