package vn.hoidanit.laptopshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

import jakarta.servlet.DispatcherType;
import vn.hoidanit.laptopshop.service.CustomUserDetailsService;
import vn.hoidanit.laptopshop.service.UserService;

@Configuration
@EnableMethodSecurity(securedEnabled = true)

public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // cho hiểu đang xài encoder ,
    // ghi đè lại userDetailsService của spring security
    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return new CustomUserDetailsService(userService);
    }

    // @Bean
    // public AuthenticationManager authenticationManager(HttpSecurity http,
    // PasswordEncoder passwordEncoder,
    // UserDetailsService userDetailsService) throws Exception {
    // AuthenticationManagerBuilder authenticationManagerBuilder = http
    // .getSharedObject(AuthenticationManagerBuilder.class);
    // authenticationManagerBuilder
    // .userDetailsService(userDetailsService)
    // .passwordEncoder(passwordEncoder);
    // return authenticationManagerBuilder.build();
    // }

    // Dùng userDetailsService để tìm user trong DB.
    // Dùng passwordEncoder để so sánh mật khẩu.
    @Bean
    public DaoAuthenticationProvider authProvider(
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        // authProvider.setHideUserNotFoundExceptions(false);
        return authProvider;
    }

    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return new CustomSuccessHandler();
    }

    // cái này khi tắt browser đi thì nó sẽ nhớ đăng nhập
    // nếu muốn tắt trình duyệt did đăng nhập lại thì xóa cái này đi
    @Bean
    public SpringSessionRememberMeServices rememberMeServices() {
        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        // optionally customize
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }

    // dòng code này trang login của bản thân sẽ hiện ra thay cho trang login của
    // spring security
    // sẽ có lỗi redirect too many
    // gắn thêm máy dòng thì dô lại dc
    // thêm "/" để khi dô trang chủ nó ko đá mình về login (tragn chủ ko cần đăng
    // nhập nữa)
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                // DispatcherType.FORWARD cho phép truy cập tới view
                // DispatcherType.INCLUDE ko có cái này dô trang chủ nó ko load dc

                // có product hì khi nhấn xem chi tiết sản phẩm ko bị đá vò trng login
                .dispatcherTypeMatchers(DispatcherType.FORWARD,
                        DispatcherType.INCLUDE)
                .permitAll()
                .requestMatchers("/", "/login", "/product/**", "/register", "/client/**", "/css/**", "/js/**",
                        "/images/**")
                .permitAll()

                // giao diện admin chỉ có admin mới dc dô

                // Đây là phần cấu hình bảo mật: chặn người không có quyền truy cập trang admin.

                // Nếu user cố tình truy cập /admin, Spring sẽ kiểm tra role.

                // Nếu không có ROLE_ADMIN → 403 Forbidden.

                // ➡️ Đây là logic phân quyền, để ngăn chặn truy cập trái phép.

                .requestMatchers("/admin/**").hasRole("ADMIN")

                .anyRequest().authenticated())


                .sessionManagement((sessionManagement) -> sessionManagement 
                // nếu người dùng chưa có session thì sẽ tạo session mới
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) 
                // nếu session đã hết hạn thì sẽ redirect về trang logout với query param expired
                .invalidSessionUrl("/logout?expired") 
                //  giới hạn số lượng session cho mỗi người dùng
                .maximumSessions(1) 
                // người sau đăng nhập sẽ đá người trước ra khỏi session
                .maxSessionsPreventsLogin(false)) 

                // mỗi lần đăng xuat sẽ xóa cookie JSESSIONID và hủy session HTTP
                .logout(logout->logout.deleteCookies("JSESSIONID").invalidateHttpSession(true)) 

// cái này khi tắt browser đi thì nó sẽ nhớ đăng nhập
                .rememberMe(r -> r.rememberMeServices(rememberMeServices()))

                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        // khi login failed nó dẫn tới link này
                        .failureUrl("/login?error")

                        // truyền vô cái hàm
                        .successHandler(customSuccessHandler())
                        .permitAll())
                // user mà đăng nhập dô trang admin thì sẽ dô cái đường link /access-deny

                // định nghía /access-deny ở homepageController

                .exceptionHandling(ex -> ex.accessDeniedPage("/access-deny"));

        return http.build();
    }

}
