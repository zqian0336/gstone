package com.gstone.friend.dao;

import com.gstone.friend.pojo.NoFriend;
import org.springframework.data.jpa.repository.JpaRepository;



public interface NoFriendDao extends JpaRepository<NoFriend,String> {

    public NoFriend findByUseridAndFriendid(String userid,String friendid);

}
