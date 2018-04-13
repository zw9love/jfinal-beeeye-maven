package controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.MyUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zenngwei
 * @date 2018/04/10 14:39
 */
public class BeeeyeResourcesCollection extends Controller {

    public void get() throws JSONException {
        Map<String, Object> json = MyUtil.getJsonData(getRequest());
        String host_ids = MyUtil.getString(json, "host_ids");
        int time = MyUtil.getInt(json,"time");
//        int timeDeff = MyUtil.getInt(json,"time");
//        String host_ids = MyUtil.getString(json, "host_ids");
//        int time = (int)Math.floor((System.currentTimeMillis() - timeDeff * 1000) / 1000);
        List<Record> list = Db.find("select * from beeeye_res where host_ids = ? and mod_time > ? order by mod_time asc", host_ids, time);
        JSONArray postList = MyUtil.getRecordData(list);
        JSONObject jsonObj = MyUtil.getJson("成功", 200, postList);
        renderJson(jsonObj.toString());
    }

}
