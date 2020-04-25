package ifaka.service;

import cn.hutool.core.text.StrSpliter;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ifaka.dao.KeyDao;
import ifaka.bean.Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaofeng
 * @date 2020/3/30 19:22
 */
@Service
public class KeyService {
    @Autowired
    private KeyDao keyDao;

    public Map<String, Object> insertKey(Map<String, String> map) {
        Map<String, Object> r = new HashMap<>();
        String productid = map.get("productid");
        String typeid = map.get("typeid");
        String code = map.get("key");
        if (StrUtil.hasBlank(productid) || StrUtil.hasBlank(typeid) || StrUtil.hasBlank(code)) {
            r.put("status", "warning");
            r.put("text", "请填写所有内容");
            return r;
        } else {
            List<String> s = StrSpliter.split(code, "\n", 0, true, true);
            List<Key> keys = new ArrayList<>();
            for (String x : s) {
                Key key = new Key();
                key.setCode(x);
                key.setProductid(Integer.parseInt(productid));
                key.setTypeid(Integer.parseInt(typeid));
                key.setSold(0);
                keys.add(key);
            }
            int i = keyDao.insertKeys(keys);
            r.put("status", "success");
            r.put("text", "成功添加了" + i + "条卡密");
            return r;
        }
    }

    public IPage<Key> selectkey(int p) {
        QueryWrapper<Key> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        Page<Key> page = new Page<>(p,15);
        IPage<Key> iPage = keyDao.selectPage(page,wrapper);
        return iPage;
    }

    public Map<String,Object> deleteKey(Map<String,List<Integer>> map){
        Map<String,Object> r = new HashMap<>();
        List<Integer> list = map.get("ids");
        int i = keyDao.deleteKeys(list);
        r.put("status","success");
        r.put("text","成功删除了"+i+"条卡密");
        return r;
    }
    
    public List<Key> selectKeyByProductId(long productid,int count){
        List<Key> keys = keyDao.selectKeys(productid,count);
        keyDao.updatekeys(keys);
        return keys;
    }
}
