package ifaka.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xiaofeng
 * @date 2020/3/24 13:25
 */
@Data
@TableName(value = "ifaka_product_type")
public class Type {
    @TableId(value = "id",type = IdType.AUTO)
    private long id;
    @TableField(value = "name")
    private String name;
    @TableField(value = "sort")
    private int sort;
}
