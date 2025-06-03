package vn.hoidanit.laptopshop.config;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.hoidanit.laptopshop.domain.User;
// người dùng bình thường về trang hơm , người dùng admin về trang admin
import vn.hoidanit.laptopshop.service.UserService;

public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private UserService userService;


    // Phương thức này sẽ được gọi khi người dùng đăng nhập thành công
    // và sẽ xác định URL đích dựa trên quyền của người dùng đã đăng nhập.
    protected String determineTargetUrl(final Authentication authentication) {

        // Nếu user có ROLE_USER, chuyển về /.

        // Nếu user có ROLE_ADMIN, chuyển về /admin.

        // ➡️ Đây là logic điều hướng (redirect) sau khi đăng nhập, KHÔNG liên quan đến
        // việc bảo vệ route.

        // map // dùng để ánh xạ quyền của người dùng với URL đích tương ứng
        // nếu user có quyền ROLE_USER thì sẽ chuyển về trang chủ, nếu có quyền
        // ROLE_ADMIN thì sẽ chuyển về trang admin
        Map<String, String> roleTargetUrlMap = new HashMap<>();
        roleTargetUrlMap.put("ROLE_USER", "/");
        roleTargetUrlMap.put("ROLE_ADMIN", "/admin");

        // lấy ra danh sách quyền của người dùng đã đăng nhập
        // authentication là đối tượng đại diện cho người dùng đã đăng nhập, nó chứa
        // thông tin về người dùng và quyền của họ
        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        // duyệt qua từng quyền của người dùng
        for (final GrantedAuthority grantedAuthority : authorities) {
            // lấy ra tên quyền (authority) của người dùng
            // authorityName sẽ là "ROLE_USER" hoặc "ROLE_ADMIN"
            String authorityName = grantedAuthority.getAuthority();
            // kiểm tra xem authorityName có trong roleTargetUrlMap không
            // nếu có thì trả về URL tương ứng
            if (roleTargetUrlMap.containsKey(authorityName)) {
                return roleTargetUrlMap.get(authorityName);
            }
        }

        throw new IllegalStateException();
    }

    // Phương thức này sẽ được gọi khi người dùng đăng nhập thành công
    // và sẽ dọn dẹp các thuộc tính xác thực trong session để tránh việc lặp lại
    // thông tin xác thực.
    protected void clearAuthenticationAttributes(HttpServletRequest request, Authentication authentication) {
        // truyền vào false ám chỉ chỉ khi nào có session thì mới lấy ra, nếu không có
        // thì trả về null
        // nếu không có session thì không cần làm gì cả

        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }

        // xóa thuộc tính AUTHENTICATION_EXCEPTION khỏi session
        // thuộc tính này được sử dụng để lưu trữ thông tin về lỗi xác thực nếu có
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);


        // lấy động tên và avatar của người dùng đã đăng nhập (thông qua email vì người dùng đăng nhập bằng email)
        // get email  (username là email)
        String email = authentication.getName();
        // query user
        User user = this.userService.getUserByEmail(email);
        if (  user != null) {
            
            session.setAttribute("fullName", user.getFullname());
            session.setAttribute("avatar", user.getAvatar());
            session.setAttribute("id", user.getId());
            session.setAttribute("email", user.getEmail());
        }
        ;
    }

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                Authentication authentication) throws IOException, ServletException {

        // xác định URL đích dựa trên quyền của người dùng đã đăng nhập (authentication)
        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {

            return;
        }
// sử dụng redirectStrategy để chuyển hướng người dùng đến URL đích
        // redirectStrategy là một đối tượng được sử dụng để thực hiện việc chuyển
        // hướng (redirect) người dùng đến một URL khác
        redirectStrategy.sendRedirect(request, response, targetUrl);

        // dọn dẹp session
        clearAuthenticationAttributes(request, authentication);
    }

}
