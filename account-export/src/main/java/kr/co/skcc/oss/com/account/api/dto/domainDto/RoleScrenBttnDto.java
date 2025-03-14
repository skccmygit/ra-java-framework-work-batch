package kr.co.skcc.oss.com.account.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.skcc.oss.com.account.domain.auth.RoleScrenBttn;
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
@Schema(name = "RoleScrenBttnDto", description = "역할별화면버튼")
public class RoleScrenBttnDto implements Entitiable<RoleScrenBttn> {

    @Schema(description = "화면ID", required = true)
    private String screnId;

    @Schema(description = "버튼ID", required = true)
    private String bttnId;

    @Schema(description = "사용자역할ID", required = true)
    private String userRoleId;

    @Schema(description = "권한 추가/삭제", required = true)
    private String authYn;

    @Schema(description = "권한신청순번")
    private Integer athrtyReqstSeq;

    @Schema(description = "변경사유")
    private String chngResonCntnt;

    public RoleScrenBttn toEntity() {
        RoleScrenBttn roleScrenBttn = new RoleScrenBttn();
        BeanUtils.copyProperties(this, roleScrenBttn);
        return roleScrenBttn;
    }

}
