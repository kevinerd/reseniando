package com.reseniando.grupo4.security;
import com.reseniando.grupo4.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Security extends WebSecurityConfigurerAdapter {

    @Autowired
    public UsuarioServicio usuarioServicio; //Lo necesito para buscar usuario por id o nombre o etc

    // @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(usuarioServicio)
            .passwordEncoder(new BCryptPasswordEncoder());
    }
    
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.headers().frameOptions().sameOrigin().and()
                .authorizeRequests()
                    .antMatchers("/css/", "/js/", "/img/", "/*")
                    .permitAll()
                .and().formLogin()
                    .loginPage("/login") // El formulario esta mi login (Mpaearlo en el controler)
                            .loginProcessingUrl("/logincheck") //autentifica usuario
                            .usernameParameter("username") // Como viajan los parametros usuario
                            .passwordParameter("password")// Como viajan los parametros clave
                            .defaultSuccessUrl("/inicio") // A que URL viaja si el usuario se autentica con exÃ­to
                            .permitAll()
                .and().logout() // Aca configuro la URL de salida
                        .logoutUrl("/logout") //si el usuario sale
                        .logoutSuccessUrl("/login?logout") //pagÃ­na que redirige
                        .permitAll().and().csrf().disable();
    }

}