package kr.co.skcc.base.com.account.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.skcc.base.com.account.domain.auth.Agent;
import kr.co.skcc.base.com.common.jpa.Entitiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AgentDto", description = "대무자등록내역")
public class AgentDto implements Entitiable<Agent> {

    @Schema(description = "대무자등록SEQ")
    private Integer agentRegSeq;

    @Schema(description = "사용자부서코드")
    private String userDeptcd;

    @Schema(description = "사용자부서명")
    private String userDeptNm;

    @Schema(description = "사용자ID", required = true)
    private String userid;

    @Schema(description = "사용자이름")
    private String userNm;

    @Schema(description = "대무자부서코드")
    private String agentDeptcd;

    @Schema(description = "대무자부서명")
    private String agentDeptNm;

    @Schema(description = "대무자ID", required = true)
    private String agentId;

    @Schema(description = "대무자이름")
    private String agentNm;

    @Schema(description = "부서코드")
    private String deptcd;

    @Schema(description = "부서명")
    private String deptNm;

    @Schema(description = "대무시작일자", required = true)
    private String agentStartDt;

    @Schema(description = "대무종료예정일자", required = true)
    private String agentEndSchdlDt;

    @Schema(description = "대무종료일자")
    private String agentEndDt;

    @Schema(description = "종료여부")
    private String endYn;

    @Schema(description = "대무자등록사유")
    private String agentRegResonCntnt;

    public Agent toEntity() {
        Agent agent = new Agent();
        BeanUtils.copyProperties(this, agent);
        return agent;
    }

}
