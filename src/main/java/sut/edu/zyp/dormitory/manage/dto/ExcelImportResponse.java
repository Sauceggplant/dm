package sut.edu.zyp.dormitory.manage.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Excel导入数据
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
public class ExcelImportResponse extends AbstractBaseResponse<ExcelImportResponse> implements Serializable {

    /**
     * 导入的表
     */
    private String table;

    /**
     * 导入成功行数
     */
    private int successRows;

    /**
     * 导入成功的数据
     */
    private List<Map<String,String>> saveData;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public int getSuccessRows() {
        return successRows;
    }

    public void setSuccessRows(int successRows) {
        this.successRows = successRows;
    }

    public List<Map<String, String>> getSaveData() {
        return saveData;
    }

    public void setSaveData(List<Map<String, String>> saveData) {
        this.saveData = saveData;
    }
}
