package kr.co.skcc.oss.com.account.domain.auth.pk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRolePK  implements Serializable {
    String userRoleId;
    String userid;
//    Integer userRoleSeq;
}
