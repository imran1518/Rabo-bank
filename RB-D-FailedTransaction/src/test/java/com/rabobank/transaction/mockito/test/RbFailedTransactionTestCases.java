package com.rabobank.transaction.mockito.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.rabobank.transaction.aggregator.RbFailedTransactionAggregator;
import com.rabobank.transaction.controller.RbFailedTransactionController;
import com.rabobank.transaction.model.CustomerTransaction;
import com.rabobank.transaction.model.ErrorResponse;
import com.rabobank.transaction.model.FailedTransaction;
import com.rabobank.transaction.model.RbFailedTransactionResponse;
import com.rabobank.transaction.util.Constants;
import com.rabobank.transaction.util.RbFailedTransactionParser;
import com.rabobank.transaction.validator.RbFailedTransactionValidator;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RbFailedTransactionTestCases {

	private static final List<CustomerTransaction> FileNotFoundException = null;

	@Mock
	RbFailedTransactionController controller;

	@Mock
	ErrorResponse errorResponse;

	@Mock
	RbFailedTransactionAggregator aggregator;

	@Mock
	RbFailedTransactionResponse rbFailedTransactionResponse;

	@Mock
	ResponseEntity<RbFailedTransactionResponse> rbFailedTransactionResponseEntity;

	@Mock
	RbFailedTransactionParser parser;

	@Mock
	List<CustomerTransaction> customerTransactionList;

	@Mock
	List<FailedTransaction> failedTransactionList;

	@Mock
	RbFailedTransactionValidator validator;

	@Before
	public void objectCreation() {
		errorResponse = new ErrorResponse();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void validate_response_status_code_200() throws Exception {

		Object response = new ResponseEntity<>(rbFailedTransactionResponse, HttpStatus.OK);

		when(controller.getFailedTransactions()).thenReturn((ResponseEntity<Object>) response);
		assertEquals(new ResponseEntity<>(rbFailedTransactionResponse, HttpStatus.OK),
				controller.getFailedTransactions());
	}

	@Test
	public void validateAggregator() throws Exception {

		when(aggregator.retrieveFailedTransaction(errorResponse)).thenReturn(rbFailedTransactionResponse);
		assertEquals(rbFailedTransactionResponse, aggregator.retrieveFailedTransaction(errorResponse));
	}

	@Test
	public void csvAndXmlFileNotFound() throws Exception {

		when(parser.parseCsv(null)).thenReturn(FileNotFoundException);
		assertEquals(FileNotFoundException, parser.parseCsv(null));

		when(parser.parseXml(null)).thenReturn(FileNotFoundException);
		assertEquals(FileNotFoundException, parser.parseXml(null));
	}

	@Test
	public void csvAndXmlFileFound() throws Exception {

		Resource csvResource = new ClassPathResource(Constants.CSV_NAME);
		Resource xmlResource = new ClassPathResource(Constants.XML_NAME);

		when(parser.parseCsv(csvResource.toString())).thenReturn(customerTransactionList);
		assertEquals(customerTransactionList, parser.parseCsv(csvResource.toString()));

		when(parser.parseXml(xmlResource.toString())).thenReturn(customerTransactionList);
		assertEquals(customerTransactionList, parser.parseXml(xmlResource.toString()));
	}

	@Test
	public void validateTransactionList() throws Exception {

		when(validator.validateTransactions(customerTransactionList)).thenReturn(failedTransactionList);
		assertEquals(failedTransactionList, validator.validateTransactions(customerTransactionList));
	}

}
