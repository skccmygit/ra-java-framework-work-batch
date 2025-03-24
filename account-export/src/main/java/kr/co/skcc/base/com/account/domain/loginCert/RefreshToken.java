package kr.co.skcc.base.com.account.domain.loginCert;

import kr.co.skcc.base.com.account.api.dto.domainDto.RefreshTokenDto;
import kr.co.skcc.base.com.common.jpa.Apiable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.Instant;

@Data
@RedisHash(value="RefreshToken", timeToLive = 86400)
public class RefreshToken implements Apiable<RefreshTokenDto> {

    @Id
    private String userid;

    @Indexed
    private String accessToken;

    @Indexed
    private String oldAccessToken;

    private String refreshToken;

    private String updateTime;

    private Instant expiryDate;

    @Override
    public RefreshTokenDto toApi() {
        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();
        BeanUtils.copyProperties(this, refreshTokenDto);
        return refreshTokenDto;
    }
}