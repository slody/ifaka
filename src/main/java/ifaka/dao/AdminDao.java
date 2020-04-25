package ifaka.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ifaka.bean.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author xiaofeng
 * @date 2020/3/20 13:31
 */
@Mapper
@Component
public interface AdminDao extends BaseMapper<Admin> {


}
