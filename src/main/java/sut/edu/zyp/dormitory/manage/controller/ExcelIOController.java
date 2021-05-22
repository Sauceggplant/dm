package sut.edu.zyp.dormitory.manage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.read.biff.BiffException;
import jxl.write.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sut.edu.zyp.dormitory.manage.dto.AbstractExcelExportRequest;
import sut.edu.zyp.dormitory.manage.dto.ExcelImportResponse;
import sut.edu.zyp.dormitory.manage.dto.RestDataExcelExportRequest;
import sut.edu.zyp.dormitory.manage.entity.*;
import sut.edu.zyp.dormitory.manage.enums.ResponseCodeEnum;
import sut.edu.zyp.dormitory.manage.repository.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Excel批处理导入和导出操作
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
@Controller
public class ExcelIOController {

    private static final Logger logger = LoggerFactory.getLogger(ExcelIOController.class);

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private DormitoryManagerRepository dormitoryManagerRepository;

    @Autowired
    private DormitoryRepository dormitoryRepository;

    @Autowired
    private LiveRepository liveRepository;

    @Autowired
    private StudentRepository studentRepository;

    /**
     * 通过http的get请求，返回指定table对应的数据
     */
    @RequestMapping(method = RequestMethod.GET, path = "/excel/export")
    public void restDataExcelExport(@RequestParam(name = "table", required = true) String table, HttpServletResponse response) {
        //获取数据
        List<Map<String, String>> data = getDataByUrl("/" + table);
        RestDataExcelExportRequest restDataExcelExportRequest = GenByUrl("/" + table);
        //数据写入Excel
        writeData2Excel(restDataExcelExportRequest, response, data);
    }

    /**
     * 将数据写入excel，并通过httpResponse下载到本地
     *
     * @param request  写入excel的请求，包含对应元数据等
     * @param response http返回，需要将excel文件数据流，回写到response，返回给前端
     * @param data     需要写入excel的数据，List为每行数据，Map为每列数据
     */
    private void writeData2Excel(AbstractExcelExportRequest request, HttpServletResponse response, List<Map<String, String>> data) {
        try {
            // 获取输出流
            OutputStream os = response.getOutputStream();
            // 清空输出流
            response.reset();
            // 设置输出字符集
            response.setCharacterEncoding("UTF-8");
            // 设定输出文件头
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(request.getExcelName() + ".xls", "UTF-8"));
            // 定义输出类型
            response.setContentType("application/msexcel");
            // 创建Excel
            WritableWorkbook workbook = Workbook.createWorkbook(os);
            WritableSheet sheet = workbook.createSheet(request.getSheetName(), 0);


            //标题样式
            WritableFont fontTitle = new WritableFont(WritableFont.createFont("微软雅黑"), 12, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            WritableCellFormat formatTitle = new WritableCellFormat(fontTitle);
            formatTitle.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);

            //标题数据，写入各列（各字段）的数据
            for (int i = 0; i < request.getColumnNames().size(); i++) {
                //初始化列宽25
                sheet.setColumnView(i, 25);
                String eachTitleStr = String.valueOf(request.getColumnNames().get(i));
                Label eachTitle = new Label(i, 0, eachTitleStr, formatTitle);
                sheet.addCell(eachTitle);
                //logger.info("{}", eachTitleStr);
            }

            //数据样式
            WritableFont fontData = new WritableFont(WritableFont.createFont("微软雅黑"), 12, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            WritableCellFormat formatData = new WritableCellFormat(fontData);
            formatData.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            //将数据写入Excel
            for (int row = 0; row < data.size(); row++) {
                for (int column = 0; column < request.getColumns().size(); column++) {
                    try {
                        String eachDataStr = String.valueOf(data.get(row).get(request.getColumns().get(column)));
                        if ("createTime".equals(request.getColumns().get(column)) || "updateTime".equals(request.getColumns().get(column))) {
                            long timestamp = Long.parseLong(eachDataStr);
                            eachDataStr = simpleDateFormat.format(new Date(timestamp));
                        }
                        Label eachData = new Label(column, row + 1, eachDataStr, formatData);
                        sheet.addCell(eachData);
                        //logger.info("{}", eachDataStr);
                    } catch (Exception e) {
                        logger.error("", e);
                        continue;
                    }
                }
            }
            workbook.write();
            workbook.close();
            os.close();
        } catch (IOException e) {
            logger.error("", e);
        } catch (WriteException e) {
            logger.error("", e);
        }
    }

