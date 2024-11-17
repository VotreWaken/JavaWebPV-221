package itstep.learning.services.formparse;

import javax.servlet.http.HttpServletRequest;

public interface FormParseService {
    FormParseResult parse( HttpServletRequest request ) ;
}
