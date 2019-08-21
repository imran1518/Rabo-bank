package com.rabobank.transaction.aggregator;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.rabobank.transaction.model.CustomerTransaction;
import com.rabobank.transaction.model.ErrorResponse;
import com.rabobank.transaction.model.FailedTransaction;
import com.rabobank.transaction.model.RbFailedTransactionResponse;
import com.rabobank.transaction.util.Constants;
import com.rabobank.transaction.util.RbFailedTransactionParser;
import com.rabobank.transaction.validator.RbFailedTransactionValidator;

@Component
public class RbFailedTransactionAggregator {

	@Autowired
	RbFailedTransactionParser parser;

	@Autowired
	RbFailedTransactionValidator validator;

	private static final Logger log = LoggerFactory.getLogger(RbFailedTransactionAggregator.class);

	public static final String CLASS_NAME = RbFailedTransactionAggregator.class.getName();

	/**
	 * This method will find out duplicate transactions
	 * 
	 * @author Muhammad Imran
	 * @return RbFailedTransactionResponse
	 * @throws FileNotFoundException,Exception
	 */
	public RbFailedTransactionResponse retrieveFailedTransaction(ErrorResponse errorResponse) throws FileNotFoundException, Exception {

		String function = CLASS_NAME + ".retrieveFailedTransaction-->";

		log.info(Constants.METHOD_STARTS + function);

		RbFailedTransactionResponse rbFailedTransactionResponse = new RbFailedTransactionResponse();
		
		List<CustomerTransaction> transactionList = new ArrayList<>();
		List<CustomerTransaction> xmlTransactionList = new ArrayList<>();
		List<FailedTransaction> failedTransactions = new ArrayList<>();

		try {

			Resource csvResource = new ClassPathResource(Constants.CSV_NAME);
			Resource xmlResource = new ClassPathResource(Constants.XML_NAME);

			if ((null != csvResource && csvResource.isFile())
					&& (null != xmlResource && xmlResource.isFile())) {

				transactionList = parser.parseCsv(csvResource.getFile().toString());
				xmlTransactionList = parser.parseXml(xmlResource.getFile().toString());

				if (null != xmlTransactionList) {
					transactionList.addAll(xmlTransactionList);
				}

			} else {

				errorResponse.setTimestamp(LocalDateTime.now());
				errorResponse.setMessage("Service failed - CSV or XML file missing");
				errorResponse.setDescription("Service failed - "
						+ "Please check csv file and xml file is present inside resource folder");
				errorResponse.setLocation(Constants.LOCATION);
			}

			//validating the transaction list to get failed transactions
			if (null != transactionList && transactionList.size() > 0)
				failedTransactions = validator.validateTransactions(transactionList);

			if (null != failedTransactions && failedTransactions.size() > 0)
				rbFailedTransactionResponse.setFailedTransactions(failedTransactions);

		} catch (FileNotFoundException ex) {
			log.error(Constants.EXCEPTION_OCCURRED + function + ex.getMessage());
			throw new Exception(ex.getMessage());

		} catch (Exception e) {
			log.error(Constants.EXCEPTION_OCCURRED + function + e.getMessage());
			throw new Exception(e.getMessage());
		}

		log.info(Constants.METHOD_ENDS + function);

		return rbFailedTransactionResponse;
	}
}
