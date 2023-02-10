package com.clone.ohouse.auth;

public class NaverOAuthApi {

    protected NaverOAuthApi(){

    }
    private static class InstanceHolder{
        private static final NaverOAuthApi INSTANCE = new NaverOAuthApi();
    }

    public static NaverOAuthApi instance(){
        return InstanceHolder.INSTANCE;
    }


}
