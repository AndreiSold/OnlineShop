package ro.msg.learning.shop.odata;

import lombok.RequiredArgsConstructor;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@RestController
@RequestMapping(CustomController.URI)
@RequiredArgsConstructor
public class CustomController {

    public static final String URI = "/odata";

    private final Storage storage;
    private final CustomEdmProvider customEdmProvider;

    @RequestMapping(value = "**")
    public void process(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession(true);

        session.setAttribute(Storage.class.getName(), storage);

        OData odata = OData.newInstance();
        ServiceMetadata edm = odata.createServiceMetadata(customEdmProvider, new ArrayList<>());

        ODataHttpHandler handler = odata.createHandler(edm);
        handler.register(new CustomEntityCollectionProcessor(storage));
        handler.register(new CustomEntityProcessor(storage));
        handler.register(new CustomPrimitiveProcessor(storage));
        handler.process(new HttpServletRequestWrapper(request) {
            // Spring MVC matches the whole path as the servlet path
            // Olingo wants just the prefix, ie upto /odata, so that it
            // can parse the rest of it as an OData path. So we need to override
            // getServletPath()
            @Override
            public String getServletPath() {
                return CustomController.URI;
            }
        }, response);
    }

}