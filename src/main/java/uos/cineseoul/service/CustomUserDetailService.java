package uos.cineseoul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import uos.cineseoul.utils.CustomUserDetails;
import uos.cineseoul.entity.User;
import uos.cineseoul.exception.ResourceNotFoundException;
import uos.cineseoul.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String id) {
        User user = userRepo.findByUserId(id).orElseThrow(() -> new ResourceNotFoundException(""));

        CustomUserDetails userDetail = CustomUserDetails.builder().id(user.getId()).pw(user.getPw()).build();

        return userDetail;
    }
}
