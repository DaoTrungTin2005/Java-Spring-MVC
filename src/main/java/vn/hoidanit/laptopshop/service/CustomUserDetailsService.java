package vn.hoidanit.laptopshop.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Lấy thông tin người dùng từ database khi người dùng đăng nhập.

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // viết logic
        // 2 cái User (1 cái cảu hoidanit 1 cái của spring )
        vn.hoidanit.laptopshop.domain.User user = this.userService.getUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("user ko tồn tại");

        }
        // đa hinh : thằng User nó kế thừa UserDetail nên ko cần trả lại kiểu thằng cha
        // mà trả kiểu thằng con là đươc
        // thằng java tự ddộng ép kiểu
        return new User(
                user.getEmail(),
                user.getPassword(),
                // role là 1 đối tượng
                // lấy động role theo database
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())));
    }
}
