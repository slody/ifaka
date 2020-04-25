package ifaka.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ifaka.bean.Key;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xiaofeng
 * @date 2020/3/30 19:22
 */
@Mapper
@Component
public interface KeyDao extends BaseMapper<Key> {
    @Insert("<script>insert into ifaka_key (productid,typeid,code,sold) values <foreach collection='list' item='key' separator=','>(#{key.productid},#{key.typeid},#{key.code},0)</foreach></script>")
    int insertKeys(List<Key> keys);

    @Delete("<script>DELETE from ifaka_key where id in <foreach collection='list' item='id' open='(' separator=',' close=')'>#{id}</foreach></script>")
    int deleteKeys(List<Integer> list);

    @Update("<script>update ifaka_key set sold = 1 where id in <foreach collection='list' item='key' open='(' separator=',' close=')'>#{key.id}</foreach></script>")
    int updatekeys(List<Key> list);

    @Select("<script>select * from ifaka_key where sold = 0 and productid = #{productid} limit #{count}</script>")
    List<Key> selectKeys(long productid,int count);

}
