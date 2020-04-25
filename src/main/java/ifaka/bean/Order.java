package ifaka.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xiaofeng
 * @date 2020/4/8 16:33
 */
@Data
@TableName(value = "ifaka_order")
public class Order {
    @TableId(value = "id",type = IdType.AUTO)
    private long id;
    @TableField(value = "create_time")
    private String createTime;
    @TableField(value = "expire_time")
    private String expireTime;
    @TableField(value = "status")
    private int status;
    @TableField(value = "order_number")
    private String orderNumber;
    @TableField(value = "secure_number")
    private String secureNumber;
    @TableField(value = "key")
    private String key;
    @TableField(value = "contact")
    private String contact;
    @TableField(value = "count")
    private int count;
    @TableField(value = "trading_number")
    private String tradingNumber;
    @TableField(value = "price")
    private BigDecimal price;
    @TableField(value = "money")
    private BigDecimal money;
    @TableField(value = "qrcode_url")
    private String qrcodeUrl;
    @TableField(value = "payment")
    private String payment;
    @TableField(value = "discount")
    private String discount;
    @TableField(value = "productid")
    private long productid;
    @TableField(exist = false)
    private String productname;
}
