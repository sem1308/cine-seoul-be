package uos.cineseoul.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private String secretKey = "cineseoulbackendsecretkey120120120";
    public final static String HEADER_NAME="Authorization";

    // 토큰 유효시간 300분 - 개발시에만 - prod 할땐 30분 or 60분 예전
    private long tokenValidTime = 300 * 60 * 1000L;

    // private final UserDetailsService userDetailsService;

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성
    public String createToken(Long num, String id, String name, List<String> roles) {
        Claims claims = Jwts.claims().setSubject("USER_TOKEN").setIssuer("CINEMA_SEOUL"); // JWT payload 에 저장되는 정보단위

        claims.put("num", num);
        claims.put("name", name);
        claims.put("id", id);
        claims.put("roles", roles); // 정보는 key / value 쌍으로 저장된다.

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과 signature 에 들어갈 secret값 세팅
                .compact();
    }

    public String createToken(Long num, String id, String name, String role) {
        Claims claims = Jwts.claims().setSubject("USER_TOKEN").setIssuer("CINEMA_SEOUL"); // JWT payload 에 저장되는 정보단위

        claims.put("num", num);
        claims.put("name", name);
        claims.put("id", id);
        List<String> roles = new ArrayList<>();
        roles.add(role);
        claims.put("roles", roles); // 정보는 key / value 쌍으로 저장된다.

        Date now = new Date();
        return Jwts.builder()
            .setClaims(claims) // 정보 저장
            .setIssuedAt(now) // 토큰 발행 시간 정보
            .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
            .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과 signature 에 들어갈 secret값 세팅
            .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        Long num = claims.get("num", Long.class);
        String id = claims.get("id", String.class);
        String name = claims.get("name", String.class);

        @SuppressWarnings("unchecked")
        List<String> roles = (ArrayList<String>)claims.get("roles");

        CustomUserDetails userDetails = CustomUserDetails.builder().num(num).name(name).id(id).roles(roles).build();

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    // Request의 Header에서 token 값을 가져옵니다. "Authorization" : "TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(HEADER_NAME);
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}