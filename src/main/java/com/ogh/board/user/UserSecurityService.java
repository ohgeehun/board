package com.ogh.board.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService{
    // UserSecurityService는 사용자 인증과 관련된 서비스로, 사용자 정보를 로드하고 권한을 확인하는 기능을 제공합니다.
    // 이 클래스는 Spring Security의 UserDetailsService 인터페이스를 구현하여 사용자 인증을 처리합니다.
    // 또한, 사용자 정보를 기반으로 권한을 부여하는 기능도 포함되어 있습니다.
    // 이 클래스는 사용자 인증과 관련된 다양한 메서드를 제공하여, 애플리케이션의 보안을 강화하는 데 기여합니다.

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // loadUserByUserName 메서드는 사용자 이름을 기반으로 사용자를 로드하는 기능을 제공합니다.
        // 이 메서드는 사용자 이름이 데이터베이스에 존재하지 않을 경우 UsernameNotFoundException을 발생시킵니다.
        // 이 메서드는 Spring Security의 인증 프로세스에서 사용됩니다.
        Optional<SiteUser> _siteUser = this.userRepository.findByUsername(username);
        if(_siteUser.isEmpty()){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        SiteUser siteUser = _siteUser.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
        
    }

}
