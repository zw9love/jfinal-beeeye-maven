package model.base;
/**
 * Created by admin on 2018/2/2.
 */
import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

public class BaseProcessSubject<M extends BaseProcessSubject<M>> extends Model<M> implements IBean {
	public void setIds(String ids) {
		set("ids", ids);
	}

	public String getIds() {
		return get("ids");
	}

	public void setName(String name) {
		set("name", name);
	}

	public String getName() {
		return get("name");
	}

	public void setPath(String path) {
		set("path", path);
	}

	public String getPath() {
		return get("path");
	}

	public Integer getSensValue() {
		return get("sens_value");
	}

	public void setSensValue(Integer sensValue) {
		set("sens_value", sensValue);
	}

	public Integer getReliValue() {
		return get("reli_value");
	}

	public void setReliValue(Integer reliValue) {
		set("reli_value", reliValue);
	}

	protected String getTable() {
		return "beeneedle_process_subject";
	}
}
