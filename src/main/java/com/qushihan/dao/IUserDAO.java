package com.qushihan.dao;

import com.qushihan.po.Bbsuser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface IUserDAO extends CrudRepository<Bbsuser, Integer> {
    @Query("select c from Bbsuser c where username = :u and password= :p")
    Bbsuser login(@Param("u") String username, @Param("p") String password);

    @Override
    Bbsuser save(Bbsuser user);

    @Query("select c from Bbsuser c where username = :u")
    Bbsuser judgeUsername(@Param("u") String username);

    @Override
    Bbsuser findOne(Integer id);

    @Modifying
    @Query("update Bbsuser set pagenum = :pagenum where userid = :userid")
    void updatePageNumById(@Param("pagenum") Integer pagenum, @Param("userid") Integer userid);
}
