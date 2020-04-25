package ifaka.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xiaofeng
 * @date 2020/4/2 20:14
 */
@Data
@TableName(value = "ifaka_payment")
public class Payment {
    @TableId(type = IdType.AUTO,value = "id")
    private long id;
    @TableField(value = "type")
    private String type;
    @TableField(value = "name")
    private String name;
    @TableField(value = "source")
    private String source;
    @TableField(value = "greenpay_uid")
    private String greenpayUid;
    @TableField(value = "greenpay_token")
    private String greenpayToken;
    @TableField(value = "codepay_key")
    private String codepayKey;
    @TableField(value = "codepay_id")
    private String codepayId;
    @TableField(value = "codepay_token")
    private String codepayToken;
}
