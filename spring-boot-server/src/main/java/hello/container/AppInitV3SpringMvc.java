package hello.container;

import hello.spring.HelloConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

// 스프링 컨테이너를 등록하기 위해서 WebApplicationInitializer 를 구현하면 된다. ( AppInit 과 달리 HandlesTypes을 추가하지 않아도 된다. )
// spring-web 소스에 들어가 보면 spring이 WebApplicationInitializer를 미리 등록해 놓았다.
public class AppInitV3SpringMvc implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("AppInitV3SpringMvc.onStartup");

        // 스프링 컨테이너 생성
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(HelloConfig.class);

        // 스프링 MVC 디스패처 서블릿 생성, 스프링 컨테이너 연결
        DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);

        // 디스패처 서블릿을 서블릿 컨테이너에 등록
        servletContext.addServlet("dispatcherV3", dispatcherServlet)
                .addMapping("/");
    }
}
