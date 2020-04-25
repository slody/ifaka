package ifaka.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaofeng
 * @date 2020/3/20 22:39
 */
public class LoginRecord {
    public static Map<String,LoginRecord> LoginRecords = new HashMap<>();
    private int wrongTimes;
    private Date blockTime;

    public int getWrongTimes() {
        return wrongTimes;
    }

    public void setWrongTimes(int wrongTimes) {
        this.wrongTimes = wrongTimes;
    }

    public Date getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(Date blockTime) {
        this.blockTime = blockTime;
    }
}
