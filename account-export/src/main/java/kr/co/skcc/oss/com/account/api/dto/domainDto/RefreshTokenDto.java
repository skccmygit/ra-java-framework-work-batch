package kr.co.skcc.oss.com.account.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.skcc.oss.com.account.domain.loginCert.RefreshToken;
import kr.co.skcc.oss.com.common.jpa.Entitiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

/**
 * RefreshTokenDto.java
 * : 작성필요
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-03-11, 최초 작성
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "RefreshTokenDto", description = "Refresh Token")
public class RefreshTokenDto implements Entitiable<RefreshToken> {

    @Schema(description = "access token", required = true)
    private String accessToken;

    @Schema(description = "refresh token", required = true)
    private String refreshToken;

    @Override
    public RefreshToken toEntity() {
        RefreshToken token = new RefreshToken();
        BeanUtils.copyProperties(this, token);
        return token;
    }

}
