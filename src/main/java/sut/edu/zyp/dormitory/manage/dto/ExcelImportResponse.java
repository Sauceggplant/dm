package sut.edu.zyp.dormitory.manage.dto;

import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
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
}
