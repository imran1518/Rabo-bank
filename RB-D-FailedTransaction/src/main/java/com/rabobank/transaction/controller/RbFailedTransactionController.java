package com.rabobank.transaction.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rabobank.transaction.aggregator.RbFailedTransactionAggregator;
import com.rabobank.transaction.model.ErrorResponse;
import com.rabobank.transaction.model.RbFailedTransactionResponse;
import com.rabobank.transaction.util.Constants;

@RequestMapping("v1/rabo/")
@RestController
public class RbFailedTransactionController {

	@Autowired
	RbFailedTransactionAggregator aggregator;

	private static final Logger log = LoggerFactory.getLogger(RbFailedTransactionController.class);

	public static final String CLASS_NAME = RbFailedTransactionController.class.getName();

	@GetMapping("failed/transaction")
	public ResponseEntity<Object> getFailedTransactions() throws Exception {

		ErrorResponse errorResponse = new ErrorResponse();

		String function = CLASS_NAME + ".getFailedTransactions-->";

		log.info(Constants.METHOD_STARTS + function);

		RbFailedTransactionResponse rbFailedTransactionResponse = null;

		try {

			rbFailedTransactionResponse = aggregator.retrieveFailedTransaction(errorResponse);

		} catch (Exception e) {
			log.error(Constants.EXCEPTION_OCCURRED + function + e.getMessage());
			throw new Exception(e.getMessage());
		}

		log.info(Constants.METHOD_ENDS + function);

		if (null == rbFailedTransactionResponse.getFailedTransactions()
				|| rbFailedTransactionResponse.getFailedTransactions().size() < 1) {

			if (null != errorResponse.getMessage())
				return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
			else
				return new ResponseEntity<>(rbFailedTransactionResponse, HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<>(rbFailedTransactionResponse, HttpStatus.OK);
	}

}
