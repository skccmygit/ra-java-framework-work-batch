package kr.co.skcc.base.com.account.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.skcc.base.com.account.api.dto.responseDto.ifDto.UserRoleDeptMappingIDto;
import kr.co.skcc.base.com.account.domain.baseAuth.UserRoleDept;
import kr.co.skcc.base.com.common.jpa.Entitiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserRoleDeptDto", description = "부서, 직책, 직군")
public class UserRoleDeptDto implements Entitiable<UserRoleDept> {

    @Schema(description = "역할부서팀코드", required = true)
    private String roleDeptTeamCd;

    @Schema(description = "역할부서팀명")
    private String roleDeptTeamNm;

    @Schema(description = "역할매핑직책코드", required = true)
    private String roleMappReofoCd;

    @Schema(description = "역할매핑직책명")
    private String roleMappReofoNm;

    @Schema(description = "역할부서팀구분코드", required = true)
    private String roleDeptTeamClCd;

    @Schema(description = "자동생성여부", required = true)
    private String accntCratAutoYn;

    @Schema(description = "사용여부", required = true)
    private String useYn;

    public UserRoleDeptDto(UserRoleDept t, String roleDeptTeamNm, String roleMappReofoNm) {
        BeanUtils.copyProperties(t, this);
        this.roleDeptTeamNm = roleDeptTeamNm;
        this.roleMappReofoNm = roleMappReofoNm;
    }

    public UserRoleDeptDto(UserRoleDeptMappingIDto t) {
        BeanUtils.copyProperties(t, this);
    }

    public UserRoleDept toEntity() {
        UserRoleDept userRoleDept = new UserRoleDept();
        BeanUtils.copyProperties(this, userRoleDept);
        return userRoleDept;
    }

}
