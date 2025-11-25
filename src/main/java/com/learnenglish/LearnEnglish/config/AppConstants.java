package com.learnenglish.LearnEnglish.config;


public class AppConstants {


    public static final String[] USER_APIS = {
        "api/users/**",
        "/api/vocabularies/**",
        "/api/grammar/**"
        ,"/api/conversations/**"
        ,"/api/exercies/**",
        "api/level/**",
        "/api/exercise-items/**"
    };
     public static final String[] ADMIN_APIS = {
        "api/admin/**",
        "/api/vocabularies/admin/**",
    };
    public static final String[] PUBLIC_APIS = {
        "/api/auth/**",
        "/api/public/**",
         "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
         "/oauth2/**",      
        "/login/oauth2/**" 
    };
}
