package com.lczy.media.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.lczy.common.data.Dao;
import com.lczy.media.entity.AdvSetting;

public interface AdvSettingDao extends Dao<AdvSetting, Serializable>{

	@Query(value = "select * from t_adv_setting order by weight desc", nativeQuery = true)
	List<AdvSetting> findAdvSetting();
}
