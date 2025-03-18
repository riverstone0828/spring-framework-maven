package co.kr.abacus.base.config.mybatis;

import co.kr.abacus.base.config.gcp.GCPProperties;
import co.kr.abacus.base.config.gcp.GCPServiceAccountImpersonator;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class MyBatisConfig {

    private final GCPServiceAccountImpersonator gcpServiceAccountImpersonator;
    private final GCPProperties gcpProperties;
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource dataSource1() {
//        return DataSourceBuilder.create().build();
//    }

//    @Bean
//    public SqlSessionFactoryBean sqlSessionFactoryBean1() throws IOException {
//        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//        factoryBean.setDataSource(dataSource1());
//        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
//        return factoryBean;
//    }

//    @Bean
//    public SqlSessionTemplate sqlSessionTemplate1() throws Exception {
//        return new SqlSessionTemplate(Objects.requireNonNull(sqlSessionFactoryBean1().getObject()));
//    }


    @Bean
    public DataSource bigQueryDataSource() throws IOException {
        String accessToken = gcpServiceAccountImpersonator.getAccessToken();
        String jdbcUrl = String.format(gcpProperties.getSimbaJdbcUrl(), gcpProperties.getProjectId(), accessToken);

        // HikariDataSource 설정
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setDriverClassName("com.simba.googlebigquery.jdbc42.Driver"); // Simba 드라이버 클래스 이름
        return dataSource;
    }

    @Bean
    public SqlSessionFactoryBean bigQuerySessionFactoryBean() throws IOException {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(bigQueryDataSource());
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:sql/bigquery/*.xml"));
        return factoryBean;
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate2() throws Exception {
        return new SqlSessionTemplate(Objects.requireNonNull(bigQuerySessionFactoryBean().getObject()));
    }

}
