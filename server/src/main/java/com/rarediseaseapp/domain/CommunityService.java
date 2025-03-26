package com.rarediseaseapp.domain;

import com.rarediseaseapp.data.CommunityRepository;
import com.rarediseaseapp.models.Community;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityService {
    @Autowired
    private CommunityRepository communityRepository;

    public CommunityService() {
    }

    public void createCommunity(String communityName, String communityDescription, int userId) {
        this.communityRepository.saveCommunity(communityName, communityDescription, userId);
    }

    public Community getCommunityById(int communityId) {
        return this.communityRepository.getCommunityById(communityId);
    }

    public List<Community> getAllCommunities() {
        List<Community> communities = this.communityRepository.findAll();

        for(Community community : communities) {
            int totalMembers = this.communityRepository.getTotalMembers(community.getCommunityId());
            community.setTotalMembers(totalMembers);
        }

        return communities;
    }
}
