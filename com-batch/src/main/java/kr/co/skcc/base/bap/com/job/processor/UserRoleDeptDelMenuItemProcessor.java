package kr.co.skcc.base.bap.com.job.processor;

import kr.co.skcc.base.bap.com.repository.BookmarkMenuRepository;
import kr.co.skcc.base.bap.com.repository.ShortcutMenuRepository;
import kr.co.skcc.base.com.account.domain.account.Account;
import kr.co.skcc.base.com.account.domain.hist.UserRoleHist;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class UserRoleDeptDelMenuItemProcessor implements ItemProcessor<Object, Account> {

    ShortcutMenuRepository shortcutMenuRepository;
    BookmarkMenuRepository bookmarkMenuRepository;

    public UserRoleDeptDelMenuItemProcessor(ShortcutMenuRepository shortcutMenuRepository, BookmarkMenuRepository bookmarkMenuRepository) {
        this.shortcutMenuRepository = shortcutMenuRepository;
        this.bookmarkMenuRepository = bookmarkMenuRepository;
    }

    @Override
    public Account process(Object items) {

        Object[] objects = (Object[]) items;
        Iterator<Object> iterator = Arrays.stream(objects).iterator();
        List<String> list = new ArrayList<>();
        List<UserRoleHist> userRoleHistList = new ArrayList<>();

        while (iterator.hasNext()) {
            String value = String.valueOf(iterator.next());
            list.add(value);
        }

        //바로가기 삭제
        shortcutMenuRepository.deleteByRoleDeptTeamCdAndRoleMappReofoCd(list.get(0));
        //즐겨찾기 삭제
        bookmarkMenuRepository.deleteByRoleDeptTeamCdAndRoleMappReofoCd(list.get(0));

        return null;
    }
}