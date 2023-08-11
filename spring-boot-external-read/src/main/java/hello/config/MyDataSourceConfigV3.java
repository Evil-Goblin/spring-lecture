package hello.config;

import hello.datasource.MyDataSource;
import hello.datasource.MyDataSourcePropertiesV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Slf4j
@EnableConfigurationProperties(MyDataSourcePropertiesV3.class)
public class MyDataSourceConfigV3 {

    private final MyDataSourcePropertiesV3 dataSourcePropertiesV1;

    public MyDataSourceConfigV3(MyDataSourcePropertiesV3 dataSourcePropertiesV1) {
        this.dataSourcePropertiesV1 = dataSourcePropertiesV1;
    }

    @Bean
    public MyDataSource myDataSource() {
        return new MyDataSource(dataSourcePropertiesV1.getUrl(), dataSourcePropertiesV1.getUsername(), dataSourcePropertiesV1.getPassword(), dataSourcePropertiesV1.getEtc().getMaxConnection(), dataSourcePropertiesV1.getEtc().getTimeout(), dataSourcePropertiesV1.getEtc().getOption());
    }
}
