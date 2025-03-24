package kr.co.skcc.base.com.account.domain.userSpecificMenu;

import jakarta.persistence.*;
import kr.co.skcc.base.com.account.api.dto.domainDto.ShortcutMenuDto;
import kr.co.skcc.base.com.account.domain.auth.pk.UserMenuPK;
import kr.co.skcc.base.com.common.jpa.Apiable;
import kr.co.skcc.base.com.common.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(UserMenuPK.class)
@Table(name="OCO10105", catalog = "OCO")
public class ShortcutMenu extends BaseEntity implements Apiable<ShortcutMenuDto> {

    @Id
    @Column(name="USERID", length=10, nullable=false)
    private String userid;

    @Id
    @Column(name="MENU_ID", nullable=false)
    private String menuId;

    @Column(name="SORT_SEQN")
    private int sortSeqn;

    @Override
    public ShortcutMenuDto toApi() {
        ShortcutMenuDto shortcutMenuDto = new ShortcutMenuDto();
        BeanUtils.copyProperties(this, shortcutMenuDto);
        return shortcutMenuDto;
    }

}
