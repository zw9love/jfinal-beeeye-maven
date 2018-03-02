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
			int pageNumber = (int) Double.parseDouble(page.get("pageNumber").toString());
			int pageSize = (int) Double.parseDouble(page.get("pageSize").toString());
			Page<ProcessSubject> paginate = dao.paginate(pageNumber, pageSize, "select *",
					" from " + tableName);
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
		// ProcessSubject bean = getModel(ProcessSubject.class);
		ProcessSubject bean = getBean(ProcessSubject.class);
		bean.setIds(ids);
		bean.setName(name);
		bean.setPath(path);
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
		ProcessSubject bean = getBean(ProcessSubject.class);
		bean.setIds(ids);
		bean.setName(name);
		bean.setPath(path);
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
