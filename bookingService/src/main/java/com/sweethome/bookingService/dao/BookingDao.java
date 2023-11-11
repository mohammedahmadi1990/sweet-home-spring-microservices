package com.sweethome.bookingService.dao;

import com.sweethome.bookingService.entity.BookingInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingDao extends JpaRepository<BookingInfoEntity, Integer> {
}
