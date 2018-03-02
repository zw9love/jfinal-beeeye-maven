package controller;
/**
 * Created by admin on 2018/2/2.
 */
import java.util.List;
import java.util.Map;


import com.jfinal.plugin.activerecord.Db;
import model.Host;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

import util.MyUtil;

public class BeeeyeHostController extends Controller {
    private static final Host dao = new Host().dao();
    private final String tableName = "beeeye_host";

    @SuppressWarnings("unchecked")
    public void get() throws JSONException {

        String ids = getPara();

        if (ids != null) {
            Host host = dao.findById(ids);
            // System.out.println(host.toString());
            String[] Names = host._getAttrNames();
            JSONObject obj = new JSONObject();
            for (String param : Names) {
                Object object = host.get(param);
                obj.put(param, object);
            }
            JSONObject jsonObj = MyUtil.getJson("成功", 200, obj);
            renderJson(jsonObj.toString());
        } else {
            Map<String, Object> json = MyUtil.getJsonData(getRequest());
//            System.out.println(json);
            if(json == null || json.isEmpty()){
                List<Host> list = dao.find("select * from " + tableName);
                JSONArray postList = new JSONArray();
                for (Host item : list) {
                    String[] Names = item._getAttrNames();
                    JSONObject obj = new JSONObject();
                    for (String param : Names) {
                        Object object = item.get(param);
                        obj.put(param, object);
                    }
                    postList.put(obj);
                }
                JSONObject jsonObj = MyUtil.getJson("成功", 200, postList);
                renderJson(jsonObj.toString());
            }else{
                Map<String, Object> page = (Map<String, Object>) json.get("page");
                int pageNumber = (int) Double.parseDouble(page.get("pageNumber").toString());
                int pageSize = (int) Double.parseDouble(page.get("pageSize").toString());
                Page<Host> paginate = dao.paginate(pageNumber, pageSize, "select *", "from " + tableName);
                List<Host> list = paginate.getList();
                JSONArray postList = new JSONArray();
                for (Host item : list) {
                    String[] Names = item._getAttrNames();
                    JSONObject obj = new JSONObject();
                    for (String param : Names) {
                        Object object = item.get(param);
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

    }

    public void getSystems() {
        JSONObject jsonObj = MyUtil.getJson("成功", 200, "");
        renderJson(jsonObj.toString());
    }

    public void checkInfo(){
        JSONObject jsonObj = MyUtil.getJson("成功", 200, "");
        renderJson(jsonObj.toString());
    }

    public void post() {
        Map<String, Object> json = MyUtil.getJsonData(getRequest());
        String ids = MyUtil.getRandomString();
        Host bean = getBean(Host.class);
        bean.setHostIds(ids);
        for(String key : json.keySet()){
            Object value = json.get(key);
            bean.set(key, value);
        }
        bean.setRecordHash("");
        bean.save();
        JSONObject jsonObj = MyUtil.getJson("成功", 200, "");
        renderJson(jsonObj.toString());
    }


    public void put() {
        Map<String, Object> json = MyUtil.getJsonData(getRequest());
        String ids = (String) json.get("host_ids");
        Host bean = getBean(Host.class);
        bean.setHostIds(ids);
        for(String key : json.keySet()){
            Object value = json.get(key);
            bean.set(key, value);
        }
        bean.update();
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
