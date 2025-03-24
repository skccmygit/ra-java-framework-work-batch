package kr.co.skcc.base.com.account.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.skcc.base.com.account.domain.userSpecificMenu.ShortcutMenu;
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
@Schema(name = "ShortcutMenuDto", description = "바로가기메뉴")
public class ShortcutMenuDto implements Entitiable<ShortcutMenu> {

    @Schema(description = "사용자ID", required = true)
    private String userid;

    @Schema(description = "메뉴ID", required = true)
    private String menuId;

    @Schema(description = "순번", required = true)
    private int sortSeqn;

    public ShortcutMenu toEntity() {
        ShortcutMenu shortcutMenu = new ShortcutMenu();
        BeanUtils.copyProperties(this, shortcutMenu);
        return shortcutMenu;
    }

}
