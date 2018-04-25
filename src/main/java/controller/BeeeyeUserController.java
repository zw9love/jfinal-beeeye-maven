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

import model.User;
import util.MD5Util;
import util.MyUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class BeeeyeUserController extends Controller {
	private static final User dao = new User().dao();

	@SuppressWarnings("unchecked")
	public void get() throws JSONException {
		String ids = getPara();
		if(ids == null){
			HttpServletRequest request = getRequest();
			HttpSession session = getSession();
			String token = request.getHeader("token");
			JSONObject role = (JSONObject) session.getAttribute(token);
			Map<String, Object> json = MyUtil.getJsonData(request);
			Map<String, Object> page = (Map<String, Object>) json.get("page");
			Page<User> paginate;
			int pageNumber = (int) Double.parseDouble(page.get("pageNumber").toString());
			int pageSize = (int) Double.parseDouble(page.get("pageSize").toString());
			if(role != null){
				String roleIds = (String) role.get("role_ids");
				String where = " where role_ids = ? ";
				paginate = dao.paginate(pageNumber, pageSize, "select *", " from common_user" + where, roleIds);
			}else{
				paginate = dao.paginate(pageNumber, pageSize, "select *", " from common_user");
			}
			List<User> list = paginate.getList();
			JSONArray postList = new JSONArray();
			for (User user : list) {
				String[] Names = user._getAttrNames();
				JSONObject obj = new JSONObject();
				for (String param : Names) {
					Object object = user.get(param);
					obj.put(param, object);
				}
				postList.put(obj);
			}

			int totalPage = paginate.getTotalPage();
			int totalRow = paginate.getTotalRow();
			JSONObject resObj = MyUtil.getPageJson(postList, pageNumber, pageSize, totalPage, totalRow);
			JSONObject jsonObj = MyUtil.getJson("成功", 200, resObj);
			renderJson(jsonObj.toString());
		}else{
			User user = dao.findById(ids);
//			System.out.println(user);
			String[] Names = user._getAttrNames();
			JSONObject obj = new JSONObject();
			for (String param : Names) {
				Object object = user.get(param);
				obj.put(param, object);
			}
			JSONObject jsonObj = MyUtil.getJson("成功", 200, obj);
			renderJson(jsonObj.toString());
		}

	}

	public void put(){
		Map<String, Object> json = MyUtil.getJsonData(getRequest());
		String ids = (String) json.get("ids");
		User bean = getBean(User.class);
		bean.setIds(ids);
		for (String key : json.keySet()) {
			Object value = json.get(key);
			bean.set(key, value);
		}
		bean.update();
		JSONObject jsonObj = MyUtil.getJson("成功", 200, "");
		renderJson(jsonObj.toString());
	}

	public void post() {
		HttpSession session = getSession();
		HttpServletRequest request = getRequest();
		String token = request.getHeader("token");
		JSONObject role = (JSONObject) session.getAttribute(token);

		Map<String, Object> json = MyUtil.getJsonData(getRequest());
		String ids = MyUtil.getRandomString();
		String loginName = MyUtil.getString(json, "login_name");
		String loginPwd = MD5Util.encrypt(MyUtil.getString(json, "login_pwd"));
		String username = MyUtil.getString(json, "username");
		String email = MyUtil.getString(json, "email");
		User bean = getBean(User.class);
		bean.setIds(ids);
		bean.setEmail(email);
		bean.setLoginName(loginName);
		bean.setLoginPwd(loginPwd);
		bean.setUsername(username);
		bean.setRecordHash("");
		bean.setStatus("normal");
		bean.setRoleIds((String)role.get("role_ids"));
		bean.setLastPasswordChange((int) Math.floor(System.currentTimeMillis() / 1000));
		bean.save();
		JSONObject jsonObj = MyUtil.getJson("成功", 200, null);
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

		}else{
			jsonObj = MyUtil.getJson("传递参数有误", 606, null);
		}
		renderJson(jsonObj.toString());
	}

}
