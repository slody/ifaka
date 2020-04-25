package ifaka.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ifaka.bean.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author xiaofeng
 * @date 2020/4/6 22:12
 */
@Mapper
@Component
public interface PaymentDao extends BaseMapper<Payment> {
}
