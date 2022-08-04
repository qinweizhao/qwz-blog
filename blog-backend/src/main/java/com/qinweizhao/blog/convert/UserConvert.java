package com.qinweizhao.blog.convert;


import com.qinweizhao.blog.model.dto.UserDTO;
import com.qinweizhao.blog.model.entity.User;
import com.qinweizhao.blog.model.params.UserUpdateParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qinweizhao
 * @since 2022/5/27
 */
@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    /**
     * convert
     *
     * @param user user
     * @return UserDTO
     */
    UserDTO convert(User user);


    /**
     * convert
     *
     * @param userParam userParam
     * @return User
     */
    User convert(UserUpdateParam userParam);


}
