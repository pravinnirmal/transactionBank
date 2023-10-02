package pnd.pravin.bank.BankConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class BankConfiguration {

    @Autowired
    private DataSource dataSource;

    @Value("${bank.queries.users-query}")
    private String usersQuery;

    @Value("${bank.queries.authority-query}")
    private String authorityQuery;
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(authorityQuery);
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(5);

    }

    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(Customizer.withDefaults())

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/").hasAnyRole())
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());
        return httpSecurity.build();
    }


}
