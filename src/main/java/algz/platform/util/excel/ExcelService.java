package algz.platform.util.excel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.View;

public interface ExcelService {

	public abstract View createExcelView(ModelMap model,HttpServletRequest request,
			HttpServletResponse response) throws Exception;

}