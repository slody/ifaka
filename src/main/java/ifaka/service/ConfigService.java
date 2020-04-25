package ifaka.service;

import cn.hutool.core.util.StrUtil;
import ifaka.dao.ConfigDao;
import ifaka.bean.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaofeng
 * @date 2020/3/21 21:49
 */
@Service
public class ConfigService {
    @Autowired
    private ConfigDao configDao;

    public Config selectConfig() {
        return configDao.selectById(1);
    }

    public Map<String, Object> updateConfig(Config config) {
        config.setId(1);
        Map<String, Object> r = new HashMap<>();
        if (StrUtil.hasBlank(config.getTitle()) || StrUtil.hasBlank(config.getDomain()) || StrUtil.hasBlank(config.getDescription()) | StrUtil.hasBlank(config.getKeyword()) || StrUtil.hasBlank(config.getNotice()) || StrUtil.hasBlank(config.getLogo()) || StrUtil.hasBlank(config.getIco()) || StrUtil.hasBlank(config.getQq()) || StrUtil.hasBlank(config.getBackgroundImage())) {
            r.put("text", "请填写所有内容");
            r.put("status", "warning");
            return r;
        } else {
            config.setId(1);
            int i = configDao.updateById(config);
            if (i > 0) {
                r.put("text", "设置修改成功");
                r.put("status", "success");
                return r;
            } else {
                r.put("text", "数据库或后台发生异常");
                r.put("status", "error");
                return r;
            }
        }
    }
}
