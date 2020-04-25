package ifaka.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ifaka.bean.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author xiaofeng
 * @date 2020/4/8 16:48
 */
@Component
@Mapper
public interface OrderDao extends BaseMapper<Order> {
    @Select("<script>select * from ifaka_order where status = 0 and order_number = #{orderNumber} limit 1</script>")
    Order selectByOrderNumberAndStatus(String orderNumber);

    @Select("<script>select * from ifaka_order where order_number = #{orderNumber} limit 1</script>")
    Order selectByOrderNumber(String orderNumber);

    @Select("<script>select * from ifaka_order where status = 0 and secure_number = #{securenumber} limit 1</script>")
    Order selectBySecureNumber(String securenumber);

    @Select("<script>select * from ifaka_order where status = 1 and (contact = #{keyword} or order_number = #{keyword})</script>")
    List<Order> searchOrder(String keyword);

    @Select("<script>select *,(select name from ifaka_product where id = productid) as productname from ifaka_order ${ew.customSqlSegment}</script>")
    IPage<Order> selectOrderPage(Page<Order> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("<script>select sum(price) from ifaka_order where create_time like #{time} and status = 1</script>")
    BigDecimal selectTodayIncome(String time);

    @Select("<script>select sum(price) from ifaka_order</script>")
    BigDecimal selectAllIncome();

    @Select("<script>select count(*) from ifaka_order where status = 1</script>")
    long selectAllOrderCount();

    @Select("<script>select count(*) from ifaka_order where status = 1 and create_time like #{time}</script>")
    long selectTodayOrderCount(String time);
}
