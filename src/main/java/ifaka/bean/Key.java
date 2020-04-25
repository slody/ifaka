package ifaka.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xiaofeng
 * @date 2020/3/30 19:20
 */
@Data
@TableName(value = "ifaka_key")
public class Key {
    @TableId(value = "id",type = IdType.AUTO)
    private long id;
    @TableField(value = "code")
    private String code;
    @TableField(value = "productid")
    private int productid;
    @TableField(value = "typeid")
    private int typeid;
    @TableField(value = "sold")
    private int sold;
}
