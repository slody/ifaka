package ifaka.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ifaka.dao.LogDao;
import ifaka.bean.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author xiaofeng
 * @date 2020/3/22 14:37
 */
@Service
public class LogService {

    @Autowired
    private LogDao logDao;

    public void addLog(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        Date time = new Date();
        String ua = request.getHeader("user-agent");
        String location = HttpUtil.get("http://whois.pconline.com.cn/ip.jsp?ip=" + ip);
        location = location.replaceAll("\n", "").replaceAll("\r", "");
        Log log = new Log();
        log.setIp(ip);
        log.setTime(DateUtil.format(time, "yyyy-MM-dd HH:mm:ss"));
        log.setLocation(location);
        log.setUa(ua);
        logDao.insert(log);
    }

    public Log getLastLoginInfo() {
        QueryWrapper<Log> wrapper = new QueryWrapper<Log>();
        wrapper.orderByDesc("time");
        wrapper.last("limit 1,1");
        return logDao.selectOne(wrapper);
    }

    public IPage<Log> getLogs(int page) {
        IPage<Log> ipage = new Page<>(page,15);
        QueryWrapper<Log> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("time");
        ipage = logDao.selectPage(ipage,wrapper);
        return ipage;
    }
}
