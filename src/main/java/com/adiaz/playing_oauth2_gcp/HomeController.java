package com.adiaz.playing_oauth2_gcp;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class HomeController {

  @GetMapping("/open")
  public ResponseEntity<String> getOpen(){
    return ResponseEntity.ok("vamos!");
  }

  @GetMapping("/close")
  public ResponseEntity<String> getClose(Principal principal){
    return ResponseEntity.ok("vamos ->" + principal);
  }

  @GetMapping("/user/info")
  public Map<String, Object> getUserInfo(@AuthenticationPrincipal Jwt principal) {
    Map<String, String> map = new Hashtable<String, String>();
    return principal.getClaims();
  }

  @GetMapping("/authorities")
  public Map<String,Object> getPrincipalInfo(JwtAuthenticationToken principal) {

    Collection<GrantedAuthority> authorities1 = principal.getAuthorities();
    String authoritiesStr = authorities1.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(";"));


    Collection<String> authorities = principal.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

    Map<String,Object> info = new HashMap<>();
    info.put("name", principal.getName());
    info.put("authorities", authorities);
    info.put("authoritiesStr", authoritiesStr);
    info.put("tokenAttributes", principal.getTokenAttributes());

    return info;
  }
}
