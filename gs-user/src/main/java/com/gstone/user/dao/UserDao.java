package com.gstone.user.dao;

import com.gstone.user.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserDao extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    public User findByPhone(String phone);

    @Modifying
    @Query(value="update tb_user set fanscount=fanscount+? where id=?", nativeQuery = true)
    public void updatefanscount(int x, String friendid);

    @Modifying
    @Query(value="update tb_user set followcount=followcount+? where id=?", nativeQuery = true)
    public void updatefollowcount(int x, String userid);
}
