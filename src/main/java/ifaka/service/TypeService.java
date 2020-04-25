package ifaka.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ifaka.dao.KeyDao;
import ifaka.dao.ProductDao;
import ifaka.dao.TypeDao;
import ifaka.bean.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaofeng
 * @date 2020/3/28 23:11
 */
@Service
public class TypeService {
    @Autowired
    private TypeDao typeDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private KeyDao keyDao;

    public Map<String, Object> insertType(Map<String, Object> param) {
        Map<String, Object> r = new HashMap<>();
        String name = (String) param.get("name");
        String sort = (String) param.get("sort");
        if (StrUtil.hasBlank(name) || StrUtil.hasBlank(sort)) {
            r.put("status", "warning");
            r.put("text", "请填写所有内容");
            return r;
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("name", name);
            List<Type> t = typeDao.selectByMap(map);
            if (t.size() > 0) {
                r.put("status", "error");
                r.put("text", "该分类名称已存在");
                return r;
            } else {
                Type type = new Type();
                type.setName(name);
                type.setSort(Integer.parseInt(sort));
                int i = typeDao.insert(type);
                if (i > 0) {
                    r.put("status", "success");
                    r.put("text", "分类添加成功");
                    return r;
                } else {
                    r.put("status", "error");
                    r.put("text", "数据库或后台发生异常");
                    return r;
                }
            }
        }
    }

    public Map<String, Object> updateType(Type type) {
        Map<String, Object> r = new HashMap<>();
        if (type.getId()==0 || StrUtil.hasBlank(type.getName()) || type.getSort()==0) {
            r.put("status", "warning");
            r.put("text", "请填写所有内容");
            return r;
        } else {
            int i = typeDao.updateById(type);
            if (i > 0) {
                r.put("status", "success");
                r.put("text", "分类更新成功");
                return r;
            } else {
                r.put("status", "error");
                r.put("text", "数据库或后台发生异常");
                return r;
            }
        }
    }

    public Map<String, Object> deleteType(int id) {
        Map<String, Object> r = new HashMap<>();
        Map<String,Object> map = new HashMap<>();
        map.put("typeid",id);
        int i = typeDao.deleteById(id);
        int l = productDao.deleteByMap(map);
        int k = keyDao.deleteByMap(map);;
        if (i>0){
            r.put("status", "success");
            r.put("text", "成功删除了1个分类,"+l+"个商品,"+k+"个卡密");
            return r;
        }else{
            r.put("status", "error");
            r.put("text", "数据库或后台发生异常");
            return r;
        }
    }

    public List<Type> selectType(){
        QueryWrapper<Type> wrapper = new QueryWrapper();
        wrapper.orderByAsc("sort");
        return typeDao.selectList(wrapper);
    }

}
