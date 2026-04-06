package com.FoodoraProject.FoodoraProject1.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.FoodoraProject.FoodoraProject1.Entity.FinalOrder;
import com.FoodoraProject.FoodoraProject1.Entity.Status;
import com.FoodoraProject.FoodoraProject1.Repository.FinalOrderRepository;

@Service
public class AutomaticSchedule {
	
	@Autowired
	FinalOrderRepository orderrepo;
	
	@Scheduled(fixedRate = 10000)
	public void updateOrderStatus() {
		List<FinalOrder> orderlist = orderrepo.findAll();
		
		for(FinalOrder finalorder : orderlist) {
			if(finalorder.getStatus() == Status.ordered && finalorder.getOrderDateTime() != null) {
				if(finalorder.getOrderDateTime().plusMinutes(2).isBefore(LocalDateTime.now())) {
					finalorder.setStatus(Status.ontheway);
					orderrepo.save(finalorder);
				}
			}
			else if(finalorder.getStatus() == Status.ontheway && finalorder.getOrderDateTime() != null) {
				if(finalorder.getOrderDateTime().plusMinutes(4).isBefore(LocalDateTime.now())) {
					finalorder.setStatus(Status.delivered);
					orderrepo.save(finalorder);
				}
			}
		}
	}
}
