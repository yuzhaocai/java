package com.lczy.media.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.Invoice;

@Service
@Transactional(readOnly=true)
public class InvoiceService extends AbstractService<Invoice> {

}
