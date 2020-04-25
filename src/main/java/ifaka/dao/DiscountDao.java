package ifaka.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ifaka.bean.Discount;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xiaofeng
 * @date 2020/3/24 23:13
 */
@Mapper
@Component
public interface DiscountDao extends BaseMapper<Discount> {
    @Insert("<script>insert into ifaka_product_discount (code,discount,create_time,status) values <foreach collection='list' item='discount' separator=','>(#{discount.code},#{discount.discount},#{discount.createTime},#{discount.status})</foreach></script>")
    int insertDiscounts(List<Discount> discountList);

    @Delete("<script>delete from ifaka_product_discount where id in <foreach collection='list' item='id' open='(' separator=',' close=')'>#{id}</foreach></script>")
    int deleteDiscounts(List<Integer> ids);

    @Select("<script>select * from ifaka_product_discount where code = #{code} and status = 0 LIMIT 1</script>")
    Discount selectDsicountByCode(String code);
}
