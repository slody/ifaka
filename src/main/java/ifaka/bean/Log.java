package ifaka.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xiaofeng
 * @date 2020/3/22 14:34
 */
@Data
@TableName(value = "ifaka_log")
public class Log {
    @TableId(value = "id",type = IdType.AUTO)
    private long id;
    @TableField(value = "location")
    private String location;
    @TableField(value = "time")
    private String time;
    @TableField(value = "ip")
    private String ip;
    @TableField(value = "ua")
    private String ua;

}
