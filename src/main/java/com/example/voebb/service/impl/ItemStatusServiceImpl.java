package com.example.voebb.service.impl;

import com.example.voebb.model.entity.ItemStatus;
import com.example.voebb.repository.ItemStatusRepo;
import com.example.voebb.service.ItemStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemStatusServiceImpl implements ItemStatusService {

    private final ItemStatusRepo itemStatusRepo;

    @Override
    public List<ItemStatus> getAllStatuses() {
        return itemStatusRepo.findAll();
    }

    @Override
    public ItemStatus getStatusById(Long id) {
        return itemStatusRepo.findById(id).orElseThrow(() -> new RuntimeException("Status with Id" + id + "not found"));
    }

    @Override
    public ItemStatus createStatus(ItemStatus itemStatus) {
        return itemStatusRepo.save(itemStatus);
    }

    @Override
    public void deleteStatus(Long id) {
        itemStatusRepo.deleteById(id);

    }

    @Override
    public List<ItemStatus> filterEditableStatusesForItemManagement() {
        return itemStatusRepo.filterEditableStatusesForItemManagement();
    }
}
