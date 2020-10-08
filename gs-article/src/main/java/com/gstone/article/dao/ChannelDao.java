package com.gstone.article.dao;

import com.gstone.article.pojo.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChannelDao extends JpaRepository<Channel, String>, JpaSpecificationExecutor<Channel> {
}
