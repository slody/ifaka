package ifaka.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ifaka.bean.Log;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author xiaofeng
 * @date 2020/3/22 14:37
 */
@Mapper
@Component
public interface LogDao extends BaseMapper<Log> {
}
