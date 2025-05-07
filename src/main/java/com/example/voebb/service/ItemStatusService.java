package com.example.voebb.service;

import com.example.voebb.model.entity.ItemStatus;

import java.util.List;

public interface ItemStatusService {
    List<ItemStatus> getAllStatuses();
    ItemStatus getStatusById(Long id);
    ItemStatus createStatus(ItemStatus itemStatus);
    void deleteStatus(Long id);

}
