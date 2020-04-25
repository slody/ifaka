package ifaka.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import ifaka.dao.AdminDao;
import ifaka.bean.Admin;
import ifaka.util.LoginRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaofeng
 * @date 2020/3/20 14:35
 */
@Service
public class AdminService {
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private LogService logService;

    public String login(String u , String p, HttpServletRequest request, HttpServletResponse response){
        String ip = request.getRemoteAddr();
        if (LoginRecord.LoginRecords.get(ip)!=null){
            LoginRecord lg = LoginRecord.LoginRecords.get(ip);
            if (lg.getBlockTime()!=null && DateUtil.between(lg.getBlockTime(),new Date(), DateUnit.MINUTE)<30){
                return "block";
            }
            if (lg.getWrongTimes()>5){
                lg.setBlockTime(new Date());
                return "block";
            }
            if (lg.getBlockTime()!=null && DateUtil.between(lg.getBlockTime(),new Date(), DateUnit.MINUTE)>30){
                LoginRecord.LoginRecords.remove(ip);
            }
        }
        HashMap<String,Object> map = new HashMap<>();
        map.put("username",u);
        map.put("password", SecureUtil.md5(p));
        List<Admin> list = adminDao.selectByMap(map);
        if (list.size()>0){
            if (LoginRecord.LoginRecords.get(ip)!=null){
                LoginRecord.LoginRecords.remove(ip);
            }
            request.getSession().setAttribute("name",u);
            request.getSession().setAttribute("isLogin",true);
            Cookie cookie = new Cookie("sid",request.getSession().getId());
            response.addCookie(cookie);
            logService.addLog(request);
            return "true";
        }else{
            LoginRecord lg = new LoginRecord();
            if (LoginRecord.LoginRecords.get(ip)!=null){
                lg.setWrongTimes(LoginRecord.LoginRecords.get(ip).getWrongTimes()+1);
                LoginRecord.LoginRecords.put(ip,lg);
            }else{
                lg.setWrongTimes(1);
                LoginRecord.LoginRecords.put(ip,lg);
            }
            return "false";
        }
    }

    public Map<String,Object> updatePassword(Map<String,Object> param){
        Map<String,Object> r = new HashMap<>();
        String op = (String)param.get("op");
        String np = (String)param.get("np");
        String rp = (String)param.get("rp");
        if (StrUtil.hasBlank(op) || StrUtil.hasBlank(np) || StrUtil.hasBlank(rp)){
            r.put("status","warning");
            r.put("text","请填写所有内容");
            return r;
        }else if (!np.equals(rp)){
            r.put("status","warning");
            r.put("text","两次密码输入不一致");
            return r;
        }else{
            Admin admin = adminDao.selectById(1);
            if (!admin.getPassword().equals(SecureUtil.md5(op))){
                r.put("status","error");
                r.put("text","旧密码输入错误");
                return r;
            }else{
                admin.setPassword(SecureUtil.md5(np));
                int i = adminDao.updateById(admin);
                if (i>0){
                    r.put("status","success");
                    r.put("text","密码修改成功");
                    return r;
                }else{
                    r.put("status","error");
                    r.put("text","后台或数据库出现异常");
                    return r;
                }

            }
        }
    }
}
