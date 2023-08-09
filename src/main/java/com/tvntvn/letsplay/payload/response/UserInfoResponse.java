package com.tvntvn.letsplay.payload.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class UserInfoResponse {
  @Getter @Setter private String id;
  @Getter @Setter private String username;
  @Getter @Setter private String email;
  @Getter @Setter private List<String> roles;
}
