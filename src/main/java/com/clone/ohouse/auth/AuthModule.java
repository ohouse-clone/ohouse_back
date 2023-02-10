package com.clone.ohouse.auth;

import com.clone.ohouse.community.entity.Util;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.ServiceBuilderOAuth20;
import com.github.scribejava.core.builder.api.DefaultApi20;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.AccessTokenRequestParams;
import com.github.scribejava.core.oauth.OAuth20Service;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

abstract public class AuthModule extends DefaultApi20 {

    protected OAuth20Service service;
    @Getter
    protected String unique;

    protected AuthModule(ServiceBuilderOAuth20 serviceBuillder, String unique) {
        service = serviceBuillder.build(this);
        this.unique = unique;
    }

    abstract protected String getUserInfoEndPoint();

    abstract public boolean deleteInfo(String access) throws IOException, ExecutionException, InterruptedException;

    abstract public String getUpdateAuthorizationUrl(String state);

    public String getAuthorizationUrl(String state) {
        return service.getAuthorizationUrl(state);
    }

        public com.github.scribejava.core.model.OAuth2AccessToken getAccessToken(AccessTokenRequestParams params) throws IOException, ExecutionException, InterruptedException
    {
        return service.getAccessToken(params);
    }
    public OAuth2AccessToken getRefreshAccessToken(String refresh) throws IOException {

        HashMap<String, String> params = new HashMap<>();
        params.put("client_id", service.getApiKey());
        params.put("client_secret", service.getApiSecret());
        params.put("refresh_token", refresh);

        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, String> param : params.entrySet()) {
            builder.append("&").append(URLEncoder.encode(param.getKey(), StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode(param.getValue(), StandardCharsets.UTF_8));

        }
        byte[] paramBytes = builder.toString().getBytes(StandardCharsets.UTF_8);
        URL url = new URL(getRefreshTokenEndpoint());

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.getOutputStream().write(paramBytes);

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),StandardCharsets.UTF_8));
        StringBuilder responseBuilder = new StringBuilder();
        String temp;

        while((temp = reader.readLine())!= null){
            responseBuilder.append(temp);
        }
        reader.close();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(responseBuilder.toString());

        String access_token = node.get("access_token").textValue();
        String token_type = node.get("token_type").textValue();
        int expires_in =  node.get("expires_in").intValue();

        return new OAuth2AccessToken(access_token,token_type,expires_in,refresh,null,responseBuilder.toString());
    }

    public Response getUserInfo(String access) throws IOException,ExecutionException,InterruptedException{

        OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, getUserInfoEndPoint());
        service.signRequest(access,oAuthRequest);
        return service.execute(oAuthRequest);
    }
    @Override
    public String getRefreshTokenEndpoint()
    {
        return Util.builder(getAccessTokenEndpoint(), "?grant_type=refresh_token");
    }
    protected static ApiKeyBean getApiKeyBean(String platform){
        ApiKeyBean apiKeyBean;
        apiKeyBean = new ApiKeyBean();


        try {
            HashMap<String,String> map = Util.getProperties(platform);
            apiKeyBean.setApi(map.get("api"));
            apiKeyBean.setSecret(map.get("secret"));
            apiKeyBean.setCallback(map.get("callback"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return apiKeyBean;
    }
}
