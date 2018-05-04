package model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

public class BaseBaseline<M extends BaseBaseline<M>> extends Model<M> implements IBean {
    public String getIds() {
        return get("ids");
    }

    public void setIds(String ids) {
        set("ids", ids);
    }

    public void setHostIds(String hostIds) {
        set("hostIds", hostIds);
    }

    public String getHostIds() {
        return get("hostIds");
    }

    public void setBIds(String bIds) {
        set("bIds", bIds);
    }

    public String getBIds() {
        return get("bIds");
    }

    public Integer getStatus() {
        return get("status");
    }

    public void setStatus(Integer status) {
        set("status", status);
    }
}
