package controller;
/**
 * Created by admin on 2018/2/2.
 */

import java.io.File;
import java.util.ArrayList;
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
            if (json == null || json.isEmpty()) {
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
            } else {
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
        JSONObject jsonObj;
        List<JSONObject> linuxVersionList = new ArrayList<JSONObject>();
        List<JSONObject> WindowsVersionList = new ArrayList<JSONObject>();
        List<JSONObject> resList = new ArrayList<JSONObject>();
        JSONObject linuxMap = new JSONObject();
        JSONObject windowsMap = new JSONObject();
        linuxMap.put("name", "linux");
        linuxMap.put("version", linuxVersionList);
        windowsMap.put("name", "windows");
        windowsMap.put("version", WindowsVersionList);
        resList.add(linuxMap);
        resList.add(windowsMap);
        String path = "C:/systems";
        File file = new File(path);
        if (file.exists()) {
            String[] list = file.list();
            for (String filename : list) {
                String[] splitList = filename.split("-");
                String type = splitList[1];
                String version = splitList[2];
                String arch = splitList[3];
                for (JSONObject resItem : resList) {
                    String versionName = resItem.getString("name");
                    JSONArray versionList = resItem.getJSONArray("version");
//                    List<JSONObject> versionList = (List<JSONObject>) resItem.get("version");
                    if (versionName.equals(type)) {
                        if (versionList.length() == 0) {
                            JSONObject versionMap = new JSONObject();
                            List<String> archList = new ArrayList<String>();
                            archList.add(arch);
                            versionMap.put("value", version);
                            versionMap.put("arch", archList);
                            versionList.put(versionMap);
                        } else {
                            Boolean flag = true;
                            for (int i = 0; i < versionList.length(); i++) {
                                JSONObject versionItem = (JSONObject)versionList.get(i);
                                String val = (String) versionItem.get("value");
                                JSONArray archList = versionItem.getJSONArray("arch");
//                                List<String> archList = (List<String>) versionItem.get("arch");
                                if (val.equals(version)) {
                                    archList.put(arch);
                                    flag = false;
                                }
                            }
                            if (flag) {
                                JSONObject versionMap = new JSONObject();
                                List<String> archList = new ArrayList<String>();
                                archList.add(arch);
                                versionMap.put("value", version);
                                versionMap.put("arch", archList);
                                versionList.put(versionMap);
                            }
                        }
                    }
                }
            }
            jsonObj = MyUtil.getJson("成功", 200, resList);
        } else {
            jsonObj = MyUtil.getJson("系统文件不存在。", 606, null);
        }
        renderJson(jsonObj.toString());
    }

    public void checkInfo() {
        JSONObject jsonObj = MyUtil.getJson("成功", 200, "");
        renderJson(jsonObj.toString());
    }

    public void post() {
        Map<String, Object> json = MyUtil.getJsonData(getRequest());
        String ids = MyUtil.getRandomString();
        Host bean = getBean(Host.class);
        bean.setHostIds(ids);
        for (String key : json.keySet()) {
            Object value = json.get(key);
            bean.set(key, value);
        }
        bean.setRecordHash("");
        bean.setStatus(0);
        bean.save();
        JSONObject jsonObj = MyUtil.getJson("成功", 200, "");
        renderJson(jsonObj.toString());
    }


    public void put() {
        Map<String, Object> json = MyUtil.getJsonData(getRequest());
        String ids = (String) json.get("host_ids");
        Host bean = getBean(Host.class);
        bean.setHostIds(ids);
        for (String key : json.keySet()) {
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
                sql += i == lists.size() - 1 ? "'" + lists.get(i) + "')" : "'" + lists.get(i) + "', ";
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
