package net.caltona.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class Credentials {

    @NonNull
    private String username;

    @NonNull
    private String password;

}
