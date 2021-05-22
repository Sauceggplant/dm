package sut.edu.zyp.dormitory.manage.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 导出Excel数据请求
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
public abstract class AbstractExcelExportRequest extends AbstractBaseRequest implements Serializable {

    /**
     * Excel导出文件名称
     */
    private String excelName;

    /**
     * Excel导出页签名称
     */
    private String sheetName;

    /**
     * Excel导出列名
     */
    private List<String> columnNames;

    /**
     * 列key
     */
    private List<String> columns;

    public String getExcelName() {
        return excelName;
    }

    public void setExcelName(String excelName) {
        this.excelName = excelName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
}
