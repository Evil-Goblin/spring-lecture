package hello.config;

import hello.datasource.MyDataSource;
import hello.datasource.MyDataSourcePropertiesV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Slf4j
//@EnableConfigurationProperties(MyDataSourcePropertiesV1.class)
public class MyDataSourceConfigV1 {

    private final MyDataSourcePropertiesV1 dataSourcePropertiesV1;

    public MyDataSourceConfigV1(MyDataSourcePropertiesV1 dataSourcePropertiesV1) {
        this.dataSourcePropertiesV1 = dataSourcePropertiesV1;
    }

    @Bean
    public MyDataSource myDataSource() {
        return new MyDataSource(dataSourcePropertiesV1.getUrl(), dataSourcePropertiesV1.getUsername(), dataSourcePropertiesV1.getPassword(), dataSourcePropertiesV1.getEtc().getMaxConnection(), dataSourcePropertiesV1.getEtc().getTimeout(), dataSourcePropertiesV1.getEtc().getOption());
    }
}
