package ifaka.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ifaka.bean.Type;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author xiaofeng
 * @date 2020/3/24 13:38
 */
@Mapper
@Component
public interface TypeDao extends BaseMapper<Type> {
}
