package controller;

import com.jfinal.core.Controller;
import model.Baseline;
import org.json.JSONObject;
import util.MyUtil;

import java.util.List;
import java.util.Map;

public class BeeeyeSafeBaselineController extends Controller {
    private String tableName = "beeeye_baseline";
    private static final Baseline dao = new Baseline().dao();

    public void get(){
        Map<String, Object> json = MyUtil.getJsonData(getRequest());
        Map<String, Object> row = (Map<String, Object>) json.get("row");
        String hostIds = (String) row.get("hostIds");
        String sql = "select * from " + tableName + " where host_ids = ? ";
        List<Baseline> list = dao.find(sql, hostIds);
        System.out.println(list);
        JSONObject jsonObj = MyUtil.getJson("成功", 200, list);
        renderJson(jsonObj.toString());
    }

}
