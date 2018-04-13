package controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.MyUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zenngwei
 * @date 2018/04/11 12:31
 */
public class BeeeyeAllAuditController extends Controller {
    public static void main(String[] args) {
        String host_ids = "5aab3228f266320f5b780415";
        String sql = "select bpaa.time as time, bpaa.count as processAssess, bsaa.count as sensesAssess, bgfa.count as wbg_count, bfaa.count as attributePro from \n" +
                "(\n" +
                "\tSELECT FROM_UNIXTIME(time, '%m-%e') as time, COUNT(*) AS count \n" +
                "\tFROM beeneedle_process_access_audit \n" +
                "\tWHERE host_ids = '" + host_ids + "' and time BETWEEN UNIX_TIMESTAMP('2018-2-20 00:00:00') AND UNIX_TIMESTAMP('2018-3-20 00:00:00')\n" +
                "\tGROUP BY FROM_UNIXTIME(time, '%Y%m%e') \n" +
                ") as bpaa\n" +
                "left join\n" +
                "(\n" +
                "\tSELECT FROM_UNIXTIME(time, '%m-%e') as time, COUNT(*) AS count \n" +
                "\tFROM beeneedle_senses_access_audit \n" +
                "\tWHERE host_ids = '" + host_ids + "' and time BETWEEN UNIX_TIMESTAMP('2018-3-1 00:00:00') AND UNIX_TIMESTAMP('2018-3-31 00:00:00')\n" +
                "\tGROUP BY FROM_UNIXTIME(time, '%Y%m%e') \n" +
                ") as bsaa\n" +
                "on bpaa.time = bsaa.time \n" +
                "left join\n" +
                "(\n" +
                "\tSELECT FROM_UNIXTIME(time, '%m-%e') as time, COUNT(*) AS count \n" +
                "\tFROM beeneedle_gray_file_audit \n" +
                "\tWHERE host_ids = '" + host_ids + "' and time BETWEEN UNIX_TIMESTAMP('2018-3-1 00:00:00') AND UNIX_TIMESTAMP('2018-3-31 00:00:00')\n" +
                "\tGROUP BY FROM_UNIXTIME(time, '%Y%m%e') \n" +
                ") as bgfa\n" +
                "on bpaa.time = bgfa.time \n" +
                "left join\n" +
                "(\n" +
                "\tSELECT FROM_UNIXTIME(time, '%m-%e') as time, COUNT(*) AS count \n" +
                "\tFROM beeneedle_file_access_audit \n" +
                "\tWHERE host_ids = '" + host_ids + "' and time BETWEEN UNIX_TIMESTAMP('2018-3-1 00:00:00') AND UNIX_TIMESTAMP('2018-3-31 00:00:00')\n" +
                "\tGROUP BY FROM_UNIXTIME(time, '%Y%m%e') \n" +
                ") as bfaa\n" +
                "on bpaa.time = bfaa.time \n" +
                "ORDER BY time;";
        List<Record> list = Db.find(sql);
        JSONArray postList = MyUtil.getRecordData(list);
        for (int i = 0; i < postList.length(); i++) {
            JSONObject jsonObject = postList.getJSONObject(i);
            System.out.println(jsonObject);
        }

    }

