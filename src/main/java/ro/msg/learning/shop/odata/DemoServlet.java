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
@RequestMapping(DemoServlet.URI)
@RequiredArgsConstructor
public class DemoServlet {

    public static final String URI = "/odata";

    private final Storage storage;
    private final DemoEdmProvider demoEdmProvider;

    @RequestMapping(value = "**")
    public void process(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession(true);
//        Storage storage = (Storage) session.getAttribute(Storage.class.getName());
//        if (storage == null) {
//            storage = new Storage();
        session.setAttribute(Storage.class.getName(), storage);
//        }

        OData odata = OData.newInstance();
        ServiceMetadata edm = odata.createServiceMetadata(demoEdmProvider, new ArrayList<>());

        ODataHttpHandler handler = odata.createHandler(edm);
        handler.register(new DemoEntityCollectionProcessor(storage));
        handler.register(new DemoEntityProcessor(storage));
        handler.register(new DemoPrimitiveProcessor(storage));
        handler.process(new HttpServletRequestWrapper(request) {
            // Spring MVC matches the whole path as the servlet path
            // Olingo wants just the prefix, ie upto /odata, so that it
            // can parse the rest of it as an OData path. So we need to override
            // getServletPath()
            @Override
            public String getServletPath() {
                return DemoServlet.URI;
            }
        }, response);
    }

}