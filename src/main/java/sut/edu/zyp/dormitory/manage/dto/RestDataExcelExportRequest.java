package sut.edu.zyp.dormitory.manage.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 数据库数据导出Excel
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
@Getter
@Setter
public class RestDataExcelExportRequest extends AbstractExcelExportRequest implements Serializable {

    /**
     * rest url
     */
    private String url;
}
