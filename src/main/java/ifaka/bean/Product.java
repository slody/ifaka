package ifaka.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xiaofeng
 * @date 2020/3/26 14:55
 */
@TableName("ifaka_product")
@Data
public class Product {
    @TableId(value = "id",type = IdType.AUTO)
    private long id;
    @TableField(value = "typeid")
    private long typeid;
    @TableField(value = "name")
    private String name;
    @TableField(value = "price")
    private BigDecimal price;
    @TableField(value = "create_time")
    private String createTime;
    @TableField(value = "status")
    private int status;
    @TableField(exist = false)
    private long stock;
    @TableField(value = "description")
    private String description;
    @TableField(value = "auto")
    private int auto;

}
