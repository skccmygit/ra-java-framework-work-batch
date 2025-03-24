package kr.co.skcc.base.com.account.domain.hist;

import jakarta.persistence.*;
import kr.co.skcc.base.com.account.api.dto.domainDto.AccountStsChngDto;
import kr.co.skcc.base.com.account.domain.hist.pk.AccountStsChngPK;
import kr.co.skcc.base.com.common.jpa.Apiable;
import kr.co.skcc.base.com.common.jpa.BaseEntity;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "OCO10103", catalog = "OCO")
@Entity
@Builder
@IdClass(AccountStsChngPK.class)
public class AccountStsChng extends BaseEntity implements Apiable<AccountStsChngDto> {

    //사용자 ID, Key
    @Id
    @Column(name = "USERID", length = 10, nullable = false)
    private String userid;

    @Id
    @Column(name = "CHNG_COL_ENGSH_NM", length = 50, nullable = false)
    private String chngColEngshNm;

    @Id
    @Column(name = "CHNG_DTM", length = 14, nullable = false)
    private String chngDtm;

    //신청상태코드
    @Column(name = "CHNG_COL_VAL", length = 600)
    private String chngColVal;

    @Override
    public AccountStsChngDto toApi() {
        AccountStsChngDto accountStsChngDto = new AccountStsChngDto();
        BeanUtils.copyProperties(this, accountStsChngDto);
        return accountStsChngDto;
    }
}