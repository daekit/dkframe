package com.dksys.biz.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.dksys.biz.user.vo.User;

public class CustomTokenConverter extends JwtAccessTokenConverter {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		if (authentication.getOAuth2Request().getGrantType().equalsIgnoreCase("password")) {
			User user = (User) authentication.getPrincipal();
			final Map<String, Object> additionalInfo = new HashMap<String, Object>();
			//토큰에 넣을 값 세팅
			additionalInfo.put("userId", user.getId());
			additionalInfo.put("userNm", user.getName());
			additionalInfo.put("empNo", user.getEmpNo());
			additionalInfo.put("coCd", user.getCoCd());
			additionalInfo.put("deptId", user.getDeptId());
			additionalInfo.put("levelCd", user.getLevelCd());
			additionalInfo.put("email", user.getEmail());
			additionalInfo.put("enterDt", user.getEnterDt());
			additionalInfo.put("authInfo", user.getAuthInfo());

			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
		}
		accessToken = super.enhance(accessToken, authentication);
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(new HashMap<>());
		return accessToken;
	}
    
}