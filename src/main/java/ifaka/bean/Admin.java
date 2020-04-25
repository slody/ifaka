package ifaka.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xiaofeng
 * @date 2020/3/20 13:10
 */
@Data
@TableName(value = "ifaka_admin")
public class Admin {
    @TableId(value = "id",type = IdType.AUTO)
    private long id;
    @TableField("username")
    private String username;
    @TableField("password")
    private String password;

}
