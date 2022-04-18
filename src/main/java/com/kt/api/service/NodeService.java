package com.kt.api.service;

import com.kt.api.repository.NodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


@Service
@Slf4j
public class NodeService {
    @Autowired
    NodeRepository nodeRepository;
//
//
//    public List<NodeEntity> getNode(){
//        List<NodeEntity> listNode = nodeRepository.findAll();
//        return listNode;
//    }
}