    /**
     * 根据rest方式，获取数据，这种是需要查询指定表对应的数据库数据
     *
     * @param url 请求的url
     * @return 所需数据
     */
    private List<Map<String, String>> getDataByUrl(String url) {
        List<Map<String, String>> data = new ArrayList<>();
        if ("/admin".equals(url)) {
            Iterable<AdminEntity> adminEntities = adminRepository.findAll();
            data = addDataToResult(adminEntities.iterator(), data);
        } else if ("/building".equals(url)) {
            Iterable<BuildingEntity> buildingEntities = buildingRepository.findAll();
            data = addDataToResult(buildingEntities.iterator(), data);
        } else if ("/dormitoryManager".equals(url)) {
            Iterable<DormitoryManagerEntity> dormitoryManagerEntities = dormitoryManagerRepository.findAll();
            data = addDataToResult(dormitoryManagerEntities.iterator(), data);
        } else if ("/dormitory".equals(url)) {
            Iterable<DormitoryEntity> dormitoryEntities = dormitoryRepository.findAll();
            data = addDataToResult(dormitoryEntities.iterator(), data);
        } else if ("/live".equals(url)) {
            Iterable<LiveEntity> liveEntities = liveRepository.findAll();
            data = addDataToResult(liveEntities.iterator(), data);
        } else if ("/student".equals(url)) {
            Iterable<StudentEntity> studentEntities = studentRepository.findAll();
            data = addDataToResult(studentEntities.iterator(), data);
        } else {
            Map<String, String> defaultRow = new HashMap<>();
            defaultRow.put("NONAME", "查询" + url + "数据为空");
            data.add(defaultRow);
        }
        if (data.size() <= 0) {
            Map<String, String> defaultRow = new HashMap<>();
            defaultRow.put("NONAME", "查询" + url + "数据为空");
            data.add(defaultRow);
        }
        return data;
    }

    /**
     * 根据指定的url,配置excel显示的列，以及相关数据
     * 根据url,对请求加工,形成 写入excel支持的请求格式
     *
     * @param url table表
     * @return 加工后的请求
     */
    private RestDataExcelExportRequest GenByUrl(String url) {
        RestDataExcelExportRequest restDataExcelExportRequest = new RestDataExcelExportRequest();
        if ("/admin".equals(url)) {
            String[] columns = {"id", "name", "password", "createTime", "updateTime"};
            String[] columnNames = {"主键", "管理员账号", "管理员密码", "创建时间", "更新时间"};
            restDataExcelExportRequest.setColumns(Arrays.asList(columns));
            restDataExcelExportRequest.setColumnNames(Arrays.asList(columnNames));
            restDataExcelExportRequest.setExcelName("数据导出-管理员");
            restDataExcelExportRequest.setSheetName("管理员");
            restDataExcelExportRequest.setUrl("/admin");
        } else if ("/building".equals(url)) {
            String[] columns = {"id", "name", "location", "dormitoryManagerId", "createTime", "updateTime"};
            String[] columnNames = {"主键", "楼宇名称", "楼宇位置", "所属宿管id", "创建时间", "更新时间"};
            restDataExcelExportRequest.setColumns(Arrays.asList(columns));
            restDataExcelExportRequest.setColumnNames(Arrays.asList(columnNames));
            restDataExcelExportRequest.setExcelName("数据导出-楼宇");
            restDataExcelExportRequest.setSheetName("楼宇");
            restDataExcelExportRequest.setUrl("/building");
        } else if ("/dormitoryManager".equals(url)) {
            String[] columns = {"id", "sn", "name", "password", "sex", "createTime", "updateTime"};
            String[] columnNames = {"主键", "学生编号", "姓名", "密码", "性别", "创建时间", "更新时间"};
            restDataExcelExportRequest.setColumns(Arrays.asList(columns));
            restDataExcelExportRequest.setColumnNames(Arrays.asList(columnNames));
            restDataExcelExportRequest.setExcelName("数据导出-宿管");
            restDataExcelExportRequest.setSheetName("宿管");
            restDataExcelExportRequest.setUrl("/dormitoryManager");
        } else if ("/dormitory".equals(url)) {
            String[] columns = {"id", "sn", "buildingId", "floor", "maxNumber", "livedNumber", "createTime", "updateTime"};
            String[] columnNames = {"主键", "宿舍编号", "所属楼宇id", "所属楼层", "最大可住人数", "已住人数", "创建时间", "更新时间"};
            restDataExcelExportRequest.setColumns(Arrays.asList(columns));
            restDataExcelExportRequest.setColumnNames(Arrays.asList(columnNames));
            restDataExcelExportRequest.setExcelName("数据导出-宿舍");
            restDataExcelExportRequest.setSheetName("宿舍");
            restDataExcelExportRequest.setUrl("/dormitory");
        } else if ("/live".equals(url)) {
            String[] columns = {"id", "studentId", "dormitoryId", "liveDate", "createTime", "updateTime"};
            String[] columnNames = {"主键", "学生id", "宿舍id", "入住时间", "创建时间", "更新时间"};
            restDataExcelExportRequest.setColumns(Arrays.asList(columns));
            restDataExcelExportRequest.setColumnNames(Arrays.asList(columnNames));
            restDataExcelExportRequest.setExcelName("数据导出-入住");
            restDataExcelExportRequest.setSheetName("入住");
            restDataExcelExportRequest.setUrl("/live");
        } else if ("/student".equals(url)) {
            String[] columns = {"id", "sn", "name", "password", "sex", "createTime", "updateTime"};
            String[] columnNames = {"主键", "学生编号", "姓名", "密码", "性别", "创建时间", "更新时间"};
            restDataExcelExportRequest.setColumns(Arrays.asList(columns));
            restDataExcelExportRequest.setColumnNames(Arrays.asList(columnNames));
            restDataExcelExportRequest.setExcelName("数据导出-学生");
            restDataExcelExportRequest.setSheetName("学生");
            restDataExcelExportRequest.setUrl("/student");
        } else {
            restDataExcelExportRequest.setExcelName("NONAME");
            restDataExcelExportRequest.setSheetName("NONAME");
            List<String> cols = new ArrayList<>();
            cols.add("NONAME");
            restDataExcelExportRequest.setColumns(cols);
            restDataExcelExportRequest.setColumns(cols);
        }
        return restDataExcelExportRequest;
    }

