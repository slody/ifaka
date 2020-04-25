package ifaka.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author xiaofeng
 * @date 2020/3/21 21:46
 */
@Data
@TableName(value = "ifaka_config")
public class Config {
    @TableId(type = IdType.AUTO,value = "id")
    private int id;
    @TableField(value = "title")
    private String title;
    @TableField(value = "keyword")
    private String keyword;
    @TableField(value = "description")
    private String description;
    @TableField(value = "logo")
    private String logo;
    @TableField(value = "notice")
    private String notice;
    @TableField(value = "ico")
    private String ico;
    @TableField(value = "domain")
    private String domain;
    @TableField(value = "qq")
    private String qq;
    @TableField(value = "background_image")
    private String backgroundImage;

}
