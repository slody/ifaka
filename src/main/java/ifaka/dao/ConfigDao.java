package ifaka.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ifaka.bean.Config;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author xiaofeng
 * @date 2020/3/21 21:49
 */

@Mapper
@Component
public interface ConfigDao extends BaseMapper<Config> {
}
