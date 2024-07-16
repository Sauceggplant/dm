package sut.edu.zyp.dormitory.manage.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 导出Excel数据请求
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
@Getter
@Setter
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
}
