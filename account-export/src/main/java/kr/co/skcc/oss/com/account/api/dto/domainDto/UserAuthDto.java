package kr.co.skcc.oss.com.account.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.skcc.oss.com.account.domain.loginCert.UserAuth;
import kr.co.skcc.oss.com.common.jpa.Entitiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserAuthDto", description = "사용자연락전화번호 인증")
public class UserAuthDto implements Entitiable<UserAuth> {

    @Schema(description = "사용자ID", required = true)
    private String userid;

    @Schema(description = "인증번호", required = true)
    private String authNumber;

    public UserAuth toEntity() {
        UserAuth userAuth = new UserAuth();
        BeanUtils.copyProperties(this, userAuth);
        return userAuth;
    }

}
