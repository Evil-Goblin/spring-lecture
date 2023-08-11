package hello;

import hello.config.*;
import hello.datasource.MyDataSourcePropertiesV1;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;

//@Import(MyDataSourceEnvConfig.class)
//@Import(MyDataSourceValueConfig.class)
//@Import(MyDataSourceConfigV1.class)
//@Import(MyDataSourceConfigV2.class)
@Import(MyDataSourceConfigV3.class)
@SpringBootApplication(scanBasePackages = "hello.datasource")
//@ConfigurationPropertiesScan({"hello"}/*스캔 범위*/) // @EnableConfigurationProperties(MyDataSourcePropertiesV1.class) 대신 사용 가능 (컴포턴트 스캔과 비슷)
public class ExternalReadApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExternalReadApplication.class, args);
    }

}
