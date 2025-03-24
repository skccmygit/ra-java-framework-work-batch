package kr.co.skcc.base.com.account.domain.auth;

import jakarta.persistence.*;
import kr.co.skcc.base.com.account.domain.auth.pk.UserMenuPK;
import kr.co.skcc.base.com.common.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(UserMenuPK.class)
@Table(name = "OCO10106", catalog = "OCO")
public class UserMenu extends BaseEntity {

    @Id
    @Column(name = "USERID", length = 10)
    private String userid;

    @Id
    @Column(name = "MENU_ID")
    private String menuId;

    @Column(name = "ATHRTY_REQST_SEQ")
    private Integer athrtyReqstSeq;

}

