package ifaka.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xiaofeng
 * @date 2020/3/24 23:14
 */
@Data
@TableName(value = "ifaka_product_discount")
public class Discount {
    @TableId(value = "id",type = IdType.AUTO)
    private long id;
    @TableField(value = "code")
    private String code;
    @TableField(value = "discount")
    private BigDecimal discount;
    @TableField(value = "create_time")
    private String createTime;
    @TableField(value = "status")
    private int status;
}
