package kr.co.skcc.base.com.account.api.dto.requestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.skcc.base.com.account.api.dto.domainDto.UserRoleDeptDto;
import kr.co.skcc.base.com.account.api.dto.domainDto.UserRoleDeptMappingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserDeptRoleDto", description = "역할기준매핑")
public class UserDeptRoleDto {

    @Schema(description = "변경대상")
    private UserRoleDeptDto userRoleDeptDto;

    @Schema(description = "변경내역")
    private List<UserRoleDeptMappingDto> userRoleDeptMappingDtoList;

}