    /**
     * 数据加工
     *
     * @param iterator 数据迭代
     * @param data     当前数据
     * @return 迭代后的数据
     */
    private List<Map<String, String>> addDataToResult(Iterator iterator, List<Map<String, String>> data) {
        while (iterator.hasNext()) {
            //对象转成json串数据，再由json串转成Map对象
            Map<String, String> row = (Map<String, String>) JSON.parseObject(JSON.toJSONString(iterator.next()), Map.class);
            data.add(row);
        }
        return data;
    }

    /**
     * 通过http的post请求，导入指定table对应的数据
     */
    @RequestMapping(method = RequestMethod.POST, path = "/excel/import")
    @ResponseBody
    public ResponseEntity<ExcelImportResponse> restDataExcelImport(@RequestParam("file") MultipartFile file) {
        ExcelImportResponse response = new ExcelImportResponse();
        try {
            if (file.isEmpty()) {
                response.setResponseCode(ResponseCodeEnum.FILE_IS_EMPTY);
                response.setTable("");
                response.setSuccessRows(0);
                response.setSaveData(new ArrayList<>());
            } else {
                //获取上传的Excel文件
                Workbook workbook = Workbook.getWorkbook(file.getInputStream());
                //获取第0个excel页签
                Sheet sheet = workbook.getSheet(0);
                //第0行，描述列
                //第1行，数据列行
                List<String> columns = new ArrayList<>();
                for (int column = 0; column < sheet.getColumns(); column++) {
                    columns.add(sheet.getCell(column, 1).getContents());
                }
                //第2行至末尾，数据行，中间不允许空行
                List<Map<String, String>> data = new ArrayList<>();
                for (int row = 2; row < sheet.getRows(); row++) {
                    Map<String, String> rowData = new HashMap<>();
                    for (int column = 0; column < sheet.getColumns(); column++) {
                        rowData.put(columns.get(column), sheet.getCell(column, row).getContents());
                    }
                    data.add(rowData);
                }
                //sheet标签决定导入表
                logger.info(sheet.getName());

                if (data.size() <= 0) {
                    response.setTable(sheet.getName());
                    response.setResponseCode(ResponseCodeEnum.NO_DATA);
                    response.setSuccessRows(0);
                    response.setSaveData(new ArrayList<>());
                } else if ("admin".equals(sheet.getName())) {
                    List<AdminEntity> adminEntityList = JSON.parseArray(JSON.toJSONString(data), AdminEntity.class);
                    Iterable<AdminEntity> adminEntities = adminRepository.saveAll(adminEntityList);
                    List<AdminEntity> successAdminEntityList = copyIterator(adminEntities.iterator());
                    List<Map<String, String>> successData = JSON.parseObject(JSON.toJSONString(successAdminEntityList), new TypeReference<List<Map<String, String>>>() {
                    });
                    int rows = successAdminEntityList.size();
                    response.setSaveData(successData);
                    response.setSuccessRows(rows);
                } else if ("building".equals(sheet.getName())) {
                    List<BuildingEntity> buildingEntityList = JSON.parseArray(JSON.toJSONString(data), BuildingEntity.class);
                    Iterable<BuildingEntity> buildingEntities = buildingRepository.saveAll(buildingEntityList);
                    List<BuildingEntity> successBuildingEntityList = copyIterator(buildingEntities.iterator());
                    List<Map<String, String>> successData = JSON.parseObject(JSON.toJSONString(successBuildingEntityList), new TypeReference<List<Map<String, String>>>() {
                    });
                    int rows = successBuildingEntityList.size();
                    response.setSaveData(successData);
                    response.setSuccessRows(rows);
                } else if ("dormitoryManager".equals(sheet.getName())) {
                    List<DormitoryManagerEntity> dormitoryManagerEntityList = JSON.parseArray(JSON.toJSONString(data), DormitoryManagerEntity.class);
                    Iterable<DormitoryManagerEntity> dormitoryManagerEntities = dormitoryManagerRepository.saveAll(dormitoryManagerEntityList);
                    List<DormitoryManagerEntity> successDormitoryManagerEntityList = copyIterator(dormitoryManagerEntities.iterator());
                    List<Map<String, String>> successData = JSON.parseObject(JSON.toJSONString(successDormitoryManagerEntityList), new TypeReference<List<Map<String, String>>>() {
                    });
                    int rows = successDormitoryManagerEntityList.size();
                    response.setSaveData(successData);
                    response.setSuccessRows(rows);
                } else if ("dormitory".equals(sheet.getName())) {
                    List<DormitoryEntity> dormitoryEntityList = JSON.parseArray(JSON.toJSONString(data), DormitoryEntity.class);
                    Iterable<DormitoryEntity> dormitoryEntities = dormitoryRepository.saveAll(dormitoryEntityList);
                    List<DormitoryEntity> successDormitoryEntityList = copyIterator(dormitoryEntities.iterator());
                    List<Map<String, String>> successData = JSON.parseObject(JSON.toJSONString(successDormitoryEntityList), new TypeReference<List<Map<String, String>>>() {
                    });
                    int rows = successDormitoryEntityList.size();
                    response.setSaveData(successData);
                    response.setSuccessRows(rows);
                } else if ("live".equals(sheet.getName())) {
                    List<LiveEntity> liveEntityList = JSON.parseArray(JSON.toJSONString(data), LiveEntity.class);
                    Iterable<LiveEntity> liveEntities = liveRepository.saveAll(liveEntityList);
                    List<LiveEntity> successLiveEntityList = copyIterator(liveEntities.iterator());
                    List<Map<String, String>> successData = JSON.parseObject(JSON.toJSONString(successLiveEntityList), new TypeReference<List<Map<String, String>>>() {
                    });
                    int rows = successLiveEntityList.size();
                    response.setSaveData(successData);
                    response.setSuccessRows(rows);
                } else if ("student".equals(sheet.getName())) {
                    List<StudentEntity> studentEntityList = JSON.parseArray(JSON.toJSONString(data), StudentEntity.class);
                    Iterable<StudentEntity> studentEntities = studentRepository.saveAll(studentEntityList);
                    List<StudentEntity> successStudentEntityList = copyIterator(studentEntities.iterator());
                    List<Map<String, String>> successData = JSON.parseObject(JSON.toJSONString(successStudentEntityList), new TypeReference<List<Map<String, String>>>() {
                    });
                    int rows = successStudentEntityList.size();
                    response.setSaveData(successData);
                    response.setSuccessRows(rows);
                } else {
                    response.setTable(sheet.getName());
                    response.setResponseCode(ResponseCodeEnum.NO_TABLE);
                    response.setSuccessRows(0);
                    response.setSaveData(new ArrayList<>());
                }
            }
            file.getInputStream().close();
            response.setTimestamp(System.currentTimeMillis());
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (IOException | BiffException e) {
            response.setTimestamp(System.currentTimeMillis());
            response.setSuccessRows(0);
            response.setSaveData(new ArrayList<>());
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static <T> List<T> copyIterator(Iterator<T> iter) {
        List<T> copy = new ArrayList<T>();
        while (iter.hasNext())
            copy.add(iter.next());
        return copy;
    }
}
