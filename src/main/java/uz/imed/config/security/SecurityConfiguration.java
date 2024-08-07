package uz.imed.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration
{

    private final JwtFilter jwtFilter;

    private final AuthService authService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationRequest ->
                        authorizationRequest
                                .requestMatchers(
                                        "/**"
//                                        getMatches()
                                )
                                .permitAll()
                                .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .userDetailsService(authService)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .rememberMe(remember -> remember.rememberMeCookieDomain("Authorization"));
        return http.build();
    }

    private RequestMatcher[] getMatches()
    {
        return new RequestMatcher[]
                {
                        //Auth
                        new AntPathRequestMatcher("/api/auth/login", "POST"),
                        //About-us
                        new AntPathRequestMatcher("/about-us/get", "GET"),
                        new AntPathRequestMatcher("/about-us/header/get", "GET"),
                        new AntPathRequestMatcher("/about-us/partner-service/get/*", "GET"),
                        new AntPathRequestMatcher("/about-us/choose-us/get/*", "GET"),
                        new AntPathRequestMatcher("/about-us/choose-us/get-all", "GET"),
                        //Address
                        new AntPathRequestMatcher("/address/get/*", "GET"),
                        new AntPathRequestMatcher("/address/get-all", "GET"),
                        //Application
                        new AntPathRequestMatcher("/application/create", "POST"),
                        //Application-form-text
                        new AntPathRequestMatcher("/application-form-text/get", "GET"),
                        //Banner
                        new AntPathRequestMatcher("/banner/get", "GET"),
                        //Category
                        new AntPathRequestMatcher("/category/*", "GET"),
                        new AntPathRequestMatcher("/category/name-list", "GET"),
                        //Contact-form
                        new AntPathRequestMatcher("/contact-form/get", "GET"),
                        //Contact-page
                        new AntPathRequestMatcher("/contact/body/get", "GET"),
                        new AntPathRequestMatcher("/contact/representative/get-all", "GET"),
                        new AntPathRequestMatcher("/contact/representative/get/*", "GET"),
                        //Event
                        new AntPathRequestMatcher("/event/all", "GET"),
                        new AntPathRequestMatcher("/event/city-list", "GET"),
                        //Footer
                        new AntPathRequestMatcher("/footer/get", "GET"),
                        //Home
                        new AntPathRequestMatcher("/", "GET"),
                        //Navbar
                        new AntPathRequestMatcher("/navbar/get", "GET"),
                        //News Controller
                        new AntPathRequestMatcher("/news/get/*", "GET"),
                        new AntPathRequestMatcher("/news/get-all", "GET"),
                        new AntPathRequestMatcher("/news/get-all-other/*", "GET"),
                        //Partner
                        new AntPathRequestMatcher("/partner/get/*", "GET"),
                        new AntPathRequestMatcher("/partner/get-all", "GET"),
                        new AntPathRequestMatcher("/partner/get-all-partner-name", "GET"),
                        new AntPathRequestMatcher("/partner/get-all-partner", "GET"),
                        new AntPathRequestMatcher("/partner/get-others/*", "GET"),
                        new AntPathRequestMatcher("/partner/header/get", "GET"),
                        //Photo
                        new AntPathRequestMatcher("/photo/*", "GET"),
                        //Product
                        new AntPathRequestMatcher("/product/v2/*", "GET"),
                        new AntPathRequestMatcher("/product/v2/all", "GET"),
                        //Schema Work
                        new AntPathRequestMatcher("/scheme-work/get/*", "GET"),
                        new AntPathRequestMatcher("/scheme-work/get-all", "GET"),
                        //Search
                        new AntPathRequestMatcher("/search", "GET"),

                };
    }



    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
