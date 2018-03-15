package model.base;

import com.alibaba.fastjson.annotation.JSONField;
import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;
/**
 * @author zenngwei
 * @date 2018/03/09 17:39
 */
public class BaseMac<M extends BaseMac<M>> extends Model<M> implements IBean {
    /**
     * [{column:"",sort:""},...]
     */
    @JSONField(name = "orders")

    public String getIds() {
        return get("ids");
    }

    public void setIds(String ids) {
        set("ids", ids);
    }

    public Integer getType() {
        return get("type");
    }

    public void setType(Integer type) {
        set("type", type);
    }

    public String getHostIds() {
        return get("host_ids");
    }

    public void setHostIds(String hostIds) {
        set("host_ids", hostIds);
    }

    public String getSubIds() {
        return get("sub_ids");
    }

    public void setSubIds(String subIds) {
        set("sub_ids", subIds);
    }

    public String getObjIds() {
        return get("obj_ids");
    }

    public void setObjIds(String objIds) {
        set("obj_ids", objIds);
    }

    public Integer getPrivilege() {
        return get("privilege");
    }

    public void setPrivilege(Integer privilege) {
        set("privilege", privilege);
    }

    protected String getTable() {
        return "beeneedle_mac";
    }

}
