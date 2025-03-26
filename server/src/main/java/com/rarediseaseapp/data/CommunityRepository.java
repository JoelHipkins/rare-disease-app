package com.rarediseaseapp.data;

import com.rarediseaseapp.models.Community;

import java.util.List;

public interface CommunityRepository {
    void saveCommunity(String communityName, String communityDescription, int userId);

    List<Community> findAll();

    Community getCommunityById(int communityId);

    int getTotalMembers(int communityId);
}
