package ifaka.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ifaka.bean.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xiaofeng
 * @date 2020/3/26 14:58
 */
@Component
@Mapper
public interface ProductDao extends BaseMapper<Product> {
    @Select("<script>SELECT id,typeid,name,price,create_time,description,auto,(select COUNT(*) FROM ifaka_key WHERE productid = ifaka_product.id and sold = 0) as stock from ifaka_product</script>")
    List<Product> selectProducts();

    @Select("<script>SELECT id,typeid,name,price,create_time,description,auto,(select COUNT(*) FROM ifaka_key WHERE productid = ifaka_product.id and sold = 0) as stock from ifaka_product where id = #{id}</script>")
    Product selectProductById(int id);
}
