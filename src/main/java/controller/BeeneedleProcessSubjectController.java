package controller;
/**
 * Created by admin on 2018/2/2.
 */
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

import model.ProcessSubject;
import util.MyUtil;

public class BeeneedleProcessSubjectController extends Controller {
	private static final ProcessSubject dao = new ProcessSubject().dao();
	private final String tableName = "beeneedle_process_subject";
	@SuppressWarnings("unchecked")
	public void get() throws JSONException {
		String ids = getPara();
		// System.out.println("ids = " + ids);
		if (ids != null) {
			ProcessSubject processSubject = dao.findById(ids);
//			System.out.println(processSubject.toString());
			String[] Names = processSubject._getAttrNames();
			JSONObject obj = new JSONObject();
			for (String param : Names) {
				Object object = processSubject.get(param);
				obj.put(param, object);
			}
			JSONObject jsonObj = MyUtil.getJson("成功", 200, obj);
			renderJson(jsonObj.toString());

		} else {
			Map<String, Object> json = MyUtil.getJsonData(getRequest());
			Map<String, Object> page = (Map<String, Object>) json.get("page");
			Map<String, Object> row = (Map<String, Object>) json.get("row");
            // select * from beeneedle_process_subject where ids in (select process_ids from beeneedle_process_host where host_ids = "5a95240944e8c221d915d33f" )
            String hostIds = (String) row.get("hostIds");
			String where = " where ids in (select process_ids from beeneedle_process_host where host_ids = ? )";
			int pageNumber = (int) Double.parseDouble(page.get("pageNumber").toString());
			int pageSize = (int) Double.parseDouble(page.get("pageSize").toString());
			Page<ProcessSubject> paginate = dao.paginate(pageNumber, pageSize, "select *",
					" from " + tableName + where, hostIds);
			List<ProcessSubject> list = paginate.getList();
			JSONArray postList = new JSONArray();
			for (ProcessSubject processSubject : list) {
				String[] Names = processSubject._getAttrNames();
				JSONObject obj = new JSONObject();
				for (String param : Names) {
					Object object = processSubject.get(param);
					obj.put(param, object);
				}
				postList.put(obj);
			}

			int totalPage = paginate.getTotalPage();
			int totalRow = paginate.getTotalRow();
			JSONObject resObj = MyUtil.getPageJson(postList, pageNumber, pageSize, totalPage, totalRow);
			JSONObject jsonObj = MyUtil.getJson("成功", 200, resObj);
			renderJson(jsonObj.toString());
		}

	}

	public void post() {
		Map<String, Object> json = MyUtil.getJsonData(getRequest());
		String ids = MyUtil.getRandomString();
		String name = (String) json.get("name");
		String path = (String) json.get("path");
        int reli_value = (int) Double.parseDouble(json.get("reli_value").toString());
        int sens_value = (int) Double.parseDouble(json.get("sens_value").toString());
		String hostIds = (String) json.get("host_ids");
		String sql = "INSERT INTO beeneedle_process_host (ids, process_ids, host_ids) VALUES (?,?,?)";
        Db.update(sql, MyUtil.getRandomString(),ids, hostIds);
		// ProcessSubject bean = getModel(ProcessSubject.class);
		ProcessSubject bean = getBean(ProcessSubject.class);
		bean.setIds(ids);
		bean.setName(name);
		bean.setPath(path);
        bean.setSensValue(sens_value);
        bean.setReliValue(reli_value);
		// bean.update();
		bean.save();
		// System.out.println("name = " + name);
		JSONObject jsonObj = MyUtil.getJson("成功", 200, "");
		renderJson(jsonObj.toString());
	}

	public void put() {
		Map<String, Object> json = MyUtil.getJsonData(getRequest());
		String ids = (String) json.get("ids");
		String name = (String) json.get("name");
		String path = (String) json.get("path");
        int reli_value = (int) Double.parseDouble(json.get("reli_value").toString());
        int sens_value = (int) Double.parseDouble(json.get("sens_value").toString());
		ProcessSubject bean = getBean(ProcessSubject.class);
		bean.setIds(ids);
		bean.setName(name);
		bean.setPath(path);
		bean.setSensValue(sens_value);
		bean.setReliValue(reli_value);
		bean.update();
		// bean.save();
		// System.out.println("name = " + name);
		JSONObject jsonObj = MyUtil.getJson("成功", 200, "");
		renderJson(jsonObj.toString());
	}

	public void delete() {
		String ids = getPara();
		JSONObject jsonObj;
		if (ids != null) {
			boolean flag = dao.deleteById(ids);
			if (flag)
				jsonObj = MyUtil.getJson("成功", 200, "");
			else
				jsonObj = MyUtil.getJson("失败，此ids不存在", 606, "");

		} else {
			List<String> lists = MyUtil.getListData(getRequest());
			String sql = "delete from " + tableName + " where ids in (";
			for (int i = 0; i < lists.size(); i++) {
				sql += i == lists.size() - 1 ? "'" + lists.get(i) + "')": "'" + lists.get(i) + "', ";
			}
			// System.out.println(sql);
			int effectCount = Db.delete(sql);
			if (effectCount >= 0)
				jsonObj = MyUtil.getJson("成功", 200, "");
			else
				jsonObj = MyUtil.getJson("删除失败", 606, "");
		}
		renderJson(jsonObj.toString());
	}

}
