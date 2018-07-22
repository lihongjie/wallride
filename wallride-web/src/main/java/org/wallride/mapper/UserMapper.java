package org.wallride.mapper;

import org.wallride.domain.User;

import java.util.Date;

public interface UserMapper extends BaseMapper<User, String> {

    User findOneByLoginId(String loginId);

    User findOneByEmail(String email);

    int updateLastLoginTime(long id, Date lastLoginTime);
}
