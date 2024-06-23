package uos.cineseoul.util;

import uos.cineseoul.entity.User;
import uos.cineseoul.utils.JwtTokenProvider;

public class JwtTokenUtil {
    public static String createToken(JwtTokenProvider tokenProvider, User user){
        return tokenProvider.createToken(user.getUserNum(),user.getId(),user.getName(),user.getRole().toString());
    }
}
