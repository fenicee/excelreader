package com.excelreader.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.excelreader.demo.entity.AuditLog;
import com.excelreader.demo.entity.Description;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ExcelReader {
    //获取接口，返回值为List<>
    //
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * 获取文件Reader流,推荐这一种方式
     *
     * @param pathFile json文件路径
     * @return
     */
    public static Reader getFileReader(String pathFile) throws FileNotFoundException {
        Reader reader = new FileReader(pathFile);//转化为流
        return reader;
    }

    @GetMapping("/getAllData")
    public ModelAndView getAll(ModelAndView mv) throws ParseException, IOException {
        mv.addObject("auditlogs", getData(false));
        mv.setViewName("table");
        return mv;
    }

    @GetMapping("/getSelectedData")
    public ModelAndView getSelected(ModelAndView mv) throws ParseException, IOException {
        mv.addObject("auditlogs", getData(true));
        mv.setViewName("table");
        return mv;
    }


    public List<AuditLog> getData(boolean fiselected) throws IOException, ParseException {

        String url = "http://172.16.31.28:8008/vManager/V1.0/audit/getAudit";
        //MGJ本来想用自动fastjson里的json自动适配javabean的，没想到这给的json数据也是残缺不全的。气死我了
        /*String str = readFromTxt("D:\\jsondata.txt");*/

        String str = readFromUrl(url);
       /* InputStream is = new FileInputStream("D:\\jsondata.txt"); //测试用
        DataFormat dataFormat = JSONObject.parseObject(is,DataFormat.class);
        System.out.println(dataFormat.getList().size()+"--------------------");
        mv.addObject("auditlogs",dataFormat.getList());*/
        JSONObject jsonObject = JSONObject.parseObject(str);
        JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("list"));
        List<AuditLog> auditLogs = new ArrayList<>();
        Boolean whetherjoin = false;
        for (int i = 0; i < jsonArray.size(); i++) {
            whetherjoin = false;
            JSONObject job = jsonArray.getJSONObject(i);
            AuditLog auditLog = new AuditLog();
            String opevt = job.getString("operate_event");
            String dtret = job.getString("detail_result");
            if (false == fiselected) {
                auditLog.setOperate_event(opevt);
                auditLog.setDetail_result(dtret);
            } else {
                if ("请求标准桌面连接".equals(opevt) || "请求静态池桌面连接".equals(opevt)) {
                    if ("成功".equals(dtret)) {
                        auditLog.setOperate_event(opevt);
                        auditLog.setDetail_result("成功");
                    }
                } else {
                    continue;
                }
            }
            JSONObject desjob = job.getJSONObject("description");
            Description description = new Description();
            description.setName(desjob.getString("name"));
            description.setIp(desjob.getString("ip"));
            auditLog.setDescription(description);
            auditLog.setOperate_object(job.getString("operate_object"));
            auditLog.setOperate_time(simpleDateFormat.parse(job.getString("operate_time")));
            //最终判定，剔除
            if (true == fiselected) {
                for (AuditLog compareauditlog : auditLogs) {
                    if (compareauditlog.getOperate_time().equals(auditLog.getOperate_time().getTime())) {
                        if (compareauditlog.getDescription().getName().equals(auditLog.getDescription().getName())) {
                            //剔除
                            whetherjoin = true;
                            break;
                        }
                    }
                }
            }
            if (!whetherjoin) {
                auditLogs.add(auditLog);
            }
        }
        return auditLogs;
    }

    @GetMapping("/lookupdate")
    public ModelAndView lookupdate(ModelAndView mv, @RequestParam String selectdate) throws IOException, ParseException {
        selectdate = selectdate.replace("T", " ");
        List<AuditLog> auditLogs = getData(true);
        List<AuditLog> selectedlogs = new ArrayList<>();
        if (selectdate.isEmpty()) {
            mv.addObject("warninginfo", "请选择日期");
            mv.addObject("auditlogs", auditLogs);
        } else {
            Date select = sdf.parse(selectdate);
            for (AuditLog auditLog : auditLogs
            ) {
                if (auditLog.getOperate_time().after(select)) {
                    selectedlogs.add(auditLog);
                    //人员去重
                }
            }
            for (int i = 0; i < selectedlogs.size() - 1; i++) {
                // 从list中索引为 list.size()-1 开始往前遍历
                for (int j = selectedlogs.size() - 1; j > i; j--) {
                    // 进行比较
                    if (selectedlogs.get(j).getDescription().getName().equals(selectedlogs.get(i).getDescription().getName())) {
                        // 去重
                        selectedlogs.remove(j);
                    }
                }
            }
            // 从list中索引为0开始往后遍历
        mv.addObject("auditlogs", selectedlogs);
        mv.addObject("warninginfo", "查询成功");
    }

        mv.setViewName("table");
        return mv;
    }

    public String readFromTxt(String filepath) throws IOException {
        File file = new File(filepath);//定义一个file对象，用来初始化FileReader
        FileReader reader = new FileReader(file);//定义一个fileReader对象，用来初始化BufferedReader
        BufferedReader bReader = new BufferedReader( new InputStreamReader(new FileInputStream(file),"UTF-8") );//new一个BufferedReader对象，将文件内容读取到缓存

        StringBuilder sb = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中
        String s = "";
        while ((s =bReader.readLine()) != null) {//逐行读取文件内容，不读取换行符和末尾的空格
            sb.append(s + "\n");//将读取的字符串添加换行符后累加存放在缓存中
        }
        bReader.close();
        return sb.toString();
    }
    public String readFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        } finally {
            is.close();
        }
    }

}
