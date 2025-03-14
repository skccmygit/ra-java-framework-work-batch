package kr.co.skcc.oss.com.account.api.dto.domainDto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.skcc.oss.com.account.domain.userSpecificMenu.BookmarkMenu;
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
@Schema(name = "BookmarkMenuDto", description = "즐겨찾기메뉴")
public class BookmarkMenuDto implements Entitiable<BookmarkMenu> {

    @Schema(description = "사용자ID", required = true)
    private String userid;

    @Schema(description = "메뉴ID", required = true)
    private String menuId;

    @Schema(description = "정렬순서")
    private int sortSeqn;

    public BookmarkMenu toEntity() {
        BookmarkMenu bookmarkMenu = new BookmarkMenu();
        BeanUtils.copyProperties(this, bookmarkMenu);
        return bookmarkMenu;
    }

}
