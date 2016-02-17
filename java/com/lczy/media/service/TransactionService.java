package com.lczy.media.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lczy.common.service.AbstractService;
import com.lczy.media.entity.Transaction;

@Service
@Transactional(readOnly = true)
public class TransactionService extends AbstractService<Transaction> {

}
