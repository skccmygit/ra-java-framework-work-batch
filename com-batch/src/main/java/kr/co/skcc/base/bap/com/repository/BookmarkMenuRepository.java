package kr.co.skcc.base.bap.com.repository;

import jakarta.persistence.QueryHint;
import kr.co.skcc.base.com.account.domain.auth.pk.UserMenuPK;
import kr.co.skcc.base.com.account.domain.userSpecificMenu.BookmarkMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface BookmarkMenuRepository extends JpaRepository<BookmarkMenu, UserMenuPK> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 천지은"))
    @Modifying
    @Query(value = "DELETE FROM BookmarkMenu t WHERE t.userid = :userid \n" +
                   "   AND t.menuId NOT IN (SELECT k.menuId FROM RoleMenu k\n" +
            "                          WHERE k.userRoleId IN (SELECT l.userRoleId FROM UserRole l\n" +
            "                                                  WHERE l.userid = :userid ))\n")
    void deleteByRoleDeptTeamCdAndRoleMappReofoCd(String userid);

    void deleteByUserid(String userid);
}