    public void get() throws JSONException {
        Map<String, Object> json = MyUtil.getJsonData(getRequest());
        String host_ids = MyUtil.getString(json, "host_ids");
        int dayCount = 30;
        if(json.containsKey("date"))
            dayCount = MyUtil.getInt(json, "date");

        int time = (int) Math.floor(System.currentTimeMillis() / 1000);
        int beforeTime = time - 3600 * 24 * dayCount;

//        String format = "yyyy-MM-dd HH:mm:ss";
//        SimpleDateFormat sdf = new SimpleDateFormat(format);
//        String dateNow = sdf.format(new Date(Long.valueOf(time + "000")));
//        System.out.println(dateNow);
        String sql = "select bpaa.time as time, bpaa.count as processAssess, bsaa.count as sensesAssess, bgfab.count as blacktotal, bgfag.count as graytotal, bfaa.count as attributePro from \n" +
                "(\n" +
                "\tSELECT FROM_UNIXTIME(time, '%m-%e') as time, COUNT(*) AS count \n" +
                "\tFROM beeneedle_process_access_audit \n" +
//                "\tWHERE host_ids = '" + host_ids + "' and time BETWEEN UNIX_TIMESTAMP('2018-2-20 00:00:00') AND UNIX_TIMESTAMP('2018-3-20 00:00:00')\n" +
                "\tWHERE result = 1 and host_ids = '" + host_ids + "' and time BETWEEN " + beforeTime + " AND " + time + "\n" +
                "\tGROUP BY FROM_UNIXTIME(time, '%Y%m%e') \n" +
                ") as bpaa\n" +
                "left join\n" +
                "(\n" +
                "\tSELECT FROM_UNIXTIME(time, '%m-%e') as time, COUNT(*) AS count \n" +
                "\tFROM beeneedle_senses_access_audit \n" +
//                "\tWHERE host_ids = '" + host_ids + "' and time BETWEEN UNIX_TIMESTAMP('2018-3-1 00:00:00') AND UNIX_TIMESTAMP('2018-3-31 00:00:00')\n" +
                "\tWHERE result = 1 and host_ids = '" + host_ids + "' and time BETWEEN " + beforeTime + " AND " + time + "\n" +
                "\tGROUP BY FROM_UNIXTIME(time, '%Y%m%e') \n" +
                ") as bsaa\n" +
                "on bpaa.time = bsaa.time \n" +
                "left join\n" +
                "(\n" +
                "\tSELECT FROM_UNIXTIME(time, '%m-%e') as time, COUNT(*) AS count \n" +
                "\tFROM beeneedle_gray_file_audit \n" +
//                "\tWHERE host_ids = '" + host_ids + "' and time BETWEEN UNIX_TIMESTAMP('2018-3-1 00:00:00') AND UNIX_TIMESTAMP('2018-3-31 00:00:00')\n" +
                "\tWHERE type = 0 and result = 1 and host_ids = '" + host_ids + "' and time BETWEEN " + beforeTime + " AND " + time + "\n" +
                "\tGROUP BY FROM_UNIXTIME(time, '%Y%m%e') \n" +
                ") as bgfab\n" +
                "on bpaa.time = bgfab.time \n" +
                "left join\n" +
                "(\n" +
                "\tSELECT FROM_UNIXTIME(time, '%m-%e') as time, COUNT(*) AS count \n" +
                "\tFROM beeneedle_gray_file_audit \n" +
//                "\tWHERE host_ids = '" + host_ids + "' and time BETWEEN UNIX_TIMESTAMP('2018-3-1 00:00:00') AND UNIX_TIMESTAMP('2018-3-31 00:00:00')\n" +
                "\tWHERE type = 2 and result = 1 and host_ids = '" + host_ids + "' and time BETWEEN " + beforeTime + " AND " + time + "\n" +
                "\tGROUP BY FROM_UNIXTIME(time, '%Y%m%e') \n" +
                ") as bgfag\n" +
                "on bpaa.time = bgfag.time \n" +
                "left join\n" +
                "(\n" +
                "\tSELECT FROM_UNIXTIME(time, '%m-%e') as time, COUNT(*) AS count \n" +
                "\tFROM beeneedle_file_access_audit \n" +
//                "\tWHERE host_ids = '" + host_ids + "' and time BETWEEN UNIX_TIMESTAMP('2018-3-1 00:00:00') AND UNIX_TIMESTAMP('2018-3-31 00:00:00')\n" +
                "\tWHERE result = 1 and host_ids = '" + host_ids + "' and time BETWEEN " + beforeTime + " AND " + time + "\n" +
                "\tGROUP BY FROM_UNIXTIME(time, '%Y%m%e') \n" +
                ") as bfaa\n" +
                "on bpaa.time = bfaa.time \n" +
                "ORDER BY time desc;";

        JSONArray dataList = new JSONArray();

        List<Record> list = Db.find(sql);
        JSONArray postList = MyUtil.getRecordData(list);
        for (int i = 0; i < postList.length(); i++) {
            JSONObject jsonObject = postList.getJSONObject(i);
            if (!jsonObject.has("processAssess")) {
                jsonObject.put("processAssess", 0);
            }
            if (!jsonObject.has("sensesAssess")) {
                jsonObject.put("sensesAssess", 0);
            }
            if (!jsonObject.has("blacktotal")) {
                jsonObject.put("blacktotal", 0);
            }
            if (!jsonObject.has("graytotal")) {
                jsonObject.put("graytotal", 0);
            }
            if (!jsonObject.has("attributePro")) {
                jsonObject.put("attributePro", 0);
            }

        }

        Calendar cal = Calendar.getInstance();
        JSONArray tempList = new JSONArray();
        for (int i = 0; i < dayCount; i++) {
            JSONObject obj = new JSONObject();
            String day = cal.get(Calendar.DATE) + "";
            String month = cal.get(Calendar.MONTH) + 1 + "";
            String dateStr = MyUtil.getNumbers(month) + "-" + MyUtil.getNumbers(day);
            obj.put("time", dateStr);
            obj.put("processAssess", 0);
            obj.put("sensesAssess", 0);
            obj.put("blacktotal", 0);
            obj.put("graytotal", 0);
            obj.put("attributePro", 0);
            tempList.put(obj);
            cal.add(Calendar.DATE, -1);
        }

        for (int i = 0; i < tempList.length(); i++) {
            JSONObject dataObject = tempList.getJSONObject(i);
            String dateStr = dataObject.getString("time");
            for (int j = 0; j < postList.length(); j++) {
                JSONObject jsonObject = postList.getJSONObject(j);
                if (jsonObject.get("time").equals(dateStr)) {
                    dataObject = jsonObject;
                }
            }
            dataList.put(dataObject);
        }

        JSONObject jsonObj = MyUtil.getJson("成功", 200, dataList);
        renderJson(jsonObj.toString());
    }
}
