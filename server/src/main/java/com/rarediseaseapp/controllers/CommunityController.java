package com.rarediseaseapp.controllers;

import com.rarediseaseapp.domain.CommunityRequest;
import com.rarediseaseapp.domain.CommunityService;
import com.rarediseaseapp.models.Community;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(
        origins = {"http://localhost:5173"}
)
@RestController
@RequestMapping({"/api/communities"})
public class CommunityController {
    @Autowired
    private CommunityService communityService;

    public CommunityController() {
    }

    @PostMapping
    public ResponseEntity<String> createCommunity(@RequestBody CommunityRequest communityRequest) {
        try {
            this.communityService.createCommunity(communityRequest.getCommunityName(), communityRequest.getCommunityDescription(), communityRequest.getUserId());
            return new ResponseEntity("Community created successfully!", HttpStatus.CREATED);
        } catch (Exception var3) {
            return new ResponseEntity("Error creating community", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Community>> getAllCommunities() {
        List<Community> communities = this.communityService.getAllCommunities();
        return communities.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(communities);
    }

    @GetMapping({"/{communityId}"})
    public ResponseEntity<Community> getCommunityById(@PathVariable int communityId) {
        try {
            Community community = this.communityService.getCommunityById(communityId);
            return community != null ? ResponseEntity.ok(community) : ResponseEntity.notFound().build();
        } catch (Exception var3) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

