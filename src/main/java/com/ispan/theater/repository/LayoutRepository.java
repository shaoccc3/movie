package com.ispan.theater.repository;

import com.ispan.theater.domain.Layout;
import com.ispan.theater.domain.LayoutId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LayoutRepository extends JpaRepository<Layout, LayoutId> {
}