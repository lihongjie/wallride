package org.wallride.mapper;

import org.wallride.domain.UserInvitation;

public interface UserInvitationMapper extends BaseMapper<UserInvitation, String> {

    UserInvitation findOneByToken(String token);

    UserInvitation findOneForUpdateByToken(String token);


}
