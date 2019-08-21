package com.rabobank.transaction.util;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.rabobank.transaction.aggregator.RbFailedTransactionAggregator;
import com.rabobank.transaction.model.CustomerTransaction;
import com.rabobank.transaction.model.CustomerTransactions;

@Component
public class RbFailedTransactionParser {

	@Autowired
	private CustomerTransactions transactions;

	private static final Logger log = LoggerFactory.getLogger(RbFailedTransactionParser.class);

	public static final String CLASS_NAME = RbFailedTransactionAggregator.class.getName();

	private List<CustomerTransaction> customerTransaction = new ArrayList<CustomerTransaction>();

	
	/**
	* This method will parse data from csv file
	* 
	* @author Muhammad Imran
	* @param csvFilePath
	* @return List<CustomerTransaction>
	* @throws Exception
	*/
	public List<CustomerTransaction> parseCsv(String csvFilePath) throws Exception {

		String function = CLASS_NAME + ".parseCsv-->";

		log.info(Constants.METHOD_STARTS + function);

		try {

			log.info("Reading CSV record file -->" + csvFilePath);

			CSVReader reader = new CSVReader(new FileReader(csvFilePath), ',', '"', 1);

			ColumnPositionMappingStrategy<CustomerTransaction> bean = new ColumnPositionMappingStrategy<CustomerTransaction>();
			bean.setType(CustomerTransaction.class);

			bean.setColumnMapping(new String[] { "reference", "accountNumber", "description", "startBalance",
					"mutation", "endBalance" });

			CsvToBean<CustomerTransaction> csvToBean = new CsvToBean<CustomerTransaction>();
			customerTransaction = csvToBean.parse(bean, reader);

			reader.close();

			log.info("CSV record file closed-->");

		} catch (Exception e) {

			log.error(Constants.EXCEPTION_OCCURRED + function + e.getMessage());
			throw new Exception(e.getMessage());

		}
		log.info(Constants.METHOD_ENDS + function);

		return customerTransaction;
	}

	
	
	/**
	* This method will parse data from xml file
	* 
	* @author Muhammad Imran
	* @param xmlFilePath
	* @return List<CustomerTransaction>
	* @throws Exception
	*/
	public List<CustomerTransaction> parseXml(String xmlFilePath) throws Exception {

		String function = CLASS_NAME + ".parseXml-->";

		log.info(Constants.METHOD_STARTS + function);

		try {
			log.info("Reading XML file record -->" + xmlFilePath);

			JAXBContext jaxbContext = JAXBContext.newInstance(CustomerTransactions.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			transactions = (CustomerTransactions) jaxbUnmarshaller.unmarshal(new File(xmlFilePath));

		} catch (JAXBException ex) {

			log.error("Exception occurred in" + function + ex.getMessage());
			throw new JAXBException("Exception occurred while parsing xml file-" + ex.getMessage());

		} catch (Exception e) {

			log.error(Constants.EXCEPTION_OCCURRED + function + e.getMessage());
			throw new Exception(e.getMessage());

		}

		log.info(Constants.METHOD_ENDS + function);

		return transactions.getCustomerTransaction();
	}
}
