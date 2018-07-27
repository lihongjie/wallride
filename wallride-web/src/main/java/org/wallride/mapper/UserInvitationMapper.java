package org.wallride.mapper;

import org.wallride.domain.UserInvitation;

public interface UserInvitationMapper extends BaseMapper<UserInvitation, String> {

    UserInvitation findOneByToken(@Params("token") String token);
}
