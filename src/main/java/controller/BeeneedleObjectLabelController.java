package controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import model.ObjectHost;
import model.ObjectLabel;
import model.ProcessSubject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.MyUtil;

import java.util.List;
import java.util.Map;

public class BeeneedleObjectLabelController  extends Controller {
    private static final ObjectLabel dao = new ObjectLabel().dao();
    private final String tableName = "beeneedle_object_label";

    public void get() throws JSONException {
        String ids = getPara();
        // System.out.println("ids = " + ids);
        if (ids != null) {
            ObjectLabel objectLabel = dao.findById(ids);
//			System.out.println(processSubject.toString());
            String[] Names = objectLabel._getAttrNames();
            JSONObject obj = new JSONObject();
            for (String param : Names) {
                Object object = objectLabel.get(param);
                obj.put(param, object);
            }
            JSONObject jsonObj = MyUtil.getJson("成功", 200, obj);
            renderJson(jsonObj.toString());

        } else {
            Map<String, Object> json = MyUtil.getJsonData(getRequest());
            Map<String, Object> page = (Map<String, Object>) json.get("page");
            Map<String, Object> row = (Map<String, Object>) json.get("row");
            int pageNumber = (int) Double.parseDouble(page.get("pageNumber").toString());
            int pageSize = (int) Double.parseDouble(page.get("pageSize").toString());
            String hostIds = (String) row.get("hostIds");
            String where = " where ids in (select object_ids from beeneedle_object_host where host_ids = ? )";
            Page<ObjectLabel> paginate = dao.paginate(pageNumber, pageSize, "select *",
                    " from " + tableName + where, hostIds);
            List<ObjectLabel> list = paginate.getList();
            JSONArray postList = new JSONArray();
            for (ObjectLabel objectLabel : list) {
                String[] Names = objectLabel._getAttrNames();
                JSONObject obj = new JSONObject();
                for (String param : Names) {
                    Object object = objectLabel.get(param);
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

    public void getHost() throws JSONException {
        Map<String, Object> json = MyUtil.getJsonData(getRequest());
        String ids = (String) json.get("ids");
        JSONObject jsonObj;
        if (ids == null) {
            jsonObj = MyUtil.getJson("请传递objectIds", 606, "");
            renderJson(jsonObj.toString());
        } else {
            String sql = " select * from beeneedle_object_host where object_ids = ? ";
            List<Record> list = Db.find(sql, ids);
            JSONArray postList = new JSONArray();
            for (Record item : list) {
                String[] Names = item.getColumnNames();
//                System.out.println(Names.toString());
                JSONObject obj = new JSONObject();
                for (String param : Names) {
                    Object object = item.get(param);
                    obj.put(param, object);
                }
                postList.put(obj);
            }
            jsonObj = MyUtil.getJson("成功", 200, postList);
            renderJson(jsonObj.toString());
        }
    }

    public void post() {
        Map<String, Object> json = MyUtil.getJsonData(getRequest());
        String ids = MyUtil.getRandomString();
        String hostIds = (String) json.get("host_ids");
        String name = (String) json.get("name");
        String path = (String) json.get("path");
        int type = (int) Double.parseDouble(json.get("type").toString());
        int reli_value = (int) Double.parseDouble(json.get("reli_value").toString());
        int sens_value = (int) Double.parseDouble(json.get("sens_value").toString());
        ObjectLabel bean = getBean(ObjectLabel.class);
        String sql = "INSERT INTO beeneedle_object_host (ids, object_ids, host_ids) VALUES (?,?,?)";
        Db.update(sql, MyUtil.getRandomString(),ids, hostIds);
        bean.setIds(ids);
        bean.setName(name);
        bean.setPath(path);
        bean.setType(type);
        bean.setSensValue(sens_value);
        bean.setReliValue(reli_value);
//        for(String key : json.keySet()){
//            Object value = json.get(key);
//            bean.set(key, value);
//        }
        bean.save();
        JSONObject jsonObj = MyUtil.getJson("成功", 200, "");
        renderJson(jsonObj.toString());
    }

    public void put() {
        Map<String, Object> json = MyUtil.getJsonData(getRequest());
        ObjectLabel bean = getBean(ObjectLabel.class);
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
