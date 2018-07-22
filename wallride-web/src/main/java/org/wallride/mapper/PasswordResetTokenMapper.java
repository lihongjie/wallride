package org.wallride.mapper;

import org.apache.ibatis.annotations.Param;
import org.wallride.domain.PasswordResetToken;

public interface PasswordResetTokenMapper extends BaseMapper<PasswordResetToken, String> {

    PasswordResetToken findOneByToken(@Param("token") String token);


}
