package kr.co.skcc.oss.com.account.domain.auth;

import jakarta.persistence.*;
import kr.co.skcc.oss.com.account.api.dto.domainDto.UserRoleDto;
import kr.co.skcc.oss.com.account.domain.auth.pk.UserRolePK;
import kr.co.skcc.oss.com.common.jpa.Apiable;
import kr.co.skcc.oss.com.common.jpa.BaseEntity;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@IdClass(UserRolePK.class)
@Table(name = "OCO10101", catalog = "OCO")
public class UserRole extends BaseEntity implements Apiable<UserRoleDto> {
    @Id
    @Column(name = "USER_ROLE_ID", length = 4)
    private String userRoleId;

    @Id
    @Column(name = "USERID", length = 10)
    private String userid;

    @Column(name = "ATHRTY_REQST_SEQ")
    private Integer athrtyReqstSeq;

    @Override
    public UserRoleDto toApi() {
        UserRoleDto userRoleDto = new UserRoleDto();
        BeanUtils.copyProperties(this, userRoleDto);
        return userRoleDto;
    }

}

