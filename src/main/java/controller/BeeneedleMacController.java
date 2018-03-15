package controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import model.Mac;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.MyUtil;

import java.util.List;
import java.util.Map;

/**
 * @author zenngwei
 * @date 2018/03/09 17:34
 */
public class BeeneedleMacController extends Controller {
    private static final Mac dao = new Mac().dao();
    private final String from = "from beeneedle_mac as bm " +
            "left join beeneedle_process_subject bps " +
            "on bps.ids=bm.sub_ids " +
            "join beeneedle_object_label bol " +
            "on bol.ids=bm.obj_ids ";

    public void get() {
        String ids = getPara();
        if (ids != null) {
            Mac mac = dao.findById(ids);
            String[] Names = mac._getAttrNames();
            JSONObject obj = new JSONObject();
            for (String param : Names) {
                Object object = mac.get(param);
                obj.put(param, object);
            }
            JSONObject jsonObj = MyUtil.getJson("成功", 200, obj);
            renderJson(jsonObj.toString());

        } else {
            Map<String, Object> json = MyUtil.getJsonData(getRequest());
            Map<String, Object> page = (Map<String, Object>) json.get("page");
            Map<String, Object> row = (Map<String, Object>) json.get("row");
            int pageNumber = MyUtil.getInt(page, "pageNumber");
            int pageSize = MyUtil.getInt(page, "pageSize");
            int type = MyUtil.getInt(row, "type");
            int labelType = MyUtil.getInt(row, "labelType");
            String hostIds = (String) row.get("hostIds");
            String select = "select bm.ids, bm.host_ids, bm.sub_ids, bm.obj_ids, bm.privilege, bm.type, bps.name as subject, bol.name as object ";
            String where = " where bm.host_ids = ? and bm.type = ? and bm.obj_ids in ( select ids from beeneedle_object_label where type = ? ) ";
            Page<Mac> paginate = dao.paginate(pageNumber, pageSize, select,
                    from + where, hostIds, type, labelType);
            List<Mac> list = paginate.getList();
            JSONArray postList = new JSONArray();
            for (Mac objectLabel : list) {
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

    public void post() {
        Map<String, Object> json = MyUtil.getJsonData(getRequest());
        String ids = MyUtil.getRandomString();
        String host_ids = (String) json.get("host_ids");
        String obj_ids = (String) json.get("obj_ids");
        String sub_ids = (String) json.get("sub_ids");
        int privilege = MyUtil.getInt(json, "privilege");
        int type = MyUtil.getInt(json, "type");
        Mac bean = getBean(Mac.class);
        bean.setIds(ids);
        bean.setHostIds(host_ids);
        bean.setObjIds(obj_ids);
        bean.setSubIds(sub_ids);
        bean.setPrivilege(privilege);
        bean.setType(type);
        bean.save();
        JSONObject jsonObj = MyUtil.getJson("成功", 200, "");
        renderJson(jsonObj.toString());
    }


    public void put() {
        Map<String, Object> json = MyUtil.getJsonData(getRequest());
        String ids = (String) json.get("ids");
        String host_ids = (String) json.get("host_ids");
        String obj_ids = (String) json.get("obj_ids");
        String sub_ids = (String) json.get("sub_ids");
        int privilege = MyUtil.getInt(json, "privilege");
        Mac bean = getBean(Mac.class);
        bean.setIds(ids);
        bean.setHostIds(host_ids);
        bean.setObjIds(obj_ids);
        bean.setSubIds(sub_ids);
        bean.setPrivilege(privilege);
        bean.update();
        JSONObject jsonObj = MyUtil.getJson("成功", 200, "");
        renderJson(jsonObj.toString());
    }

    public void delete() {
        String ids = getPara();
        JSONObject jsonObj;
        boolean flag = dao.deleteById(ids);
        if (flag)
            jsonObj = MyUtil.getJson("成功", 200, "");
        else
            jsonObj = MyUtil.getJson("失败，此ids不存在", 606, "");
        renderJson(jsonObj.toString());
    }
}
